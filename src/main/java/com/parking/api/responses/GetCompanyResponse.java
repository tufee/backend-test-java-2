package com.parking.api.responses;

import java.time.OffsetDateTime;

public record GetCompanyResponse(
        Long id,
        String name,
        String cnpj,
        String address,
        String phone,
        Integer motorcycleParkingSpace,
        Integer carParkingSpace,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {
}
