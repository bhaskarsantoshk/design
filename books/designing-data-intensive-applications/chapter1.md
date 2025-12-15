# Chapter 1 - Reliable, Scalable, and Maintainable Applications  
Date: December 1, 2025

---

## Important Points

- Applications today are **data-intensive** rather than compute-intensive. CPU is rarely the bottleneck. Challenges usually come from **data volume, complexity, and speed of change**.

### Data-Intensive Application Building Blocks
- Databases
- Caches
- Search indexes (search data via keywords or filters)
- Stream processing (asynchronous messaging)
- Batch processing

Different tools exist for different tasks. Hard to combine when one tool alone isn’t enough.

### Data Systems
Databases, queues, caches store data temporarily but have **very different access patterns** → different performance trade-offs and implementations.

Optimized for different use cases → categories have blurred:
- Redis → datastore + message queue
- Kafka → message queue + durability like a database

If caching/search is external to DB (Memcached, Elasticsearch, Solr) → **application must keep them in sync**.

```
                         ┌───────────────┐
                         │    Client     │
                         └───────┬───────┘
                                 │
                                 ▼
                        ┌──────────────────┐
                        │ Application Code │
                        └───────┬──────────┘
                        │       │
                        │       │ Search Queries
                        ▼       ▼
                ┌───────────┐  ┌───────────────┐
                │ In-Memory │  │ Search Index   │
                │  Cache    │  └───────────────┘
                └──────┬────┘
                       │ Cache Miss
                       ▼
                ┌───────────────┐
                │ Main Database │
                └──────┬────────┘
                       │ Writes
                       ▼
          ┌───────────────────────────────┐
          │ Parallel Fan-Out on Write     │
          └─────────┬───────────┬────────┘
                    │           │
                    ▼           ▼
           ┌───────────────┐   ┌──────────────────┐
           │ Update Cache  │   │  Message Queue    │
           └───────────────┘   └─────────┬────────┘
                                         │
                                         │ Async Update Tasks
                                         ▼
                                 ┌───────────────┐
                                 │ Search Index  │
                                 └───────────────┘
```

API hides implementation from the client.

---

## Key Questions in Data System Design
- How to ensure correctness and completeness under internal failures?
- How to maintain consistent performance under component degradation?
- How to scale with load?
- What should the service API look like?

### Main Concerns
1. **Reliability** — correct function despite faults
2. **Scalability** — handle growth in traffic, data, complexity
3. **Maintainability** — many people must safely evolve the system

---
Date: December 2, 2025

## Reliability

Definition: System continues functioning correctly **even when things go wrong**.

- Performs expected function for the user
- Tolerates user mistakes
- Performance holds under expected load and data volume
- Prevents unauthorized access and abuse

### Fault vs Failure
- **Fault** = component deviates from spec
- **Failure** = system stops functioning correctly for users  
Faults are inevitable → design for fault tolerance (resilience)

Testing needs to include deliberate fault injection → *Chaos Monkey*

### Hardware Faults
Examples:
- Disk crashes
- RAM corruption
- Power outage
- Accidental cable removal
- Shark bites underwater cables

Hard disk **MTTF ~10–50 years** → with **10,000 disks → 1 failure/day** expected

Mitigation:
- RAID
- Dual power supplies, hot‑swap hardware
- Datacenter redundancy (UPS + generators)
- **Rolling upgrades** avoid downtime by patching node-by-node

Hardware faults weakly correlated except shared failures (racks overheating)

### Software Errors
Harder to anticipate. Usually correlated → simultaneous multi-node impact.

Examples:
- Crash for specific rare input (e.g., **2012 leap second Linux kernel bug**)
- Runaway process exhausting shared resources
- Dependency slowdown/unresponsive/corrupt responses
- Cascading failures

Mitigation:
- Process isolation
- Restart strategies
- Thorough testing
- Operational analytics
- Monitoring to ensure invariants (e.g., incoming = outgoing in queue)

### Human Errors (75–90% outages)
Mitigation:
- Good abstractions, safe APIs, admin tooling
- Isolate high‑impact operations
- Thorough testing
- Fast rollback options
- Telemetry: metrics + error reporting
- Strong operational practices + training

Reliability required even for non‑critical apps. Only skip if prototype in uncertain market. Do so consciously.

---
Date: December 3, 2025

## Scalability

System must cope with **increased load**.

Questions:
- If load grows in X way, what options do we have?
- How to add resources to maintain performance?

### Describe Load
Use **load parameters**, examples:
- Requests per second
- Read/write ratio
- Active users
- Cache hit rate
- Fan-out factor

#### Example: X (formerly Twitter)

| Operation | Avg Rate | Peak Rate |
|----------|----------|-----------|
| Post Tweet | 4.6k/s | 12k/s |
| Home Timeline | ~300k/s | — |

Tweets small issue → **fan‑out is main scaling challenge**.

##### Approach 1: Compute on Read
Home timeline assembled at read time using joins.

```sql
SELECT tweets.*, users.* FROM tweets
JOIN users ON tweets.sender_id = users.id
JOIN follows ON follows.followee_id = users.id
WHERE follows.follower_id = current_user;
```

Cheap writes, expensive reads.

##### Approach 2: Fan‑Out on Write
Push new tweet into each follower’s cache.

4.6k tweets/s × **75 followers avg → 345k writes/s**  
Celebrities: **30M followers → 30M writes** per tweet  
Delivery requirement **< 5 seconds**

##### Hybrid Approach
Fan‑out for most users, compute‑on‑read for high‑fanout users

---
Date: December 4, 2025

## Describe Performance

How does system behave when load increases with fixed resources?  
How much must resources grow to hold performance constant?

### Batch vs Online
- Batch → throughput matters (records/sec, job completion time)
- Online → response time matters

### Response Time vs Latency
- Response time = **latency + network + queuing delays**
- It **varies**, so analyze **distribution**, not average

Mean (avg) is misleading → doesn’t show tail users.

### Use Percentiles
- Median (p50) = 50% faster, 50% slower
- To measure worst-case performance → **p95, p99, p999**

Example:
- p95 = 1.5s → **95% < 1.5s, 5% slower**

Tail latencies matter because:
- Slow users often have **most data**, thus most valuable

Amazon SLO example: **optimize to p999**, not p9999 (too costly)

Percentiles define **SLO/SLA** terms:
- Median < 200 ms
- p99 < 1s
- 99.9% uptime required → refunds if unmet

### Queueing + Head‑of‑Line Blocking
- Few slow requests can **block many fast ones**
- Impacts tail latency significantly
- Always measure response at **client side**

---

Date: December 8, 2025
* When generating load artficially in order to test scalability of a system, the load generating client needs to keep sending requests 

---
Date: December 15, 2025

* When generating load artficially in order to test scalability of a system, the load generating client needs to keep sending requests independently of the response time. If the client waits for the prevous request to complete before sending the next one, that behaviour has the effect of artificially keeping the queue shorter in the test than they would in reality, which skew the measurements.
### Percentiles in Practice
* It just takes one slow call to make the entire end-user request slow ( even if all calls called in parallel )
* Even if a small amount of backend calls are slow, the chance of getting a slow call increases if an enduser request requires multiple backend calls, and so a higher proportion of end-user requests end up being slow. known as tail latency amplification



