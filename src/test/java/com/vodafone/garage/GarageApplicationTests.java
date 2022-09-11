package com.vodafone.garage;

import com.vodafone.garage.dto.Ticket;
import com.vodafone.garage.dto.Vehicle;
import com.vodafone.garage.dto.VehicleType;
import com.vodafone.garage.exception.NotAvailablePlaceException;
import com.vodafone.garage.service.GarageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;

@SpringBootTest
@Slf4j

class GarageApplicationTests {

	@Autowired
	GarageService garageService;

	@Test
	void contextLoads() {
	}

	@Test
	void testParkingAndLeavingVehicles()
	{
		Vehicle blackVehicle = Vehicle.builder().colour("Black").Type(VehicleType.CAR).plate("34-SO-1988").build();
		garageService.park(blackVehicle);
		Vehicle redVehicle = Vehicle.builder().colour("Red").Type(VehicleType.TRUCK).plate("34-BO-1987").build();
		garageService.park(redVehicle);
		Vehicle blueVehicle = Vehicle.builder().colour("Blue").Type(VehicleType.JEEP).plate("34-VO-2018").build();
		Ticket blueVehicleTicket = garageService.park(blueVehicle);

		Vehicle blackTruck = Vehicle.builder().colour("Black").Type(VehicleType.TRUCK).plate("34-HBO-2020").build();
		try {
			garageService.park(blackTruck);
		} catch (Exception e) {
			assert (e instanceof NotAvailablePlaceException);
		}

		garageService.leave(blueVehicleTicket.getId());

		Vehicle whiteCar = Vehicle.builder().colour("White").Type(VehicleType.CAR).plate("34-LO-2000").build();
		garageService.park(whiteCar);

		Collection<Ticket> status = garageService.status();

		for (Ticket ticket : status) {
			String plate = ticket.getVehicle().getPlate();
			switch (plate)
			{
				case "34-SO-1988":
					assert ticket.getVehicleFirstSlotPlace()==1;
					break;
				case "34-BO-1987":
					assert ticket.getVehicleFirstSlotPlace()==3;
					break;
				case "34-LO-2000":
					assert ticket.getVehicleFirstSlotPlace()==8;
					break;
			}
		}

	}

}
