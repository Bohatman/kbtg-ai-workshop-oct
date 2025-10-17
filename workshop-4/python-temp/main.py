from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from routers import users, hello, transfers

# Create FastAPI app
app = FastAPI(
    title="LBK Points - Transfer API",
    description="โอนแต้ม, ดูสถานะ, และค้นประวัติ พร้อม CRUD User management",
    version="1.1.0",
    contact={
        "name": "API Support",
        "url": "https://example.com/support",
        "email": "support@example.com",
    },
    license_info={
        "name": "MIT License",
        "url": "https://opensource.org/licenses/MIT",
    },
)

# CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"],
    allow_headers=["*"],
)

# Include routers
app.include_router(hello.router)
app.include_router(users.router)
app.include_router(transfers.router)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=3000)