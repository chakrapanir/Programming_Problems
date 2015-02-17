/**********************************************************
  * Randomzied Queue implementation with resizing array
  * 
  ********************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;             //array of items
    private int N = 0;           // number of elements in the queue
    private int last = 0;        // index of the last element in the queue
    
    // construct an empty randomized queue
    public RandomizedQueue() {
        q = (Item[]) new Object[2];
    }
    
    // is the queue empty?
    public boolean isEmpty() {
        return N == 0;
    }
    
    // return the number of items on the queue
    public int size() {
        return N;
    }
    
    private void resizeArray(int capacity) {
        assert capacity >= N;
        Item[] temparr = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            temparr[i] = q[i];
        }
        q = temparr;
    }
    
    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException("Null Item Passed");
        if (N == q.length)  resizeArray(2*q.length);
        q[last++] = item;
        N++;
    }
    
    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())  throw new NoSuchElementException("Queue Underflow");
        int randidx = StdRandom.uniform(N);
        Item item = q[randidx];
        if (randidx != (last-1))  q[randidx] = q[last-1];
        q[last-1] = null;
        last--;
        N--;
        // Shrink the array when number of elements is one-quarter of array length
        if (N > 0 && N == q.length/4)  resizeArray(q.length/2);
        return item;
    }
    
    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty())  throw new NoSuchElementException("Queue Underflow");
        int randidx = StdRandom.uniform(N);
        Item item = q[randidx];
        return item;
    }
    
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }
    
    private class RandomQueueIterator implements Iterator<Item> {
        private int i = 0;
        private Item[] itrarr;
        
        public RandomQueueIterator() {
            itrarr = (Item[]) new Object[N];
            for (int j = 0; j < N; j++) 
                itrarr[j] = q[j];
            StdRandom.shuffle(itrarr);
        }
        
        public boolean hasNext() {
            return i < N;
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("End of Queue");
            Item item = itrarr[i++];
            return item;
        }
    }
}
    