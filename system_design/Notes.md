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

## Horizontal vs. Vertical Scaling

### Horizontal Scaling

**Definition**: Horizontal scaling, also known as scaling out, involves adding more servers to a system to distribute the load.

**Key Characteristics**:
- **Capacity Increase**: Increases capacity by adding more machines to the system.
- **Fault Tolerance**: Improves fault tolerance by distributing the load across multiple servers. If one server fails, others can take over.
- **Resiliency**: Higher resiliency as the system can continue to operate even if some servers fail.
- **Network Calls**: Requires network calls between servers, which can introduce latency.
- **Data Consistency**: Can lead to data inconsistency if not managed properly due to distributed nature.
- **Hardware Limitations**: Scales well as it is not limited by hardware constraints of a single machine.
- **Cost**: Can be cost-effective as commodity hardware can be used.
- **Complexity**: Often increases system complexity due to the need for load balancing and data distribution mechanisms.
- **Examples**: Adding more web servers behind a load balancer, distributing databases across multiple nodes.

### Vertical Scaling

**Definition**: Vertical scaling, also known as scaling up, involves adding more resources (CPU, RAM, storage) to an existing server.

**Key Characteristics**:
- **Capacity Increase**: Increases capacity by enhancing the power of a single machine.
- **Fault Tolerance**: Limited improvement in fault tolerance since the failure of the single, more powerful machine can lead to downtime.
- **Single Point of Failure**: Higher risk of a single point of failure as the entire system relies on one machine.
- **Inter-Process Communication**: Uses inter-process communication, which is faster than network calls.
- **Data Consistency**: Easier to maintain data consistency as all operations are on a single machine.
- **Hardware Limitations**: Limited by the maximum hardware capacity of a single machine.
- **Cost**: Can be expensive as it often requires high-end, specialized hardware.
- **Complexity**: Simpler to implement as it doesn't require changes to the application architecture.
- **Examples**: Upgrading a server's CPU, adding more memory to a database server.

### Comparison

| Aspect                       | Horizontal Scaling                    | Vertical Scaling                        |
|------------------------------|----------------------------------------|-----------------------------------------|
| **Capacity Increase**        | By adding more machines                | By enhancing the power of existing machines |
| **Fault Tolerance**          | Higher due to distribution             | Lower, as it relies on a single machine |
| **Resiliency**               | Higher, continues operation if some servers fail | Lower, failure leads to system downtime |
| **Network Calls**            | Requires network calls, introduces latency | Uses faster inter-process communication |
| **Data Consistency**         | Can lead to inconsistency              | Easier to maintain consistency          |
| **Single Point of Failure**  | Lower risk                            | Higher risk                             |
| **Hardware Limitations**     | Scales well, not limited by hardware constraints | Limited by maximum hardware capacity    |
| **Cost**                     | Potentially lower with commodity hardware | Higher due to the need for specialized hardware |
| **Complexity**               | Higher, requires load balancing        | Lower, simpler implementation           |
| **Use Cases**                | Web servers, distributed databases     | Single large databases, legacy systems  |


## Fault Tolerance vs. Fault Resilience

### Fault Tolerance

**Definition**: The system continues operating without noticeable impact to the user, except for a minor delay during failover.

**Key Point**: Users experience no disruption, only a slight delay as the system switches to a backup component.

### Fault Resilience

**Definition**: The system continues operating despite failures in non-critical components, maintaining overall functionality.

**Key Point**: Main services remain functional even if non-critical services fail. For example, if Netflix's recommendation or comments services fail, users can still search for and watch movies.

# Monolith vs. Microservices

## Monolith

**Definition**: A monolithic architecture is a single, unified codebase where all functionalities and components are interconnected and interdependent.

**Key Characteristics**:
- **Single Codebase**: All components and services are part of a single application.
- **Deployment**: Deployed as a single unit.
- **Scalability**: Scaling requires scaling the entire application, not individual components.
- **Development**: Easier to develop initially but can become complex as the application grows.
- **Maintenance**: Difficult to maintain and update; changes in one part can affect the entire system.
- **Examples**: Traditional web applications, early-stage startups.

## Microservices

**Definition**: A microservices architecture divides the application into small, independent services, each responsible for a specific functionality.

**Key Characteristics**:
- **Independent Services**: Each service is developed, deployed, and scaled independently.
- **Deployment**: Each service can be deployed separately.
- **Scalability**: Individual services can be scaled independently based on demand.
- **Development**: Facilitates continuous deployment and allows different teams to work on different services simultaneously.
- **Maintenance**: Easier to maintain and update; changes in one service do not affect others.
- **Examples**: Large-scale applications like Netflix, Amazon.

## Comparison

| Aspect            | Monolith                                | Microservices                             |
|-------------------|-----------------------------------------|-------------------------------------------|
| **Codebase**      | Single unified codebase                 | Multiple independent services             |
| **Deployment**    | Deployed as a single unit               | Services deployed separately              |
| **Scalability**   | Entire application scaled together      | Individual services scaled independently  |
| **Development**   | Easier to start, complex over time      | Allows parallel development               |
| **Maintenance**   | Difficult, changes can affect whole app | Easier, isolated changes                  |
| **Examples**      | Traditional web apps, early startups    | Large-scale apps like Netflix, Amazon     |



