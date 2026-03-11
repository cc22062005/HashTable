import java.util.*;

public class ParkingLot {
    private final int capacity;
    private final String[] slots;      // open addressing hash table
    private int occupiedCount;

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        this.slots    = new String[capacity];
        this.occupiedCount = 0;
    }

    private int hash(String plateNumber) {
        return Math.abs(plateNumber.hashCode()) % capacity;
    }

    public boolean parkVehicle(String plateNumber) {
        if (occupiedCount == capacity) {
            System.out.println("Parking FULL! Cannot park " + plateNumber);
            return false;
        }
        int index = hash(plateNumber);
        // Linear probing for open addressing
        while (slots[index] != null) {
            index = (index + 1) % capacity;
        }
        slots[index] = plateNumber;
        occupiedCount++;
        System.out.println("Parked: " + plateNumber + " at slot " + index);
        return true;
    }

    public boolean removeVehicle(String plateNumber) {
        int index = hash(plateNumber);
        for (int i = 0; i < capacity; i++) {
            int probe = (index + i) % capacity;
            if (plateNumber.equals(slots[probe])) {
                slots[probe] = null;
                occupiedCount--;
                System.out.println("Removed: " + plateNumber + " from slot " + probe);
                return true;
            }
        }
        System.out.println("Vehicle not found: " + plateNumber);
        return false;
    }

    public void printStatus() {
        System.out.println("\nParking Status: " + occupiedCount + "/" + capacity + " occupied");
        for (int i = 0; i < capacity; i++) {
            System.out.println("  Slot " + i + ": " + (slots[i] != null ? slots[i] : "[EMPTY]"));
        }
    }

    public static void main(String[] args) {
        ParkingLot lot = new ParkingLot(5);

        lot.parkVehicle("TN01AB1234");
        lot.parkVehicle("TN02CD5678");
        lot.parkVehicle("TN03EF9012");
        lot.parkVehicle("TN04GH3456");
        lot.parkVehicle("TN05IJ7890");
        lot.parkVehicle("TN06KL1111"); // should be full

        lot.printStatus();

        lot.removeVehicle("TN03EF9012");
        lot.parkVehicle("TN06KL1111"); // now should fit

        lot.printStatus();
    }
}
