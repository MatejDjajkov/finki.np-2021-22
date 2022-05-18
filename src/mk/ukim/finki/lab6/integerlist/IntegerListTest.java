package mk.ukim.finki.lab6.integerlist;
import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.*;
import java.util.stream.Collectors;

class IntegerList {
    private List<Integer> integerList;

    public IntegerList() {
        this.integerList = new LinkedList<>();
    }

    public IntegerList(Integer... numbers) {
        this.integerList = new LinkedList<>();
        integerList.addAll(Arrays.asList(numbers));
    }

    public void add(int el, int idx) {
        if (idx <= size())
            this.integerList.add(idx, el);
        else {
            List<Integer> temp = new LinkedList<>();
            temp.addAll(integerList);
            for (int i = integerList.size(); i < idx; ++i)
                temp.add(0);
            temp.add(idx, el);
            integerList.clear();
            integerList.addAll(temp);
        }
    }

    private boolean notValidIndex(int index) {
        return index < 0 || index > size();
    }

    public int remove(int idx) {
        if (notValidIndex(idx)) throw new ArrayIndexOutOfBoundsException();
        return integerList.remove(idx);
    }

    public void set(int el, int idx) {
        if (notValidIndex(idx)) throw new ArrayIndexOutOfBoundsException();
        integerList.set(idx, el);
    }

    public int get(int idx) {
        if (notValidIndex(idx)) throw new ArrayIndexOutOfBoundsException();
        return integerList.get(idx);
    }

    public int size() {
        return integerList.size();
    }

    public int count(int el) {
        return Collections.frequency(integerList, el);
    }

    public void removeDuplicates() {
        Hashtable<Integer, Boolean> hashtable = new Hashtable<>();
        ListIterator<Integer> listIterator = integerList.listIterator(integerList.size());
        while (listIterator.hasPrevious()) {
            Integer element = listIterator.previous();
            if (hashtable.containsKey(element))
                listIterator.remove();
            else
                hashtable.put(element, true);
        }
        hashtable.clear();
    }

    public int sumFirst(int k) {
        return integerList.stream().mapToInt(Integer::intValue).limit(k).sum();
    }

    public int sumLast(int k) {
        LinkedList<Integer> tmp = new LinkedList<>();
        tmp.addAll(integerList);
        Collections.reverse(tmp);
        return tmp.stream().mapToInt(Integer::intValue).limit(k).sum();
    }

    public void shiftRight(int idx, int k) {
        if (notValidIndex(idx)) throw new ArrayIndexOutOfBoundsException();
        int newIndex = (idx + k) % size();
        if (newIndex != idx) {
            int temp = get(idx);
            remove(idx);
            add(temp, newIndex);
        }
    }

    public void shiftLeft(int idx, int k) {
        if (notValidIndex(idx)) throw new ArrayIndexOutOfBoundsException();
        int newIndex;
        if (idx - k < 0)
            newIndex = size() - (Math.abs(idx - k) % size());
        else
            newIndex = idx - k;
        if (newIndex != idx) {
            int temp = get(idx);
            remove(idx);
            add(temp, newIndex);
        }
    }

    public IntegerList addValue(int value) {
        IntegerList il = new IntegerList();
        il.integerList = this.integerList.stream().map(integer -> (integer + value)).collect(Collectors.toList());
        return il;
    }
}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) { //test standard methods
            int subtest = jin.nextInt();
            if ( subtest == 0 ) {
                IntegerList list = new IntegerList();
                while ( true ) {
                    int num = jin.nextInt();
                    if ( num == 0 ) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if ( num == 1 ) {
                        list.remove(jin.nextInt());
                    }
                    if ( num == 2 ) {
                        print(list);
                    }
                    if ( num == 3 ) {
                        break;
                    }
                }
            }
            if ( subtest == 1 ) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for ( int i = 0 ; i < n ; ++i ) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if ( k == 1 ) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if ( num == 1 ) {
                    list.removeDuplicates();
                }
                if ( num == 2 ) {
                    print(list.addValue(jin.nextInt()));
                }
                if ( num == 3 ) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
        if ( k == 2 ) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if ( num == 1 ) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if ( num == 2 ) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if ( num == 3 ) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if ( il.size() == 0 ) System.out.print("EMPTY");
        for ( int i = 0 ; i < il.size() ; ++i ) {
            if ( i > 0 ) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}