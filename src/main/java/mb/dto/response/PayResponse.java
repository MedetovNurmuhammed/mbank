package mb.dto.response;

import lombok.Data;
import mb.entities.Payment;

@Data
public class PayResponse {
    private String status;
    private String transactionId;

    public PayResponse(Payment payment) {
        this.status = "success";
        this.transactionId = payment.getTransactionId();
    }

    public String toXml() {
        return "<response>" +
                "<status>" + status + "</status>" +
                "<transactionId>" + transactionId + "</transactionId>" +
                "</response>";
    }
}
