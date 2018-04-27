import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int n;
    private int enqueues;

    public RandomizedQueue(){
        items = (Item[]) new Object[1];
    }

    public boolean isEmpty(){
        return n == 0;
    }

    public int size(){
        return n;
    }

    public void enqueue(Item item){
        if (item == null) throw new IllegalArgumentException();
        if (enqueues == items.length)resize(2*items.length);
        items[enqueues++] = item;
        n++;
    }

    public Item dequeue(){
        if (isEmpty()) throw new NoSuchElementException();
        int indexToRemove = randIndex();
        Item item = items[indexToRemove];
        items[indexToRemove] = null;
        n--;
        if (n > 0 && n == items.length/4){
            shrink(items.length/2);
            enqueues = n;
        }
        return item;
    }

    public Item sample(){
        if (isEmpty()) throw new NoSuchElementException();
        return items[randIndex()];
    }

    private int randIndex(){
        int indexToRemove = StdRandom.uniform(0, items.length);
        while (items[indexToRemove] == null){
            indexToRemove = StdRandom.uniform(0, items.length);
        }

        return indexToRemove;
    }

    @Override
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item>{
        private int curr;
        private int size;
        private Item[] theItems;
        private boolean[] seen;

        private RandomIterator(){
            theItems = (Item[]) new Object[items.length];
            seen = new boolean[theItems.length];
            System.arraycopy(items, 0, theItems, 0, items.length);
            size = n;
        }

        @Override
        public boolean hasNext() {
            return size != 0;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            curr = randIndexIter();
            size--;
            return theItems[curr];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private int randIndexIter(){
            int indexToRemove = StdRandom.uniform(0, theItems.length);
            while (theItems[indexToRemove] == null || seen[indexToRemove]){
                indexToRemove = StdRandom.uniform(0, theItems.length);
            }

            seen[indexToRemove] = true;
            return indexToRemove;
        }
    }

    private void shrink(int size) {
        Item[] copy = (Item[]) new Object[size];
        int copycounter = 0;
        for (Item item : items) {
            if (item != null) copy[copycounter++] = item;
        }
        items = copy;
    }

    private void resize(int size) {
        Item[] copy = (Item[]) new Object[size];
        int copycounter = 0;
        for (Item item : items) {
            if (item != null) copy[copycounter++] = item;
        }
        items = copy;
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue(1);
        randomizedQueue.dequeue();
        randomizedQueue.enqueue(2);
        randomizedQueue.enqueue(3);
        randomizedQueue.dequeue();
        randomizedQueue.enqueue(4);
        randomizedQueue.enqueue(5);

        System.out.println("Size: " + randomizedQueue.size());
        for (Integer s: randomizedQueue){
            System.out.print(s + " ");
        }
        System.out.println();
        for (Integer s: randomizedQueue){
            System.out.print(s + " ");
        }
        System.out.println();
        randomizedQueue.enqueue(6);
//        randomizedQueue.dequeue();
//        randomizedQueue.dequeue();
//        randomizedQueue.dequeue();

        System.out.println("Removed: " + randomizedQueue.dequeue());
        System.out.println("Sample: " + randomizedQueue.sample());
        System.out.println("Size: " + randomizedQueue.size());
        for (Integer s: randomizedQueue){
            System.out.print(s + " ");
        }
        System.out.println();
    }
}
