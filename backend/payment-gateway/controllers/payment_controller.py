from fastapi import APIRouter, HTTPException, status
from data.dto.requests.payment_details import PaymentDetails
from services.payment_service import process_payment, validate_card_number, validate_cvv
from custom_exceptions import CardValidationError, CVVValidationError, PaymentProcessingError

router = APIRouter()

@router.post("/payments")
async def execute_payment(payment_details: PaymentDetails):
    try:
        card_type = validate_card_number(payment_details)
        validate_cvv(payment_details, card_type)
        return process_payment(payment_details)
    except CardValidationError as e:
        raise HTTPException(status_code=status.HTTP_422_UNPROCESSABLE_ENTITY, detail=str(e))
    except CVVValidationError as e:
        raise HTTPException(status_code=status.HTTP_422_UNPROCESSABLE_ENTITY, detail=str(e))
    except PaymentProcessingError as e:
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail="Payment processing failed")
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_500_INTERNAL_SERVER_ERROR, detail="An unexpected error occurred")

