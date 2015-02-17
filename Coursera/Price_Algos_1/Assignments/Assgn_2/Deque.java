/*****************************************************
  * A generic double ended queue implemented using Linked List. Each element is
  * of the type Item.
  * 
  *****************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int N;
    private Node first;
    private Node last;
    
    // Helper Linked List Class
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }
    
    // Construct an empty Dequeue
    public Deque() {
        first = last = null;
        N = 0;
    }
    
    // is the deque empty?
    public boolean isEmpty() {
        return N == 0;
    }
    
    // return the number of items on the deque
    public int size() {
        return N;
    }
    
    // add the item to the front
    public void addFirst(Item item) {
        if(item == null) {
            throw new NullPointerException("Null Item Passed");
        }
        Node newnode = new Node();
        newnode.item = item;
        newnode.next = first;
        newnode.prev = null;
        if(isEmpty()) 
            last = first = newnode;
        else {
            first.prev = newnode;
            first = newnode;
        }
        
        N++;
    }
    
    // add the item to the end
    public void addLast(Item item) {
        if(item == null) {
            throw new NullPointerException("Null Item Passed");
        }
        Node newnode = new Node();
        newnode.item = item;
        newnode.next = null;
        newnode.prev = last;
        if(isEmpty()) 
            first = last = newnode;
        else {
            last.next = newnode;
            last = last = newnode;;
        }
        
        N++;
    }
    
    // remove and return the item from the front
    public Item removeFirst() {
       if(isEmpty())
           throw new NoSuchElementException("Dequeue Underflow");
       Item item = first.item;
       first = first.next;
       N--;
       if(isEmpty())
           last = first;
       else
           first.prev = null;
       return item;
    }
    
    // remove and return the item from the end
    public Item removeLast() {
        if(isEmpty())
            throw new NoSuchElementException("Dequeue Underflow");
        Item item = last.item;
        last = last.prev;
        N--;
        if(isEmpty())
            first = last;
        else 
            last.next = null;
        return item;
    }
    
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator();
    }
    
    private class ListIterator implements Iterator<Item> {
        private Node current;
        
        ListIterator() {
            current = first;
        }
        
        public boolean hasNext() {
            return current != null;
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        public Item next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}
    
        