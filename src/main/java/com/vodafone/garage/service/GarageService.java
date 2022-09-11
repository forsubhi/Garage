package com.vodafone.garage.service;

import com.vodafone.garage.dto.Ticket;
import com.vodafone.garage.dto.Vehicle;
import com.vodafone.garage.exception.NotAvailablePlaceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class GarageService {

    @Value("${garageSize}")
    private Integer garageSize;
    private boolean[] garageSlots;
    private ConcurrentHashMap<String, Ticket> currentTickets = new ConcurrentHashMap();
    private final static int EMPTY_SPACE_BETWEEN_VEHICLES = 1;


    public synchronized Ticket park(@RequestBody Vehicle vehicle) {
        log.info("vehicle with plate= " + vehicle.getPlate() + "is parking");
        int slots = vehicle.getType().getSlots();
        boolean availablePlace = false;
        for (int i = 0; i < garageSize - slots; i++) {
            availablePlace = checkAvailablePlace(i, slots + EMPTY_SPACE_BETWEEN_VEHICLES);
            if (availablePlace)
                return reserveTicket(i, vehicle);
        }

        throw new NotAvailablePlaceException("Cannot find a place for vehicle " + vehicle.getPlate());

    }

    private Ticket reserveTicket(int currentPlace, Vehicle car) {
        String id = UUID.randomUUID().toString();
        Ticket ticket = Ticket.builder().id(id).vehicleFirstSlot(currentPlace).vehicle(car).build();
        currentTickets.put(id, ticket);
        for (int i = currentPlace; i < currentPlace + car.getType().getSlots() + EMPTY_SPACE_BETWEEN_VEHICLES; i++) {
            garageSlots[i] = true;
        }
        return ticket;
    }


    private boolean checkAvailablePlace(int currentPlace, int slots) {
        for (int i = currentPlace; i < currentPlace + slots; i++) {
            if (garageSlots[i])
                return false;
        }
        return true;
    }


    public void leave(String ticketId) {
        log.info("vehicle with ticketId= " + ticketId + "is leaving");
        Ticket ticket = currentTickets.get(ticketId);
        if (ticket != null) {
            log.info("vehicle with plate= " + ticket.getVehicle().getPlate() + "is leaving");
            int vehicleFirstSlot = ticket.getVehicleFirstSlot();
            int slots = ticket.getVehicle().getType().getSlots();
            releaseSpace(vehicleFirstSlot, slots);
            currentTickets.remove(ticketId);
        }
    }

    synchronized private void releaseSpace(int vehicleFirstSlot, int slots) {
        for (int i = vehicleFirstSlot; i < vehicleFirstSlot + slots + EMPTY_SPACE_BETWEEN_VEHICLES; i++) {
            garageSlots[i] = false;
        }
    }


    public Collection<Ticket> status() {
        return currentTickets.values();
    }


    @PostConstruct
    public void init() {
        garageSlots = new boolean[garageSize];
    }
}
