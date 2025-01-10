package mb.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import mb.enums.Role;
import org.springframework.http.HttpStatus;
@Builder
public record SignInResponse (Long id,
                             String token,
                             String email,
                             @Enumerated(EnumType.STRING)
                             Role role,
                             HttpStatus httpStatus,
                             String message

) {
}
