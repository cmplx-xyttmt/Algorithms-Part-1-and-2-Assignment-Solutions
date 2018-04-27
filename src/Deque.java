import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    private class Node{
        Item item;
        Node previous;
        Node next;

        @Override
        public String toString() {
            String s = "";
            if (previous == null) s += "No previous ";
            else s += previous.item + " ";

            s += item + " ";

            if (next == null) s += "No next";
            else s += next.item;

            return s;
        }
    }

    public Deque(){
        first = null;
        last = null;
    }

    public boolean isEmpty(){
        return first == null || last == null;
    }

    public int size(){
        return size;
    }

    public void addFirst(Item item){
        if (item == null) throw new IllegalArgumentException();
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        
        if (isEmpty()) last = first;
        else {
            first.next = oldFirst;
            oldFirst.previous = first;
        }
        size++;
    }

    public void addLast(Item item){
        if (item == null) throw new IllegalArgumentException();
        Node oldLast = last;
        last = new Node();
        last.item = item;
        if (isEmpty()) first = last;
        else {
            oldLast.next = last;
            last.previous = oldLast;
        }
        size++;
    }

    public Item removeFirst(){
        if (isEmpty()) throw new NoSuchElementException();
        Item item = first.item;
        first = first.next;
        if (first != null)first.previous = null;
        else last = null;
        size--;
        return item;
    }

    public Item removeLast(){
        if (isEmpty()) throw new NoSuchElementException();
        Item item = last.item;
        last = last.previous;
        if (last != null)last.next = null;
        else first = null;
        size--;
        return item;
    }

    @Override
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item>{
        private Node curr = first;

        @Override
        public boolean hasNext() {
            return curr != null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = curr.item;
            curr = curr.next;
            return item;
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        Node node = first;
        while (node != null){
            s.append(node.toString()).append("\n");
            node = node.next;
        }
        return s.toString();
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        System.out.println(deque.toString());
        System.out.println(deque.isEmpty());
        deque.addFirst(1);
        System.out.println(deque.isEmpty());
        System.out.println(deque.toString());
        deque.addFirst(2);
        System.out.println(deque.isEmpty());
        System.out.println(deque.toString());
        deque.addLast(3);
        System.out.println(deque.isEmpty());
        System.out.println(deque.toString());
        deque.addLast(5);
        System.out.println(deque.isEmpty());
        System.out.println(deque.toString());
        System.out.println(deque.isEmpty());
        System.out.println("Size: " + deque.size());
        System.out.println(deque.removeFirst());
        System.out.println(deque.isEmpty());
        System.out.println("Size: " + deque.size());
        System.out.println(deque.toString());
        System.out.println(deque.removeLast());
        System.out.println(deque.isEmpty());
        System.out.println("Size: " + deque.size());
        System.out.println(deque.toString());

//        Iterator<Integer> ut = deque.iterator();
        for (Integer s: deque) System.out.println(s);

        deque.removeLast();
        System.out.println(deque.isEmpty());
        System.out.println("Size: " + deque.size());
        deque.removeFirst();
        System.out.println(deque.isEmpty());
        System.out.println("Size: " + deque.size());

        System.out.print("\n\n\n\n\n");
        //Next test:
        Deque<Integer> deques = new Deque<Integer>();
        System.out.println(deques.isEmpty());
        deques.addFirst(1);
        System.out.println(deques.isEmpty());
        System.out.println(deques.removeFirst());
        System.out.println(deques.isEmpty());
        deques.addFirst(4);
    }
}
