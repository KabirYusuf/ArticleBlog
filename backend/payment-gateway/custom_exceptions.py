class PaymentProcessingError(Exception):
    def __init__(self, message="Error processing payment"):
        self.message = message
        super().__init__(self.message)

class CardValidationError(Exception):
    def __init__(self, message="Invalid card details"):
        self.message = message
        super().__init__(self.message)

class CVVValidationError(Exception):
    def __init__(self, message="Invalid CVV"):
        self.message = message
        super().__init__(self.message)
