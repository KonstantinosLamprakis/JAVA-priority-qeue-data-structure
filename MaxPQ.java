// MaxPQ represents a priority queue, which contains Processors.

public class MaxPQ {

    private Processor[] heap; // list of processors inside the queue.
    private int size; // number of processors inside the queue.

    // constructor.
    MaxPQ(int capacity) throws IllegalArgumentException{
        if(capacity < 1) throw new IllegalArgumentException();
        heap = new Processor[capacity+1];
        size = 0;
    }

    // insert processor to queue.
    void insert(Processor p){

        if (p == null) throw new IllegalArgumentException(); // processor != null
        if (size == heap.length-1) throw new IllegalStateException(); // enough space
        heap[++size] = p; // put processor to queue.
        swim(size); // put processor at the right position.
    }

    // getMax return processor with minimum active time(which is processor with maximum priority)
    Processor getMax(){

        if (size == 0) throw new IllegalStateException(); // queue != empty
        Processor p = heap[1]; // p will be returned(and removed) as result
        if (size > 1) heap[1] = heap[size]; // set the most right leaf as root.
        heap[size--] = null;
        sink(1); // put root at the right position.
        return p;
    }

    // move an element from bottom to top of queue at right position.
    private void swim(int i){
        while (i > 1) {  // if i is not root then i = child
            int p = i/2;  // p = parent
            int result = heap[i].compareTo(heap[p]); // compare parent with child
            if (result >= 0) return; //if child <= parent return
            swap(i, p); // swap i, p
            i = p;
        }
    }

    // move an element from top to bottom of queue at right position.
    private void sink(int i){

        int left = 2*i, right = left+1, max = left;

        while (left <= size) { // while i has left child..(thus isn't a leaf)

            if (right <= size) {  // if i has right child...
                max = (heap[left].compareTo(heap[right])> 0) ? right : left;// find the max of 2 leafs.
            }

            if (heap[i].compareTo(heap[max]) <= 0) return; // if i(parent) has higher priority return, else swap and continue.
            swap(i, max);
            i = max; left = 2*i; right = left+1; max = left;
        }
    }

    // swap elements at indexes i,j.
    private void swap(int i, int j){
        Processor temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // return processor with makespan(maximum jobs active time and minimum priority)
    Processor getMakespan() {
        Processor makespan = heap[1];
        for(int i=2; i<=size; i++ ){
            if(makespan.getActiveTime() < heap[i].getActiveTime()) makespan = heap[i];
        }
        return makespan;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i=1; i <= size; i++){
            result += heap[i]+ "\n\n";
        }
        return result;
    }
}

