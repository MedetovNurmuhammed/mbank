package mb.dto.request;

import lombok.Data;

@Data
public class PaymentRequest {
    private String stripeToken;
    private Double amount;
    private String username;
}
