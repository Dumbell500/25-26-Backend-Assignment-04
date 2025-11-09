package com.gdg.jwtexample.dto.idcard;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record IDCardSaveRequest(
        @NotBlank String realName,
        @NotBlank String nationalId,
        LocalDate birth
) {
}
