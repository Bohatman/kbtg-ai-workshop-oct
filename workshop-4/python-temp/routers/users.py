from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from typing import List
from datetime import datetime

from database import get_db
from models import User, MembershipLevel
from schemas import UserCreate, UserUpdate, UserResponse, ApiResponse, PointsOperation

router = APIRouter(prefix="/users", tags=["User Management"])

@router.get("/", response_model=ApiResponse)
async def get_all_users(db: Session = Depends(get_db)):
    """
    Get all users
    
    Retrieve a list of all users from the SQLite database
    """
    try:
        users = db.query(User).all()
        user_responses = [UserResponse.from_orm(user) for user in users]
        return ApiResponse(
            status="success",
            message="Users retrieved successfully",
            data=user_responses
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to retrieve users: {str(e)}"
        )

@router.get("/{user_id}", response_model=ApiResponse)
async def get_user_by_id(user_id: int, db: Session = Depends(get_db)):
    """
    Get user by ID
    
    Retrieve a specific user by their unique identifier from SQLite database
    """
    try:
        user = db.query(User).filter(User.id == user_id).first()
        if not user:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail=f"User not found with ID: {user_id}"
            )
        
        return ApiResponse(
            status="success",
            message="User found",
            data=UserResponse.from_orm(user)
        )
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to retrieve user: {str(e)}"
        )

@router.post("/", response_model=ApiResponse, status_code=status.HTTP_201_CREATED)
async def create_user(user_data: UserCreate, db: Session = Depends(get_db)):
    """
    Create a new user
    
    Create a new user in the SQLite database
    """
    try:
        # Check if email already exists
        existing_user = db.query(User).filter(User.email == user_data.email).first()
        if existing_user:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail=f"Email already exists: {user_data.email}"
            )
        
        # Create new user
        db_user = User(
            first_name=user_data.first_name,
            last_name=user_data.last_name,
            phone=user_data.phone,
            email=user_data.email,
            membership_level=user_data.membership_level or MembershipLevel.BRONZE,
            points=user_data.points or 0,
            member_since=datetime.now()
        )
        
        db.add(db_user)
        db.commit()
        db.refresh(db_user)
        
        return ApiResponse(
            status="success",
            message="User created successfully",
            data=UserResponse.from_orm(db_user)
        )
    except HTTPException:
        raise
    except Exception as e:
        db.rollback()
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to create user: {str(e)}"
        )

@router.put("/{user_id}", response_model=ApiResponse)
async def update_user(user_id: int, user_data: UserUpdate, db: Session = Depends(get_db)):
    """
    Update an existing user
    
    Update user information for an existing user in SQLite database
    """
    try:
        # Find user
        user = db.query(User).filter(User.id == user_id).first()
        if not user:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail=f"User not found with id: {user_id}"
            )
        
        # Check if email is being changed and if new email already exists
        if user_data.email != user.email:
            existing_user = db.query(User).filter(User.email == user_data.email).first()
            if existing_user:
                raise HTTPException(
                    status_code=status.HTTP_400_BAD_REQUEST,
                    detail=f"Email already exists: {user_data.email}"
                )
        
        # Update fields
        user.first_name = user_data.first_name
        user.last_name = user_data.last_name
        user.phone = user_data.phone
        user.email = user_data.email
        
        if user_data.membership_level is not None:
            user.membership_level = user_data.membership_level
        if user_data.points is not None:
            user.points = user_data.points
        if user_data.member_since is not None:
            user.member_since = user_data.member_since
        
        user.updated_at = datetime.now()
        
        db.commit()
        db.refresh(user)
        
        return ApiResponse(
            status="success",
            message="User updated successfully",
            data=UserResponse.from_orm(user)
        )
    except HTTPException:
        raise
    except Exception as e:
        db.rollback()
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to update user: {str(e)}"
        )

@router.delete("/{user_id}", response_model=ApiResponse)
async def delete_user(user_id: int, db: Session = Depends(get_db)):
    """
    Delete a user
    
    Delete a user from the SQLite database by their ID
    """
    try:
        user = db.query(User).filter(User.id == user_id).first()
        if not user:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail=f"User not found with id: {user_id}"
            )
        
        db.delete(user)
        db.commit()
        
        return ApiResponse(
            status="success",
            message="User deleted successfully",
            data=None
        )
    except HTTPException:
        raise
    except Exception as e:
        db.rollback()
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to delete user: {str(e)}"
        )

@router.post("/{user_id}/points/add", response_model=ApiResponse)
async def add_points(user_id: int, points_data: PointsOperation, db: Session = Depends(get_db)):
    """
    Add points to user
    
    Add points to a user's account
    """
    try:
        user = db.query(User).filter(User.id == user_id).first()
        if not user:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail=f"User not found with id: {user_id}"
            )
        
        user.add_points(points_data.points)
        user.updated_at = datetime.now()
        
        db.commit()
        db.refresh(user)
        
        return ApiResponse(
            status="success",
            message="Points added successfully",
            data=UserResponse.from_orm(user)
        )
    except HTTPException:
        raise
    except Exception as e:
        db.rollback()
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to add points: {str(e)}"
        )

@router.post("/{user_id}/points/deduct", response_model=ApiResponse)
async def deduct_points(user_id: int, points_data: PointsOperation, db: Session = Depends(get_db)):
    """
    Deduct points from user
    
    Deduct points from a user's account
    """
    try:
        user = db.query(User).filter(User.id == user_id).first()
        if not user:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail=f"User not found with id: {user_id}"
            )
        
        if not user.deduct_points(points_data.points):
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail=f"Insufficient points. Current balance: {user.points}"
            )
        
        user.updated_at = datetime.now()
        
        db.commit()
        db.refresh(user)
        
        return ApiResponse(
            status="success",
            message="Points deducted successfully",
            data=UserResponse.from_orm(user)
        )
    except HTTPException:
        raise
    except Exception as e:
        db.rollback()
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to deduct points: {str(e)}"
        )