from fastapi import FastAPI
from controllers.payment_controller import router as payment_router

app = FastAPI()

app.include_router(payment_router)
