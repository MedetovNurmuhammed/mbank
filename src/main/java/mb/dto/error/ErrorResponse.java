package mb.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {
    private String error;
    private String message;

    public String toXml() {
        return "<error>" +
                "<type>" + error + "</type>" +
                "<message>" + message + "</message>" +
                "</error>";
}}
