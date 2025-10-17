# Database Structure - ER Diagram

แผนผังความสัมพันธ์ของฐานข้อมูล LBK Points API

```mermaid
erDiagram
    User ||--o{ Transfer_Sent : "sends"
    User ||--o{ Transfer_Received : "receives"
    User ||--o{ PointLedger : "has"
    Transfer ||--o{ PointLedger : "generates"

    User {
        integer id PK "Primary Key"
        string first_name "ชื่อผู้ใช้"
        string last_name "นามสกุล"
        string phone "เบอร์โทรศัพท์"
        string email UK "อีเมล (Unique)"
        datetime member_since "วันที่เป็นสมาชิก"
        enum membership_level "ระดับสมาชิก (BRONZE, SILVER, GOLD, PLATINUM)"
        integer points "แต้มปัจจุบัน"
        datetime created_at "เวลาที่สร้าง"
        datetime updated_at "เวลาที่แก้ไขล่าสุด"
    }

    Transfer {
        integer id PK "Primary Key"
        integer from_user_id FK "ผู้โอน (Foreign Key -> User.id)"
        integer to_user_id FK "ผู้รับโอน (Foreign Key -> User.id)"
        integer amount "จำนวนแต้มที่โอน (> 0)"
        enum status "สถานะ (pending, processing, completed, failed, cancelled, reversed)"
        string note "ข้อความถึงผู้รับ (Optional)"
        string idempotency_key UK "รหัสอ้างอิงเพื่อป้องกันการทำรายการซ้ำ (Unique)"
        datetime created_at "เวลาที่สร้าง"
        datetime updated_at "เวลาที่แก้ไขล่าสุด"
        datetime completed_at "เวลาที่รายการเสร็จสิ้น"
        string fail_reason "สาเหตุที่ล้มเหลว (ถ้ามี)"
    }

    PointLedger {
        integer id PK "Primary Key"
        integer user_id FK "ผู้ใช้ (Foreign Key -> User.id)"
        integer change "การเปลี่ยนแปลงแต้ม (+รับโอน / -โอนออก)"
        integer balance_after "ยอดคงเหลือหลังการเปลี่ยนแปลง"
        enum event_type "ประเภทรายการ (transfer_out, transfer_in, adjust, earn, redeem)"
        integer transfer_id FK "อ้างอิงรายการโอน (Foreign Key -> Transfer.id)"
        string reference "ข้อมูลอ้างอิง"
        string metadata_json "ข้อมูลเพิ่มเติม (JSON)"
        datetime created_at "เวลาที่สร้าง"
    }

    %% Relationship aliases for better visibility
    Transfer_Sent }|--|| User : "from_user_id"
    Transfer_Received }|--|| User : "to_user_id"
```

## คำอธิบาย

### ตาราง User (ผู้ใช้)
- เก็บข้อมูลผู้ใช้และแต้มปัจจุบัน
- มีระดับสมาชิก (BRONZE, SILVER, GOLD, PLATINUM)
- อีเมลเป็น unique key

### ตาราง Transfer (รายการโอนแต้ม)
- เก็บประวัติการโอนแต้มระหว่างผู้ใช้
- มี idempotency_key เป็น unique key สำหรับค้นหารายการและป้องกันการทำรายการซ้ำ
- บันทึกสถานะการโอนแต้ม (pending, processing, completed, failed, cancelled, reversed)

### ตาราง PointLedger (สมุดบัญชีแต้ม)
- เก็บประวัติการเปลี่ยนแปลงแต้มทั้งหมดแบบ append-only
- รองรับหลายประเภทรายการ (transfer_out, transfer_in, adjust, earn, redeem)
- บันทึกยอดคงเหลือหลังการเปลี่ยนแปลงทุกครั้ง

## ความสัมพันธ์
- ผู้ใช้หนึ่งคนสามารถโอนแต้มได้หลายรายการ (1:N)
- ผู้ใช้หนึ่งคนสามารถรับโอนแต้มได้หลายรายการ (1:N)
- การโอนแต้มหนึ่งรายการสร้าง point ledger entries 2 รายการ (1:2) - ฝั่งผู้โอนและฝั่งผู้รับ
- ผู้ใช้หนึ่งคนมีประวัติรายการแต้ม (point ledger) หลายรายการ (1:N)