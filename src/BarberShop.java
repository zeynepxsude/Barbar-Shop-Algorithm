/**
 * Sleeping Barber Problem - Shared State Management
 *
 * This class manages the shared state and resources for the barber shop simulation.
 * It contains all semaphores, configuration constants, and synchronized methods
 * that coordinate access between barber and customer threads.
 *
 * Key components:
 * - Semaphore management (customers, barber, mutex)
 * - Configuration constants (timing, capacity)
 * - Shared data structures (waiting queue, statistics)
 * - Thread-safe status display and statistics methods
 *
 * @author Sleeping Barber Simulation Team
 * @version 1.0
 * @since 2025
 */
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class BarberShop {
    // ==================== CONFIGURATION ====================
    public static final int CHAIRS = 5;
    public static final int TOTAL_CUSTOMERS = 20;
    public static final int MIN_HAIRCUT_TIME = 3000; // ms
    public static final int MAX_HAIRCUT_TIME = 6000; // ms
    public static final int CUSTOMER_ARRIVAL_DELAY = 1500; // ms

    // ==================== SEMAPHORES ====================
    public static final java.util.concurrent.Semaphore customers = new java.util.concurrent.Semaphore(0);
    public static final java.util.concurrent.Semaphore barber = new java.util.concurrent.Semaphore(0);
    public static final java.util.concurrent.Semaphore mutex = new java.util.concurrent.Semaphore(1);

    // ==================== SHARED STATE ====================
    public static int freeSeats = CHAIRS;
    public static final Queue<Integer> waitingQueue = new LinkedList<>();
    public static int currentCustomer = 0;
    public static boolean isBarberSleeping = true;
    public static int totalCustomersServed = 0;
    public static int totalCustomersLeft = 0;
    public static int customersAdded = 0;
    public static boolean simulationFinished = false;
    public static double averageSatisfaction = 0.0;
    public static int totalSatisfaction = 0;

    public static void displayStatus() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("          BARBER SHOP STATUS");
        System.out.println("=".repeat(50));

        // Barber status
        System.out.print("BARBER: ");
        if (isBarberSleeping) {
            System.out.println("SLEEPING");
        } else {
            System.out.println("WORKING (Customer " + currentCustomer + ")");
        }

        // Barber chair
        System.out.println("\nBARBER CHAIR:");
        if (currentCustomer == 0) {
            System.out.println("   [   EMPTY   ]");
        } else {
            System.out.println("   [ Customer " + currentCustomer + " ]");
        }

        // Waiting chairs
        System.out.println("\nWAITING CHAIRS (" + CHAIRS + " total):");
        System.out.print("   ");

        synchronized (waitingQueue) {
            Iterator<Integer> it = waitingQueue.iterator();
            int shown = 0;
            while (it.hasNext()) {
                int c = it.next();
                System.out.print("[C" + String.format("%02d", c) + "] ");
                shown++;
            }

            // Empty chairs
            for (int i = shown; i < CHAIRS; i++) {
                System.out.print("[ --- ] ");
            }

            System.out.println("\n\nSTATISTICS:");
            System.out.println("   Free seats: " + freeSeats + "/" + CHAIRS);
            System.out.println("   Waiting: " + waitingQueue.size() + " customers");
        }
        System.out.println("   Served: " + totalCustomersServed);
        System.out.println("   Left: " + totalCustomersLeft);
        System.out.println("   Average Satisfaction: " + String.format("%.1f", averageSatisfaction) + "/10");
        System.out.println("=".repeat(50));

        // Thread.sleep removed - shouldn't block the barber thread
    }

    public static void updateSatisfaction(int satisfaction) {
        totalSatisfaction += satisfaction;
        // Prevent division by zero
        if (totalCustomersServed > 0) {
            averageSatisfaction = (double) totalSatisfaction / totalCustomersServed;
        }
    }

    /**
     * Resets the simulation - for testing and restarting
     */
    public static void resetSimulation() {
        freeSeats = CHAIRS;
        waitingQueue.clear();
        currentCustomer = 0;
        isBarberSleeping = true;
        totalCustomersServed = 0;
        totalCustomersLeft = 0;
        customersAdded = 0;
        simulationFinished = false;
        averageSatisfaction = 0.0;
        totalSatisfaction = 0;

        // Reset semaphores (as much as possible)
        try {
            // Consume existing permits
            while (customers.tryAcquire()) {}
            while (barber.tryAcquire()) {}
            mutex.drainPermits();
            mutex.release(); // Set back to 1
        } catch (Exception e) {
            System.err.println("Warning: Could not fully reset semaphores: " + e.getMessage());
        }
    }

    /**
     * Thread-safe statistics getters
     */
    public static synchronized int getTotalCustomersServed() {
        return totalCustomersServed;
    }

    public static synchronized int getTotalCustomersLeft() {
        return totalCustomersLeft;
    }

    public static synchronized double getAverageSatisfaction() {
        return averageSatisfaction;
    }
}