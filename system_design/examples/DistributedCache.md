
# Distributed Key-Value Caching System Design

## Overview

Designing a distributed key-value caching system similar to Memcached or Redis that can handle the scale of services like Google or Twitter.

### System Skeleton

```
App <-> Cache <-> Database
```

## Features

- **Data Scale**: Capable of handling 30 TB of data.
- **Eviction Strategy**: Least Recently Used (LRU).
- **Access Patterns**:
  - **Write-through**: Writes occur on both the database and cache simultaneously, leading to higher write latency.
  - **Write-around**: Writes go to the database only, and reads from the database in case of a cache miss, leading to higher read latency, especially if data is re-read shortly after a write.
  - **Write-back**: Writes occur in the cache, with the database being updated asynchronously. This can result in data loss in case of a crash.

### System Estimations

- **Data**: 30 TB
- **Queries Per Second (QPS)**: 10 million QPS
- **Machines**: 420 machines (assuming 72 or 144 GB per machine)

### Design Goals

- **Latency**: Latency is of utmost importance; it’s the core purpose of using a cache.
- **Consistency vs Availability**: Prioritize availability. Minor inconsistencies (e.g., missing recent tweets) are acceptable. Unavailability leads to latency spikes and increases the load on the database.

## Detailed Design

### LRU Cache

- **Single Machine LRU Cache**:
  - If the cache had enough space and never needed to remove entries, a single hashmap would suffice.
  - However, since entries need to be evicted, use a combination of a hashmap and a linked list.

#### LRU Cache Operations

- **Read Operation**:
  - Check if the entry exists in the hashmap.
  - If it exists, remove the corresponding node from the linked list and reinsert it at the head.
  - If it does not exist, fetch from the database, insert it at the head, and add it to the hashmap.

- **Write Operation**:
  - If the cache is full, remove the node at the tail.
  - Insert the new entry into the hashmap and at the head of the linked list.

### Concurrency Handling

Concurrency arises from the simultaneous execution of reads and writes. Proper locking mechanisms are necessary to prevent race conditions:

- **Read Locks**: Required to ensure the consistency of the linked list while reading.
- **Write Locks**: Required during insertion or eviction to maintain the integrity of both the hashmap and the linked list.
- **Granularity**: The finer the granularity of the locks, the better the concurrency. It’s crucial to break the problem down into the smallest possible parts to minimize locking contention.

### CPU Time Consideration

Given the high QPS (10M) and 30 TB of data distributed over 420 machines:

- **QPS per Machine**: 23,000 QPS/machine
- **CPU Time per Query**: 174 microseconds (considering a 4-second window per 1-second interval)

This is challenging, given context switches and overheads, meaning less than 174 microseconds are available for handling each query.

### Sharding Strategy

To optimize performance, sharding is used:

- **Sharding among Machines with 16GB RAM**:
  - **Number of Shards**: 1,875 shards (30 TB / 16 GB per shard)
  - **QPS per Shard**: Approximately 5,500 QPS/shard
  - **Sharding Strategy**: Hash the key using `hash(key) % TOTAL_SHARDS`. The hash function might vary depending on the key properties (e.g., auto-incremental user_id).

### Handling Failures

When a machine handling a shard goes down, requests will fall back to the database, increasing latency:

- **Single Machine per Shard**:
  - Simpler system, less maintenance, but higher risk of elevated latency when a machine fails.
  
- **Multiple Machines per Shard**:
  - Allows redundancy, reducing the impact of failures.
  - **Complications**: Data may become inconsistent across machines, requiring strategies to handle data synchronization:
    - **Master-Slave Replication**:
      - One active server per shard with a follower updating in real-time.
      - On failure, the slave becomes the master.
      - Synchronization via a change log with versioning.
    - **Eventual Consistency**:
      - Writes go to the master, and reads can be served by slaves, leading to eventual consistency.

