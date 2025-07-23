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

# Active-Passive Failover and the Risk of Split-Brain

## What is Active-Passive Failover?

In this setup:
- **Active node** handles all requests.
- **Passive node** stays on standby and takes over if the active node fails.

Used for **high availability**, especially in databases, load balancers, or file systems.

---

## The Problem with Port-Only Health Checks

### TCP Port Liveness Check:
- Probes only whether a service port (like 5432 for Postgres) is "open" or responsive.
- Does **not** verify whether the service is fully functional or reachable from all nodes.

---

## What is Split-Brain?

**Definition:**
A condition where both the active and passive nodes believe they are active due to inconsistent failure detection.

**Example:**
- Network partition causes each node to think the other is down.
- Health checks see their own TCP port as "healthy."
- Both start accepting writes → leads to **divergent data** and possible corruption.

---

## Why It Happens

TCP liveness ≠ end-to-end health.  
Without verifying full cluster connectivity or quorum, you can't guarantee which node should be active.

---

## How to Avoid It

- Use **application-level health checks** (not just TCP).
- Verify cluster **quorum** before promoting a passive node.
- Use **split-brain prevention mechanisms**:
  - Etcd, Zookeeper, or consensus-based leader election.
  - Fencing tokens (to prevent two nodes from writing simultaneously).

---

## Sample Interview Question

**Question:**  
In an active-passive failover setup, what is the primary availability risk if health checks only probe TCP port liveness?

**Options:**
A. Split-brain write conflicts  
B. False negatives during GC pauses  
C. Fail-stop norm violations  
D. Silent data corruption on primary

**Correct Answer:** A

**Explanation:**  
TCP liveness doesn't capture full connectivity. If both nodes appear "healthy" during a network partition, each may become active, leading to conflicting writes — the classic split-brain scenario.

# Cache Warm-up Strategies

When an application starts or deploys, the cache is often empty. This leads to **cold-start latency**, where the first set of user requests hit the database directly because the cache hasn’t been populated yet.

To avoid this, **cache warm-up strategies** are used to pre-fill the cache with important data ahead of time.

---

## A. Lazy Loading

**Definition:**  
Cache entries are populated on-demand when the application receives a request.

**Pros:**
- Simple to implement
- Saves memory by only caching what’s actually needed

**Cons:**
- First access is slow (cold-start)
- Poor fit for latency-sensitive use cases

---

## B. Batch Pre-population (During Deployment)

**Definition:**  
Cache is proactively filled with key data immediately after deployment or restart.

**Pros:**
- Fast initial responses — ideal for latency-sensitive apps
- Reduces risk of stampede to the backend database

**Cons:**
- Needs good prediction of which data is “hot”
- Additional complexity during deployment pipeline
 
- **Best for infrequently accessed but latency-sensitive data**

---

## C. Write-through Caching

**Definition:**  
Every time the database is updated, the cache is also updated.

**Pros:**
- Keeps cache always in sync with DB
- Useful for data with frequent writes

**Cons:**
- Doesn’t help with cold-start if no write happens before first read
- More write overhead

---

## D. TTL of Zero

**Definition:**  
Cache entries expire immediately after being written. TTL of zero means the Time-To-Live (TTL) for a cache entry is set to 0 seconds, which causes the cache to expire the item immediately after it’s written. In effect, the cache never holds the data, and every request is forced to fetch fresh data from the backend (usually a database).

**Pros:**
- Forces cache to always fetch fresh data

**Cons:**
- Completely defeats the purpose of caching
- High DB load, high latency

---

## Sample Interview Question

**Question:**  
Which cache warm-up strategy minimizes cold-start latency for infrequently accessed yet latency-sensitive dataset?

**Options:**
A. Lazy loading  
B. Batch pre-population during deployment  
C. Write-through caching  
D. TTL of zero

**Correct Answer:** B

**Explanation:**  
Batch pre-population allows critical cache entries to be loaded before any user request hits the system. This avoids cold starts and improves initial response times for important but rarely accessed data.
## Ensuring Single Execution in Distributed Cron Schedulers

### Concept: Fencing Tokens and Leadership Epochs

In distributed systems with active-passive scheduling (e.g., cron jobs), it's critical to ensure that a job runs **only once globally**, even during failovers or leader re-elections.

### What Are Fencing Tokens?

- A **fencing token** is a **monotonically increasing number** assigned to each leader when it is elected.
- Every time the leader tries to perform an operation (e.g., trigger a job), it includes its token.
- The underlying system (like a database or job queue) **accepts only the highest token**, rejecting stale ones.
- This ensures **previous leaders** (who might still be alive due to network delay or isolation) **cannot act on old leadership**.

### Why It’s Needed

Without fencing:
- A previously active node (after network partition or delayed failover) may still think it’s the leader.
- It could trigger the job again, resulting in **duplicate job execution**, which violates the one-job-at-a-time guarantee.

### Comparison with Other Strategies

| Option | Description | Drawback |
|--------|-------------|----------|
| A. Table-level pessimistic lock | Locks the row or table to prevent duplicate execution | Doesn’t prevent stale leaders from acting |
| B. Fencing tokens with monotonically increasing epoch | Uses leadership tokens to validate execution | ✅ Correct answer – prevents stale writes |
| C. Client-side retries with exponential backoff | Retries on failure | Doesn’t solve the distributed consensus problem |
| D. Idempotent job handlers | Safe to run jobs multiple times | Still runs multiple times, just minimizes harm |

## Definitions of Distributed Execution Safeguards

### A. Table-level Pessimistic Lock

A **pessimistic lock** prevents other processes from accessing or modifying a resource (like a database row or table) by locking it upfront.
- Typically used in relational databases.
- Ensures that only one process can act on the data at a time.
- Example: `SELECT ... FOR UPDATE` in SQL.

**Limitation:**  
In distributed systems, this doesn't prevent stale nodes (e.g., old leaders after a partition) from acquiring locks unless external coordination (like fencing) is in place.

---

### B. Fencing Tokens with Monotonically Increasing Epoch

A **fencing token** is a unique, ever-increasing number (an epoch) given to a leader at the time of election.
- All operations by the leader must include this token.
- Shared systems (like databases or queues) **compare the token** to previous values.
- Only the **most recent (highest) token** is allowed to act.
- Prevents stale leaders from making changes after losing leadership.

**Usage:**  
Critical for ensuring **exactly-once execution** across nodes in distributed job schedulers or locking systems.

---

### C. Client-side Retries with Exponential Backoff

This is a **resiliency pattern** where clients automatically retry failed operations after waiting, with increasing delay between retries.
- The first retry happens quickly.
- Subsequent retries wait longer and longer (e.g., 1s, 2s, 4s, 8s...).
- Helps reduce server overload and handle transient errors.

**Limitation:**  
It addresses *failure recovery*, not coordination or correctness in distributed leadership.

---

### D. Idempotent Job Handlers

An **idempotent operation** can be performed multiple times without changing the outcome beyond the initial application.
- Example: Setting a value to `42` is idempotent — no matter how many times you do it, the result is the same.
- In job scheduling, this means the handler is designed so that even if the same job runs twice, **it doesn’t cause harm**.

**Limitation:**  
It doesn't prevent duplicates; it only makes them less dangerous.

### Sample Question

**Q: A distributed cron scheduler must ensure each job runs once globally. Which mechanism best prevents duplicate execution if leadership changes?**  
**A. Table-level pessimistic lock**  
**B. Fencing tokens with monotonically increasing epoch**  
**C. Client-side retries with exponential backoff**  
**D. Idempotent job handlers**

**Correct Answer: B**  
**Explanation:** Fencing tokens ensure that stale leaders cannot trigger jobs after losing leadership, preventing duplicates.

## Partitioning Techniques: Hash vs Range

### Hash-Based Partitioning

- **Definition**: Uses a hash function on a key (e.g., user ID) to assign data to a shard.
- **Benefits**:
  - Ensures uniform distribution of data and requests.
  - Reduces risk of hotspots by balancing load across all shards.
  - Ideal for random-access workloads where any record can be queried.
- **Drawbacks**:
  - Inefficient for range queries (`<`, `>`, `BETWEEN`), since related keys may be on different shards.
  - Cross-shard aggregation and sorting are more complex.

---

### Range-Based Partitioning

- **Definition**: Divides data based on continuous key ranges (e.g., 1–1000 in one shard, 1001–2000 in another).
- **Benefits**:
  - Enables efficient range queries and sorted access.
  - Easy to understand and implement for ordered data.
- **Drawbacks**:
  - Can cause uneven load distribution if data is not uniformly accessed.
  - Vulnerable to hotspotting — recent or popular ranges may overload specific shards.

---

### When to Use Which?

| Use Case                         | Best Partitioning Type     |
|----------------------------------|-----------------------------|
| Uniform/random access patterns   | Hash-Based Partitioning     |
| Range queries or sorted data     | Range-Based Partitioning    |

---

### Sample Question

**Why does hash-based data partitioning outperform range partitioning for a social graph with uniform random access?**

**A.** Simplifies query planner  
**B.** Enables compression  
**C.** Evens data and traffic across shards  
**D.** Eliminates foreign keys

**Correct Answer:** C  
**Explanation:** In a social graph with uniformly random access, hash partitioning distributes load evenly across shards, preventing hotspots that often occur with range partitioning.

# What is a Hotspot in Distributed Systems?

## Definition

A **hotspot** is a condition in distributed systems where one node, shard, or partition receives significantly more traffic or load than others. This leads to **load imbalance**, **increased latency**, and potential **performance bottlenecks**.

---

## Why Hotspots Occur

- **Skewed Data Access:** For example, if many users access the same resource repeatedly (e.g., a celebrity profile).
- **Inefficient Partitioning:** Range-based partitioning might group all recent data into a single shard.
- **Non-uniform Traffic:** Specific data becomes temporarily or permanently popular (e.g., trending news, viral posts).

---

## Consequences of Hotspots

- Slower response times for affected users
- Increased error rates or timeouts
- Underutilized resources in the rest of the system
- Difficulty scaling effectively

---

## Strategies to Prevent or Mitigate Hotspots

| Technique                                | Description |
|-----------------------------------------|-------------|
| **Hash-based partitioning**             | Spreads keys more evenly by applying a hash function. |
| **Consistent hashing with virtual nodes** | Further distributes keys across multiple nodes to avoid load spikes. |
| **Caching**                             | Reduces repeated backend calls for the same data. |
| **Rate limiting**                       | Prevents excessive requests to a single resource. |
| **Load balancing**                      | Distributes incoming requests to less busy servers or instances. |

---

## Example Scenario

In a social media platform, if 1 million users follow a single celebrity and constantly refresh their profile page, the server handling that user's data becomes a hotspot. Without load distribution, this server can slow down or crash while others remain idle.

# API Rate Limiting and Response Headers

## Concept: Handling Rate Limits in APIs

When clients exceed the allowed number of requests to an API within a certain timeframe, the server enforces **rate limiting**. This protects backend resources and ensures fair usage across clients.

---

## Key HTTP Response Header: `Retry-After`

### What is it?

- **`Retry-After`** is an HTTP response header that tells the client **how long to wait before retrying** a request that was rate-limited.

### When is it used?

- Most commonly used with **HTTP status code 429** (Too Many Requests).
- Can also be used with **503** (Service Unavailable).

### Format

The header can specify either:
- A **number of seconds** to wait:
  - Retry-After: 120
- Or an **absolute date/time** in HTTP date format:
  - Retry-After: Wed, 21 Oct 2025 07:28:00 GMT
---

## Other Response Headers (for contrast)

| Header          | Purpose |
|-----------------|---------|
| **Content-Type** | Describes the media type of the response (e.g., `application/json`). |
| **Last-Modified** | Tells when the resource was last changed. Useful for caching. |
| **ETag**         | Provides a version identifier for the resource; used in cache validation. |

---

## Sample Question

**Which API rate-limiting response header informs clients when they can retry after a throttle?**

**A. Content-Type**  
**B. Retry-After**  
**C. Last-Modified**  
**D. ETag**

**Correct Answer:** B  
**Explanation:** The `Retry-After` header is specifically designed to inform clients about how long to wait before making another request after being throttled.

# Eventual Consistency and User Profile Updates

## What is Eventual Consistency?

**Eventual consistency** is a model in distributed systems where data updates may not be immediately visible across all replicas, but all nodes are guaranteed to converge to the same value if no new updates occur.

- **Short-term:** Reads may return stale or divergent data.
- **Long-term:** All replicas reflect the latest value eventually.

---

## Why Is This a Problem for Profile Updates?

- Users might update their profile (e.g., name, photo) on one device.
- On another device or browser tab, they may still see outdated data.
- This causes **confusion** and may result in **conflicting updates**.

---

## Solution: Client-side Conflict Resolution UI

To reduce confusion:

- Show a **conflict resolution prompt** if versions diverge.
- Let the user **choose between conflicting changes**, or merge manually.
- Optionally show metadata like **timestamp** or **source of change**.
- Provide a clear **"last write wins"** explanation if auto-resolution is used.

---

## Sample Question

**Under eventual consistency, what must an application developer implement to avoid user confusion on profile updates?**

**A. Idempotent GET requests**  
**B. Client-side conflict resolution UI**  
**C. Strict schema validation**  
**D. Sticky sessions**

**Correct Answer:** **B**

**Explanation:** Since eventual consistency allows divergent reads, users might see conflicting profile data. Providing a UI to resolve or explain these conflicts ensures clarity and trust in the application.

# Semi-Synchronous vs Asynchronous Replication in MySQL

## What is Asynchronous Replication?

- In **asynchronous replication**, the primary database **does not wait** for replicas to acknowledge writes.
- This offers **low write latency** but risks **data loss** if the primary fails before replicas catch up.

## What is Semi-Synchronous Replication?

- In **semi-synchronous replication**, the primary **waits for at least one replica** to acknowledge receiving the write **before committing**.
- This ensures that at least one backup copy exists, improving **durability**.

---

## Latency Trade-off

### Pros:
- **Improved fault tolerance:** At least one replica has the write before commit completes.
- **Reduced data loss window** compared to async.

### Cons:
- **Increased write latency:** Transactions block until one replica ACKs.
- **Slower commit times** in write-heavy systems or when replica lag is high.

---

## Sample Question

**A high-availability MySQL cluster uses semi-sync replication. What latency impact arises compared to async replication?**

**A. None; reads only slow**  
**B. Added commit latency waiting for ACK from a replica**  
**C. Increased TLS handshake time**  
**D. Longer DNS resolution**

**Correct Answer:** **B**

**Explanation:** Semi-synchronous replication introduces extra latency during commit because the primary waits for at least one replica acknowledgment before considering a write successful.

# Caching Layers in a Web Architecture

## A. Browser Cache

**Definition**:  
A cache maintained by the user's web browser that stores static assets (e.g., images, scripts, HTML) locally on the user's device.

**Purpose**:  
Reduce repeat HTTP requests by serving content from the local disk, improving page load time.

**Best For**:  
Static and public content that doesn’t change often (e.g., CSS files, logos).

**Limitations**:
- Limited control by server after cache expiry.
- Not suitable for sensitive or personalized data.

---

## B. Edge POP Cache

**Definition**:  
Edge servers (Points of Presence) deployed by CDNs that cache content closer to end users.

**Purpose**:  
Reduce load on origin servers and improve global latency by serving cached content at the network edge.

**Best For**:  
High-traffic, bandwidth-heavy applications like video streaming, file downloads, etc.

**Supports**:  
Some personalization using headers like `Vary`, cookies, or URL parameters.

---

## C. Application Memory Cache

**Definition**:  
In-memory caching done within the application server using tools like Redis or Memcached.

**Purpose**:  
Speed up access to frequently used data like computed results, user sessions, etc.

**Best For**:  
Dynamic content and per-user session data.

**Limitation**:  
Limited to the memory of the application server, and may not persist across server restarts.

---

## D. DB Buffer Cache

**Definition**:  
A memory region in a database server that caches frequently accessed disk pages.

**Purpose**:  
Reduce disk I/O and improve query performance.

**Scope**:  
Internal to the database engine (e.g., MySQL, PostgreSQL), not application-controlled.

**Limitation**:  
Only helps optimize DB performance, doesn’t reduce load on origin servers.

---

## Sample Question

**In a video-streaming CDN, which caching layer reduces origin bandwidth the most while maintaining per-user personalization?**

**A. Browser cache**  
**B. Edge POP cache** 
**C. Application memory cache**  
**D. DB buffer cache**

**Correct Answer:** **B**

**Explanation:**  
Edge POP caches, placed geographically close to users, reduce bandwidth use by caching at the edge. They also allow for controlled personalization through cache key manipulation or HTTP headers.

# Saga Pattern in Distributed Systems

## What is the Saga Pattern?

The **Saga pattern** is a design used to manage long-running transactions and maintain data consistency across microservices, without relying on distributed transactions (like two-phase commit).

It works by breaking a global transaction into a series of **local transactions**, each of which performs a small part of the whole. If one of these fails, a corresponding **compensating transaction** is issued to undo the work of the previous successful steps.

Sagas can be implemented in two ways:
- **Choreography**: Each service knows who to call next or what event to publish.
- **Orchestration**: A central coordinator tells each service when to act.

## Why Sagas are Needed

Distributed systems often can't use traditional transactions because:
- They span multiple databases or services
- They introduce performance or availability bottlenecks
- They create coupling between systems

Sagas offer a fault-tolerant alternative where rollback is not automatic but handled manually through compensating actions.

---

## Related Concepts

** Max TPS threshold exceeded**  
This refers to exceeding the allowed number of transactions per second. It might result in throttling but doesn't trigger rollback in a Saga.

**Compensating action from downstream step**  
This is the correct answer. In Sagas, if a later step fails, it initiates compensating actions to undo earlier steps, effectively rolling back the distributed transaction.

**Broken circuit breaker**  
Circuit breakers are used to prevent repeated calls to failing services. They help with resilience but don't relate directly to rollback logic in Sagas.

**GC pause detection**  
Garbage Collection pauses affect system performance but don't influence Saga rollbacks.

---

## Question

**Q:** When designing a Saga, local transaction rollback is triggered by:

**A.** Max TPS threshold exceeded  
**B.** Compensating action from downstream step  
**C.** Broken circuit breaker  
**D.** GC pause detection

** Correct Answer:** B

**Explanation:**  
Sagas use compensating transactions to manually undo previous steps when a failure is detected in a downstream step. This ensures eventual consistency in distributed systems without global locks or atomic transactions.

# Understanding Message Queue Latency and Consumer Lag

In distributed systems using messaging platforms like **Kafka**, **RabbitMQ**, or **Google Pub/Sub**, latency can stem not only from network delays but also from how queues are consumed.

---

## 1. Consumer Lag

**Consumer lag** refers to the number of messages waiting in the queue that haven't yet been consumed.

- It increases when producers send messages faster than consumers can process.
- A growing lag leads to **higher latency**, even when message production is steady.
- High p-95 or p-99 latency often points to consumer lag.

### Example:
If messages arrive every 500 ms but are not processed quickly, they pile up. Over time, even new messages have to wait their turn, increasing end-to-end latency.

---

## 2. Dead-letter Queues (DLQs)

**DLQs** hold messages that failed repeatedly during processing.

- They are for isolating bad messages, not for handling performance bottlenecks.
- A rising DLQ size indicates bad data or bugs, but not consumer backlog.

---

## 3. Message TTL Violations

**TTL (Time-to-Live)** is how long a message can exist in the queue before being discarded.

- TTL violations occur when messages are not processed within their allowed time window.
- They indicate severe processing delays or unresponsive consumers.
- Not directly related to latency spikes unless the backlog is extreme.

---

## 4. Broker CPU Idle

**Broker CPU Idle** measures the unused CPU of the message broker.

- High CPU idle means the broker is underutilized.
- Low CPU idle may suggest it's overwhelmed, but doesn’t always correlate with consumer delay.
- This is more of a system health metric than a direct latency indicator.

---

## Sample Question

**Scenario:**  
A client consistently sends requests 500 ms apart. However, it starts experiencing 200 ms latency spikes at the p-95 percentile. Which queue metric is most likely trending upward?

**Options:**
- A. Dead-letter size
- B. Consumer lag
- C. Message TTL violations
- D. Broker CPU idle

**Correct Answer:**  
**B. Consumer lag**

**Explanation:**  
Steady input with increasing latency typically signals that the system is queuing up messages faster than they are being consumed. This is exactly what consumer lag measures. The other metrics reflect different issues not directly tied to increasing latency under stable load.