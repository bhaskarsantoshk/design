package LLD.readerWriterProblem;

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
