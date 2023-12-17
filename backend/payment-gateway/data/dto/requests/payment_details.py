from pydantic import BaseModel
from decimal import Decimal
from typing import Union

class PaymentDetails(BaseModel):
    card_holder: str
    card_number: str
    card_cvv: str
    price: Union[Decimal, None] = None
