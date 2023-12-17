from data.dto.requests.payment_details import PaymentDetails
from custom_exceptions import CardValidationError, CVVValidationError, PaymentProcessingError
from fastapi.responses import JSONResponse
from fastapi import status

CardType = str

def process_payment(payment_details: PaymentDetails) -> dict:
    # TODO: actual processing would go here
    return JSONResponse({}, status_code=status.HTTP_200_OK)

def validate_card_number(payment_details: PaymentDetails) -> CardType:
    normalized_card_number = payment_details.card_number.replace(" ", "")

    if not normalized_card_number:
        raise CardValidationError("Card number must be supplied")

    if not all(map(str.isdigit, normalized_card_number)):
        raise CardValidationError("Card number must consist only of numbers")

    if len(normalized_card_number) != 16:
        raise CardValidationError("Card number must be exactly 16 digits")

    if not luhn_algorithm_valid(normalized_card_number):
        raise CardValidationError("Invalid card number (failed Luhn check)")

    if normalized_card_number.startswith("4"):
        return "visa"
    elif normalized_card_number.startswith("35") or normalized_card_number.startswith("37"):
        return "mastercard"

    raise CardValidationError("Unsupported card type")

def validate_cvv(payment_details: PaymentDetails, card_type: CardType) -> None:
    normalized_cvv = payment_details.card_cvv.replace(" ", "")

    if not normalized_cvv:
        raise CVVValidationError("CVV must be supplied")

    if not all(map(str.isdigit, normalized_cvv)):
        raise CVVValidationError("CVV must consist only of numbers")

    if card_type == "mastercard" and len(normalized_cvv) != 4:
        raise CVVValidationError("Mastercard CVV should have exactly 4 digits")

    if card_type != "mastercard" and len(normalized_cvv) != 3:
        raise CVVValidationError("CVV should have exactly 3 digits")

def luhn_algorithm_valid(number: str) -> bool:
    def digits_of(n: str):
        return [int(d) for d in n]

    digits = digits_of(number)
    odd_digits = digits[-1::-2]
    even_digits = digits[-2::-2]
    checksum = sum(odd_digits, 0)
    for d in even_digits:
        checksum += sum(digits_of(str(d * 2)))

    return checksum % 10 == 0
