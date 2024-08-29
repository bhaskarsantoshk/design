
# LRU and LFU Cache Interview Discussion

## Introduction
During the interview, the discussion centered around implementing LRU (Least Recently Used) and LFU (Least Frequently Used) caches. The focus was on the data structures used in these approaches and the thought process behind solving the problem. Code was not expected, but a clear approach was required.

---

## LRU Cache Approach

**Interviewer Question:** _"Can you explain how you would implement an LRU cache?"_

**Response:**
- **Data Structures:**
  - **Doubly Linked List:** This is used to maintain the order of elements, with the most recently used elements being closer to the head and the least recently used closer to the tail.
  - **HashMap:** This is used for O(1) access to the elements by storing a mapping from the key to the node in the doubly linked list.

- **Operation Overview:**
  - **Get Operation:** When an element is accessed, it is moved to the head of the linked list to mark it as most recently used.
  - **Put Operation:** When a new element is added:
    - If the cache is full, the element at the tail (least recently used) is removed.
    - The new element is added to the head of the linked list.

**Challenges Addressed:**
- **Efficiency:** Both the `get` and `put` operations are O(1) due to the combined use of the doubly linked list and HashMap.
- **Eviction:** The least recently used element is always easily accessible at the tail of the list, ensuring efficient eviction.

---

## LFU Cache Approach

**Interviewer Question:** _"How would you approach implementing an LFU cache?"_

**Response:**
- **Data Structures:**
  - **HashMap:** Two HashMaps are used:
    - One for storing the frequency of access for each key.
    - Another for mapping keys to a doubly linked list that stores the keys with the same frequency.
  - **Doubly Linked List:** This stores the keys with the same frequency. The list is ordered by access time, with the least recently used elements at the tail.

- **Operation Overview:**
  - **Get Operation:** When an element is accessed:
    - The frequency of that element is increased.
    - The element is moved to the corresponding linked list for its new frequency.
    - If it becomes the only element in the new frequency list, it is moved to the head.
  - **Put Operation:** When a new element is added:
    - If the cache is full, the least frequently used element (from the lowest frequency list) is removed.
    - The new element is added to the frequency list for frequency 1.

**Challenges Addressed:**
- **Efficiency:** The use of HashMaps ensures O(1) access and update operations, while the doubly linked lists maintain the order of usage.
- **Complexity:** LFU is more complex than LRU due to the need to track and update frequencies, but the approach efficiently handles both access frequency and recency.

**Iteration Process:**
- Initially, I struggled with maintaining the correct order within the frequency lists and ensuring O(1) operations for both `get` and `put`. However, after several iterations, I was able to arrive at a solution that met the efficiency requirements.

---

## Low-Level Design (LLD) Discussion

**Interviewer Question:** _"Can you abstract common elements from both LRU and LFU caches and describe an LLD?"_

**Response:**
- **Abstract Class - Cache:**
  - **Purpose:** To abstract common operations and attributes of both LRU and LFU caches.
  - **Common Elements:**
    - **Capacity:** The maximum number of elements the cache can hold.
    - **Get Method:** To retrieve an element from the cache.
    - **Put Method:** To insert an element into the cache.

### Abstract Class Design

#### Diagram
\`\`\`plaintext
+----------------------+
|        Cache         |
+----------------------+
| - capacity: int      |
| + get(key: K): V     |
| + put(key: K, value: V): void |
+----------------------+
           /  \  
          /    +----------------------+    +----------------------+
|       LRUCache       |    |       LFUCache       |
+----------------------+    +----------------------+
| + get(key: K): V     |    | + get(key: K): V     |
| + put(key: K, value: V): void | + put(key: K, value: V): void |
+----------------------+    +----------------------+
\`\`\`

#### Explanation
- **Cache Class:** The abstract base class that defines the common interface for cache operations. It includes the `capacity`, `get`, and `put` methods.
- **LRUCache and LFUCache Classes:** These classes extend the `Cache` class and implement the specific logic for LRU and LFU caching strategies.

**Challenges Addressed:**
- **Reusability:** The abstract class allows for code reusability and enforces a consistent interface for different cache implementations.
- **Extensibility:** New caching strategies can be easily added by extending the `Cache` class and implementing the specific logic.

---

### Conclusion
The discussion highlighted the differences and complexities involved in implementing LRU and LFU caches. While LRU is more straightforward and easier to implement, LFU requires careful handling of both frequency and recency. The interviewer emphasized the importance of abstracting common elements in the design, leading to the creation of an abstract `Cache` class that both LRU and LFU caches can extend. This approach ensures a clean, maintainable, and extensible design.
