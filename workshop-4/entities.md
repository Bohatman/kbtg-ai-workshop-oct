# 🧩 Entities — LBK Points System

### 1. **USERS**

**หน้าที่:** เก็บข้อมูลสมาชิกผู้ใช้ระบบ
**Fields สำคัญ:**

* `id` (PK) — รหัสผู้ใช้
* `first_name`, `last_name` — ชื่อ-นามสกุล
* `phone`, `email (UK)` — ข้อมูลติดต่อ (email ต้องไม่ซ้ำ)
* `member_since`, `membership_level` — ข้อมูลสมาชิก
* `points` — คะแนนปัจจุบัน
* `created_at`, `updated_at` — เวลาสร้างและแก้ไข

---

### 2. **TRANSFERS**

**หน้าที่:** เก็บข้อมูลการโอนแต้มระหว่างผู้ใช้

**Fields สำคัญ:**
* `id` (PK) — รหัสการโอน
* `from_user_id (FK)` — ผู้โอน
* `to_user_id (FK)` — ผู้รับ
* `amount` — จำนวนแต้ม
* `status` — สถานะ (`pending`, `completed`, `failed`, etc.)
* `note` — หมายเหตุเพิ่มเติม
* `idempotency_key (UK)` — ใช้กันซ้ำคำสั่งโอน
* `created_at`, `updated_at`, `completed_at` — เวลาที่เกี่ยวข้อง
* `fail_reason` — เหตุผลหากโอนล้มเหลว

**Relations:**

* USERS (as sender) → TRANSFERS
* USERS (as receiver) → TRANSFERS

---

### 3. **POINT_LEDGER**

**หน้าที่:** บันทึกประวัติการเปลี่ยนแปลงแต้มของผู้ใช้ (Transaction History)
**Fields สำคัญ:**

* `id` (PK) — รหัสบันทึก
* `user_id (FK)` — เจ้าของแต้ม
* `change` — จำนวนแต้มที่เปลี่ยน (+/-)
* `balance_after` — แต้มคงเหลือหลังการเปลี่ยน
* `event_type` — ประเภทเหตุการณ์ (`earn`, `spend`, `transfer_in`, `transfer_out`, etc.)
* `transfer_id (FK)` — ลิงก์กับการโอนถ้ามี
* `reference`, `metadata` — ข้อมูลเพิ่มเติม
* `created_at` — วันที่บันทึก

**Relations:**

* USERS → POINT_LEDGER
* TRANSFERS → POINT_LEDGER

---

### 🔗 สรุปความสัมพันธ์ทั้งหมด

| Entity    | ความสัมพันธ์                                                  |
| --------- | ------------------------------------------------------------- |
| USERS     | 1 → N กับ TRANSFERS (ทั้งจาก `from_user_id` และ `to_user_id`) |
| USERS     | 1 → N กับ POINT_LEDGER                                        |
| TRANSFERS | 1 → N กับ POINT_LEDGER                                        |

---

**รวมทั้งหมดมี 3 Entities:**
`USERS`, `TRANSFERS`, และ `POINT_LEDGER` ✅
