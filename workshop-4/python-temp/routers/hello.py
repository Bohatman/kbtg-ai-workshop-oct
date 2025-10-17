from fastapi import APIRouter
from schemas import ApiResponse

router = APIRouter(prefix="/hello", tags=["Hello World"])

@router.get("/")
async def hello_world():
    """
    Hello World endpoint
    
    Returns a simple hello world message
    """
    return "Hello World!"

@router.get("/health")
async def health_check():
    """
    Health check endpoint
    
    Returns application status
    """
    return {
        "status": "UP",
        "message": "Application is running successfully",
        "port": 3000
    }