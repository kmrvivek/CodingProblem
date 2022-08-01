package com.machinecoding.parking_lot.model;

public class Bike implements Vehicle {

    private final VehicleType type;
    private final String registrationNumber;
    private final String color;

    public Bike(String registrationNumber, String color){
        this.type = VehicleType.BIKE;
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
