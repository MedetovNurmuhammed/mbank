package mb.api;

import lombok.RequiredArgsConstructor;
import mb.dto.PayRequest;
import mb.dto.error.ErrorResponse;
import mb.dto.request.CheckRequest;
import mb.dto.response.CheckResponse;
import mb.dto.response.PayResponse;
import mb.entities.Payment;
import mb.exceptions.UnauthorizedAccessException;
import mb.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pay")
public class PaymentApi {

    private final PaymentService paymentService;
    private static final Logger logger = LoggerFactory.getLogger(PaymentApi.class);

    @PostMapping("/pay")
    @Secured("USER")
    public ResponseEntity<String> pay(@RequestBody PayRequest payRequest) {
        try {
            logger.info("Received payment request: {}", payRequest);

            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                throw new UnauthorizedAccessException("User is not authenticated");
            }

            // Обрабатываем платеж
            Payment payment = paymentService.processPayment(payRequest);
            PayResponse payResponse = new PayResponse(payment);
            logger.info("Payment processed successfully: {}", payment.getTransactionId());
            return ResponseEntity.ok(payResponse.toXml());

        } catch (UnauthorizedAccessException e) {
            logger.error("Unauthorized access attempt", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("Unauthorized Access", e.getMessage()).toXml());
        } catch (Exception e) {
            logger.error("Error processing payment", e);
            return ResponseEntity.badRequest().body(new ErrorResponse("Invalid Request", e.getMessage()).toXml());
        }
    }

    @PostMapping("/check")
    @Secured("USER")
    public ResponseEntity<String> check(@RequestBody CheckRequest checkRequest) {
        try {
            logger.info("Checking payment for transaction ID: {}", checkRequest.getTransactionId());

            Payment payment = paymentService.findByTransactionId(checkRequest.getTransactionId());
            if (payment == null) {
                throw new IllegalArgumentException("Transaction not found");
            }

            CheckResponse checkResponse = new CheckResponse(payment);
            return ResponseEntity.ok(checkResponse.toXml());
        } catch (Exception e) {
            logger.error("Error checking transaction", e);
            return ResponseEntity.badRequest().body(new ErrorResponse("Invalid Request", e.getMessage()).toXml());
        }
    }
}
