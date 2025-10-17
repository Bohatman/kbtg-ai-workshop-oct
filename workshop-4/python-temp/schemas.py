from pydantic import BaseModel, EmailStr, Field
from typing import Optional, Any, List
from datetime import datetime
from enum import Enum

class MembershipLevel(str, Enum):
    BRONZE = "BRONZE"
    SILVER = "SILVER"
    GOLD = "GOLD"
    PLATINUM = "PLATINUM"

class TransferStatus(str, Enum):
    PENDING = "pending"
    PROCESSING = "processing"
    COMPLETED = "completed"
    FAILED = "failed"
    CANCELLED = "cancelled"
    REVERSED = "reversed"

class EventType(str, Enum):
    TRANSFER_OUT = "transfer_out"
    TRANSFER_IN = "transfer_in"
    ADJUST = "adjust"
    EARN = "earn"
    REDEEM = "redeem"

# User Schemas
class UserBase(BaseModel):
    first_name: str = Field(..., description="User's first name", example="สมชาย")
    last_name: str = Field(..., description="User's last name", example="ใจดี")
    phone: Optional[str] = Field(None, description="User's phone number", example="081-234-5678")
    email: EmailStr = Field(..., description="User's email address (unique)", example="somchai@example.com")

class UserCreate(UserBase):
    membership_level: Optional[MembershipLevel] = Field(MembershipLevel.BRONZE, description="User's membership level")
    points: Optional[int] = Field(0, ge=0, description="Current points balance", example=100)

class UserUpdate(UserBase):
    membership_level: Optional[MembershipLevel] = Field(None, description="User's membership level")
    points: Optional[int] = Field(None, ge=0, description="Current points balance")
    member_since: Optional[datetime] = Field(None, description="Date when user became a member")

class UserResponse(UserBase):
    id: int = Field(..., description="Unique identifier for the user", example=123)
    member_since: datetime = Field(..., description="Date when user became a member")
    membership_level: MembershipLevel = Field(..., description="User's membership level")
    points: int = Field(..., ge=0, description="Current points balance")
    created_at: datetime = Field(..., description="Record creation timestamp")
    updated_at: datetime = Field(..., description="Record last update timestamp")
    
    class Config:
        from_attributes = True

# Transfer Schemas
class TransferCreateRequest(BaseModel):
    fromUserId: int = Field(..., ge=1, description="Sender user ID")
    toUserId: int = Field(..., ge=1, description="Receiver user ID")
    amount: int = Field(..., ge=1, description="Amount to transfer")
    note: Optional[str] = Field(None, max_length=512, description="Transfer note")

class Transfer(BaseModel):
    idemKey: str = Field(..., description="Idempotency key as main reference ID")
    transferId: Optional[int] = Field(None, description="Internal system ID (autoincrement)")
    fromUserId: int = Field(..., ge=1, description="Sender user ID")
    toUserId: int = Field(..., ge=1, description="Receiver user ID")
    amount: int = Field(..., ge=1, description="Transfer amount")
    status: TransferStatus = Field(..., description="Transfer status")
    note: Optional[str] = Field(None, max_length=512, description="Transfer note")
    createdAt: datetime = Field(..., description="Creation timestamp")
    updatedAt: datetime = Field(..., description="Last update timestamp")
    completedAt: Optional[datetime] = Field(None, description="Completion timestamp")
    failReason: Optional[str] = Field(None, description="Failure reason if failed")

    class Config:
        from_attributes = True

class TransferCreateResponse(BaseModel):
    transfer: Transfer

class TransferGetResponse(BaseModel):
    transfer: Transfer

class TransferListItem(Transfer):
    pass

class TransferListResponse(BaseModel):
    data: List[TransferListItem] = Field(..., description="List of transfers")
    page: int = Field(..., ge=1, description="Current page number")
    pageSize: int = Field(..., ge=1, description="Items per page")
    total: int = Field(..., ge=0, description="Total number of items")

class ErrorResponse(BaseModel):
    error: str = Field(..., example="VALIDATION_ERROR")
    message: str = Field(..., example="amount must be > 0")
    details: Optional[dict] = Field(None, description="Additional error details")

# Point Ledger Schema
class PointLedgerResponse(BaseModel):
    id: int
    user_id: int
    change: int
    balance_after: int
    event_type: EventType
    transfer_id: Optional[int]
    reference: Optional[str]
    metadata_json: Optional[str]  # Changed from 'metadata' to 'metadata_json'
    created_at: datetime

    class Config:
        from_attributes = True

# Legacy schemas for backward compatibility
class ApiResponse(BaseModel):
    status: str = Field(..., description="Response status", example="success")
    message: str = Field(..., description="Response message", example="Operation completed successfully")
    data: Optional[Any] = Field(None, description="Response data")

class PointsOperation(BaseModel):
    points: int = Field(..., gt=0, description="Points amount", example=100)