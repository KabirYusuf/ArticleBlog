from pydantic import BaseModel
from typing import Union

class PaymentDetails(BaseModel):
    card_holder: str
    card_number: str
    card_cvv: str
    price: Union[float, None] = None
