package ParkingLotImpl;

import Utils.VehicleType;
import VehicleImpl.Vehicles;

import java.util.HashMap;
import java.util.Map;

public class ParkingLot {
    private int totalMotorcycleSpots;
    private int totalCarSpots;
    private int totalVanSpots;
    private int remainingMotorcycleSpots;
    private int remainingCarSpots;
    private int remainingVanSpots;
    private Map<VehicleType, Integer> spotsTaken;
    private Map<Integer, Vehicles> parkedVehicles;
    private boolean[] carSpotAvailability;

    public ParkingLot(int motorcycleSpots, int carSpots, int vanSpots) {
        this.totalMotorcycleSpots = motorcycleSpots;
        this.totalCarSpots = carSpots;
        this.totalVanSpots = vanSpots;
        this.remainingMotorcycleSpots = motorcycleSpots;
        this.remainingCarSpots = carSpots;
        this.remainingVanSpots = vanSpots;
        this.parkedVehicles = new HashMap<>();
        this.spotsTaken = new HashMap<>();
        spotsTaken.put(VehicleType.Motorcycle, 0);
        spotsTaken.put(VehicleType.Car, 0);
        spotsTaken.put(VehicleType.Van, 0);
        this.carSpotAvailability = new boolean[carSpots];
    }

    // Parks specific VehicleType in their specific places
    public void parkVehicle(VehicleType type) {
        Vehicles vehicles = new Vehicles();
        if (type == VehicleType.Motorcycle) {
            if(remainingMotorcycleSpots > 0) {
                parkVehicleInSpot(vehicles, type, VehicleType.Motorcycle);
                remainingMotorcycleSpots--;
            } else if (remainingCarSpots > 0) {
                int spotIndex = findAvailableCarSpot();
                if (spotIndex != -1) {
                    parkVehicleInSpot(vehicles, VehicleType.Car, VehicleType.Motorcycle);
                    carSpotAvailability[spotIndex] = true;
                    remainingCarSpots--;
                }
            } else if (remainingVanSpots > 0) {
                parkVehicleInSpot(vehicles, VehicleType.Van, VehicleType.Motorcycle);
                remainingVanSpots--;
            }
            else {
                System.out.println("No available spots for " + type + " with ID " + vehicles.getVehicleId());
            }
        } else if (type == VehicleType.Car) {
            int spotIndex = findAvailableCarSpot();
            if (spotIndex != -1) {
                parkVehicleInSpot(vehicles, VehicleType.Car, spotIndex, VehicleType.Car);
                carSpotAvailability[spotIndex] = true;
                remainingCarSpots--;
            } else if (remainingVanSpots > 0) {
                parkVehicleInSpot(vehicles, VehicleType.Van, VehicleType.Car);
                remainingVanSpots--;
            } else {
                System.out.println("No available spots for " + type + " with ID " + vehicles.getVehicleId());
            }
        } else if (type == VehicleType.Van) {
            if(remainingVanSpots > 0) {
                parkVehicleInSpot(vehicles, type, VehicleType.Van);
                remainingVanSpots--;
            } else if (remainingCarSpots >= 3) {
                int startIndex = findAvailableConsecutiveCarSpots(3);
                if (startIndex != -1) {
                    parkVehicleInSpot(vehicles, type, startIndex, 3);
                    markCarSpotsOccupied(startIndex, 3);
                    remainingCarSpots = remainingCarSpots - 3;
                }
            }
            else {
                System.out.println("No available spots for VAN " + " with ID " + vehicles.getVehicleId());
            }
        } else {
            System.out.println("No available spots for " + type);
        }
    }

    public void unparkVehicle(VehicleType type) {
        if (spotsTaken.get(type) > 0) {
            spotsTaken.put(type, spotsTaken.get(type) - 1);

            if (type == VehicleType.Motorcycle) {
                remainingMotorcycleSpots++;
            } else if (type == VehicleType.Car) {
                remainingCarSpots++;
            } else if (type == VehicleType.Van) {
                remainingVanSpots++;
            }

            System.out.println(type + " unparked. Remaining spots: " + getRemainingSpots());
        } else {
            System.out.println("No " + type + " to unpark.");
        }
    }

    // this function updates the spotsTaken map
    private void parkVehicleInSpot(Vehicles vehicle, VehicleType type, VehicleType originalType) {
        int spotIndex = spotsTaken.get(type);
        parkVehicleInSpot(vehicle, type, spotIndex, originalType);
        spotsTaken.put(type, spotIndex + 1);
    }

    // this function prints VehicleType with their ID and where the vehicle is parked
    private void parkVehicleInSpot(Vehicles vehicle, VehicleType type, int spotIndex, VehicleType originalType) {
        System.out.println(originalType + " with ID " + vehicle.getVehicleId() + " parked in " + type + " spot " + spotIndex);
    }

    // this function is called when a van is parked in 3 adjacent car spots
    private void parkVehicleInSpot(Vehicles vehicle, VehicleType type, int startIndex, int consecutiveSpots) {
        for (int i = 0; i < consecutiveSpots; i++) {
            parkVehicleInSpot(vehicle, VehicleType.Car, startIndex + i, VehicleType.Van);
        }
    }

    // used to find a free car spot using bool array
    private int findAvailableCarSpot() {
        for (int i = 0; i < carSpotAvailability.length; i++) {
            if (!carSpotAvailability[i]) {
                return i;
            }
        }
        return -1;
    }

    // used to find 3 consecutive car spots for the van to be parked
    private int findAvailableConsecutiveCarSpots(int consecutiveSpots) {
        for (int i = 0; i <= carSpotAvailability.length - consecutiveSpots; i++) {
            boolean available = true;
            for (int j = 0; j < consecutiveSpots; j++) {
                if (carSpotAvailability[i + j]) {
                    available = false;
                    break;
                }
            }
            if (available) {
                return i;
            }
        }
        return -1;
    }

    // marks 3 positions in the bool array when a van fills 3 adjacent car spots
    private void markCarSpotsOccupied(int startIndex, int consecutiveSpots) {
        for (int i = 0; i < consecutiveSpots; i++) {
            carSpotAvailability[startIndex + i] = true;
        }
    }

    public int getRemainingSpots() {
        return remainingMotorcycleSpots + remainingCarSpots + remainingVanSpots;
    }

    public int getTotalSpots() {
        return totalMotorcycleSpots + totalCarSpots + totalVanSpots;
    }

    public boolean isFull() {
        return getRemainingSpots() == 0;
    }

    public boolean isEmpty() {
        return getRemainingSpots() == getTotalSpots();
    }

    public boolean areMotorcycleSpotsFull() {
        return spotsTaken.get(VehicleType.Motorcycle) == totalMotorcycleSpots;
    }

    public boolean areCarSpotsFull() {
        return spotsTaken.get(VehicleType.Car) == totalCarSpots;
    }

    public boolean areVanSpotsFull() {
        return spotsTaken.get(VehicleType.Van) == totalVanSpots;
    }

    public int getMotorcycleSpotsTaken() {
        return spotsTaken.get(VehicleType.Motorcycle);
    }

    public int getCarSpotsTaken() {
        return spotsTaken.get(VehicleType.Car);
    }

    public int getVanSpotsTaken() {
        return spotsTaken.get(VehicleType.Van);
    }
}
