package com.machinecoding.parking_lot.main;

import com.machinecoding.parking_lot.model.ParkingLot;
import com.machinecoding.parking_lot.model.VehicleType;

import java.util.Map;

public interface ParkingLotService {

    public ParkingLot createParkLot(String parkingId, int floors, int slots);
    public boolean parkVehicle(ParkingLot parkingLot, VehicleType type, String regNo, String color);
    public void unParkVehicle(Map<String, ParkingLot> parkingLots, String parkId);
    public void displayFreePark(VehicleType type, ParkingLot parkingLot);
    public void displayFreeSlots(VehicleType type, ParkingLot parkingLot);
    public void displayOccupiedSlots(VehicleType type, ParkingLot parkingLot);

}
