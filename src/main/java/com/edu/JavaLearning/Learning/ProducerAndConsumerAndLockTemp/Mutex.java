package com.edu.JavaLearning.Learning.ProducerAndConsumerAndLockTemp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Author: tangzh
 * @Date: 2018/10/19$ 上午10:29$
 * 共享锁的实现(java api)
 **/
public class Mutex implements Lock,java.io.Serializable {

    private final static Sync sync = new Sync();

    private static class Sync extends AbstractQueuedSynchronizer{
        protected boolean isHeldExclusively(){
            return getState()==1;
        }
        public boolean tryAcquire(int arg){
            assert arg == 1;
            if(compareAndSetState(0,1)){
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }
        public boolean tryRelease(int arg){
            assert arg == 1;
            if(getState()==0){
                throw new IllegalArgumentException();
            }
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        Condition newCondition(){return new ConditionObject();}

        private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
            s.defaultReadObject();
            setState(0); // reset to unlocked state
      }
    }

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }

    public static void main(String[] args) {

    }
}

