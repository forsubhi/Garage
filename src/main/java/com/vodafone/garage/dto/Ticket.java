package com.vodafone.garage.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ticket {
    String id;
    Vehicle vehicle;
    @JsonIgnore
    int vehicleFirstSlot;

    @JsonProperty("vehicleFirstSlotPlace")
    public int getVehicleFirstSlotPlace() {
        return vehicleFirstSlot + 1;
    }
}
