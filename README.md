 # Campus Compass 🧭🏫

[![Java](https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/) 
[![License](https://img.shields.io/badge/License-Educational-blue?style=for-the-badge)](https://choosealicense.com/licenses/) 
[![Version](https://img.shields.io/badge/Version-1.0.0-green?style=for-the-badge)](https://github.com/TechTutors/CampusCompass)

---
## 🚀Project Overview
**Campus Compass** 🧭 is a **fully GUI-based, Object-Oriented Java application** developed by **Tech Tutors** 💻 to provide **complete campus navigation** for the University of Ghana 🎓.  
With **Swing GUI**, users can interactively select starting points, destinations, and landmarks via **dropdowns and buttons**, while the system computes **optimal routes** considering:  
- Shortest distance 🏃
- Traffic Conditions🚦
- Accessibility ♿
- Weather Conditions 🌤️

  This makes the system **user-friendly, interactive, and practical**, while demonstrating **real-world OOP design principles**.

  ---
 ## ✨Key Features
 - **Interactive Swing GUI** 🖥️ for easy navigation
 -  **Dropdown Selections** for start, destination, and landmarks
 -  **Responsive & Complete**: handles multiple routes and alternative paths
 -  **Route Visualization** with distance and estimated time display

## 🔹Core Navigation
 - **Multi-Algorithm Pathfinding**: Dijkstra, Floyd-Warshall, A* 📍
 - **Accessibility Features** ♿
 - **Traffic-Aware Routing** 🚦
 - **Landmark-Based Routing** 🏛️

## 🔹Advanced Features
 - **Sorting Algorithms**: Quick Sort ⚡ & Merge Sort 🔀
 - **Search Algorithms**: Binary 🔍 & Linear 📝
 - **Performance Optimization**: Divide & Conquer ⚔️, Greedy 🏹, Dynamic Programming 💾
 - **Critical Path Analysis** ⏱️

## 🏫 Campus Locations

### Academic Buildings
 - CS Department 💻
 - Math Department ➗
 - Engineering School 🏗️
 - Chemistry Department ⚗️
 - Business School 💼
 - Law Faculty ⚖️

### Administrative
 - JQB 🏢
 - Main Gate🚪

### Student Halls
 - Volta Hall 🏠
 - Commonwealth Hall 🏘️
 - Akuafo Hall 🏠
 - Legon Hall 🏘️
 - Diaspora Halls 🌍

### Facilities and Services
 - Bush Canteen 🍔
 - Sarbah Park 🌳
 - Fire Station 🚒
 - Banking Square 🏦
 - UG Basic School 🏫
 - Night Market 🌙

   ---
## 🧠Algorithms Implemented

### PathFinding Algorithms
1. **Dijkstra** - Single-source shortest path 📍
2. **Floyd-Warshall** – All-pairs shortest path 🔗
3.  **A*** – Heuristic-based pathfinding 🎯

### Sorting Algorithm
 - **Quick Sort**⚡- Fast route sorting
 - **Merge Sort** 🔀 - Stable sorting for alternative

### Searching Algorithm
 - **Binary Search** 🔍 - Fast landmark lookup
 - **Linear Search** 📝 - Full landmark scan

### Optimization Techniques
 - **Divide & Conquer** ⚔️ -  Route efficiency
 - **Greedy Algorithm** 🏹 – Local optimal path choices
 - **Dynamic Programming** 💾 – Memoization & repeated calculation optimization

### Transportation Methods
 - **Vogel Approximation** 🚌 -  Transport optimization
 - **Northwest Corner Method** 🧭 – Feasible initial solution generation

---
## ⚙️ Installation & Setup

### Prerequesites
 - **Java Development Kit**☕
 - **Java IDE** (Eclipse, IntelliJ IDEA, VS Code) 🖥️

### Run from terminal
1. Clone the repo:
   ```bash
   git clone https://github.com/KelvCodes/Campus-Compas
   cd Campus-Compass

 2. Compile Java files
    ```bash
    javac -d bin src/*.java
    
 3. Run the application
    ```bash
    java -cp bin App

### Run from IDE
1. Open the project in your IDE.
2. Navigate to ```src/App.java```
3. Run the main method ▶️

---
### 🗺️Usage Guide
## Basic Navigation
1. Launch the application🖥️
2. Select starting location 📍
3. Select destination 🎯
4. Click Start to find the optimal route🛣️
 

### Advanced Features
 - Multiple Routes 🔄 – Compare alternative paths
 - Landmark Search 🏛️ – Navigate via landmarks
 - Traffic Optimization 🚦 – Routes adapt to traffic

### 🗂️Project Structure
```bash
CampusCompass/
├── src/
│   ├── App.java                 # Main entry point
│   ├── AppFrame.java            # GUI implementation
│   ├── Graph.java               # Graph data structure
│   ├── Nodes.java               # Node representation
│   ├── Edge.java                # Edge representation
│   ├── VogelAlgo.java           # Vogel approximation method
│   ├── CriticalPath.java        # Critical path analysis
│   ├── FloydWarshall.java       # Floyd-Warshall algorithm
│   ├── AStarSearch.java         # A* search algorithm
│   ├── SortingAlgorithms.java   # Quick & Merge Sort
│   ├── SearchAlgorithms.java    # Binary & Linear Search
│   └── RouteOptimizer.java      # Route optimization logic
├── bin/                         # Compiled class files
└── README.md                    # Project documentation
```

### 📊Technical Implementation
## Data Structures
 - Graph - adjacency list weighted edges🔗
 - Priority Queue - efficient node selection⚡
 - Hashmap - fast node/edge lookup🗂️
 - Linked list - path tracking📋

## Performance Optimizations
 - Memoization 💾 – caches repeated routes
 - Early Termination ⏹️ – stops search when optimal path is found
 - Optimized Structure⚙️ -  designed for campus-scale navigation

### 🤝Contr
✅ **Note:** This project is **highly complete**, demonstrating **OOP principles, GUI design, and advanced algorithmic implementations**, making it suitable for **academic demonstrations, practical navigation tools, and learning purposes**.
   


