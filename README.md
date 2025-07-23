# design
* SOLID Principles
* Design Patterns
* Clean Coding
* Object Oriented Design
* System Design

#### Resoures

1. [Helpful list of LeetCode Posts on System Design at Facebook, Google, Amazon, Uber, Microsoft](https://leetcode.com/discuss/interview-question/1140451/Helpful-list-of-LeetCode-Posts-on-System-Design-at-Facebook-Google-Amazon-Uber-Microsoft, "Link" )
2. [Awesome Scalability](https://github.com/binhnguyennus/awesome-scalability)


# System Design
# Consistency Models in Distributed Systems

## 1. Strong Consistency (Linearizability)
**Definition**:  
Every read always reflects the **most recent write**, as if all operations occurred in a strict global sequence. This provides the illusion that there is only one copy of the data.

- Guarantees **monetary correctness**, **no stale reads**, and **no double-spend**.
- Used in systems like payment processing, account balance, banking, etc.

---

## 2. Eventual Consistency
**Definition**:  
All nodes will **eventually** converge to the same value, but **temporary inconsistencies are allowed**. There are **no guarantees** on how long this convergence will take.

- Common in high-availability systems like DNS or social media feeds.
- Prioritizes availability and partition tolerance (AP in CAP).

---

## 3. Causal Consistency
**Definition**:  
Only operations that are **causally related** are guaranteed to be seen in the same order by all nodes. **Concurrent (unrelated) operations may be seen in different orders.**

- Weaker than strong consistency but stronger than eventual consistency.
- Suitable for collaborative apps like Google Docs or chat.

---

## 4. Session Consistency
**Definition**:  
Within a **single client session**, all reads see the client’s own writes. However, there’s **no global consistency guarantee** across different clients.

- Useful for shopping carts or personalized views where user experience matters more than strict correctness.

---

# Sample Interview Question

**Q: Designing a payment ledger, which consistency model is required to guarantee balance accuracy after concurrent transfers?**

A. Eventual consistency  
B. Causal consistency  
C. Strong (linearizable) consistency  
D. Session consistency

** Correct Answer: C. Strong (linearizable) consistency**

**Explanation**:  
Payment systems must reflect the latest state of all committed transactions immediately. This ensures users cannot double-spend and that account balances are accurate. Only **strong consistency** enforces this level of correctness by making all reads reflect the most recent write across all replicas.

# Rate Limiting in High-Performance Systems

## What is Rate Limiting?

**Rate limiting** is a mechanism that restricts the number of requests a client can make to a service within a specific time window. It is commonly used to:

- Prevent abuse or overuse (e.g., brute-force attacks or DDoS)
- Ensure fair usage among users
- Protect backend systems from overload
- Maintain performance guarantees

---

## Placement Options for Rate Limiters

### 1. Client SDK

**Definition**:  
The rate limiter is implemented on the client side (e.g., in mobile apps or browser-based JavaScript).

**Pros**:
- Reduces unnecessary network traffic from the client
- Immediate user feedback (e.g., disabling a button)

**Cons**:
- Cannot be trusted — clients can be modified or spoofed
- Doesn’t protect the server from malicious or buggy clients

---

### 2. CDN Edge

**Definition**:  
Rate limiting is enforced at the **Content Delivery Network (CDN) edge nodes**, which are geographically distributed servers near users.

**Pros**:
- Rejects excess traffic as early as possible, minimizing latency
- Reduces load on origin infrastructure
- Ideal for public APIs or latency-sensitive services

**Cons**:
- Less control over fine-grained or user-specific policies
- Requires CDN that supports rate limiting features

---

### 3. API Gateway Middleware

**Definition**:  
Rate limits are enforced within a centralized API gateway before requests reach backend services.

**Pros**:
- Centralized control, logging, and monitoring
- Can apply dynamic policies based on user, token, or headers

**Cons**:
- Adds some latency as requests still travel into infrastructure
- May become a bottleneck under extreme load

---

### 4. Origin Application Server

**Definition**:  
The application server itself enforces rate limits as part of business logic.

**Pros**:
- Most flexible; can implement custom rules per user or resource
- No dependency on external services

**Cons**:
- Traffic reaches deepest layer of infrastructure before being rejected
- Increases risk of overloading backend services
- Not ideal for latency-critical or high-throughput applications

---

## Sample Interview Question

**Q. A real-time bidding system needs microsecond decision latency. Where should the rate limiter be placed for minimal request path overhead?**

**Options**:  
A. Client SDK  
B. CDN edge  
C. API gateway middleware  
D. Origin application server

**Correct Answer**: **B. CDN edge**

**Explanation**:  
In latency-sensitive systems like real-time bidding, every microsecond counts. Placing the rate limiter at the **CDN edge** allows invalid or excess traffic to be dropped at the earliest point in the network, protecting the core systems and preserving the strict latency budget.

---

## Summary

| Placement           | Pros                                               | Cons                                                  |
|---------------------|----------------------------------------------------|-------------------------------------------------------|
| Client SDK          | Reduces client-side calls, immediate feedback      | Unreliable, no protection for backend systems         |
| CDN Edge            | Ultra-low latency, minimal backend impact          | Less flexible for dynamic rules                       |
| API Gateway         | Centralized control, good observability            | Some latency and infrastructure cost                  |
| Origin App Server   | Highly customizable logic                          | Worst latency path, backend may already be overloaded |

# Database Redundancy Patterns and Write Amplification

## What is Redundancy in Databases?

**Redundancy** in database systems refers to the duplication of data and infrastructure to improve availability, fault tolerance, and disaster recovery.

---

## Redundancy Patterns

### 1. Read Replicas

**Definition**:  
Read-only copies of the primary database used to distribute read traffic.

**Pros**:
- Reduces read load on the primary database
- Useful for analytics and reporting

**Cons**:
- Not suitable for write failover
- Data may be slightly stale (eventual consistency)

---

### 2. Master–Slave Failover (Active–Passive)

**Definition**:  
One primary node handles writes; a secondary (slave) stays on standby and is promoted during failure.

**Pros**:
- Simple setup for high availability
- Works well for systems with one write source

**Cons**:
- Downtime during failover
- Risk of data loss if replication is asynchronous

---

### 3. Active–Active Multi-Master

**Definition**:  
All nodes can handle writes; data is replicated across all masters.

**Pros**:
- Zero-downtime during maintenance or failover
- High availability across regions

**Cons**:
- Doubles (or more) write traffic due to replication
- Requires complex conflict resolution
- Potential for write amplification

---

### 4. Cold Standby

**Definition**:  
A backup server that stays offline and is manually activated in case of failure.

**Pros**:
- Low cost

**Cons**:
- High recovery time
- Not suitable for systems needing high uptime

---

## Sample Interview Question

**Q13. Which redundancy pattern provides zero-downtime maintenance but doubles database write traffic?**

**Options**:  
A. Read replicas  
B. Master–slave failover  
C. Active–active multi-master  
D. Cold standby

**Correct Answer**: **C. Active–active multi-master**

**Explanation**:  
In an **active–active multi-master** setup, each write operation must be replicated to all other masters to maintain consistency. This provides high availability and allows zero-downtime upgrades or maintenance — but incurs **write amplification**, effectively doubling the write traffic or more, depending on the number of nodes.

---

## Summary Table

| Pattern                | Write Support | Failover Time | Write Overhead | Use Case                                 |
|------------------------|---------------|----------------|----------------|-------------------------------------------|
| Read Replicas          | No            | N/A            | None           | Scaling read-heavy workloads              |
| Master–Slave Failover  | Yes (one node)| Seconds         | Low            | Basic HA with simple failover             |
| Active–Active Multi-Master | Yes (all nodes)| None       | High           | High availability with no downtime        |
| Cold Standby           | Yes (manual)  | Minutes         | None           | Cost-effective backup strategy            |

# Canary Deployment with Session Stickiness

## Overview

**Canary deployment** is a gradual rollout strategy where a new version of a service is released to a small subset of users before full deployment. It helps detect bugs or performance issues in production with minimal blast radius.

**Session stickiness (or session affinity)** ensures that all requests from a user during a session go to the same backend pod. This is especially important for applications that store session-specific state like authentication data or in-memory cache.

---

## What Is Ingress?

In Kubernetes, an **Ingress** is an API object that manages external access to services within a cluster, typically HTTP. It provides:

- URL path-based routing
- Host-based routing
- Header/cookie-based routing
- TLS termination

An **Ingress Controller** (like NGINX or Istio) interprets the Ingress rules and configures load balancing, routing, and even session affinity at the edge.

---

## Techniques for Canary Deployment

### 1. Header-Based Routing via Ingress

Use Ingress rules that inspect HTTP headers or cookies to route traffic. For example, send `X-User-Type: beta` users to the canary pod.

- **Pros**:
    - Fine-grained user targeting
    - Preserves session stickiness
    - Supports progressive rollout strategies

- **Cons**:
    - Requires a properly configured Ingress controller
    - Slightly more complex to set up

---

### 2. Random Pod Selector

Requests are distributed randomly to available pods, without inspecting request context.

- **Pros**:
    - Simple to implement
    - Suitable for stateless apps

- **Cons**:
    - No session affinity
    - No control over who receives the canary version

---

### 3. Blue-Green Deployment with DNS Flip

Two production environments (blue and green) are maintained. The new version is deployed to the green environment, tested, and then traffic is switched using a DNS update.

- **Pros**:
    - Quick rollback by switching DNS back
    - Isolation between environments

- **Cons**:
    - DNS caching may delay changes
    - No gradual rollout or session stickiness
    - Not ideal for real-time canary

---

### 4. DaemonSet Rollout

DaemonSets ensure that one pod runs on each node. This is used for background tasks like log collection or monitoring agents.

- **Pros**:
    - Ideal for infrastructure-level tasks

- **Cons**:
    - Not applicable for service-level rollout
    - No session management or traffic control

---

## Sample Interview Question

**Which strategy best achieves canary deployment in Kubernetes while preserving per-user session stickiness?**

**Options**:  
A. Random pod selector  
B. Header-based routing via Ingress  
C. Blue-green deployment with DNS flip  
D. DaemonSet rollout

**Correct Answer**: **B. Header-based routing via Ingress**

**Explanation**:  
Ingress allows you to define rules that route specific users (based on headers or cookies) to the canary version, while maintaining session stickiness. This enables safe and controlled deployment.

# Canary Deployment with Session Stickiness

## Overview

**Canary deployment** is a gradual rollout strategy where a new version of a service is released to a small subset of users before full deployment. It helps detect bugs or performance issues in production with minimal blast radius.

**Session stickiness (or session affinity)** ensures that all requests from a user during a session go to the same backend pod. This is especially important for applications that store session-specific state like authentication data or in-memory cache.

---

## What Is Ingress?

In Kubernetes, an **Ingress** is an API object that manages external access to services within a cluster, typically HTTP. It provides:

- URL path-based routing
- Host-based routing
- Header/cookie-based routing
- TLS termination

An **Ingress Controller** (like NGINX or Istio) interprets the Ingress rules and configures load balancing, routing, and even session affinity at the edge.

---

## Techniques for Canary Deployment

### 1. Header-Based Routing via Ingress

Use Ingress rules that inspect HTTP headers or cookies to route traffic. For example, send `X-User-Type: beta` users to the canary pod.

- **Pros**:
    - Fine-grained user targeting
    - Preserves session stickiness
    - Supports progressive rollout strategies

- **Cons**:
    - Requires a properly configured Ingress controller
    - Slightly more complex to set up

---

### 2. Random Pod Selector

Requests are distributed randomly to available pods, without inspecting request context.

- **Pros**:
    - Simple to implement
    - Suitable for stateless apps

- **Cons**:
    - No session affinity
    - No control over who receives the canary version

---

### 3. Blue-Green Deployment with DNS Flip

Two production environments (blue and green) are maintained. The new version is deployed to the green environment, tested, and then traffic is switched using a DNS update.

- **Pros**:
    - Quick rollback by switching DNS back
    - Isolation between environments

- **Cons**:
    - DNS caching may delay changes
    - No gradual rollout or session stickiness
    - Not ideal for real-time canary

---

### 4. DaemonSet Rollout

DaemonSets ensure that one pod runs on each node. This is used for background tasks like log collection or monitoring agents.

- **Pros**:
    - Ideal for infrastructure-level tasks

- **Cons**:
    - Not applicable for service-level rollout
    - No session management or traffic control

---

## Sample Interview Question

**Which strategy best achieves canary deployment in Kubernetes while preserving per-user session stickiness?**

**Options**:  
A. Random pod selector  
B. Header-based routing via Ingress  
C. Blue-green deployment with DNS flip  
D. DaemonSet rollout

**Correct Answer**: **B. Header-based routing via Ingress**

**Explanation**:  
Ingress allows you to define rules that route specific users (based on headers or cookies) to the canary version, while maintaining session stickiness. This enables safe and controlled deployment.

# What is ZooKeeper?

**Apache ZooKeeper** is a centralized service for maintaining **configuration information**, **naming**, **synchronization**, and **group services** in distributed systems.

It is most commonly used to solve coordination problems in large-scale distributed environments.

---

## Key Features

### 1. **Centralized Coordination**
ZooKeeper acts like a "manager" in a distributed system that helps multiple nodes stay in sync and agree on the system's state.

### 2. **Consistent State**
All nodes see the same view of data because ZooKeeper uses **strong consistency** guarantees.

### 3. **Ephemeral Nodes and Watches**
Clients can create temporary nodes that disappear if they disconnect. They can also "watch" for changes to be notified in real-time.

---

## Common Use Cases

- **Distributed Locks**: Ensures only one process accesses a resource at a time.
- **Leader Election**: Helps services elect a leader node in a cluster.
- **Service Discovery**: Tracks which services are up and where they're running.
- **Metadata Management**: Stores configuration or coordination data in a consistent way.

---

## Analogy

Think of ZooKeeper as a **whiteboard in a meeting room** where all participants (nodes) write and read shared information. If one participant writes “I’m the leader,” others can read and react to that info consistently.

---

## Downsides

- **Single Point of Coordination**: While ZooKeeper is highly available through replication, if misconfigured, it can become a bottleneck.
- **Not Meant for Large Data**: Best suited for small metadata or control information — not bulk data.

---

## Example: ZooKeeper for Distributed Locking

When multiple services want exclusive access to a shared resource:

1. Each service tries to create a lock node.
2. Only one succeeds — others wait or retry.
3. When the lock holder is done, it deletes the node.
4. Another service gets the lock.

This prevents race conditions without having to manually implement distributed locking logic.

# Kafka Consumer Isolation Levels

Kafka supports **exactly-once semantics (EOS)** using transactions. When consuming from Kafka topics, especially in streaming or transactional applications, the **consumer isolation level** determines **what kind of messages the consumer is allowed to see**.

## Isolation Levels

### 1. Read Uncommitted

**Definition**:  
The consumer reads **all messages** — including:
- Committed records
- Records that are part of an **ongoing** transaction
- Records that belong to **aborted transactions**

This is the **default isolation level** for most Kafka consumers unless explicitly set otherwise.

**Why it reads committed records too**:  
Because **"uncommitted"** doesn’t mean it skips committed data — it just means the consumer doesn't differentiate. It reads **everything available**: both **committed** and **uncommitted** records. Think of it as: "I don’t care about the transaction status."

**Use Cases**:
- Internal analytics where **eventual correctness** is acceptable
- Logging or debugging systems where performance is more important than data integrity

---

### 2. Read Committed

**Definition**:  
The consumer **only reads records from completed (committed) transactions**. Any data from:
- In-progress transactions
- Aborted transactions  
  is **skipped**.

**Use Cases**:
- Financial systems or **critical analytics jobs** where **correctness** and **integrity** are more important than latency

---

## Summary Comparison

| Feature                     | Read Uncommitted             | Read Committed                  |
|----------------------------|------------------------------|----------------------------------|
| Reads committed data       | Yes                          | Yes                              |
| Reads uncommitted data     | Yes                          | No                               |
| Skips aborted transactions | No                           | Yes                              |
| Latency                    | Lower                        | Slightly higher                  |
| Use case                   | Debugging, non-critical apps | Analytics, finance, correctness |

---

## Sample Interview Question

**Question**:  
A streaming analytics job replays Kafka topics from an earlier offset. Which consumer isolation level prevents reading uncommitted transactional records?

**Options**:
A. Read Committed  
B. Read Uncommitted  
C. Repeatable Read  
D. Serializable

**Correct Answer**: **A. Read Committed**

**Explanation**:  
Kafka's `read_committed` isolation level ensures the consumer only reads messages from completed transactions. This prevents issues like double counting, processing partially written data, or reading messages that were later rolled back.

# SSL Termination and Connection Reuse in Load Balancing

In high-throughput systems like IoT platforms, where tens of thousands of devices connect and send frequent updates, establishing a new TLS (SSL) connection for each request can become a bottleneck.

---

## SSL/TLS Handshake: The Cost

Every new TLS connection requires a handshake:
- **Key exchange**
- **Certificate validation**
- **Session negotiation**

This handshake adds **latency** and **CPU overhead**, especially when scaled to thousands of devices per second.

---

## SSL Termination

### What it does:
The load balancer (or proxy) handles the TLS handshake on behalf of backend servers. Once the connection is terminated:
- Data is decrypted at the load balancer.
- Internal communication with backend services can happen over plain TCP or reused secure channels.

### Benefits:
- Offloads encryption workload from backend servers.
- Simplifies certificate management in one central place (the load balancer).
- Enables performance optimizations like **connection reuse**.

---

## Connection Reuse (Pooling)

After terminating TLS:
- The load balancer maintains **persistent connections** to backend services.
- Multiple client sessions can share the same backend connection, avoiding the cost of creating new TCP/TLS connections each time.

### Benefits:
- Reduces **latency** and **CPU usage**.
- Especially valuable in **burst-heavy** systems like IoT fleets, where frequent reconnects are common.

---

## Why It's Ideal for IoT

IoT devices often:
- Sleep and wake frequently.
- Reconnect often due to intermittent connectivity.
- Send small, bursty messages.

SSL termination + connection reuse ensures:
- Minimal handshake overhead.
- Scalable backend performance without compromising security.

---

## Sample Question

**Q: For an IoT fleet sending 100,000 writes/sec, which cloud load-balancer feature reduces TLS handshake cost per device reconnect?**

**A. HTTP/2 multiplexing**  
**B. Connection draining**  
**C. Session affinity cookies**  
**D. SSL termination with connection reuse**

**Correct Answer: D — SSL termination with connection reuse**

**Explanation:**  
SSL termination offloads the handshake from the backend, while connection reuse prevents the overhead of creating new backend connections for every device reconnect.

# Read Replicas and Latency in Strong-Leader Replication

## What Are Read Replicas?

Read replicas are secondary database instances that replicate data from a primary (leader) database. They serve **read-only traffic** to reduce the load on the primary.

## Use Case

- Improve **read scalability**
- Distribute **read queries** across multiple nodes
- Useful for **read-heavy workloads**

## Strong-Leader Replication Model

- All **writes** go to a single primary node.
- **Replicas pull updates asynchronously** from the primary.
- This can cause **replication lag** — a delay between the primary committing data and replicas reflecting it.

## Replica Lag and Its Impact

When a new read replica is added:
- It starts with a **cold cache** and **incomplete replication state**.
- Queries routed to this replica may return **stale data**.
- If the load balancer distributes traffic evenly without replica awareness, this can **increase average read latency**.

Even though total capacity increases, performance may temporarily degrade due to inconsistent read speed across replicas.

## Trade-offs

### Pros
- Improved throughput for read queries
- Reduced load on the primary

### Cons
- Risk of stale reads due to replica lag
- Uneven latency across replicas
- Additional operational complexity in monitoring and managing lag

---

## Sample Question

**Why does adding a second read replica sometimes increase average read latency under strong-leader replication?**

**A. Replica lag introduces staleness**  
**B. Load balancer favors the new replica until warmed**  
**C. Cache-aside misses double**  
**D. Leader election frequency rises**

**Correct Answer:** A

**Explanation:**  
New replicas may lag behind the leader. If queries are sent to lagging replicas, they take longer or return stale results, increasing average read latency across the system.

# Key Load Balancer Features Explained

## A. HTTP/2 Multiplexing

### What It Is:
A feature of the HTTP/2 protocol that allows **multiple requests and responses to be sent over a single TCP connection simultaneously**.

### Why It Matters:
- Reduces latency by eliminating the need to create new connections for each request.
- Efficient for mobile or IoT clients making many small API calls.
- Prevents "head-of-line blocking" that was common in HTTP/1.1.

### Example:
An IoT device can send telemetry data and receive configuration updates at the same time without opening new connections.

---

## B. Connection Draining

### What It Is:
A mechanism used during load balancer updates or server shutdowns to **gracefully remove backends from service** by allowing existing connections to complete.

### Why It Matters:
- Prevents termination of in-flight requests.
- Ensures **zero downtime** during server upgrades, deployments, or scaling events.

### How It Works:
When a server is marked for removal, new connections are blocked, but existing sessions are allowed to finish before the server is de-registered.

---

## C. Session Affinity Cookies

### What It Is:
Also called **sticky sessions**, this technique uses cookies (or sometimes IP hashing) to ensure that **all requests from a client go to the same backend server**.

### Why It Matters:
- Important for apps that **store session state locally** on the server (instead of using external shared storage like Redis).
- Ensures consistent user experience (e.g., staying logged in, seeing the same cart items).

### Trade-offs:
- Can lead to uneven load distribution if many clients are bound to a few servers.
- Doesn't scale well for completely stateless services.

---
# Consistency in Dynamo-Style Quorum Systems

## Concept: Quorum-Based Consistency

In a Dynamo-style distributed system, each data item is replicated across `N` nodes.

### Parameters:
- `N`: Total number of replicas
- `W`: Number of nodes that must acknowledge a write
- `R`: Number of nodes that must return a response for a read

### Key Rule:
If `R + W > N`, the system ensures that at least one node in the read quorum has the latest write. This overlap guarantees that a read reflects the most recent successful write, offering **strong consistency per object**.

This approach avoids the need for a central coordinator and allows the system to be both available and consistent for successful operations.

---

## Sample Question

**Which consistency guarantee is naturally provided by Dynamo-style quorum reads/writes when R + W > N?**

**A. Causal**  
Tracks logical ordering of events but does not guarantee reading the latest write.

**B. Monotonic read**  
Ensures that a user never sees older data after having seen newer, but not necessarily the latest data from all nodes.

**C. Read-your-writes**  
A client always sees its own writes. Useful for session-based applications but not guaranteed in all distributed systems.

**D. Strong consistency for successful operations**  
**Correct Answer**

**Explanation**:  
With overlapping read and write quorums (`R + W > N`), the system ensures that reads see the most recent successful write. This leads to strong consistency for individual operations, even in the presence of replication.


# Redis vs Memcached: Persistence Capabilities

## Concept: In-Memory Caching with Persistence

Both Redis and Memcached are popular in-memory key-value stores, but they differ in their ability to **persist data** across process restarts.

### Redis
- **Supports Persistence**:
  - **RDB (Redis Database Backup)**: Periodic snapshots of data saved to disk.
  - **AOF (Append-Only File)**: Logs every write operation for full recovery.
- **Use Case**: Ideal when cached data must survive server crashes or restarts.

### Memcached
- **No Persistence**:
  - All data is stored purely in memory.
  - On restart, everything is lost.
- **Use Case**: Good for ephemeral caching where losing data is acceptable (e.g., page fragments, session tokens).

### Comparison Summary
| Feature                 | Redis            | Memcached        |
|------------------------|------------------|------------------|
| Persistence            | Yes (RDB, AOF)   | No               |
| Data Structures        | Rich (Lists, Sets, Hashes) | Simple (Strings only) |
| Scalability (multi-core)| Moderate         | Better           |
| Protocol               | Custom ASCII & Binary | ASCII & Binary   |

---

## Sample Question

**Choosing between Redis and Memcached for a cache that must persist across process restarts, which feature tilts the decision?**

**A. LRU eviction**  
Both Redis and Memcached support LRU eviction. Not a distinguishing factor.

**B. Binary protocol**  
Both can use binary protocols, but it does not affect persistence.

**C. RDB/AOF persistence**  
**Correct Answer**

**Explanation**:  
Redis provides disk-based durability options (RDB, AOF) to persist data across process restarts, whereas Memcached does not. Hence, Redis is the right choice when persistence is needed.

**D. Multi-core scalability**  
Memcached handles multi-threading better, but that isn’t relevant to persistence.

# Redis vs Memcached: Key Feature Definitions

## A. LRU Eviction (Least Recently Used)

**What it is:**  
An eviction policy where the cache removes the item that hasn't been accessed for the longest time when space is full.

**Use case:**  
Helps retain frequently used data and discard less relevant items automatically.

**Support:**  
Available in both Redis and Memcached.

---

## B. Binary Protocol

**What it is:**  
A compact, machine-readable communication format between client and server, as opposed to human-readable text (ASCII) protocols.

**Use case:**  
Reduces message size and improves speed of parsing and transmission.

**Support:**  
Memcached has optional binary protocol. Redis uses a custom protocol (RESP), which is efficient but not a binary protocol in the traditional sense.

---

## C. RDB / AOF Persistence

**RDB (Redis Database Backup):**  
Saves snapshots of the in-memory data to disk at specified intervals.

**AOF (Append Only File):**  
Logs every write operation to a file for full recovery.

**Use case:**  
Enables data recovery after crashes or restarts.

**Support:**  
Only Redis provides persistence. Memcached is purely in-memory and loses data on restart.

---

## D. Multi-core Scalability

**What it is:**  
The ability of a system to use multiple CPU cores in parallel to process requests.

**Use case:**  
Improves throughput and performance in high-load environments.

**Support:**  
Memcached has strong multi-threading support.  
Redis is traditionally single-threaded, but newer versions (Redis 6+) support I/O threading for improved concurrency.

---

## Recap: Feature Comparison

| Feature                 | Redis                   | Memcached               |
|------------------------|-------------------------|-------------------------|
| LRU Eviction           | Yes                     | Yes                     |
| Binary Protocol        | Custom protocol (RESP)  | Optional binary support |
| RDB / AOF Persistence  | Yes                     | No                      |
| Multi-core Scalability | Partial (Redis 6+)      | Yes                     |