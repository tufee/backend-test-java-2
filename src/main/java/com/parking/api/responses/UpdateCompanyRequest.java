package com.parking.api.responses;

public record UpdateCompanyRequest(
        String name,
        String address,
        String phone,
        Integer motorcycleParkingSpace,
        Integer carParkingSpace) {
}
