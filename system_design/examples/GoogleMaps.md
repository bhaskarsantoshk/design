# **System Design of Google Maps**

## **1. Functional Requirements**
- **Identify current location** – Uses GPS, WiFi, or cellular networks.
- **Recommend the fastest route** – Computes the best route based on real-time traffic data.
- **Provide turn-by-turn navigation** – Gives directions with step-by-step guidance.

## **2. Non-Functional Requirements**
- **High Availability** – Must be reliable 24/7.
- **Scalability** – Handles millions of users and integrations with services like Uber & Lyft.
- **Low Latency** – Quick response times for real-time navigation.
- **Accuracy** – Provides precise location and routing data.

## **3. Challenges**
- **Scalability** – Processing millions of route queries per second with billions of nodes and edges requires optimization beyond Dijkstra’s algorithm.
- **ETA Computation** – Traffic, road conditions, and incidents impact ETA calculations, making them complex.

## **4. Key Calculations**
- **Server Estimation** – With ~32M daily active users and an estimated 64,000 requests per second per server, approximately **500 servers** are needed at peak load.
- **Storage Needs** – Road network data (~20 PB as of 2022) has minimal daily changes, so ongoing storage requirements are low.

## **5. Building Blocks**
- **Load Balancers** – Distribute traffic efficiently.
- **Databases** – Store road data, traffic conditions, and user preferences.
- **Distributed Search** – Optimized for high-speed location and route lookups.
- **Key-Value Store** – For caching frequently accessed data like map tiles and traffic conditions.

## **6. Core Components**
- **Location Finder** – Determines user position using GPS, WiFi, or cellular data.
- **Route Finder** – Computes optimal routes based on traffic, road conditions, and user preferences.
- **Navigator** – Provides turn-by-turn guidance.
- **GPS/WiFi/Cellular Tech** – Determines user location.
- **Distributed Search** – Handles fast lookup queries.
- **Area Search Service** – Finds points of interest and search results.
- **Graph Processing Service** – Computes routes efficiently.
- **Graph Database** – Stores map data in an optimized format.
- **Pub/Sub System (Kafka)** – Handles event-driven updates like live traffic data.
- **Third-Party Road Data** – Integrates external traffic and road condition data.
- **Graph Building System** – Precomputes and updates road network graphs.
- **User Interface** – Mobile/web app providing user interaction.
- **Load Balancer** – Ensures even distribution of user requests.

## **7. System Architecture Diagram**

```
User -> Load Balancer -> [ Location Finder -> Route Finder -> Navigator ]
        -> [ Area Search Service / Pub Sub System (Kafka) -> Distributed Search ]
        -> Graph Processing Service <- Graph DB
```

## **8. APIs**
- `findRoute(source, destination, transport_type)` – Returns the best route based on mode of transport.
- `directions(currentLocation)` – Provides navigation instructions.
- `currentLocation(location)` – Returns the user's current position.

## **9. Data Modeling**
- **Graph Representation**: Roads, intersections, and traffic signals are stored as a **directed graph**, where:
    - **Nodes**: Intersections, waypoints.
    - **Edges**: Roads with weights based on distance, speed limits, and real-time traffic.
- **Spatial Indexing**:
    - **Quadtree/R-tree**: For efficient spatial queries.
    - **Geohashing**: Used for nearby search and clustering.

## **10. Architecture Breakdown**
### **Frontend**
- **Web & Mobile Clients**: Handle user requests, render maps, and show directions.
- **APIs**: Expose services for routing, location search, and traffic updates.

### **Backend Services**
- **User Location Service** – Retrieves the user's real-time location.
- **Route Computation Service** – Determines the shortest/fastest route based on live data.
- **Traffic Aggregation Service** – Collects real-time data from GPS-enabled devices, road sensors, and third-party sources.
- **Geocoding Service** – Converts addresses into latitude/longitude and vice versa.
- **Reverse Geocoding** – Converts coordinates back to human-readable addresses.
- **Map Rendering Service** – Generates map images and tiles.

### **Data Storage**
- **Graph Database** – Stores road network data (e.g., Neo4j, Google S2).
- **Time-Series Database** – Logs historical traffic data for analytics (e.g., InfluxDB, Prometheus).
- **Key-Value Store** – Caches frequently accessed data (e.g., Redis, Memcached).

## **11. Optimization Strategies**
- **Precomputed Routes** – Frequently used routes are cached to avoid repetitive computation.
- **Hierarchical Graph Partitioning** – Divides road networks into smaller, more manageable subgraphs.
- **Machine Learning for Traffic Prediction** – Uses historical data to predict congestion and adjust routes dynamically.
- **Edge Computing** – Processes data closer to users for reduced latency.
- **CDN (Content Delivery Network)** – Caches map tiles and static assets for faster access.

## **12. Fault Tolerance & Scalability**
- **Multi-Region Deployment** – Ensures high availability and disaster recovery.
- **Load Balancing** – Distributes traffic across multiple servers to handle peak loads.
- **Failover Mechanisms** – Ensures redundancy to prevent service disruptions.
- **Sharding & Replication** – Improves database performance and fault tolerance.