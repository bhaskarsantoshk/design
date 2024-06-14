# System Design

## Fundamentals

These are some techniques/components involved in designing a robust system:

1. **Pre-processing**: Transforming raw data into a suitable format for analysis or processing, improving efficiency and performance.

2. **Vertical Scaling**: Increasing the capacity of a single server by adding more resources (CPU, RAM, etc.).

3. **Horizontal Scaling**: Adding more servers to distribute the load and handle increased traffic, enhancing system capacity and redundancy.

4. **Distributed Systems**: Systems in which components located on networked computers communicate and coordinate their actions by passing messages to achieve a common goal.

5. **Master-Slave Architecture**: A setup where one server (master) handles writes and distributes read tasks to multiple servers (slaves) to balance load and ensure data redundancy.

6. **Microservice Architecture**: Designing systems as a collection of loosely coupled services, each responsible for specific functionality, facilitating easier maintenance and scalability.

7. **Load Balancing**: Distributing incoming network traffic across multiple servers to ensure no single server becomes overwhelmed, enhancing performance and reliability.

8. **Decoupling**: Designing system components to be independent of each other, reducing dependencies and allowing for easier updates and maintenance.

9. **Logging and Metrics**: Collecting and analyzing data about system performance and behavior to monitor health, diagnose issues, and improve performance.

10. **Extensibility**: Designing systems to easily incorporate new features and adapt to changing requirements without significant rework.
