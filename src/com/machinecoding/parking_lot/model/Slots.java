package com.machinecoding.parking_lot.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Slots {

    private int space;
    private VehicleType type;
    private Map<Integer, Boolean> slotsAvailable;
    private Map<Integer, Vehicle> slotVehicle = new HashMap<>();
    private int floorNo;

    public Slots(int space, VehicleType type) {
        this.space = space;
        this.type = type;
    }

}
