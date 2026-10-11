/**
 * Sleeping Barber Problem - Graphical User Interface
 *
 * This class provides the Swing-based graphical user interface for the
 * Sleeping Barber simulation. It displays real-time status information,
 * provides interactive controls for simulation management, and shows
 * detailed statistics.
 *
 * GUI Components:
 * - Status panel: Barber state, chair status, queue information
 * - Control panel: Buttons for simulation control
 * - Log area: Real-time event logging
 * - Statistics display: Served/left customers, satisfaction metrics
 *
 * Features:
 * - Real-time updates using Timer
 * - Interactive buttons for manual control
 * - Visual representation of waiting queue
 * - Comprehensive logging and statistics
 *
 * @author Sleeping Barber Simulation Team
 * @version 1.0
 * @since 2025
 */
import javax.swing.*;
import java.awt.*;

public class BarberUI extends JFrame {
    private JButton addCustomerBtn;
    private JButton addMultipleBtn;
    private JButton startBarberBtn;
    private JButton stopBarberBtn;
    private JButton resetBtn;
    private JButton stopCustomerArrivalBtn; // new button
    private JLabel barberStatusLabel;
    private JLabel barberChairLabel;
    private JLabel freeSeatsLabel;
    private JLabel statsLabel;
    private JPanel waitingPanel;
    private JTextArea logArea;
    private JSlider speedSlider;

    private Barber barberThread;
    private int nextCustomerId = 1;
    private Timer simulationTimer;
    private boolean customerArrivalStopped = false;

    public BarberUI() {
        setTitle("Sleeping Barber Simulation");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        startAutoRefresh();
    }

    private void initComponents() {
        // Butonlar
        addCustomerBtn = createStyledButton("Add Single Customer", new Color(46, 204, 113));
        addMultipleBtn = createStyledButton("Add 3 Customers", new Color(52, 152, 219));
        startBarberBtn = createStyledButton("Start Barber", new Color(41, 128, 185));
        stopBarberBtn = createStyledButton("Stop Barber", new Color(231, 76, 60));
        resetBtn = createStyledButton("Reset Simulation", new Color(155, 89, 182));
        stopCustomerArrivalBtn = createStyledButton("Stop Customer Arrival", new Color(230, 126, 34));

        // Durum etiketleri
        barberStatusLabel = createStatusLabel("BARBER: --", Color.GRAY);
        barberChairLabel = createStatusLabel("Barber Chair: --", new Color(52, 152, 219));
        freeSeatsLabel = createStatusLabel("Free Seats: --/--", new Color(46, 204, 113));
        statsLabel = createStatusLabel("Served: 0 | Left: 0", new Color(230, 126, 34));

        // Bekleme sandalyeleri paneli
        waitingPanel = new JPanel();
        waitingPanel.setLayout(new GridLayout(2, 3, 15, 15));
        waitingPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "WAITING CHAIRS (" + BarberShop.CHAIRS + " seats)",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(52, 152, 219)
        ));
        waitingPanel.setBackground(new Color(30, 30, 30));

        // Hiz kontrolu
        speedSlider = new JSlider(1, 10, 5);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setForeground(Color.WHITE);

        JLabel speedLabel = new JLabel("Simulation Speed:");
        speedLabel.setForeground(Color.WHITE);
        speedLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel speedPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        speedPanel.add(speedLabel);
        speedPanel.add(speedSlider);

        // Log alani
        logArea = new JTextArea(12, 60);
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logArea.setBackground(new Color(20, 20, 20));
        logArea.setForeground(new Color(220, 220, 220));
        logArea.setCaretColor(Color.WHITE);

        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
            "EVENT LOG",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(46, 204, 113)
        ));
        logScroll.getViewport().setBackground(new Color(20, 20, 20));

        // PANEL DUZENI
        JPanel topPanel = new JPanel(new BorderLayout(20, 20));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Sol panel - Durum
        JPanel statusPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        statusPanel.add(barberStatusLabel);
        statusPanel.add(barberChairLabel);
        statusPanel.add(freeSeatsLabel);
        statusPanel.add(statsLabel);

        // Orta panel - Kontroller
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.add(addCustomerBtn);
        controlPanel.add(addMultipleBtn);
        controlPanel.add(startBarberBtn);
        controlPanel.add(stopBarberBtn);
            controlPanel.add(stopCustomerArrivalBtn);

        // Top panel duzeni
        topPanel.add(statusPanel, BorderLayout.WEST);
        topPanel.add(controlPanel, BorderLayout.CENTER);
        topPanel.add(speedPanel, BorderLayout.EAST);

        // Barber sandalyesi paneli
        JPanel barberChairPanel = new JPanel();
        barberChairPanel.setLayout(new BorderLayout());
        barberChairPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(230, 126, 34), 2),
            "BARBER CHAIR",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 16),
            new Color(230, 126, 34)
        ));
        barberChairPanel.setBackground(new Color(40, 40, 40));

        JLabel barberIcon = new JLabel("B", SwingConstants.CENTER);
        barberIcon.setFont(new Font("Arial", Font.BOLD, 60));
        barberIcon.setForeground(new Color(52, 152, 219));
        barberChairPanel.add(barberIcon, BorderLayout.CENTER);

        // Ana duzen
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(15, 15));
        contentPane.setBackground(new Color(45, 45, 45));

        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(barberChairPanel, BorderLayout.WEST);
        contentPane.add(waitingPanel, BorderLayout.CENTER);
        contentPane.add(logScroll, BorderLayout.SOUTH);

        // Buton aksiyonlari
        addCustomerBtn.addActionListener(e -> addCustomer());
        addMultipleBtn.addActionListener(e -> addMultipleCustomers(3));
        startBarberBtn.addActionListener(e -> startBarber());
        stopBarberBtn.addActionListener(e -> stopBarber());
        resetBtn.addActionListener(e -> resetSimulation());
        stopCustomerArrivalBtn.addActionListener(e -> stopCustomerArrival());

        // Slider aksiyonu
        speedSlider.addChangeListener(e -> {
            int speed = speedSlider.getValue();
            int delay = 1100 - (speed * 100); // 100ms ile 1000ms arasi
            if (simulationTimer != null) {
                simulationTimer.setDelay(delay);
            }
        });

        // Otomatik musteri ekleme timer'i
        simulationTimer = new Timer(1500, e -> {
            if (BarberShop.customersAdded >= BarberShop.TOTAL_CUSTOMERS) {
                ((Timer)e.getSource()).stop();
                BarberShop.simulationFinished = true;
                log("All customers added. Waiting for barber to finish remaining customers.");
                return;
            }
            if (Math.random() > 0.7) { // %30 sansla musteri ekle
                addCustomer();
            }
        });
        simulationTimer.start();
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover efekti
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private JLabel createStatusLabel(String text, Color color) {
        JLabel label = new JLabel(" " + text + " ");
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        label.setOpaque(true);
        label.setBackground(new Color(50, 50, 50));
        return label;
    }

    public void addCustomer() {
        if (BarberShop.customersAdded >= BarberShop.TOTAL_CUSTOMERS) {
            log("All customers have been added. Simulation will finish soon.");
            BarberShop.simulationFinished = true;
            return;
        }
        int id = nextCustomerId++;
        BarberShop.customersAdded++;
        Customer c = new Customer(id);
        log("Customer " + id + " arrived at shop (" + BarberShop.customersAdded + "/" + BarberShop.TOTAL_CUSTOMERS + ")");
        c.start();
    }

    private void addMultipleCustomers(int count) {
        log("Adding " + count + " customers at once...");
        for (int i = 0; i < count; i++) {
            addCustomer();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}
        }
    }

    public void startBarber() {
        if (barberThread != null && barberThread.isAlive()) {
            log("Barber is already working");
            return;
        }
        barberThread = new Barber();
        barberThread.start();
        log("Barber started working");
        startBarberBtn.setEnabled(false);
        stopBarberBtn.setEnabled(true);
    }

    private void stopBarber() {
        if (barberThread != null) {
            barberThread.stopBarber();
            barberThread = null;
            log("Barber stopped working");
        }
        startBarberBtn.setEnabled(true);
        stopBarberBtn.setEnabled(false);
    }

    private void resetSimulation() {
        stopBarber();

        // Sifirlama
        synchronized (BarberShop.waitingQueue) {
            BarberShop.waitingQueue.clear();
            BarberShop.freeSeats = BarberShop.CHAIRS;
            BarberShop.currentCustomer = 0;
            BarberShop.isBarberSleeping = true;
            BarberShop.totalCustomersServed = 0;
            BarberShop.totalCustomersLeft = 0;
            BarberShop.customersAdded = 0;
            BarberShop.simulationFinished = false;
            BarberShop.totalSatisfaction = 0;
            BarberShop.averageSatisfaction = 0.0;
        }

        nextCustomerId = 1;
        customerArrivalStopped = false;
        stopCustomerArrivalBtn.setText("Stop Customer Arrival");
        logArea.setText("");
        log("Simulation reset");
        refreshUI();
    }

    private void stopCustomerArrival() {
        if (customerArrivalStopped) {
            // Resume customer arrival
            if (simulationTimer != null) {
                simulationTimer.start();
                log("Customer arrival resumed");
                stopCustomerArrivalBtn.setText("Stop Customer Arrival");
                customerArrivalStopped = false;
            }
        } else {
            // Stop customer arrival
            if (simulationTimer != null) {
                simulationTimer.stop();
                log("Customer arrival stopped");
                stopCustomerArrivalBtn.setText("Resume Customer Arrival");
                customerArrivalStopped = true;
            }
        }
    }

    private void startAutoRefresh() {
        Timer refreshTimer = new Timer(300, e -> refreshUI());
        refreshTimer.start();
    }

    private void refreshUI() {
        // Barber durumu
        String statusText;
        Color statusColor;

        if (BarberShop.isBarberSleeping) {
            statusText = "SLEEPING";
            statusColor = new Color(155, 89, 182); // Mor
        } else {
            statusText = "WORKING (C" + BarberShop.currentCustomer + ")";
            statusColor = new Color(46, 204, 113); // Yesil
        }
        barberStatusLabel.setText(" BARBER: " + statusText);
        barberStatusLabel.setBorder(BorderFactory.createLineBorder(statusColor, 2));

        // Barber sandalyesi
        String chairText;
        Color chairColor;
        if (BarberShop.currentCustomer == 0) {
            chairText = "[ EMPTY ]";
            chairColor = Color.GRAY;
        } else {
            chairText = "[ C" + BarberShop.currentCustomer + " ]";
            chairColor = new Color(52, 152, 219); // Mavi
        }
        barberChairLabel.setText(" Barber Chair: " + chairText);
        barberChairLabel.setBorder(BorderFactory.createLineBorder(chairColor, 2));

        // Bos koltuklar
        freeSeatsLabel.setText(" Free Seats: " + BarberShop.freeSeats + "/" + BarberShop.CHAIRS);

        // Istatistikler
        String simStatusText = BarberShop.simulationFinished ? "FINISHED" : "RUNNING";
        statsLabel.setText(" Served: " + BarberShop.totalCustomersServed +
                         " | Left: " + BarberShop.totalCustomersLeft +
                         " | Avg Satisfaction: " + String.format("%.1f", BarberShop.averageSatisfaction) +
                         " | Status: " + simStatusText);

        // Bekleme sandalyelerini guncelle
        waitingPanel.removeAll();

        synchronized (BarberShop.waitingQueue) {
            java.util.Iterator<Integer> it = BarberShop.waitingQueue.iterator();
            int chairCount = 0;

            while (it.hasNext() && chairCount < BarberShop.CHAIRS) {
                int customerId = it.next();
                JPanel chairPanel = createChairPanel(customerId, chairCount + 1);
                waitingPanel.add(chairPanel);
                chairCount++;
            }

            // Bos sandalyeler
            for (int i = chairCount; i < BarberShop.CHAIRS; i++) {
                JPanel emptyChairPanel = createEmptyChairPanel(i + 1);
                waitingPanel.add(emptyChairPanel);
            }
        }

        waitingPanel.revalidate();
        waitingPanel.repaint();
    }

    private JPanel createChairPanel(int customerId, int chairNumber) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel customerLabel = new JLabel("C" + customerId, SwingConstants.CENTER);
        customerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        customerLabel.setForeground(new Color(46, 204, 113));

        JLabel chairLabel = new JLabel("Chair #" + chairNumber, SwingConstants.CENTER);
        chairLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        chairLabel.setForeground(Color.GRAY);

        panel.add(customerLabel, BorderLayout.CENTER);
        panel.add(chairLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createEmptyChairPanel(int chairNumber) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 40));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel emptyLabel = new JLabel("EMPTY", SwingConstants.CENTER);
        emptyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emptyLabel.setForeground(new Color(150, 150, 150));

        JLabel chairLabel = new JLabel("Chair #" + chairNumber, SwingConstants.CENTER);
        chairLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        chairLabel.setForeground(Color.GRAY);

        panel.add(emptyLabel, BorderLayout.CENTER);
        panel.add(chairLabel, BorderLayout.SOUTH);

        return panel;
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = String.format("[%tT]", new java.util.Date());
            logArea.append(timestamp + " " + message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
}