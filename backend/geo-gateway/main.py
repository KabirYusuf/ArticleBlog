from fastapi import FastAPI
from controllers.geo_controller import router as geo_router

app = FastAPI()

app.include_router(geo_router)
