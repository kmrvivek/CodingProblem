package com.machinecoding.parking_lot.main;

import com.machinecoding.parking_lot.model.ParkingLot;
import com.machinecoding.parking_lot.model.VehicleType;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ParkingLotMain {

    public static void main(String[] args) throws IOException {

        Map<String, Integer> userCommands = new HashMap<>();
        userCommands.put("create_parking_lot", 1);
        userCommands.put("display free_count", 2);
        userCommands.put("display free_slots", 3);
        userCommands.put("display occupied_slots", 4);
        userCommands.put("park_vehicle", 5);
        userCommands.put("unpark_vehicle", 6);
        userCommands.put("exit", 7);

        ParkingLotService parkingLotService = new ParkingLotServiceImpl();
        Map<String, ParkingLot> parkingLots = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader("parking_lot.txt"));
        String input;
        while ((input = br.readLine()) != null) {
            String[] commands = input.split(" ");
            if (commands[0].equalsIgnoreCase("display")) {
                followCommand(userCommands.get(commands[0] + " " + commands[1]), commands, parkingLots, parkingLotService);
            } else {
                followCommand(userCommands.get(commands[0]), commands, parkingLots, parkingLotService);
            }
        }
        br.close();
    }

    private static void followCommand(Integer userInput, String[] commands, Map<String, ParkingLot> parkingLots, ParkingLotService parkingLotService) {
        switch (userInput) {
            case 1:
                String parkingLotId = commands[1];
                Integer floor = Integer.parseInt(commands[2]);
                Integer slots = Integer.parseInt(commands[3]);
                ParkingLot parkingLot = parkingLotService.createParkLot(parkingLotId, floor, slots);
                parkingLots.put(parkingLotId, parkingLot);
                break;
            case 2:
                parkingLotService.displayFreePark(VehicleType.valueOf(commands[2].trim()),
                        parkingLots.get("PR1234"));
                break;
            case 3:
                parkingLotService.displayFreeSlots(VehicleType.valueOf(commands[2].trim()),
                        parkingLots.get("PR1234"));
                break;
            case 4:
                parkingLotService.displayOccupiedSlots(VehicleType.valueOf(commands[2].trim()),
                        parkingLots.get("PR1234"));
                break;
            case 5:
                String regNo = commands[2];
                String color = commands[3];
                parkingLotService.parkVehicle(parkingLots.get("PR1234"),
                        VehicleType.valueOf(commands[1].trim()), regNo, color);
                break;
            case 6:
                String parkId = commands[1];
                parkingLotService.unParkVehicle(parkingLots, parkId);
                break;
            default:
                break;
        }
    }
}
