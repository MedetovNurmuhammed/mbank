package mb.dto.response;


import lombok.Data;
import mb.entities.Payment;

@Data
    public class CheckResponse {
        private String transactionId;
        private double amount;
        private String description;
        private String status;
        private String date;

        public CheckResponse(Payment payment) {
            this.transactionId = payment.getTransactionId();
            this.amount = payment.getAmount();
            this.description = payment.getDescription();
            this.status = "Completed";
            this.date = payment.getTimestamp().toString();
        }

        public String toXml() {
            return "<receipt>" +
                    "<transactionId>" + transactionId + "</transactionId>" +
                    "<amount>" + amount + "</amount>" +
                    "<description>" + description + "</description>" +
                    "<status>" + status + "</status>" +
                    "<date>" + date + "</date>" +
                    "</receipt>";
        }
    }


