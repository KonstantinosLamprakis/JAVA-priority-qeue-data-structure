// This class represents a Processor.

public class Processor implements Comparable<Processor> {

    private static int totalId = 0; // represents the id of last processor.
    private final int id; // id of processor.
    private ProcessesList processed_jobs; // array with processors that are completed.

    // constructors.

    // default constructor.
    Processor(){
        id = ++totalId; // id = 1, 2, 3 .. N
        processed_jobs = new ProcessesList(); // empty list._
    }

    // processor executes these processes.
    void execute(int process){
        processed_jobs.insert(process);
    }

    // returns total time of processes that are completed.
    int getActiveTime(){
        return processed_jobs.getSum();
    }

    // compares two processors based on active time.
    @Override
    public int compareTo(Processor p) throws NullPointerException {
        if(p==null) throw new NullPointerException();
        if(getActiveTime() > p.getActiveTime()){
            return 1;
        }else if(getActiveTime() < p.getActiveTime()){
            return -1;
        }
        return 0;

    }

    @Override
    public String toString() {
        return "id: " + id + " load= "+ getActiveTime() + "\t\t" + processed_jobs;
    }

    // class ProcessesList represents a simple list with processes, that are completed.
    private class ProcessesList{

        private Node head = null, tail=null;

        boolean isEmpty() {
            return head==null;
        }

        void insert(int value) {
            try{
                if(isEmpty()){
                    tail = head = new Node(value);
                } else{
                    Node newNode = new Node(value, null);
                    tail.setNext(newNode);
                    tail = newNode;
                }
            }catch (OutOfMemoryError a){
                System.out.println("Not enough memory.");
            }
        }

        int getSum() {
            int sum = 0;
            for (Node n = head; n != null; n = n.getNext()) sum+= n.getValue();
            return sum;
        }

        @Override
        public String toString() {
            if(isEmpty())return "";
            String result ="";
            Node n;
            for (n = head; n.next != null; n = n.getNext()) result += n + ", ";
            result += n;
            return result;
        }

        // class Node represents a single element of ProcessesList.
        private class Node{
            private int value;
            private Node next;

            Node(int value, Node next) {
                this.value = value;
                this.next = next;
            }

            Node(int value) {
                this(value, null);
            }

            int getValue() {
                return value;
            }

            Node getNext() {
                return next;
            }

            void setNext(Node next) {
                this.next = next;
            }

            @Override
            public String toString() {
                return "" + value;
            }
        }
    }
}
