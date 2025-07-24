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

### Concept: Horizontal Scalability in Master-Slave Relational Databases

Horizontal scalability refers to increasing a system's capacity by adding more machines. In the context of relational databases, **master-slave (or primary-replica)** setups are common.

- The **master** handles both **reads and writes**.
- The **slaves (replicas)** only handle **reads**, and they receive updates from the master through replication.

This design improves **read scalability** — as more slaves are added, more read traffic can be handled. But **write operations always go through the master**, which becomes a bottleneck. This architectural limit makes it **impossible to scale writes horizontally** unless you adopt more advanced setups like **sharding** (splitting data across databases) or **multi-master replication**.

---

### Related Concepts

- **Binlog Format**:  
  The binary log (binlog) records all changes made to the database. It’s used by slaves to replicate data from the master. Different formats (e.g., statement-based, row-based) impact replication efficiency and reliability. However, changing the binlog format does **not enable multiple masters** or improve write scalability.

- **Single-Writer Master Node**:  
  In a master-slave setup, only the master node is allowed to perform write operations. No matter how many replicas you add, **write throughput remains limited** to what the master can handle. This is the primary blocker for horizontal write scalability.

- **Buffer Pool Size**:  
  The buffer pool is an in-memory cache used to speed up read and write operations by reducing disk I/O. A larger buffer pool can make a single node faster, but it doesn't allow **more nodes** to handle writes.

- **Query Plan Cache**:  
  When the database processes a query, it creates a plan to retrieve the data efficiently. Storing and reusing this plan (the query plan cache) speeds up repeated queries. However, this optimization mainly benefits **reads** and has no impact on how many machines can handle **writes**.

---

### Q31. Which factor most limits horizontal scalability of a master-slave relational database for write traffic?

- **A. Binlog format**
- **B. Single-writer master node**
- **C. Buffer pool size**
- **D. Query plan cache**

** Correct Answer: B. Single-writer master node**

---

### Option-by-Option Explanation:

- **A. Binlog format**  
  While it affects replication mechanics, it doesn’t solve the problem that only one node handles writes. Doesn’t help scale write throughput horizontally.

- **B. Single-writer master node**  
  This is the key limitation. All writes funnel through the master, so adding more replicas doesn’t help handle more write requests.

- **C. Buffer pool size**  
  Helps a single node perform better, but doesn’t let you distribute write traffic to more machines.

- **D. Query plan cache**  
  A performance optimization for repeated queries, mostly read-related. It doesn’t address the scaling of writes.

### Concept: Horizontal Scalability in Master-Slave Relational Databases

Horizontal scalability refers to increasing a system's capacity by adding more machines. In the context of relational databases, **master-slave (or primary-replica)** setups are common.

- The **master** handles both **reads and writes**.
- The **slaves (replicas)** only handle **reads**, and they receive updates from the master through replication.

This design improves **read scalability** — as more slaves are added, more read traffic can be handled. But **write operations always go through the master**, which becomes a bottleneck. This architectural limit makes it **impossible to scale writes horizontally** unless you adopt more advanced setups like **sharding** (splitting data across databases) or **multi-master replication**.

---

### Related Concepts

- **Binlog Format**:  
  The binary log (binlog) records all changes made to the database. It’s used by slaves to replicate data from the master. Different formats (e.g., statement-based, row-based) impact replication efficiency and reliability. However, changing the binlog format does **not enable multiple masters** or improve write scalability.

- **Single-Writer Master Node**:  
  In a master-slave setup, only the master node is allowed to perform write operations. No matter how many replicas you add, **write throughput remains limited** to what the master can handle. This is the primary blocker for horizontal write scalability.

- **Buffer Pool Size**:  
  The buffer pool is an in-memory cache used to speed up read and write operations by reducing disk I/O. A larger buffer pool can make a single node faster, but it doesn't allow **more nodes** to handle writes.

- **Query Plan Cache**:  
  When the database processes a query, it creates a plan to retrieve the data efficiently. Storing and reusing this plan (the query plan cache) speeds up repeated queries. However, this optimization mainly benefits **reads** and has no impact on how many machines can handle **writes**.

---

### Q31. Which factor most limits horizontal scalability of a master-slave relational database for write traffic?

- **A. Binlog format**
- **B. Single-writer master node**
- **C. Buffer pool size**
- **D. Query plan cache**

**✅ Correct Answer: B. Single-writer master node**

---

### Option-by-Option Explanation:

- **A. Binlog format**  
  While it affects replication mechanics, it doesn’t solve the problem that only one node handles writes. Doesn’t help scale write throughput horizontally.

- **B. Single-writer master node**  
  ✅ This is the key limitation. All writes funnel through the master, so adding more replicas doesn’t help handle more write requests.

- **C. Buffer pool size**  
  Helps a single node perform better, but doesn’t let you distribute write traffic to more machines.

- **D. Query plan cache**  
  A performance optimization for repeated queries, mostly read-related. It doesn’t address the scaling of writes.

### Concept: Consistent Hashing in Distributed Systems

**Consistent hashing** is a technique used to distribute keys across a dynamic set of nodes (like servers or caches) in a way that minimizes data movement when nodes are added or removed.

Instead of assigning keys directly to nodes, both **keys and nodes** are mapped onto a **circular hash space (hash ring)**. A key is stored on the **first node clockwise from its hash** on the ring. This approach ensures:

- Adding or removing a node affects only a **subset** of keys.
- The rest of the keys stay on the same nodes, reducing rebalancing overhead.

This property is especially useful in systems like **distributed caches (e.g., Memcached)** or **Dynamo-style databases**, where nodes frequently scale up or down.

---

### Related Concepts

- **Hash Slot / Range**:  
  In consistent hashing, each node "owns" a section of the hash ring — a range of hash values. When a new node is added, it **splits** the range of an existing node. Only keys that fall into the **newly acquired portion** are remapped.

- **Key Migration**:  
  When nodes are added, only a **fraction of keys** — those that now hash to the new node’s position — need to move. This is in contrast to regular modulo-based hashing, where **all keys might get reassigned** when the number of nodes changes.

- **Hot Keys**:  
  These are keys that are accessed more frequently than others. They might or might not migrate depending on where their hash lands. Consistent hashing does **not migrate based on access frequency**, only on hash position.

---

### Q32. A consistent-hash ring adds servers. Which keys migrate?

- **A. None**
- **B. All keys**
- **C. Only keys whose hash slot falls between old and new node positions**
- **D. Only hot keys**

** Correct Answer: C. Only keys whose hash slot falls between old and new node positions**

---

### Option-by-Option Explanation:

- **A. None**  
 Incorrect. When a new node joins the ring, **some keys must migrate** to maintain the balance and hash-ring correctness.

- **B. All keys**  
 Incorrect. That would happen in modulo-based hashing (e.g., `hash(key) % N`), not consistent hashing. One of the core benefits of consistent hashing is **minimizing** key movement.

- **C. Only keys whose hash slot falls between old and new node positions**  
 Correct. When a node is added, it takes over a portion of the hash ring. **Only the keys in that portion** move to the new node.

- **D. Only hot keys**  
 Incorrect. Key movement in consistent hashing is based on **hash value**, not access frequency. So hot or cold, a key only moves if its hash range is affected.
### Concept: Horizontal Auto-Scaling in Kubernetes

Horizontal auto-scaling allows Kubernetes to automatically adjust the number of pod replicas based on observed metrics such as CPU usage, memory, or even custom metrics like queue length. This helps ensure application availability and responsiveness under variable workloads without manual intervention.

### Related Concepts

#### ReplicaSet
A ReplicaSet maintains a stable set of pod replicas running at a given time. While it ensures availability, it does not perform scaling based on metrics.

#### StatefulSet
Used for stateful applications like databases, StatefulSets maintain pod identity across restarts and scale events. However, auto-scaling stateful sets is more complex and not always appropriate.

#### Horizontal Pod Autoscaler (HPA)
HPA is a built-in Kubernetes controller that automatically scales the number of pods in a deployment, replica set, or stateful set based on resource utilization or external metrics like queue length or request rates.

#### CronJob
CronJobs run tasks on a schedule. They are unrelated to real-time load-based scaling and are better suited for batch jobs or periodic cleanup tasks.

---

### Question

Which Kubernetes primitive best implements horizontal auto-scaling based on custom metrics like queue length?

**A. ReplicaSet**  
**B. StatefulSet**  
**C. Horizontal Pod Autoscaler**  
**D. CronJob**

**Correct Answer:** C. Horizontal Pod Autoscaler

---

### Option-by-Option Explanation

- **A. ReplicaSet**  
  Ensures a fixed number of pods are running but lacks auto-scaling capabilities based on metrics.

- **B. StatefulSet**  
  Suitable for persistent workloads that require identity and storage, but not ideal for horizontal auto-scaling, especially based on custom metrics.

- **C. Horizontal Pod Autoscaler**  
  Correct. HPA adjusts the number of pods based on CPU, memory, or custom metrics such as queue length.

- **D. CronJob**  
  Runs scheduled jobs and does not handle real-time traffic or scale workloads based on demand.

### Concept: CQRS (Command Query Responsibility Segregation)

CQRS is an architectural pattern that separates the responsibilities of handling **commands** (writes) and **queries** (reads). Instead of using a single model to update and read data, CQRS defines distinct models for update (write) operations and read operations.

This separation enables systems to scale independently for reads and writes, apply different consistency models, and even store data in entirely different formats optimized for their access patterns.

### Related Concepts

#### Eventual Consistency
In CQRS, the write and read models are often synchronized asynchronously. This means that after a write operation, the change may not be immediately visible in the read model — a trade-off known as *eventual consistency*. It's commonly used to improve scalability and performance.

#### ACID Transactions
ACID (Atomicity, Consistency, Isolation, Durability) properties are often enforced on the write side of CQRS. The read side, however, typically does not require strong consistency and thus avoids blocking reads.

#### ORM (Object-Relational Mapping)
ORM tools are typically used for mapping objects in application code to relational database tables. In CQRS, the read side can avoid complex ORM configurations by using denormalized or pre-joined tables for fast access.

#### Foreign-Key Constraints
These are used to maintain referential integrity in relational databases. The read model in CQRS often avoids foreign keys to improve performance and simplify schema changes.

---

### Question

In a CQRS architecture, the read model often uses eventual consistency primarily to:

**A. Support ACID transactions**  
**B. Simplify ORM mappings**  
**C. Optimize queries without blocking writes**  
**D. Enforce foreign-key constraints**

**Correct Answer:** C. Optimize queries without blocking writes

---

### Option-by-Option Explanation

- **A. Support ACID transactions**  
  Incorrect. ACID transactions are typically a concern for the command/write model, not the read model.

- **B. Simplify ORM mappings**  
  While denormalized read models may reduce ORM complexity, this is not the primary reason for using eventual consistency.

- **C. Optimize queries without blocking writes**  
  Correct. Eventual consistency allows the system to asynchronously update the read model, avoiding contention with write operations and enabling faster, read-optimized queries.

- **D. Enforce foreign-key constraints**  
  Incorrect. The read model often avoids foreign keys to improve query performance and schema flexibility.

### Concept: Write-Through Cache in Redis

A **write-through cache** ensures that every write operation is first made to the cache and then immediately written to the underlying database. This ensures the cache and the backing store remain in sync, which is good for consistency but can impact performance.

Write-through is commonly used when read-after-write consistency is critical, and when data loss due to cache eviction is unacceptable. However, it comes with trade-offs in terms of latency and throughput.

---

### Related Concepts

#### Cache Miss
Occurs when the requested data is not found in the cache and must be fetched from the backend store. In write-through caching, a cache miss during a read operation doesn’t affect write latency directly.

#### Write Hit
Occurs when a write operation targets a key that already exists in the cache. In write-through, this means the write will update both the cache and the backend, adding additional overhead.

#### Read After Write
This refers to accessing data immediately after it has been written. Write-through helps maintain strong consistency for such reads because the latest value is already in the cache.

#### Pipeline Batching
Redis supports pipelining multiple commands in a single network request to improve throughput. While this reduces round-trip time, it doesn't change the write-through strategy's requirement to write to both cache and DB.

---

### Question

Under which circumstance will Redis’ write-through cache increase backend write latency?

**A. Cache miss**  
**B. Write hit**  
**C. Read after write**  
**D. Pipeline batching**

**Correct Answer:** B. Write hit

---

### Option-by-Option Explanation

- **A. Cache miss**  
  Incorrect. A cache miss during read might lead to a DB fetch, but it doesn’t affect write latency in a write-through model.

- **B. Write hit**  
  Correct. When a write occurs, both the cache and backend DB must be updated. This effectively adds an extra network and write operation, increasing the latency for each write.

- **C. Read after write**  
  Incorrect. This scenario benefits from write-through caching because the latest data is already present in the cache.

- **D. Pipeline batching**  
  Incorrect. While pipelining can help reduce latency for bulk operations, it is orthogonal to the write-through behavior.

### Concept: LRU (Least Recently Used) Cache and Access Patterns

An **LRU cache** evicts the least recently accessed items first when the cache reaches its capacity. This works well when the workload has **temporal locality**—items accessed recently are likely to be accessed again soon.

However, LRU can **break down under specific access patterns**, especially **cyclic or sequential scans** over large datasets that exceed the cache size.

---

### Related Concepts

#### Cyclically Scanning Patterns
This refers to workloads where data is accessed in cycles. For example, if a program scans through 10,000 items in a loop and the cache holds only 1,000, then by the time it loops back to an earlier item, that item is already evicted.

#### Cache Thrashing / Churn
This happens when the cache continuously evicts useful entries due to non-repeating or large working sets. This leads to high cache miss rates and degraded performance.

#### TTL (Time-to-Live)
A time-based expiration policy unrelated to usage. TTL issues don’t directly relate to usage patterns and are independent of LRU eviction.

#### Key Hashing Collisions
Common in hash-based data structures, but not relevant to LRU’s eviction logic.

#### Serialization Overhead
Serialization is the cost of converting data to a storable/transmittable format. While it can impact performance, it’s unrelated to LRU’s eviction behavior under cyclic patterns.

---

### Question

Why does an LRU cache perform poorly when workload exhibits cyclically scanning patterns?

**A. Thrashing evicts recently used but soon-needed items**  
**B. TTL resets too often**  
**C. Key hashing collisions**  
**D. Serialization overhead**

**Correct Answer:** A. Thrashing evicts recently used but soon-needed items

---

### Option-by-Option Explanation

- **A. Thrashing evicts recently used but soon-needed items**  
  Correct. Cyclical scans evict items before they’re reused, leading to repeated misses and reloads—a classic cache churn issue.

- **B. TTL resets too often**  
   Incorrect. TTL expiration is time-based, not usage-based, and unrelated to LRU eviction behavior.

- **C. Key hashing collisions**  
   Incorrect. This is a hashing issue, not an eviction or cache hit/miss behavior issue.

- **D. Serialization overhead**  
   Incorrect. While serialization can add latency, it’s not the root cause of poor LRU performance in scanning patterns.
## Exactly-Once Delivery Semantics in Message Brokers

In messaging systems, **exactly-once delivery** means a message is **processed only once** by the receiving application, even if it's sent or received multiple times due to retries, network issues, or crashes.

However, most messaging systems like **Kafka, RabbitMQ, or Pulsar** can **guarantee at-most-once** (messages might be lost) or **at-least-once** (messages might be duplicated). **Exactly-once** is more difficult to achieve and typically requires **cooperation between producer, broker, and consumer** — with **consumer-side deduplication** playing a crucial role.

### Key Related Concepts

- **Producer**: Sends messages. It may use sequence numbers or transactional IDs to support idempotent publishing, but it does not control final delivery semantics on its own.

- **Consumer Application**: The component responsible for **processing messages and ensuring idempotency** — for example, using a **deduplication store**, message IDs, or transactional database writes that ignore duplicates.

- **Broker Offset Manager**: Manages message delivery offsets and helps resume from the right place, but cannot detect whether your app already acted on a message.

- **Network NAT**: Network Address Translation (NAT) has no role in delivery guarantees or deduplication.

### Sample Question

**Q37. A message broker promises exactly-once delivery. Which component usually handles deduplication to fulfill this semantic?**

- **A. Producer**
- **B. Consumer application**
- **C. Broker offset manager**
- **D. Network NAT**

**Correct Answer**: **B. Consumer application**

### Option-wise Explanation

- **A. Producer**  
  Producers may be idempotent to avoid duplicate sends, but they cannot enforce that a message is processed exactly once by the consumer.

- **B. Consumer application** 
  The consumer is where deduplication logic (e.g., checking message IDs in a store) must exist to ensure a message is not reprocessed. This is essential to implement exactly-once semantics.

- **C. Broker offset manager**  
  The broker can track message position but doesn't know whether the consumer has already acted on a message — so it can't prevent duplicate processing.

- **D. Network NAT**  
  Irrelevant to message delivery semantics; this deals with routing packets, not ensuring reliable or unique message delivery.
## Concept: Time-Series Data Partitioning

In time-series databases or workloads — where data arrives in a sequence over time (e.g., logs, metrics, IoT sensor data) — one of the biggest challenges is **write efficiency**. Since data typically flows in order of time, how you partition the data heavily influences performance, query patterns, and scalability.

A good partitioning strategy must aim to:
- Keep **inserts localized** to minimize cross-shard coordination.
- Avoid **hotspots**, where one partition receives disproportionate load.
- Ensure **queries** (often range-based) can target specific partitions.

## Related Concepts

### A. **Hash Partitioning on Sensor ID**
- Hashing distributes writes evenly across shards based on a key (e.g., sensor ID).
- Pros: Load-balanced across shards.
- Cons: Time-based queries span many shards; inefficient for time-range queries.
- Causes **fan-out** for time-based aggregation.

### B. **Round-Robin Partitioning**
- Writes are distributed cyclically across partitions.
- Pros: Simple and balances load.
- Cons: Randomizes writes, disrupting time-based locality; poor for range queries or compression.

### C. **Range Partitioning by Timestamp**
- Each shard is responsible for a specific time range (e.g., a day, a week).
- Pros: Localizes writes for current time to a single shard.
- Pros: Efficient for time-range queries; fewer disk seeks, better compression.
- Ideal for **append-heavy time-series workloads**.

### D. **Vertical Partitioning (Split by Column)**
- Splits a table's columns into different tables (e.g., separating metadata and values).
- Pros: Useful when some columns are rarely used.
- Cons: Not helpful for minimizing write fan-out; increases query join complexity.

---

## Question

**Q38. Which data partitioning strategy minimizes multi-shard fan-outs for time-series writes?**

### Options:
**A. Hash on sensor ID**  
**B. Round-robin**  
**C. Range by timestamp**  
**D. Vertical split by column**

---

### Correct Answer: **C. Range by timestamp**

---

## Option-by-Option Breakdown

- **A. Hash on sensor ID**:  
  Distributes writes, but scatters time-based writes across shards.  
  Leads to multi-shard fan-out for time-range queries.

- **B. Round-robin**:  
  Similar to hash — randomized writes across shards. Poor write locality.  
  Not suitable for ordered time-based inserts.

- **C. Range by timestamp**:  
  Localizes writes to the current time shard. Reduces fan-out and improves performance.  
  Best suited for high-throughput time-series writes.

- **D. Vertical split by column**:  
  Doesn't address partitioning by data volume or time. Mostly a schema optimization.  
  Irrelevant for shard-localized time-series writes.
## Concept: Multi-Shard Fan-Outs

In distributed databases or sharded systems, data is split across multiple **shards**—essentially independent database partitions. Each shard holds a subset of the total data.

When a query or a write operation needs to **touch more than one shard**, it's called a **fan-out**. This happens when the system can't determine exactly which shard holds the data, or when the data spans multiple shards.

Now, when this fan-out happens during **writes**, it's referred to as a **multi-shard fan-out write**. Similarly, **multi-shard fan-out reads** involve fetching data from multiple shards to serve a single query.

### Why are multi-shard fan-outs bad?

They introduce several issues:

- **Increased Latency**: You now wait on the slowest shard to respond.
- **Higher Network Overhead**: More connections, more coordination.
- **Reduced Scalability**: A fan-out query increases system load linearly with the number of shards.
- **Complexity in Consistency**: Writes across shards are harder to keep atomic and consistent.

---

## Related Concepts

### **Sharding**
A technique to scale databases by splitting data horizontally across independent partitions called shards. Each shard is responsible for a subset of the data.

### **Write Locality**
Refers to the ability to direct a write to a **single shard**, without involving others. High write locality improves performance and simplifies consistency.

### **Time-Series Writes**
Time-series data (e.g., sensor logs, metrics, trading data) is typically written in a time-ordered fashion. Partitioning by time can help maintain write locality.

---

## Summary

**Multi-shard fan-outs** happen when a single operation—usually a query or write—has to interact with multiple shards at once. This reduces performance, increases latency, and adds complexity. Good partitioning strategies aim to **minimize fan-outs**, especially in time-sensitive systems like logging pipelines or real-time metrics platforms.

## Concept: Dead-Letter Queues (DLQ)

A **Dead-Letter Queue** (DLQ) is a secondary queue used to store messages that cannot be processed successfully. In messaging systems like RabbitMQ, messages are sent to a DLQ when they are rejected by consumers, expire, or exceed a certain number of delivery attempts.

This mechanism helps prevent problematic messages from **clogging up** the main queue and allows system operators to analyze and troubleshoot failures without losing those messages.

### Why It's Important in High-Fan-Out Systems

A **high-fan-out** setup—such as a **RabbitMQ topic exchange** that fans out one message to many bound queues—can amplify problems:
- A single bad message could block processing across many queues.
- If consumers reject the message, or it's unroutable, queues may get backed up.
- Without a DLQ, these messages either get dropped silently or cause back-pressure.

Dead-letter queues **isolate** these problematic messages and help maintain the health of the overall system.

---

## Related Concepts

### **Topic Exchange (RabbitMQ)**
A topic exchange routes messages to one or more queues based on pattern-matching against the routing key. It supports wildcard subscriptions and high flexibility, but that also means it may fan out messages to many consumers—multiplying the chances of failure.

### **Back-Pressure**
When queues become full or consumers slow down, message producers may be forced to pause or fail—this is called back-pressure. DLQs help avoid it by removing unprocessable messages from the flow.

### **Message Expiry / TTL**
Messages can have a time-to-live (TTL), after which they are considered expired. Expired messages can also be routed to the DLQ to avoid data loss or silent drops.

---

## Question

**Q40. In a high-fan-out RabbitMQ topic exchange, what risk does an improperly configured dead-letter queue mitigate?**

**A.** Duplicate routing keys  
**B.** Undeliverable messages blocking queues  
**C.** Consumer drift  
**D.** TLS renegotiation

**Correct Answer:** B

---

## Option-by-Option Explanation

- **A. Duplicate routing keys**  
  Not mitigated by DLQ. RabbitMQ handles routing duplication using exchange and binding rules.

- **B. Undeliverable messages blocking queues**  
  Correct. DLQs capture expired or rejected messages so they don't cause back-pressure or queue saturation.

- **C. Consumer drift**  
  Drift refers to consumers getting out of sync. DLQs don't solve this directly.

- **D. TLS renegotiation**  
  This is a transport-level concern. DLQs operate at the messaging level and don’t address TLS.

---

## Concept: Microservices and Bounded Contexts

In domain-driven design (DDD), **bounded contexts** represent logical boundaries where a particular domain model applies. Each bounded context is ideally owned by a team and can evolve independently. Microservices map well to bounded contexts when each service owns its data, logic, and interfaces.

A deployment topography is the structural pattern in which microservices are organized and deployed. The right topography reduces dependencies, improves scalability, and optimizes for locality of access.

## Related Concepts

- **Shared Database**: Multiple services access the same database. While simple, it breaks service boundaries and tightly couples services, leading to coordination and schema-change pain.

- **Service Mesh Sidecar**: Adds networking features like retries, metrics, and circuit breakers but doesn't change how services own or access data.

- **Self-Contained Systems (SCS)**: Each service contains its own UI, API, and database. These are fully autonomous units that map directly to a bounded context and are ideal for DDD. Data locality is natural because each SCS holds its own storage.

- **Monorepo Packaging**: Refers to source code organization, not deployment. Even if code is in one repo, services may still be tightly or loosely coupled depending on runtime architecture.

## Question

**Q41. Which microservices deployment topography eases data locality for domain-driven bounded contexts?**

- **A. Shared database**
- **B. Service mesh sidecar**
- **C. Self-contained systems (SCS) per context**
- **D. Monorepo packaging**

**Correct Answer:** C

## Explanation

**Self-contained systems (SCS)** are explicitly designed to encapsulate everything a bounded context needs — including UI, API logic, and persistence. This leads to **data locality**, meaning the service accesses its own data directly, reducing network latency, cross-service calls, and data ownership ambiguity.

### Option-wise Breakdown

- **A. Shared database**: Violates bounded context isolation and creates tight coupling. Not ideal for data locality or autonomy.
- **B. Service mesh sidecar**: Provides networking abstraction but doesn't influence how data is stored or scoped.
- **C. Self-contained systems (SCS) per context)**: Best choice — encapsulates data and logic per context, ensuring locality and independence.
- **D. Monorepo packaging**: Affects code management, not runtime architecture or data access patterns.
## Concept: Caching with Regeneration Control

When a cache expires or is empty, and multiple requests arrive simultaneously, they might all attempt to regenerate the value from the backing store. This can overwhelm backend systems — a situation known as the "thundering herd" problem.

To avoid this, a pattern is used where only the first request recomputes and repopulates the cache, while other requests either wait or serve the previous (possibly stale) value. This is commonly implemented using **cache-aside** in combination with a **double-checked locking mechanism**.

---

## Related Concepts

**Cache-aside**  
Also called lazy loading. The application first checks the cache. On a miss, it fetches from the source (like a DB), updates the cache, and then returns the value.

**Double-checked locking**  
A concurrency control method where the cache is re-checked after acquiring a lock to avoid redundant recomputations when multiple threads try to repopulate the same key.

**Write-back caching**  
Writes are performed to the cache first and written to the persistent store later. It is useful for write performance but risky for data consistency if the cache crashes.

**Cache-through**  
All reads and writes go through the cache, which automatically handles loading from and writing to the backing store. It does not inherently support stale reads.

**Reload barrier**  
A loosely defined term that might refer to blocking refreshes under high load or coordinating multiple cache updates. It's not a common or well-established cache strategy.

---

## Question

**Q42. What cache pattern allows stale reads during regeneration to shield users from expensive recomputation?**

A. Cache-aside + double-checked lock  
B. Write-back  
C. Cache-through  
D. Reload barrier

**Correct Answer:** A

---

## Explanation of Options

**A. Cache-aside + double-checked lock**  
This pattern enables serving stale values temporarily while one thread refreshes the cache. It helps avoid redundant recomputation and protects backend systems under load.

**B. Write-back**  
This pattern deals with deferring writes to the backing store. It doesn't help with stale reads or preventing recomputation storms.

**C. Cache-through**  
While cache-through simplifies read/write paths, it does not typically support serving stale values during recomputation, nor does it mitigate the thundering herd.

**D. Reload barrier**  
Not a well-defined or widely accepted pattern for caching. Does not specifically address stale read behavior or regeneration control.

## Concept: Kafka Producer Acknowledgment Levels

In Apache Kafka, a producer can control message durability using the `acks` configuration. This setting determines how many broker acknowledgments the producer requires before considering a message as successfully sent.

There are three common settings:
- `acks=0`: The producer doesn’t wait for any acknowledgment from the broker. This gives maximum throughput but no durability.
- `acks=1`: The producer gets an acknowledgment after the leader replica writes the message. If the leader crashes before replicating to others, the message may be lost.
- `acks=all` (or `acks=-1`): The producer waits for the message to be replicated to all in-sync replicas (ISRs). This ensures the highest durability, as data survives even if the leader crashes immediately after writing.

---

## Related Concepts

**Leader and ISR (In-Sync Replicas)**  
Kafka partitions are replicated across multiple brokers. One broker acts as the leader, and others are followers. The ISR is the set of replicas that are fully caught up with the leader.

**Durability Guarantees**  
To prevent message loss, especially in cases of node failure or crashes, producers must wait until their messages are written not just to one broker but to a quorum of replicas.

**FIFO (First-In First-Out)**  
Kafka does not strictly enforce global FIFO across all consumers. Ordering is guaranteed per partition, and does not relate directly to `acks`.

---

## Question

**Q43. For zero-message-loss semantics, a Kafka producer must configure acks to:**

A. 0  
B. 1  
C. All (−1)  
D. FIFO

**Correct Answer:** C

---

## Explanation of Options

**A. 0**  
No acknowledgment is expected. Fastest but messages can be lost if the broker crashes or if there’s a network failure before writing to disk.

**B. 1**  
The leader acknowledges the message once it’s written locally. Still risky — if the leader crashes before replicating, data may be lost.

**C. All (−1)**  
Correct. This ensures the message is acknowledged only after all in-sync replicas have stored it. Best for zero-message-loss guarantees.

**D. FIFO**  
Irrelevant to durability. FIFO (ordering) is managed at the partition level and doesn’t affect how many replicas acknowledge a message.

## Concept: Quorum-Based Fencing in Distributed Systems

In distributed systems, **split-brain** occurs when two nodes or clusters mistakenly believe they are both the primary or leader after a network partition. This can lead to data corruption or conflicting writes.

To avoid this, quorum-based systems issue **fencing tokens** — unique, increasing identifiers granted only to the node that obtains a majority (quorum) of votes. Any write operation must include a valid token; if two nodes attempt to act as primary, the one with the older token is ignored.

---

## Related Concepts

**Consensus Majority Election**  
This mechanism (used in Raft, Paxos, etc.) allows distributed nodes to elect a single leader using a majority quorum. Only the leader is granted authority to proceed with operations, ensuring that at most one node can write to shared storage.

**Active-Passive**  
One node is active while others are in standby. Without fencing, a partition may cause two nodes to think they’re active — leading to split-brain.

**Dual-Writes**  
This setup allows two nodes or services to write simultaneously, often without coordination. It’s prone to conflicts and inconsistency and doesn’t involve quorum or fencing.

**Shared-Nothing Clustering**  
Each node independently owns and manages its own data. While it avoids shared resources, it still requires coordination to ensure availability and leadership consistency.

---

## Question

**Q44. Which high-availability pattern uses quorum-based fencing tokens to avoid split-brain in distributed storage?**

A. Active-passive  
B. Dual-writes  
C. Shared-nothing clustering  
D. Consensus majority election

**Correct Answer:** D

---

## Explanation of Options

**A. Active-passive**  
While commonly used, this pattern can suffer from split-brain if not combined with fencing or quorum-based control.

**B. Dual-writes**  
Not a high-availability strategy. It increases risk of inconsistency and does not use fencing.

**C. Shared-nothing clustering**  
Describes architecture layout. Doesn't inherently prevent split-brain without a consensus mechanism.

**D. Consensus majority election**  
Correct. This uses quorum voting and fencing tokens to ensure only one leader can write, preventing split-brain scenarios.

## Concept: CDN Edge Caching and Time-To-Live (TTL)

A **Content Delivery Network (CDN)** serves cached content from edge servers located geographically closer to users, reducing latency and load on origin servers. Each cached resource has a **Time-To-Live (TTL)** — the duration it is considered fresh and can be served without revalidation from the origin.

The TTL is typically chosen based on how often the content changes:

- **HTML pages** (especially dynamic content) may reflect user-specific data, latest posts, or UI changes — requiring more frequent updates.
- **Static images** (like logos or product photos) rarely change, so they can be cached longer.

Setting a shorter TTL for frequently changing content ensures users get the latest version without long cache delays, while longer TTLs for static assets reduce origin bandwidth and improve speed.

---

## Related Concepts

**Dynamic vs Static Content**  
Dynamic content is generated at request time and can vary per user or context (e.g., HTML dashboards). Static content remains the same across users (e.g., images, CSS, JavaScript).

**Cache Invalidation and Freshness**  
CDNs use TTL to determine when cached items expire. Content changes before TTL expiry may require manual invalidation (e.g., cache purging).

**Cache Hit Ratio**  
A measure of how often content is served from cache rather than origin. Longer TTL improves this ratio but may risk staleness.

**Stale-While-Revalidate**  
Some caching systems serve stale content briefly while revalidating in the background — offering a balance between performance and freshness.

---

## Question

**Q45. Why does a CDN edge cache typically set a shorter TTL for HTML than for static images?**

A. Image decoding overhead  
B. HTML changes more frequently  
C. CDN limits file types  
D. HTTP/2 push constraints

**Correct Answer:** B

---

## Explanation of Options

**A. Image decoding overhead**  
Not relevant to caching TTL. This affects client-side rendering, not CDN caching policies.

**B. HTML changes more frequently**  
Correct. HTML pages often contain dynamic or user-specific content, requiring more frequent updates. Shorter TTL ensures freshness.

**C. CDN limits file types**  
CDNs do not typically restrict based on file types; this is not a factor in TTL choice.

**D. HTTP/2 push constraints**  
Unrelated to TTL decisions. HTTP/2 push deals with server-initiated resource delivery, not cache expiration.

## Concept: Wide Rows and Hotspotting in NoSQL Column-Family Stores

In NoSQL databases like Apache Cassandra or HBase, data is often modeled using **column-family stores**. These databases allow **wide rows**, meaning a single row key can hold millions of columns — commonly used for time-series data or logs.

However, if all data for a particular row key ends up on the same partition, this can lead to:

- **Hotspotting**: One partition node receives the majority of write traffic.
- **Partition Size Limits**: Exceeding the storage or memory limits for that partition.
- **Imbalanced Load**: Other nodes remain underutilized.

To mitigate this while maintaining a **logical sort order** (e.g., latest logs first), one common strategy is to modify the row key design.

---

## Related Concepts

**Row Key Design**  
Choosing a good row key is essential. If the key is too predictable (like `userID` or `deviceID`), all writes go to the same partition. To distribute load, keys are often **prefixed or suffixed** with something that varies over time or users.

**Reversed Timestamps**  
In time-series use cases, reversing the timestamp in the key (e.g., using `Long.MAX_VALUE - timestamp`) ensures that newer entries come first while also varying the prefix — helping spread data across partitions.

**Partitioning and Clustering**  
Partition keys determine how data is distributed across nodes. Clustering columns determine the sort order within a partition. When designing for scale, you often need to balance **distribution** with **query efficiency**.

**Memtable Flush Size**  
This is an in-memory buffer before flushing to disk. It affects write efficiency but not partition design or hotspotting.

---

## Question

**Q46. In a NoSQL column-family store, wide rows may breach partition size. Which mitigation avoids hotspotting while preserving sort order?**

A. Prefix reverse timestamp to row key  
B. Increase memtable flush size  
C. Disable bloom filters  
D. Switch to UTF-16 keys

**Correct Answer:** A

---

## Explanation of Options

**A. Prefix reverse timestamp to row key**  
 Correct. This spreads writes across partitions by altering the row key pattern while keeping the order of entries within the partition meaningful (usually newest first).

**B. Increase memtable flush size**  
Helps delay disk I/O but does nothing to prevent hotspotting or limit partition size breaches.

**C. Disable bloom filters**  
Would increase read latency by forcing more disk lookups, and doesn't address the write or partitioning issues.

**D. Switch to UTF-16 keys**  
Unrelated to hotspotting. Changing encoding doesn't affect partitioning behavior unless key logic is changed.


## Concept: Blue-Green Deployment for Stateless Microservices

**Blue-Green Deployment** is a release strategy that maintains two identical environments — *Blue* (current production) and *Green* (new version). Only one is live at a time. Once the Green environment is fully deployed and validated, traffic is switched from Blue to Green, enabling:

- Instant rollback if issues are detected
- Isolation of deployment failures
- No downtime deployments

This strategy is especially effective with **stateless microservices** since they don’t rely on local state or session data, making traffic switching seamless.

---

## Related Concepts

- **Stateless Microservices**: Services that do not retain client state between requests. They’re easier to scale and redeploy.
- **Deployment Blast Radius**: The scope of impact caused by a failed deployment. Smaller radius means fewer affected services or users.
- **Rolling Deployments**: Gradual rollout of new versions; may cause partial impact if an issue arises mid-rollout.
- **Canary Deployments**: Route a small percentage of traffic to the new version initially; useful for controlled testing.

---

## Question

**Q47. For stateless microservices, blue-green deployment primarily reduces:**

A. Binary size  
B. Deployment blast radius  
C. Network hops  
D. Heap fragmentation

---

**Correct Answer:** B. Deployment blast radius

---

### Option-wise Explanation

- **A. Binary size**  
  Not directly related to deployment strategies. Binary size is a build-time concern, not runtime safety.

- **B. Deployment blast radius**  
  Blue-green isolates new deployments in a separate environment, so if something goes wrong, only the new version is affected — minimizing impact.

- **C. Network hops**  
  Network topology isn’t influenced by deployment strategy in this case.

- **D. Heap fragmentation**  
  This is a low-level memory management issue, unrelated to deployment approaches like blue-green.

## Concept: Availability Metrics and Downtime Calculation

**Availability** in systems engineering refers to the proportion of time a system remains operational and accessible when needed. It's typically expressed as a percentage over a given period (e.g., per year).

A common way to describe service availability is in terms of “**nines**”:

| Availability (%) | Downtime per Year      | Downtime per Month     |
|------------------|------------------------|-------------------------|
| 99%              | ~3.65 days             | ~7.2 hours              |
| 99.9%            | ~8.76 hours            | ~43.8 minutes           |
| 99.95%           | ~4.38 hours            | ~21.9 minutes           |
| 99.99%           | ~52.6 minutes          | ~4.4 minutes            |
| 99.999%          | ~5.26 minutes          | ~26.3 seconds           |

These metrics help teams understand the **acceptable outage limits** when designing **high availability (HA)** systems.

---

## Related Concepts

- **Downtime**: The time during which a system is not available to users.
- **SLA (Service Level Agreement)**: A commitment between service provider and customer on guaranteed uptime.
- **High Availability (HA)**: System design strategies aimed at minimizing downtime and ensuring continuous operation.
- **Redundancy and Failover**: Common HA strategies include having backup systems to take over in case of failures.

---

## Question

**Q48. Which availability metric translates to approximately 52.6 minutes of downtime per year?**

A. 99%  
B. 99.9%  
C. 99.95%  
D. 99.99%

---

**Correct Answer:** D. 99.99%

---

### Option-wise Explanation

- **A. 99%**  
  This corresponds to roughly **3.65 days of downtime per year**, far more than 52.6 minutes.

- **B. 99.9%**  
  Represents about **8.76 hours** of downtime per year, which is still significantly more than 52.6 minutes.

- **C. 99.95%**  
  Equates to approximately **4.38 hours** annually — closer, but still too much.

- **D. 99.99%**  
  **Correct** — this level of availability allows about **52.6 minutes of downtime** in a year, matching the target precisely.
## Concept: Leaky Bucket vs Token Bucket Rate Limiting

**Rate limiting** is used to control how frequently a resource can be accessed. Two popular algorithms are:

### Leaky Bucket
- Imagines a bucket with a small hole at the bottom.
- Requests enter the bucket and "leak" out at a fixed rate.
- Enforces **a constant output rate**, smoothing out bursts.
- If the bucket overflows (too many incoming requests), excess requests are dropped.

### Token Bucket
- A bucket is filled with tokens at a fixed rate.
- Each request consumes a token.
- If there are no tokens, the request is delayed or rejected.
- **Allows bursts** up to the bucket size, but still enforces an average rate over time.

### Key Difference:
- **Leaky Bucket** smooths out traffic; doesn't allow bursting.
- **Token Bucket** allows short bursts while maintaining an average rate.

---

## Related Concepts

- **Burst Size**: Maximum number of requests that can be processed in quick succession.
- **Refill Rate**: The rate at which tokens are added (in Token Bucket) or processed (in Leaky Bucket).
- **Throughput Enforcement**: Both mechanisms ultimately restrict the long-term request rate.

---

## Question

**Q49. A leaky bucket rate limiter with capacity 10 requests/second is equivalent to a token bucket with:**

A. Unlimited burst  
B. 10 token refill/sec & burst 10  
C. 5 token refill/sec & burst 20  
D. Zero tokens

---

**Correct Answer:** B. 10 token refill/sec & burst 10

---

### Option-wise Explanation

- **A. Unlimited burst**  
  Incorrect — leaky buckets do **not** allow bursts; this contradicts their behavior.

- **B. 10 token refill/sec & burst 10**  
  **Correct** — Matches the leaky bucket’s fixed rate. The burst size of 10 ensures that the token bucket doesn’t exceed this steady throughput.

- **C. 5 token refill/sec & burst 20**  
  Incorrect — This would allow **burstier behavior** and a different average throughput, mismatching the leaky bucket's behavior.

- **D. Zero tokens**  
  Invalid configuration — would **block all requests**, not comparable to leaky bucket's 10 req/sec rate.
## Concept: Cold Starts in Serverless

In serverless computing, functions are typically executed in ephemeral containers. When a function is invoked after a period of inactivity, the system needs to spin up a new container, initialize runtime, and load the function code. This delay is called a **cold start**.

Cold starts can add hundreds of milliseconds to several seconds of latency, which may be unacceptable for latency-sensitive applications — especially those with sporadic or unpredictable traffic.

## Related Concepts

- **Provisioned Concurrency**: A feature (e.g., in AWS Lambda) that keeps a specified number of function instances initialized and ready to respond immediately. It avoids cold starts altogether.
- **Event Sourcing**: Captures all changes to application state as a sequence of events. This is a pattern used for reconstructing state and is not directly related to cold starts.
- **Edge Workers Caching**: Edge functions or workers can cache responses at CDN nodes, improving performance but not solving cold start latency for compute workloads.
- **MapReduce**: A batch-processing model, not suitable for real-time serverless request/response patterns.

---

## Question

**Q50. Which lambda-style serverless pattern avoids cold-start impact for sporadic traffic?**

**A. Provisioned concurrency**  
**B. Event sourcing**  
**C. Edge workers caching**  
**D. MapReduce**

**Correct Answer:** A

---

## Option-by-Option Explanation

- **A. Provisioned concurrency** 
  Keeps a pool of initialized function instances ready to serve requests instantly, removing the cold start penalty.

- **B. Event sourcing**  
  Focuses on data/state capture and replay, unrelated to function start latency.

- **C. Edge workers caching**  
  Reduces content retrieval time but does not solve compute cold-start problems.

- **D. MapReduce**  
  Meant for distributed batch processing, not reactive, real-time traffic.

## Concept: Bursts in Rate Limiting

In the context of rate limiting — especially with algorithms like **token bucket** and **leaky bucket** — a **burst** refers to a short period of time when traffic exceeds the nominal rate limit, but is still allowed without being dropped or throttled.

This happens because the system maintains a **buffer** (e.g., tokens in a bucket) that accumulates over time. If the buffer is full, a sudden spike in traffic can "burst through" as long as it doesn't exceed the burst size.

---

## Analogy

Imagine you're allowed to send 10 emails per minute. If you don’t send any emails for 3 minutes, a token bucket limiter may let you send 30 emails at once — because you've "saved up" unused tokens. That sudden flood of 30 emails is a **burst**.

---

## Related Concepts

- **Token Bucket Algorithm**: Allows bursts. Tokens accumulate at a fixed rate, and requests consume tokens. If enough tokens are available, bursts are permitted up to the bucket size.
- **Leaky Bucket Algorithm**: Does **not** allow bursts. Requests are processed at a fixed rate, and excess requests are dropped or delayed to match the constant flow rate.

---

## Why Bursts Matter

Bursts can be useful when:

- Traffic is unpredictable or bursty by nature (e.g., mobile app check-ins).
- You want to ensure smooth user experience during short spikes.

However, allowing too large a burst may overwhelm backend systems, so it's a trade-off between responsiveness and system stability.