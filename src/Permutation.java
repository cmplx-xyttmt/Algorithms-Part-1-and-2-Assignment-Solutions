import edu.princeton.cs.algs4.StdIn;

public class Permutation {

    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()){
            randomizedQueue.enqueue(StdIn.readString());
        }

        int counter = 0;
        for (String s: randomizedQueue){
            if (counter == k) break;
            System.out.println(s);
            counter++;
        }
    }
}
