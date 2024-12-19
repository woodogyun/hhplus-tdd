package io.hhplus.tdd.manager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Component;

@Component
public class LockManager {
    private ConcurrentHashMap<Long, ReentrantLock> locks = new ConcurrentHashMap<>();
    public void lock(long id) {
        ReentrantLock lock = locks.get(id);
        if (lock == null) {
            lock = new ReentrantLock();
            locks.put(id, lock);
        }
        lock.lock();
    }
    public void unlock(long id) {
        ReentrantLock lock = locks.get(id);
        if (lock == null) {
            return;
        }
        lock.unlock();
    }
}
