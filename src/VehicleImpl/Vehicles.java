package VehicleImpl;

import java.util.UUID;

public class Vehicles {
    private UUID vehicleId;

    public Vehicles() {
        this.vehicleId = UUID.randomUUID();
    }

    public UUID getVehicleId() {
        return vehicleId;
    }
}
