from fastapi import APIRouter
from services.geo_service import get_geo_info

router = APIRouter()

@router.get("/geolocation/{ip_address}")
async def geolocation(ip_address: str):
    return get_geo_info(ip_address)
