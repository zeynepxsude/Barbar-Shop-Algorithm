/**
 * Sleeping Barber Problem - Main Application
 *
 * This is the entry point for the Sleeping Barber simulation application.
 * It initializes the GUI and starts the simulation environment.
 *
 * The Sleeping Barber problem demonstrates classic thread synchronization
 * using semaphores to coordinate barber and customer threads in a shared
 * resource scenario.
 *
 * @author Sleeping Barber Simulation Team
 * @version 1.0
 * @since 2025
 */
import javax.swing.SwingUtilities;

public class SleepingBarber {
    // ==================== MAIN METHOD ====================
    public static void main(String[] args) {
        // Launch the GUI
        SwingUtilities.invokeLater(() -> {
            System.out.println("=".repeat(60));
            System.out.println("        SLEEPING BARBER SIMULATION");
            System.out.println("=".repeat(60));
            System.out.println("Instructions:");
            System.out.println("1. Click 'Start Barber' to begin");
            System.out.println("2. Click 'Add Customer' to simulate arrivals");
            System.out.println("3. Adjust speed with the slider");
            System.out.println("4. Watch the console for detailed logs");
            System.out.println("=".repeat(60) + "\n");

            BarberUI ui = new BarberUI();
            ui.setVisible(true);

            // Auto-start barber
            javax.swing.Timer startupTimer = new javax.swing.Timer(1000, e -> {
                ui.startBarber();
                ((javax.swing.Timer)e.getSource()).stop();
            });
            startupTimer.setRepeats(false);
            startupTimer.start();

            // Automatically add first customers
            javax.swing.Timer customerTimer = new javax.swing.Timer(2000, e -> {
                if (BarberShop.customersAdded >= BarberShop.TOTAL_CUSTOMERS) {
                    ((javax.swing.Timer)e.getSource()).stop();
                    return;
                }
                ui.addCustomer();
                if (Math.random() > 0.5) {
                    ui.addCustomer();
                }
            });
            customerTimer.setRepeats(false);
            customerTimer.start();
        });
    }
}