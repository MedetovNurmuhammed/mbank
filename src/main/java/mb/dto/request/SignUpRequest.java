package mb.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

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
