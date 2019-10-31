// This class provides a method for array sorting and makes use of quicksort algorithm.

public class  Sort{

    static void quickSort( int array[], int left, int right ) {
        if(left>right) return;
        if(array.length == 0) return;
        int pivot = partition(array, left, right);
        quickSort(array, left, pivot-1);
        quickSort(array, pivot+1, right);
    }

    //finds pivot's index
    private static int partition( int array[], int left, int right )  {
        //pivot's index
        int  p = left;
        //pivot's value
        int pivot = array[left];
        //seperating them according to pivot's value
        for(int i = left+1 ; i <= right ; i++) {
            if(array[i] > pivot) {
                p++;
                swap(array,i,p);
            }
        }
        //move pivot to its final position
        swap(array,p,left);
        return p;
    }


    //swapping method
    private static void swap(int A[], int a, int b)  {
        int temp = A[a];
        A[a] = A[b];
        A[b] = temp;
    }
}
