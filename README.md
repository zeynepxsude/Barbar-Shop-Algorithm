# Sleeping Barber Problem Simulation

A comprehensive Java implementation of the classic Sleeping Barber problem, demonstrating advanced thread synchronization techniques and real-time simulation with an interactive GUI.

## 🎯 Overview

The Sleeping Barber problem is a classic synchronization problem in computer science that illustrates the challenges of coordinating multiple processes (threads) accessing shared resources. This implementation provides both educational value and practical demonstration of concurrent programming concepts.

### Problem Description
- A barber shop has **one barber**, **one barber chair**, and **N waiting chairs**
- Customers arrive randomly and either sit in a waiting chair (if available) or leave (if all chairs are occupied)
- The barber sleeps when no customers are waiting
- When a customer arrives, they wake the barber
- The barber serves customers one at a time until all customers are served

## ✨ Key Features

### Core Functionality
- **Thread-Safe Synchronization**: Uses Java semaphores to prevent race conditions and deadlocks
- **Real-Time Simulation**: Dynamic customer arrival with configurable timing
- **Interactive GUI**: Swing-based interface with live status updates
- **Performance Metrics**: Real-time satisfaction calculation based on wait times

### Advanced Features
- **Customer Satisfaction Tracking**: Calculates satisfaction scores (0-10) based on waiting time
- **Comprehensive Statistics**: Tracks served customers, dissatisfied customers, and average satisfaction
- **Simulation Controls**: Start/stop barber, add customers manually or automatically
- **Visual Status Display**: Real-time updates of barber status, waiting queue, and shop statistics
- **Configurable Parameters**: Adjustable haircut times, customer arrival rates, and shop capacity

## 🏗️ Architecture

### Class Structure
```
src/
├── SleepingBarber.java     # Main application entry point
├── BarberShop.java         # Shared state management and semaphores
├── Barber.java            # Barber thread implementation
├── Customer.java          # Customer thread with satisfaction logic
└── BarberUI.java          # Swing GUI with event handling
```

### Synchronization Mechanism
- **Semaphores Used**:
  - `customers`: Controls customer queue access (initially 0)
  - `barber`: Ensures only one barber operates (initially 0)
  - `mutex`: Protects shared data structures (initially 1)
- **Thread Safety**: Volatile variables and synchronized methods prevent data races

## 📊 Technical Specifications

### Performance Characteristics
- **Memory Usage**: Minimal - O(N) where N is waiting chair capacity
- **CPU Utilization**: Efficient thread scheduling with proper semaphore usage
- **Response Time**: Real-time GUI updates with <100ms latency
- **Scalability**: Configurable shop size and timing parameters

### Algorithm Complexity
- **Time Complexity**: O(1) for most operations (semaphore acquire/release)
- **Space Complexity**: O(N) for customer queue storage
- **Synchronization Overhead**: Minimal due to efficient semaphore implementation

## 🚀 Installation & Usage

### Prerequisites
- **Java Development Kit (JDK)**: Version 8 or higher
- **Operating System**: Windows, macOS, or Linux
- **Memory**: Minimum 256MB RAM (recommended 512MB+)

### Automated Setup (Recommended)
```batch
# Clone or download the project
cd sleeping-barber-simulation

# Build the project
.\build.bat

# Run the simulation
.\run.bat

# Clean build files (optional)
.\clean.bat
```

### Manual Compilation
```bash
# Create build directory
mkdir build

# Compile source files
javac -d build -cp src src/*.java

# Run the application
java -cp build SleepingBarber
```

### GUI Controls
1. **Start Barber**: Initializes the barber thread
2. **Stop Barber**: Gracefully terminates barber operations
3. **Add Single Customer**: Manually adds one customer to the queue
4. **Add 3 Customers**: Adds three customers simultaneously
5. **Stop Customer Arrival**: Pauses automatic customer generation
6. **Reset Simulation**: Clears all data and restarts the simulation
7. **Speed Slider**: Adjusts simulation speed (0.1x to 5x)

## 📈 Simulation Metrics

### Real-Time Statistics
- **Served Customers**: Successfully completed haircuts
- **Dissatisfied Customers**: Customers who left due to full waiting area
- **Average Satisfaction**: Weighted average of customer satisfaction scores
- **Current Queue Status**: Live display of waiting customers
- **Barber Utilization**: Percentage of time barber is actively working

### Satisfaction Calculation
```java
// Satisfaction decreases with waiting time
// Max satisfaction: 10 (immediate service)
// Min satisfaction: 0 (very long wait)
satisfaction = Math.max(0, 10 - (waitTimeInSeconds / 10));
```

## 🔧 Configuration

### Default Parameters (BarberShop.java)
```java
public static final int CHAIRS = 5;                    // Waiting chairs
public static final int TOTAL_CUSTOMERS = 20;          // Simulation limit
public static final int MIN_HAIRCUT_TIME = 3000;       // Min haircut time (ms)
public static final int MAX_HAIRCUT_TIME = 6000;       // Max haircut time (ms)
public static final int CUSTOMER_ARRIVAL_DELAY = 1500; // Arrival interval (ms)
```

### Customization
Modify constants in `BarberShop.java` to adjust:
- Shop capacity (number of waiting chairs)
- Service times (haircut duration range)
- Customer arrival patterns
- Simulation duration limits

## 🎮 Usage Examples

### Basic Simulation
1. Launch the application: `.\run.bat`
2. Click "Start Barber" to begin
3. Click "Add Single Customer" to introduce customers
4. Observe real-time status updates in console and GUI
5. Monitor satisfaction statistics

### Automated Testing
1. Start the barber
2. Let automatic customer arrival run
3. Observe queue management and satisfaction trends
4. Use "Stop Customer Arrival" to pause generation
5. Analyze final statistics

### Performance Analysis
1. Run multiple simulation cycles
2. Adjust speed slider for faster testing
3. Compare statistics across different configurations
4. Identify optimal shop capacity vs. customer satisfaction

## 🐛 Troubleshooting

### Common Issues
- **GUI Not Starting**: Ensure JavaFX/Swing libraries are available
- **Compilation Errors**: Verify JDK installation and JAVA_HOME
- **Thread Deadlocks**: Check semaphore ordering in BarberShop.java
- **Performance Issues**: Reduce simulation speed or increase heap size

### Debug Mode
Enable verbose logging by modifying `BarberShop.java`:
```java
public static final boolean DEBUG_MODE = true;
```

## 📚 Educational Value

### Learning Objectives
- **Thread Synchronization**: Understanding semaphore usage
- **Race Condition Prevention**: Critical section management
- **Producer-Consumer Pattern**: Customer arrival and service flow
- **Real-Time Systems**: GUI updates and timing considerations
- **Performance Monitoring**: Statistics collection and analysis

### Academic Applications
- Operating Systems coursework
- Concurrent Programming assignments
- Algorithm analysis projects
- GUI development exercises
- Performance benchmarking studies

## 🤝 Contributing

### Code Standards
- Follow Java naming conventions
- Include comprehensive JavaDoc comments
- Maintain thread safety in all modifications
- Test thoroughly before committing changes

### Enhancement Ideas
- Add multiple barbers support
- Implement priority customer queues
- Create detailed logging system
- Add simulation replay functionality
- Develop web-based interface

## 📄 License

This project is developed for educational purposes. Feel free to use, modify, and distribute for academic and learning purposes.

## 🙏 Acknowledgments

- Classic computer science problem formulation
- Java Concurrency API documentation
- Swing GUI framework
- Educational resources on thread synchronization

---

**Note**: This implementation demonstrates production-quality code practices including proper error handling, resource management, and user interface design suitable for academic projects and professional development.