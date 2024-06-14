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

# Horizontal vs. Vertical Scaling

## Horizontal Scaling

**Definition**: Horizontal scaling, also known as scaling out, involves adding more servers to a system to distribute the load.

**Key Characteristics**:
- **Capacity Increase**: Increases capacity by adding more machines to the system.
- **Fault Tolerance**: Improves fault tolerance by distributing the load across multiple servers. If one server fails, others can take over.
- **Cost**: Can be cost-effective as commodity hardware can be used.
- **Complexity**: Often increases system complexity due to the need for load balancing and data distribution mechanisms.
- **Examples**: Adding more web servers behind a load balancer, distributing databases across multiple nodes.

## Vertical Scaling

**Definition**: Vertical scaling, also known as scaling up, involves adding more resources (CPU, RAM, storage) to an existing server.

**Key Characteristics**:
- **Capacity Increase**: Increases capacity by enhancing the power of a single machine.
- **Fault Tolerance**: Limited improvement in fault tolerance since the failure of the single, more powerful machine can lead to downtime.
- **Cost**: Can be expensive as it often requires high-end, specialized hardware.
- **Complexity**: Simpler to implement as it doesn't require changes to the application architecture.
- **Examples**: Upgrading a server's CPU, adding more memory to a database server.

## Comparison

| Aspect               | Horizontal Scaling                    | Vertical Scaling                        |
|----------------------|----------------------------------------|-----------------------------------------|
| **Capacity Increase**| By adding more machines                | By enhancing the power of existing machines |
| **Fault Tolerance**  | Higher due to distribution             | Lower, as it relies on a single machine |
| **Cost**             | Potentially lower with commodity hardware | Higher due to the need for specialized hardware |
| **Complexity**       | Higher, requires load balancing        | Lower, simpler implementation           |
| **Use Cases**        | Web servers, distributed databases     | Single large databases, legacy systems  |

