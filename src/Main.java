import ParkingLotImpl.ParkingLot;
import Utils.VehicleType;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Change the number of parking spots by changing the specific arguments.
        ParkingLot parkingLot = new ParkingLot(3, 2, 1);

        parkingLot.parkVehicle(VehicleType.Motorcycle);
        parkingLot.parkVehicle(VehicleType.Car);
        parkingLot.parkVehicle(VehicleType.Car);
        parkingLot.parkVehicle(VehicleType.Van);
        parkingLot.parkVehicle(VehicleType.Car);


        System.out.println('\n');

        System.out.println("Remaining spots: " + parkingLot.getRemainingSpots());
        System.out.println("Total spots: " + parkingLot.getTotalSpots());
        System.out.println("Parking lot full: " + parkingLot.isFull());
        System.out.println("Parking lot empty: " + parkingLot.isEmpty());

        System.out.println('\n');

        System.out.println("Motorcycle spots full: " + parkingLot.areMotorcycleSpotsFull());
        System.out.println("Van spots full: " + parkingLot.areVanSpotsFull());
        System.out.println("Motorcycle spots taken: " + parkingLot.getMotorcycleSpotsTaken());
        System.out.println("Van spots taken: " + parkingLot.getVanSpotsTaken());

        System.out.println('\n');

        parkingLot.unparkVehicle(VehicleType.Motorcycle);
        parkingLot.unparkVehicle(VehicleType.Van);

        System.out.println("Remaining spots: " + parkingLot.getRemainingSpots());
    }
}