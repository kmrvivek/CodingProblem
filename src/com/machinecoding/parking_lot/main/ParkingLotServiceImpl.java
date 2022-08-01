package com.machinecoding.parking_lot.main;

import com.machinecoding.parking_lot.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class ParkingLotServiceImpl implements ParkingLotService {

    private ParkingLot parkingLot;

    @Override
    public ParkingLot createParkLot(String parkingId, int floors, int totalSlots) {
        if (Objects.isNull(parkingLot) ||
                Objects.isNull(parkingLot.getId()) || !parkingLot.getId().equalsIgnoreCase(parkingId)) {
            Map<Integer, Map<VehicleType, Slots>> floorParkingSpots = new LinkedHashMap<>();
            int k = 1;
            while (k <= floors) {
                int[] startNum = new int[]{1};
                Map<VehicleType, Slots> parkingSlots = new HashMap<>();
                parkingSlots.put(VehicleType.TRUCK, createSlot(1, VehicleType.TRUCK, startNum));
                parkingSlots.put(VehicleType.BIKE, createSlot(2, VehicleType.BIKE, startNum));
                parkingSlots.put(VehicleType.CAR, createSlot(3, VehicleType.CAR, startNum));
                floorParkingSpots.put(k++, parkingSlots);
            }
            parkingLot = new ParkingLot(parkingId, floors, floors * totalSlots, floorParkingSpots);
            System.out.println("Created parking lot with " + floors + " floors and " +
                    totalSlots + " slots per floor");
            return parkingLot;
        }
        System.out.println("Parking lot already existed");
        return parkingLot;
    }

    private Slots createSlot(int space, VehicleType truck, int[] startNum) {
        Slots slots = new Slots(space, truck);
        Map<Integer, Boolean> slotsAvailable = new LinkedHashMap<>();
        for (int i = 1; i <= space; i++) {
            slotsAvailable.put(startNum[0]++, Boolean.TRUE);

        }
        slots.setSlotsAvailable(slotsAvailable);
        return slots;
    }

    @Override
    public boolean parkVehicle(ParkingLot parkingLot, VehicleType type, String regNo, String color) {
        Vehicle vehicle = createVehicle(type, regNo, color);
        if (parkingLot.getVehicles().contains(vehicle)) {
            System.out.println("Duplicate " + type + " " + regNo + " " + color);
            return false;
        }

        int floor = getFirstEmptySlot(parkingLot, type);
        boolean slotAvailable = false;
        if(floor != -1){
            Map<Integer, Boolean> slotsAvailable = parkingLot.getParkingSlots().get(floor).get(type).getSlotsAvailable();
            for(Map.Entry<Integer, Boolean> slot : slotsAvailable.entrySet()){
                if(slot.getValue()){
                    System.out.println("Parked vehicle. Ticket ID: "
                            + parkingLot.getId() + "_" + floor + "_" +
                            slot.getKey());
                    slot.setValue(Boolean.FALSE);
                    parkingLot.getParkingSlots().get(floor).get(type).setSpace
                            (parkingLot.getParkingSlots().get(floor).get(type).getSpace()-1);
                    parkingLot.getParkingSlots().get(floor).get(type).getSlotVehicle().put(slot.getKey(), vehicle);
                    parkingLot.getVehicles().add(vehicle);
                    slotAvailable = true;
                    break;
                }
            }
        }
        if(!slotAvailable)
            System.out.println("Parking Lot Full");
        return slotAvailable;
    }

    private int getFirstEmptySlot(ParkingLot parkingLot, VehicleType type) {
        for(int i=1; i<=parkingLot.getFloors(); i++){
            if(parkingLot.getParkingSlots().get(i).get(type).getSpace() > 0){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void unParkVehicle(Map<String, ParkingLot> parkingLots, String parkId) {
        String[] parkDetails = parkId.split("_");
        String id = parkDetails[0];
        Integer floorNo = Integer.parseInt(parkDetails[1]);
        Integer slotNo = Integer.parseInt(parkDetails[2]);
        boolean validUnPark = false;
        if (parkingLots.containsKey(id) && parkingLots.get(id).getParkingSlots().containsKey(floorNo)) {
            Map<VehicleType, Slots> vehicleTypeSlotsMap = parkingLots.get(id).getParkingSlots().get(floorNo);
            for(Map.Entry<VehicleType, Slots> vehicleTypeSlotsEntry: vehicleTypeSlotsMap.entrySet()){
                if(vehicleTypeSlotsEntry.getValue().getSlotsAvailable().containsKey(slotNo)
                && !vehicleTypeSlotsEntry.getValue().getSlotsAvailable().get(slotNo)){
                    vehicleTypeSlotsEntry.getValue().getSlotsAvailable().put(slotNo, Boolean.TRUE);
                    Vehicle vehicle = vehicleTypeSlotsEntry.getValue().getSlotVehicle().get(slotNo);
                    vehicleTypeSlotsEntry.getValue().setSpace(vehicleTypeSlotsEntry.getValue().getSpace()+1);
                    System.out.println("Unparked vehicle with Registration Number: " +
                            vehicle.getRegistration() +
                            " and Color: " + vehicle.getColor());
                    validUnPark = true;
                    break;
                }
            }
            if(!validUnPark)
            System.out.println("Invalid Ticket");
        }
    }

    @Override
    public void displayFreePark(VehicleType type, ParkingLot parkingLot) {
        Map<Integer, Map<VehicleType, Slots>> floorParkingSlots = parkingLot.getParkingSlots();
        floorParkingSlots.forEach((key, value) -> {
                System.out.println("No. of free slots for " + type + " on Floor " + key + " : " +
                        value.get(type).getSpace());
        });
    }

    @Override
    public void displayFreeSlots(VehicleType type, ParkingLot parkingLot) {
        Map<Integer, Map<VehicleType, Slots>> floorParkingSlots = parkingLot.getParkingSlots();
        floorParkingSlots.forEach((key, value) -> {
                System.out.println("Free slots for " + type + " on Floor " + key + " : " +
                        value.get(type).getSlotsAvailable().entrySet().stream()
                                .filter(Map.Entry::getValue).map(Map.Entry::getKey).collect(Collectors.toList()));
        });
    }

    @Override
    public void displayOccupiedSlots(VehicleType type, ParkingLot parkingLot) {
        Map<Integer, Map<VehicleType, Slots>> floorParkingSlots = parkingLot.getParkingSlots();
        floorParkingSlots.forEach((key, value) -> {
            List<Integer> list = new ArrayList<>();
            for (Map.Entry<Integer, Boolean> slot : value.get(type).getSlotsAvailable().entrySet()) {
                if (!slot.getValue()) {
                    list.add(slot.getKey());
                }
            }
            System.out.println("Occupied slots for " + type + " on Floor " + key + " : " +
                    list);
        });
    }


    private Vehicle createVehicle(VehicleType type, String regNo, String color) {
        switch (type) {
            case CAR:
                return new Car(regNo, color);
            case BIKE:
                return new Bike(regNo, color);
            case TRUCK:
                return new Truck(regNo, color);
        }
        return null;
    }
}
