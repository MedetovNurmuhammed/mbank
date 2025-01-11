package mb.dto;

import lombok.Data;

@Data
public class PayRequest {
    private String transactionId;
    private double amount;
    private String description;
}
