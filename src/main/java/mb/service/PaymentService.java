package mb.service;

import mb.dto.PayRequest;
import mb.entities.Payment;

public interface PaymentService {
    Payment processPayment(PayRequest payRequest);

    Payment findByTransactionId(String transactionId);
}
