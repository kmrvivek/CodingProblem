package com.machinecoding.parking_lot.model;

public class Car implements Vehicle{

    private final VehicleType type;
    private final String registrationNumber;
    private final String color;

    public Car(String registrationNumber, String color){
        this.type = VehicleType.CAR;
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
