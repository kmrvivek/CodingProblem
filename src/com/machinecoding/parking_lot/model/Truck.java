package com.machinecoding.parking_lot.model;

public class Truck implements Vehicle {

    private final VehicleType type;
    private final String registrationNumber;
    private final String color;

    public Truck(String registrationNumber, String color){
        this.type = VehicleType.TRUCK;
        this.registrationNumber = registrationNumber;
        this.color = color;
    }

    @Override
    public void display() {
        System.out.println(type +" - "+ registrationNumber+" - "+ color);
    }

    @Override
    public String getRegistration() {
        return this.registrationNumber;
    }

    @Override
    public String getColor() {
        return this.color;
    }
}
