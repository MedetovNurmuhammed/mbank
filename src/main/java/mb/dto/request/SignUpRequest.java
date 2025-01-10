package mb.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import mb.enums.Role;

public record SignUpRequest(
        @NotBlank(message = "Содержимое объявления не должно быть пустым")
        String firstName,
        @NotBlank(message = "Содержимое объявления не должно быть пустым")
        String lastName,
        @Column(unique = true)
        @NotBlank(message = "Содержимое объявления не должно быть пустым")
        String email,
        @NotBlank(message = "Содержимое объявления не должно быть пустым")
        String password,
        @NotBlank(message = "Содержимое объявления не должно быть пустым")
        String phoneNumber
) {
}
