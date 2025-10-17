from fastapi import APIRouter, Depends, HTTPException, status, Response, Query, Path
from sqlalchemy.orm import Session
from sqlalchemy import or_, and_
from typing import List, Optional
from datetime import datetime
import uuid
import json

from database import get_db
from models import User, Transfer, PointLedger, TransferStatus, EventType
from schemas import (
    TransferCreateRequest, TransferCreateResponse, TransferGetResponse, 
    TransferListResponse, Transfer as TransferSchema, ErrorResponse
)

router = APIRouter(prefix="/transfers", tags=["Transfers"])

def generate_idempotency_key() -> str:
    """Generate a unique idempotency key"""
    return str(uuid.uuid4())

def create_point_ledger_entry(
    db: Session, 
    user_id: int, 
    change: int, 
    balance_after: int, 
    event_type: EventType, 
    transfer_id: int,
    reference: str = None,
    metadata: dict = None
):
    """Create a point ledger entry"""
    ledger_entry = PointLedger(
        user_id=user_id,
        change=change,
        balance_after=balance_after,
        event_type=event_type,
        transfer_id=transfer_id,
        reference=reference,
        metadata_json=json.dumps(metadata) if metadata else None
    )
    db.add(ledger_entry)

def convert_transfer_to_schema(transfer: Transfer) -> TransferSchema:
    """Convert SQLAlchemy Transfer model to Pydantic schema"""
    return TransferSchema(
        idemKey=transfer.idempotency_key,
        transferId=transfer.id,
        fromUserId=transfer.from_user_id,
        toUserId=transfer.to_user_id,
        amount=transfer.amount,
        status=transfer.status,
        note=transfer.note,
        createdAt=transfer.created_at,
        updatedAt=transfer.updated_at,
        completedAt=transfer.completed_at,
        failReason=transfer.fail_reason
    )

@router.post("/", response_model=TransferCreateResponse, status_code=status.HTTP_201_CREATED)
async def create_transfer(
    transfer_data: TransferCreateRequest, 
    response: Response,
    db: Session = Depends(get_db)
):
    """
    สร้างคำสั่งโอนแต้ม (ระบบจะสร้าง Idempotency-Key ให้)
    
    สร้างรายการโอนแต้มแบบอะตอมมิก ระบบจะ generate `idemKey` (Idempotency-Key)
    และคืนค่าไว้ใช้ติดตามสถานะผ่าน GET /transfers/{id}
    """
    try:
        # Validate users exist
        from_user = db.query(User).filter(User.id == transfer_data.fromUserId).first()
        if not from_user:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail=ErrorResponse(
                    error="USER_NOT_FOUND",
                    message=f"From user with ID {transfer_data.fromUserId} not found"
                ).dict()
            )
        
        to_user = db.query(User).filter(User.id == transfer_data.toUserId).first()
        if not to_user:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail=ErrorResponse(
                    error="USER_NOT_FOUND",
                    message=f"To user with ID {transfer_data.toUserId} not found"
                ).dict()
            )
        
        # Validate cannot transfer to self
        if transfer_data.fromUserId == transfer_data.toUserId:
            raise HTTPException(
                status_code=status.HTTP_422_UNPROCESSABLE_ENTITY,
                detail=ErrorResponse(
                    error="INVALID_TRANSFER",
                    message="Cannot transfer points to yourself"
                ).dict()
            )
        
        # Check sufficient balance
        if from_user.points < transfer_data.amount:
            raise HTTPException(
                status_code=status.HTTP_409_CONFLICT,
                detail=ErrorResponse(
                    error="INSUFFICIENT_BALANCE",
                    message=f"Insufficient points. Current balance: {from_user.points}, requested: {transfer_data.amount}"
                ).dict()
            )
        
        # Generate idempotency key
        idem_key = generate_idempotency_key()
        
        # Create transfer record
        transfer = Transfer(
            from_user_id=transfer_data.fromUserId,
            to_user_id=transfer_data.toUserId,
            amount=transfer_data.amount,
            status=TransferStatus.PROCESSING,
            note=transfer_data.note,
            idempotency_key=idem_key,
            created_at=datetime.now(),
            updated_at=datetime.now()
        )
        
        db.add(transfer)
        db.flush()  # Get the transfer ID
        
        # Perform the transfer atomically
        # Deduct from sender
        from_user.points -= transfer_data.amount
        new_from_balance = from_user.points
        
        # Add to receiver
        to_user.points += transfer_data.amount
        new_to_balance = to_user.points
        
        # Update users' updated_at
        from_user.updated_at = datetime.now()
        to_user.updated_at = datetime.now()
        
        # Create point ledger entries
        create_point_ledger_entry(
            db=db,
            user_id=transfer_data.fromUserId,
            change=-transfer_data.amount,
            balance_after=new_from_balance,
            event_type=EventType.TRANSFER_OUT,
            transfer_id=transfer.id,
            reference=f"Transfer to user {transfer_data.toUserId}",
            metadata={"transfer_note": transfer_data.note}
        )
        
        create_point_ledger_entry(
            db=db,
            user_id=transfer_data.toUserId,
            change=transfer_data.amount,
            balance_after=new_to_balance,
            event_type=EventType.TRANSFER_IN,
            transfer_id=transfer.id,
            reference=f"Transfer from user {transfer_data.fromUserId}",
            metadata={"transfer_note": transfer_data.note}
        )
        
        # Mark transfer as completed
        transfer.status = TransferStatus.COMPLETED
        transfer.completed_at = datetime.now()
        transfer.updated_at = datetime.now()
        
        db.commit()
        db.refresh(transfer)
        
        # Set response header
        response.headers["Idempotency-Key"] = idem_key
        
        return TransferCreateResponse(
            transfer=convert_transfer_to_schema(transfer)
        )
        
    except HTTPException:
        db.rollback()
        raise
    except Exception as e:
        db.rollback()
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=ErrorResponse(
                error="INTERNAL_ERROR",
                message=f"Failed to create transfer: {str(e)}"
            ).dict()
        )

@router.get("/", response_model=TransferListResponse)
async def get_transfers(
    userId: int = Query(..., ge=1, description="แสดงเฉพาะรายการที่เกี่ยวข้องกับ userId (ทั้งโอนออกและรับเข้า)"),
    page: int = Query(1, ge=1, description="หน้าที่ต้องการ (เริ่มที่ 1)"),
    pageSize: int = Query(20, ge=1, le=200, description="จำนวนต่อหน้า (1–200)"),
    db: Session = Depends(get_db)
):
    """
    ค้น/ดูประวัติการโอน (กรองด้วย userId เท่านั้น)
    
    แสดงรายการที่ userId เกี่ยวข้อง (ทั้ง sender และ receiver) พร้อมแบ่งหน้า
    """
    try:
        # Verify user exists
        user = db.query(User).filter(User.id == userId).first()
        if not user:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail=ErrorResponse(
                    error="USER_NOT_FOUND",
                    message=f"User with ID {userId} not found"
                ).dict()
            )
        
        # Calculate offset
        offset = (page - 1) * pageSize
        
        # Query transfers where user is either sender or receiver
        query = db.query(Transfer).filter(
            or_(
                Transfer.from_user_id == userId,
                Transfer.to_user_id == userId
            )
        ).order_by(Transfer.created_at.desc())
        
        # Get total count
        total = query.count()
        
        # Get paginated results
        transfers = query.offset(offset).limit(pageSize).all()
        
        # Convert to response format
        transfer_items = [convert_transfer_to_schema(transfer) for transfer in transfers]
        
        return TransferListResponse(
            data=transfer_items,
            page=page,
            pageSize=pageSize,
            total=total
        )
        
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=ErrorResponse(
                error="INTERNAL_ERROR",
                message=f"Failed to retrieve transfers: {str(e)}"
            ).dict()
        )

@router.get("/{id}", response_model=TransferGetResponse)
async def get_transfer_by_id(
    id: str = Path(..., min_length=8, max_length=128, description="Transfer ID สำหรับค้นหาสถานะ (เท่ากับ Idempotency-Key ที่ระบบสร้างให้ตอน POST /transfers)"),
    db: Session = Depends(get_db)
):
    """
    ดูสถานะคำสั่งโอน (ใช้ idemKey เป็น id)
    """
    try:
        # Find transfer by idempotency key
        transfer = db.query(Transfer).filter(Transfer.idempotency_key == id).first()
        
        if not transfer:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail=ErrorResponse(
                    error="TRANSFER_NOT_FOUND",
                    message=f"Transfer with ID {id} not found"
                ).dict()
            )
        
        return TransferGetResponse(
            transfer=convert_transfer_to_schema(transfer)
        )
        
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=ErrorResponse(
                error="INTERNAL_ERROR",
                message=f"Failed to retrieve transfer: {str(e)}"
            ).dict()
        )