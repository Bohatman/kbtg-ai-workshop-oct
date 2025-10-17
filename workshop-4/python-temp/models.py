from sqlalchemy import Column, Integer, String, DateTime, Enum as SQLAlchemyEnum, ForeignKey, Text, CheckConstraint
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.sql import func
from sqlalchemy.orm import relationship
from datetime import datetime
import enum

Base = declarative_base()

class MembershipLevel(enum.Enum):
    BRONZE = "BRONZE"
    SILVER = "SILVER"
    GOLD = "GOLD"
    PLATINUM = "PLATINUM"

class TransferStatus(enum.Enum):
    PENDING = "pending"
    PROCESSING = "processing"
    COMPLETED = "completed"
    FAILED = "failed"
    CANCELLED = "cancelled"
    REVERSED = "reversed"

class EventType(enum.Enum):
    TRANSFER_OUT = "transfer_out"
    TRANSFER_IN = "transfer_in"
    ADJUST = "adjust"
    EARN = "earn"
    REDEEM = "redeem"

class User(Base):
    __tablename__ = "users"
    
    id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    first_name = Column(String, nullable=False)
    last_name = Column(String, nullable=False)
    phone = Column(String, nullable=True)
    email = Column(String, unique=True, nullable=False, index=True)
    member_since = Column(DateTime, nullable=False, default=func.now())
    membership_level = Column(SQLAlchemyEnum(MembershipLevel), nullable=False, default=MembershipLevel.BRONZE)
    points = Column(Integer, nullable=False, default=0)
    created_at = Column(DateTime, nullable=False, default=func.now())
    updated_at = Column(DateTime, nullable=False, default=func.now(), onupdate=func.now())
    
    # Relationships
    sent_transfers = relationship("Transfer", foreign_keys="Transfer.from_user_id", back_populates="sender")
    received_transfers = relationship("Transfer", foreign_keys="Transfer.to_user_id", back_populates="receiver")
    point_ledgers = relationship("PointLedger", back_populates="user")
    
    @property
    def full_name(self):
        return f"{self.first_name} {self.last_name}"
    
    def add_points(self, points_to_add: int) -> bool:
        if points_to_add > 0:
            self.points += points_to_add
            return True
        return False
    
    def deduct_points(self, points_to_deduct: int) -> bool:
        if points_to_deduct > 0 and self.points >= points_to_deduct:
            self.points -= points_to_deduct
            return True
        return False

class Transfer(Base):
    __tablename__ = "transfers"
    
    id = Column(Integer, primary_key=True, autoincrement=True)
    from_user_id = Column(Integer, ForeignKey("users.id"), nullable=False, index=True)
    to_user_id = Column(Integer, ForeignKey("users.id"), nullable=False, index=True)
    amount = Column(Integer, nullable=False)
    status = Column(SQLAlchemyEnum(TransferStatus), nullable=False, default=TransferStatus.PENDING)
    note = Column(Text, nullable=True)
    idempotency_key = Column(String, nullable=False, unique=True, index=True)
    created_at = Column(DateTime, nullable=False, default=func.now(), index=True)
    updated_at = Column(DateTime, nullable=False, default=func.now(), onupdate=func.now())
    completed_at = Column(DateTime, nullable=True)
    fail_reason = Column(Text, nullable=True)
    
    # Relationships
    sender = relationship("User", foreign_keys=[from_user_id], back_populates="sent_transfers")
    receiver = relationship("User", foreign_keys=[to_user_id], back_populates="received_transfers")
    point_ledgers = relationship("PointLedger", back_populates="transfer")
    
    # Constraints
    __table_args__ = (
        CheckConstraint('amount > 0', name='check_amount_positive'),
        CheckConstraint('from_user_id != to_user_id', name='check_different_users'),
    )

class PointLedger(Base):
    __tablename__ = "point_ledger"
    
    id = Column(Integer, primary_key=True, autoincrement=True)
    user_id = Column(Integer, ForeignKey("users.id"), nullable=False, index=True)
    change = Column(Integer, nullable=False)  # +receive / -send
    balance_after = Column(Integer, nullable=False)
    event_type = Column(SQLAlchemyEnum(EventType), nullable=False)
    transfer_id = Column(Integer, ForeignKey("transfers.id"), nullable=True, index=True)
    reference = Column(Text, nullable=True)
    metadata_json = Column(Text, nullable=True)  # JSON text
    created_at = Column(DateTime, nullable=False, default=func.now(), index=True)
    
    # Relationships
    user = relationship("User", back_populates="point_ledgers")
    transfer = relationship("Transfer", back_populates="point_ledgers")