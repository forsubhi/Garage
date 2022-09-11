package com.vodafone.garage.dto;

import lombok.Getter;

public enum VehicleType {
    CAR(1), TRUCK(4), JEEP(2);

    @Getter
    private final int slots;

    VehicleType(int slots) {
        this.slots = slots;
    }
}
