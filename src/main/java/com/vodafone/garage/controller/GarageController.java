package com.vodafone.garage.controller;

import com.vodafone.garage.dto.Ticket;
import com.vodafone.garage.dto.Vehicle;
import com.vodafone.garage.service.GarageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("garage")
@RequiredArgsConstructor
public class GarageController {

    private final GarageService garageService;

    @PostMapping("/park")
    public Ticket park(@RequestBody Vehicle vehicle) {
        return garageService.park(vehicle);
    }

    @DeleteMapping("/leave/{ticketId}")
    public void leave(@PathVariable("ticketId") String ticketId) {
        garageService.leave(ticketId);
    }

    @GetMapping("/status")
    public Collection<Ticket> status() {
        return garageService.status();
    }
}
