/**
 * Sleeping Barber Problem - Barber Thread
 *
 * This class represents the barber thread in the barber shop.
 * The barber waits for customers, sleeps, and cuts hair.
 */
public class Barber extends Thread {
    private volatile boolean running = true;
    private static final int MAX_RETRIES = 3;

    @Override
    public void run() {
        System.out.println("Barber thread started!");

        while (running && !isInterrupted()) {
            try {
                BarberShop.mutex.acquire();

                if (BarberShop.waitingQueue.isEmpty()) {
                    if (BarberShop.simulationFinished) {
                        // Simulation finished, permanent sleep
                        BarberShop.isBarberSleeping = true;
                        System.out.println("\nSimulation finished! Barber is sleeping permanently...");
                        BarberShop.displayStatus();
                        BarberShop.mutex.release();
                        break; // Exit loop
                    }
                    // Sleep mode
                    BarberShop.isBarberSleeping = true;
                    System.out.println("\nBarber is sleeping...");
                    BarberShop.displayStatus();
                    BarberShop.mutex.release();

                    // Customer waiting
                    System.out.println("Waiting for customers...");
                    BarberShop.customers.acquire();

                    // Woke up, acquire mutex again
                    BarberShop.mutex.acquire();
                }

                BarberShop.isBarberSleeping = false;

                // Get customer
                Integer customerObj = BarberShop.waitingQueue.poll();
                if (customerObj == null) {
                    BarberShop.mutex.release();
                    continue;
                }
                int customer = customerObj;
                BarberShop.freeSeats++;
                BarberShop.currentCustomer = customer;

                System.out.println("\nBarber picked Customer " + customer + " from queue");
                BarberShop.displayStatus();

                // Release barber semaphore
                BarberShop.barber.release();
                BarberShop.mutex.release();

                // Haircut process
                cutHair(customer);

                // Process completed
                BarberShop.totalCustomersServed++;
                BarberShop.currentCustomer = 0;
                System.out.println("Customer " + customer + " left happily!");

            } catch (InterruptedException e) {
                System.out.println("Barber thread interrupted");
                running = false;
                break;
            } catch (Exception e) {
                System.err.println("Unexpected error in barber thread: " + e.getMessage());
                e.printStackTrace();
                try {
                    Thread.sleep(1000); // Wait after error
                } catch (InterruptedException ie) {
                    running = false;
                    break;
                }
            }
        }
        System.out.println("Barber thread ended");
    }

    private void cutHair(int customerId) throws InterruptedException {
        System.out.println("Cutting hair of Customer " + customerId + "...");

        // Use configured time (3-6 seconds range)
        int cutTime = BarberShop.MIN_HAIRCUT_TIME +
                     (int)(Math.random() * (BarberShop.MAX_HAIRCUT_TIME - BarberShop.MIN_HAIRCUT_TIME));

        for (int i = 0; i < 5; i++) {
            if (!running) break;
            System.out.println("   Snip snip... (" + (i+1) + "/5)");
            Thread.sleep(cutTime / 5);
        }

        System.out.println("Customer " + customerId + "'s haircut is done!");
    }

    public void stopBarber() {
        running = false;
        this.interrupt();
    }
}