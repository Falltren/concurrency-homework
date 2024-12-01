package com.fallt.concurrency.blocking_queue;

public class BlockingQueue<E> {

    private final E[] items;
    private int count;
    private int firstElement;
    private int lastElement;
    private final int capacity;

    @SuppressWarnings("unchecked")
    public BlockingQueue(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must equals to or greater then 1 ");
        }
        this.capacity = capacity;
        items = (E[]) new Object[capacity];
    }

    public BlockingQueue() {
        this(10);
    }

    public int size() {
        return count;
    }

    public synchronized void enqueue(E newItem) throws InterruptedException {
        while (count >= capacity) {
            wait();
        }
        items[lastElement] = newItem;
        lastElement = (lastElement + 1) % capacity;
        count++;
        notifyAll();
    }

    public synchronized E dequeue() throws InterruptedException {
        while (count == 0) {
            wait();
        }
        E item = items[firstElement];
        items[firstElement] = null;
        firstElement = (firstElement + 1) % capacity;
        count--;
        notifyAll();
        return item;
    }

}
