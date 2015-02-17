/**************************************************************
  * A client program that takes a command line integer k;
  * reads in a sequence of N strings from standard input 
  * using StdIn.readString(); and prints out exactly k of them, 
  * uniformly at random. Each item from the sequence can be 
  * printed out at most once. You may assume that k is between
  * 0 and N. Where N is the number of string on standard input. 
  **************************************************************/

public class Subset {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);  // Subset length from command line
        int N = 0;                          // Current input sequence size
        RandomizedQueue rq = new RandomizedQueue<String>();
        
        while(!StdIn.isEmpty()) {
            String item = StdIn.readString();
            N++;
            //Enqueue when input sequence length is less than k
            if(N <= k)  rq.enqueue(item);
            else {
                /* When input sequence length is greater than k, 
                 * use reservoir sampling algorithm to choose 
                 * the string to an existing evict and insert 
                 * incoming string.
                 */
                int randidx = StdRandom.uniform(N);
                if(randidx < k) {
                    rq.dequeue();
                    rq.enqueue(item);
                }
            }
        }
        
        while(!rq.isEmpty()) {
            StdOut.println(rq.dequeue());
        }
    }
}