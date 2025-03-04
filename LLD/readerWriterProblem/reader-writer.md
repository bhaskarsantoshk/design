# Low-Level Design (LLD) for the Reader-Writer Problem

* It deals with the synchronization problem when multiple threads accessing a shared resource ( file , memory / database).
* Multiple readers can read simultaneously and should not block each other
* Only one writer (with exclusive access) should write at a time. When the writer is writing , no one should read

### Examples
* If a reader is reading, multiple readers can read 
* If a writer is writing , no one should be able to read until it completes
* If multiple writers are writing, they should be given access/ executed one after the other

### Problems
* Readers Priority ( Writers may starve )
* Writers Priority ( Readers may starve )
* Fair Approach ( No starvation )

### Solution using ReentrantReadWriteLock

How it works ?
We use lock to manage access

* Read Lock ( readLock() )
  * Multiple readers can hold this lock
  * If a writer is writing, new readers must wait
* Write Lock ( writeLock())
  * Only one writer can acquire this lock
  * No writers or readers can proceed until this lock is finished

```java
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SharedResource {
    private String data = "Initial Data";
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    public void read(int readerId){
        lock.readLock().lock();
        try{
            System.out.println("Reader "+ readerId + " is reading");
            Thread.sleep(1000);  // Simulating read delay
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void write(int writerId, String newData){
        lock.writeLock().lock();
        try {
            System.out.println("Writer " + writerId + " is writing");
            Thread.sleep(2000);
            this.data = newData;
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }

    }
}

```
* readLock() allows multiple threads to read at the same time.
* writeLock() ensures only one writer updates the resource.
* Fair Lock (true) ensures both readers and writers get fair access.

```text
public class Reader extends Thread{
    private SharedResource resource;
    private int readerId;

    public Reader(SharedResource resource, int id){
        this.readerId = id;
        this.resource = resource;
    }

    @Override
    public void run(){
        while(true){
            resource.read(readerId);
            try{
                Thread.sleep(1500);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
```

* A reader gets a read lock.
* It prints the data and releases the lock.


```text
public class Writer extends Thread{
    private SharedResource resource;
    private int writerId;

    public Writer(SharedResource resource, int id){
        this.writerId = id;
        this.resource = resource;
    }

    @Override
    public void run(){
        while(true){
            String newData = "Data from Writer " + writerId;
            resource.write(writerId, newData);
            try{
                Thread.sleep(1500);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
```

* A writer gets a write lock. 
* It updates the shared resource and releases the lock.