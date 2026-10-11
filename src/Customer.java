/**
 * Sleeping Barber Problem - Customer Thread
 *
 * This class represents a customer in the barber shop simulation.
 * Each customer thread simulates the behavior of a customer arriving
 * at the shop, waiting for service, and calculating satisfaction based
 * on waiting time.
 *
 * Customer behavior:
 * - Arrives at random intervals
 * - Attempts to sit in waiting chair
 * - Leaves if no chairs available
 * - Gets haircut when barber is available
 * - Calculates satisfaction based on wait time
 *
 * @author Sleeping Barber Simulation Team
 * @version 1.0
 * @since 2025
 */
public class Customer extends Thread {
    private int id;
    private boolean gotHaircut = false;
    private int satisfaction = 0;
    private long arrivalTime; // Arrival time
    private long serviceStartTime; // Service start time

    public Customer(int id) {
        this.id = id;
        this.arrivalTime = System.currentTimeMillis();
        setName("Customer-" + id);
    }

    @Override
    public void run() {
        try {
            System.out.println("\nCustomer " + id + " arrives at the shop");

            BarberShop.mutex.acquire();

            if (BarberShop.freeSeats == 0) {
                System.out.println("Customer " + id + ": NO EMPTY CHAIRS! Leaving disappointed...");
                BarberShop.totalCustomersLeft++;
                BarberShop.mutex.release();
                BarberShop.displayStatus();
                return;
            }

            // Sit in the chair
            BarberShop.freeSeats--;
            BarberShop.waitingQueue.add(id);

            System.out.println("Customer " + id + " sat in waiting chair");
            System.out.println("   Queue position: " + BarberShop.waitingQueue.size());
            BarberShop.displayStatus();

            // Wake up barber if sleeping
            if (BarberShop.isBarberSleeping) {
                System.out.println("Customer " + id + " wakes up the barber!");
                BarberShop.customers.release();
            }

            BarberShop.mutex.release();

            // Waiting for haircut
            System.out.println("Customer " + id + " waiting for haircut...");
            BarberShop.barber.acquire();

            // Service started
            serviceStartTime = System.currentTimeMillis();

            gotHaircut = true;
            // Calculate satisfaction based on actual waiting time
            long totalWaitTime = serviceStartTime - arrivalTime; // Total wait (ms)
            int waitSeconds = (int)(totalWaitTime / 1000); // Convert to seconds

            // Satisfaction based on wait time (0-10 minutes ideal)
            if (waitSeconds <= 2) {
                satisfaction = 9 + (int)(Math.random() * 2); // 9-10
            } else if (waitSeconds <= 5) {
                satisfaction = 7 + (int)(Math.random() * 2); // 7-8
            } else if (waitSeconds <= 10) {
                satisfaction = 4 + (int)(Math.random() * 3); // 4-6
            } else {
                satisfaction = 1 + (int)(Math.random() * 3); // 1-3
            }

            satisfaction = Math.max(1, Math.min(10, satisfaction));
            BarberShop.updateSatisfaction(satisfaction);
            System.out.println("Customer " + id + " got haircut and leaving! Waited: " + waitSeconds + "s, Satisfaction: " + satisfaction + "/10");

        } catch (InterruptedException e) {
            System.out.println("Customer " + id + " interrupted while waiting");
        }
    }

    public boolean gotHaircut() {
        return gotHaircut;
    }
}