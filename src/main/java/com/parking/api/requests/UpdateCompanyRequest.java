package com.parking.api.requests;

public record UpdateCompanyRequest(
    String name,
    String address,
    String phone,
    Integer motorcycleParkingSpace,
    Integer carParkingSpace) {
}
