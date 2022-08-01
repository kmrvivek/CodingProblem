package com.machinecoding.parking_lot.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class ParkingLot {

    private final String id;
    private int floors;
    private Map<Integer,Map<VehicleType, Slots>> parkingSlots;
    private int totalSlots;
    private Set<Vehicle> vehicles;


    public ParkingLot(String id, int floors, int totalSlots, Map<Integer,Map<VehicleType, Slots>> parkingSlots){
        this.id = id;
        this.floors = floors;
        this.totalSlots = totalSlots;
        this.parkingSlots = parkingSlots;
        this.vehicles = new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingLot that = (ParkingLot) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
