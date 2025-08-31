# UG Navigate: Optimal Routing Solution for University of Ghana Campus

## Project Overview

UG Navigate is a comprehensive Java application designed to help users find optimal routes between locations on the University of Ghana campus. The system considers multiple factors including shortest distance, optimal arrival time, and traffic conditions to provide the best navigation experience.

## Features

### Core Navigation Features
- **Multi-Algorithm Pathfinding**: Implements Dijkstra's, Floyd-Warshall, and A* algorithms
- **Multiple Route Options**: Provides up to 3 alternative routes for each journey
- **Landmark-Based Routing**: Find routes that pass through specific landmarks
- **Traffic-Aware Routing**: Considers traffic conditions for optimal timing
- **Interactive GUI**: User-friendly interface with dropdown selections

### Advanced Features
- **Sorting Algorithms**: Quick Sort and Merge Sort for route organization
- **Search Algorithms**: Binary and Linear search for landmark identification
- **Performance Optimization**: Divide and Conquer, Greedy, and Dynamic Programming approaches
- **Transportation Methods**: Vogel Approximation and Northwest Corner methods
- **Critical Path Analysis**: For project scheduling and time management

## Campus Locations

The system includes 24 key campus locations:

### Academic Buildings
- CS Department
- Math Department  
- Engineering School
- Chemistry Department
- Law Faculty
- Business School

### Administrative
- JQB 
- Main Gate
- UGCS (University of Ghana Computing Services)

### Libraries & Cultural
- Balme Library
- School of Performing Arts

### Student Halls
- Volta Hall
- Commonwealth Hostel
- Great Hall
- Akuafo Hall
- Legon Hall
- Diaspora Halls

### Facilities & Services
- Bush Canteen
- Sarbah Park
- Fire Station
- Banking Square
- Night Market
- UG Basic School

## Algorithms Implemented

### Pathfinding Algorithms
1. **Dijkstra's Algorithm**: Shortest path between nodes in weighted graph
2. **Floyd-Warshall Algorithm**: All-pairs shortest path calculation
3. **A* Search Algorithm**: Heuristic-based pathfinding with optimal efficiency

### Sorting Algorithms
1. **Quick Sort**: Efficient route sorting by distance and time
2. **Merge Sort**: Stable sorting for route alternatives

### Search Algorithms
1. **Binary Search**: Fast landmark lookup in sorted data
2. **Linear Search**: Comprehensive landmark scanning

### Optimization Techniques
1. **Divide and Conquer**: Route calculation optimization
2. **Greedy Algorithm**: Local optimal choices for pathfinding
3. **Dynamic Programming**: Memoization for repeated calculations

### Transportation Methods
1. **Vogel Approximation Method**: Transportation problem optimization
2. **Northwest Corner Method**: Initial feasible solution generation

## Installation and Setup

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Any Java IDE (Eclipse, IntelliJ IDEA, VS Code)

### Running the Application

1. **Clone or download the project**
   ```bash
   git clone [repository-url]
   cd UG-campus-Algorithm--Group-5-
   ```

2. **Compile the Java files**
   ```bash
   javac -d bin src/*.java
   ```

3. **Run the application**
   ```bash
   java -cp bin App
   ```

### Alternative: Using an IDE
1. Open the project in your preferred Java IDE
2. Navigate to `src/App.java`
3. Run the `main` method

## Usage Guide

### Basic Navigation
1. Launch the application
2. Select your starting location from the dropdown
3. Select your destination from the dropdown
4. Click "Start" to find the optimal route
5. View the route details including distance and estimated time

### Advanced Features
- **Multiple Routes**: The system automatically provides alternative routes
- **Landmark Search**: Use the search feature to find routes via specific landmarks
- **Traffic Optimization**: Routes are optimized based on current traffic conditions

## Project Structure

```
UG-campus-Algorithm--Group-5-/
├── src/
│   ├── App.java                 # Main application entry point
│   ├── AppFrame.java            # GUI implementation
│   ├── Graph.java               # Graph data structure and algorithms
│   ├── Nodes.java               # Node representation
│   ├── Edge.java                # Edge representation
│   ├── VogelAlgo.java           # Vogel approximation method
│   ├── CriticalPath.java        # Critical path analysis
│   ├── FloydWarshall.java       # Floyd-Warshall algorithm
│   ├── AStarSearch.java         # A* search algorithm
│   ├── SortingAlgorithms.java   # Quick sort and merge sort
│   ├── SearchAlgorithms.java    # Binary and linear search
│   └── RouteOptimizer.java      # Route optimization with multiple algorithms
├── bin/                         # Compiled class files
└── README.md                    # This file
```

## Technical Implementation

### Data Structures
- **Graph**: Adjacency list representation with weighted edges
- **Priority Queue**: For efficient algorithm implementations
- **Hash Maps**: For fast node and edge lookups
- **Linked Lists**: For edge storage and path tracking

### Performance Optimizations
- **Memoization**: Caching calculated routes for faster retrieval
- **Early Termination**: Stop calculations when optimal path is found
- **Efficient Data Structures**: Optimized for campus-scale navigation

## Contributing

This project was developed as part of the University of Ghana Algorithm course. For contributions or improvements:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is developed for educational purposes at the University of Ghana.

## Contact

For questions or support, please contact the development team or course instructor.

---

**Developed by Group 5 - University of Ghana Algorithm Course**


