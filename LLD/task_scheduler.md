# Low-Level Design (LLD) of a Task Scheduler

A Task Scheduler is responsible for executing tasks at specified times, handling recurring jobs, prioritizing tasks, and ensuring fault tolerance. Below is the Low-Level Design (LLD) of a Task Scheduler, covering its architecture, components, data models, and interactions.

## Requirements

### Functional

* Schedule tasks to run at specified time or run at intervals
* Support one time or recurring tasks
* Handle Priority based execution
* Provide a retry mechanism in case of a failures
* Allow Task Cancellation and Rescheduling 
* Ensure distributed execution to prevent duplicate scheduling. 
* Allow persistence for storing scheduled tasks.

### Non-Functional 
* Scalability - up to 500K tasks
* Fault tolerance - tasks should retry on failure
* Performance - minimal latency
* Observability - Logging / Monitoring

## High Level Architecture

* Task Scheduler
  * Persistence : Redis
  * Manages Executions ( Retry/ Delay)
* Task Executor
  * Parallel executions
  * Retry/ Failure handling
* Task Queue
  * PQ/ Distributed Message Queue ( Kafka , RabbitMQ)
* Worker Nodes
  * Poll the Task Queue and Execute Tasks
  * Scales horizontally
* Task Store
  * Meta data, execution logs/counts, retries
  * SQL ( Postgres, MySQL ), NoSQL (MongoDB, Redis)
* API Layer
  * exposes API to schedule, update, cancel, fetch task statuses
  * provide an interface for users to interact with scheduler

## Components and Class Diagram
* Task Scheduler Component
  * accepts new tasks and store them
  * assigns a unique id
  * supports both immediate and scheduled tasks
* Task Executor Component
  * executes tasks when their schdule time arrives
  * supports retry on failure
  * Logs execution details
* Task Queue
  * sorts tasks based on priority/ time
  * supports locking for preventing duplicate executions in distributed setup
* Worker Nodes
  * multiple workers to fetch tasks and execute

## Class diagram

TaskScheduler
  + scheduleTasks(Task)
  + cancelTask(Task)
  + rescheduleTask(taskId, time)
  + fatchTaskStatus(taskId)

TaskExceutor
  + execute(Task)
  + retry(Task)
  + handleFailure(Task, error)
TaskQueue
  + addTask(Task)
  + getNext()
  + removeTask(Task)
WorkerNode
  + pollTasks()
  + executeTask(Task)
  + logResult(Task, status)
## Data Model

Task Table ( SQL)
Column            Type            Desc
task_id          UUID             Unique Identifier for Task
task_name        VARCHAR(255)     name of the task
execution_time   TIMESTAMP        Time at which the task should run.
status           ENUM             Pending, Running, Completed, Failed.
retry_count      INT
priority         INT

Task Execution Log Table

Column	Type	Description
log_id	UUID	Unique identifier for each execution.
task_id	UUID	Foreign key to Task table.
execution_time	TIMESTAMP	Actual execution time.
status	ENUM	Success, Failed, Retried.

## API Design

Schedule a Task
```text
POST /scheduleTask
{
"task_name":"Backup Database",
"execution_time":"2025-03-03T12:00:00Z",
"priority":"10"
}

Response:
{
    "task_id": "abc123",
    "status": "Scheduled"
}
```

Fetch Task Status
```text
GET /taskStatus/{task_id}

Response:
{
    "task_id": "abc123",
    "status": "Running",
    "execution_time": "2025-03-05T12:00:00Z"
}
```

Cancel Task
```text
DELETE /cancelTask/{task_id}
Response:
{
    "task_id": "abc123",
    "status": "Cancelled"
}
```

## Optimization
* Use distributed locks (e.g., Redis Redlock) to ensure a task is executed only once.
* Store tasks in Kafka or RabbitMQ for high availability.
* Distribute tasks across worker nodes dynamically.
* Implement exponential backoff for retries (e.g., retry after 5s, then 10s, 20s).
* Store failed tasks in a Dead Letter Queue (DLQ) for debugging.
* Use sharding to partition tasks across multiple databases.

## Tech Stack

Component	Tech Used
Backend API	Java/Spring Boot, Python/Django, Node.js
Database	PostgreSQL, MySQL, MongoDB
Queue System	Kafka, RabbitMQ, Redis
Task Execution	Kubernetes CronJobs, Celery Workers
Logging & Monitoring	ELK Stack (Elasticsearch, Logstash, Kibana), Prometheus, Grafana


