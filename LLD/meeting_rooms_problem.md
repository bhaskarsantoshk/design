
# Low-Level Design (LLD) for Meeting Room Allocation System

## Overview

This document provides a low-level design for a system that handles the allocation of meeting rooms based on given meeting intervals. The system is designed to efficiently determine the minimum number of meeting rooms required to accommodate all meetings without overlap.

## 1. Class Diagram

### 1.1. Classes

#### 1.1.1. `Interval`
- **Attributes:**
  - `start`: `int` - The start time of the meeting.
  - `end`: `int` - The end time of the meeting.
- **Constructor:**
  - `Interval(int start, int end)`: Initializes the `Interval` object with start and end times.

#### 1.1.2. `MeetingRoomManager`
- **Attributes:**
  - `minHeap`: `PriorityQueue<Integer>` - A min-heap (priority queue) to track the end times of meetings currently using rooms.
- **Methods:**
  - `minMeetingRooms(List<Interval> intervals) -> int`: Calculates the minimum number of meeting rooms required.

## 2. Sequence Diagram

### 2.1. Sequence of Operations

1. **Input:** The `MeetingRoomManager` receives a list of `Interval` objects representing meeting times.
2. **Sort Intervals:** The intervals are sorted based on their start times.
3. **Heap Operations:**
   - For each interval, the system checks if the earliest ending meeting has finished before the next meeting starts.
   - If so, that room is freed up (remove from heap).
   - Otherwise, a new room is allocated (add to heap).
4. **Output:** The size of the heap at its maximum represents the number of rooms required.

## 3. Database Schema

### 3.1. `Meetings` Table

| Column Name   | Data Type | Description                        |
|---------------|-----------|------------------------------------|
| `id`          | `INT`     | Primary Key, Auto-increment        |
| `start_time`  | `DATETIME`| Start time of the meeting          |
| `end_time`    | `DATETIME`| End time of the meeting            |

### 3.2. Example SQL Query

```sql
SELECT start_time, end_time FROM Meetings ORDER BY start_time ASC;
```

This query retrieves all meeting intervals sorted by start time, ready for processing by the `MeetingRoomManager`.

## 4. Code Example

### 4.1. `Interval` Class

```java
public class Interval {
    int start;
    int end;

    public Interval(int start, int end) {
        this.start = start;
        this.end = end;
    }
}
```

### 4.2. `MeetingRoomManager` Class

```java
import java.util.*;

public class MeetingRoomManager {
    public int minMeetingRooms(List<Interval> intervals) {
        if (intervals == null || intervals.isEmpty()) {
            return 0;
        }

        // Step 1: Sort the intervals by start time
        intervals.sort(Comparator.comparingInt(i -> i.start));

        // Step 2: Use a min-heap to track the end time of ongoing meetings
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.add(intervals.get(0).end);

        int maxRoomsRequired = 1;

        // Step 3: Iterate over the remaining intervals
        for (int i = 1; i < intervals.size(); i++) {
            if (intervals.get(i).start >= minHeap.peek()) {
                minHeap.poll();
            }

            minHeap.add(intervals.get(i).end);

            maxRoomsRequired = Math.max(maxRoomsRequired, minHeap.size());
        }

        return maxRoomsRequired;
    }
}
```

## 5. Summary

This LLD provides a clear structure for implementing a meeting room allocation system. The key components are the `Interval` and `MeetingRoomManager` classes, with the latter managing the allocation of rooms using a min-heap. The database schema is straightforward, supporting the necessary operations to retrieve meeting intervals for processing.
