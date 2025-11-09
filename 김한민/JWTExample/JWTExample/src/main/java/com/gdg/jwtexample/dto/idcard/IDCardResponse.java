package com.gdg.jwtexample.dto.idcard;

import com.gdg.jwtexample.domain.IDCard;
import java.time.LocalDate;

public record IDCardResponse(
        Long id,
        String realName,
        String nationalId,
        LocalDate birth
) {
    public static IDCardResponse from(IDCard c) {
        return new IDCardResponse(c.getId(), c.getRealName(), c.getNationalId(), c.getBirth());
    }
}
