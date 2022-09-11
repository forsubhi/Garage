package com.vodafone.garage.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Vehicle {
    VehicleType Type;
    String plate;
    String colour;
}
