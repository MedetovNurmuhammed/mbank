package mb.service.impl;

import lombok.RequiredArgsConstructor;
import mb.dto.PayRequest;
import mb.entities.Payment;
import mb.entities.User;
import mb.repository.PaymentRepository;
import mb.repository.UserRepository;
import mb.service.PaymentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Override
    public Payment processPayment(PayRequest payRequest) {
        User currentUser = getCurrentUser();
        logger.info("Processing payment for user: {}", currentUser.getEmail()); // Логирование начала обработки
        Payment payment = new Payment();
        payment.setTransactionId(payRequest.getTransactionId());
        payment.setAmount(payRequest.getAmount());
        payment.setDescription(payRequest.getDescription());
        payment.setUser(currentUser); //
        Payment savedPayment = paymentRepository.save(payment);
        logger.info("Payment processed successfully with ID: {}", savedPayment.getId()); // Логирование успешного сохранения
        return savedPayment;

    }

    @Override
    public Payment findByTransactionId(String transactionId) {
        logger.info("Finding payment with transaction ID: {}", transactionId); // Логирование поиска
        return paymentRepository.findByTransactionId(transactionId).orElse(null);
    }
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        logger.error("User not found with email: {}", email); // Логирование ошибки
                        return new RuntimeException("User not found");
                    });
        }
        throw new RuntimeException("User is not authenticated");
    }
}
