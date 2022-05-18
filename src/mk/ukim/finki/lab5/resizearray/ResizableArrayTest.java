package mk.ukim.finki.lab5.resizearray;

import java.util.Scanner;
import java.util.LinkedList;

class ResizableArray<T> {
    private T[] array;
    private int numElements;

    public ResizableArray() {
        array = (T[]) new Object[0];
        numElements = 0;
    }

    public void addElement(T element) {
        if (numElements == 0 || numElements == array.length) {
            T[] temp = (T[]) new Object[numElements + 5];
            System.arraycopy(array, 0, temp, 0, numElements);
            temp[numElements++] = element;
            array = temp;
        } else
            array[numElements++] = element;
    }

    public boolean removeElement(T element) {
        boolean flag = false;
        int i;
        for (i = 0; i < numElements; ++i) {
            if (array[i].equals(element)) {
                flag = true;
                break;
            }
        }
        if (flag) {
            array[i] = array[--numElements];  // overwrite the element for removal with the last element
            T[] temp = (T[]) new Object[numElements];
            System.arraycopy(array, 0, temp, 0, numElements);
            array = temp;
        }
        return flag;
    }

    public boolean contains(T element) {
        for (T elem : array)
            if (elem != null)
                if (elem.equals(element))
                    return true;
        return false;
    }

    public int count() {
        int counter = 0;
        for (T anArray : array)
            if (anArray != null)
                ++counter;
        return counter;
    }

    public Object[] toArray() {
        Object[] array = new Object[numElements];
        System.arraycopy(this.array, 0, array, 0, numElements);
        return array;
    }

    public T elementAt(int idx) {
        if (idx >= 0&&idx <= count())
            return array[idx];
        throw new ArrayIndexOutOfBoundsException();
    }

    public static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src) {
        T[] temp = (T[]) new Object[dest.count() + src.count()];
        System.arraycopy(dest.array, 0, temp, 0, dest.count());
        System.arraycopy(src.array, 0, temp, dest.count(), src.count());
        dest.array = temp;
        dest.numElements = temp.length;
    }

    public boolean isEmpty() {
        return count() == 0;
    }
}
class IntegerArray extends ResizableArray<Integer> {
    public IntegerArray() {
        super();
    }

    public double sum() {
        Integer sum = 0;
        for (int i = 0; i < count(); ++i)
            sum += this.elementAt(i);
        return sum.doubleValue();
    }

    public double mean() {
        return sum() / count();
    }

    public int countNonZero() {
        int counter = 0;
        for (int i = 0; i < count(); ++i)
            if (!(elementAt(i).equals(0)))
                ++counter;
        return counter;
    }

    public IntegerArray distinct() {
        IntegerArray ia = new IntegerArray();
        for (int i = 0; i < count(); ++i)
            ia.addElement(this.elementAt(i));
        ia = deleteDuplicates();
        return ia;
    }

    private IntegerArray deleteDuplicates() {
        IntegerArray temp = new IntegerArray();
        for (int i = 0; i < count(); ++i)
            if (!(temp.contains(elementAt(i))))
                temp.addElement(elementAt(i));
        return temp;
    }

    public IntegerArray increment(int offset) {
        IntegerArray ia = new IntegerArray();
        for (int i = 0; i < count(); ++i)
            ia.addElement(elementAt(i) + offset);
        return ia;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count(); ++i) {
            sb.append(elementAt(i));
            sb.append(" ");
        }
        return sb.toString();
    }
}

public class ResizableArrayTest {
	
	public static void main(String[] args) {
		Scanner jin = new Scanner(System.in);
		int test = jin.nextInt();
		if ( test == 0 ) { //test ResizableArray on ints
			ResizableArray<Integer> a = new ResizableArray<Integer>();
			System.out.println(a.count());
			int first = jin.nextInt();
			a.addElement(first);
			System.out.println(a.count());
			int last = first;
			while ( jin.hasNextInt() ) {
				last = jin.nextInt();
				a.addElement(last);
			}
			System.out.println(a.count());
			System.out.println(a.contains(first));
			System.out.println(a.contains(last));
			System.out.println(a.removeElement(first));
			System.out.println(a.contains(first));
			System.out.println(a.count());
		}
		if ( test == 1 ) { //test ResizableArray on strings
			ResizableArray<String> a = new ResizableArray<String>();
			System.out.println(a.count());
			String first = jin.next();
			a.addElement(first);
			System.out.println(a.count());
			String last = first;
			for ( int i = 0 ; i < 4 ; ++i ) {
				last = jin.next();
				a.addElement(last);
			}
			System.out.println(a.count());
			System.out.println(a.contains(first));
			System.out.println(a.contains(last));
			System.out.println(a.removeElement(first));
			System.out.println(a.contains(first));
			System.out.println(a.count());
			ResizableArray<String> b = new ResizableArray<String>();
			ResizableArray.copyAll(b, a);
			System.out.println(b.count());
			System.out.println(a.count());
			System.out.println(a.contains(first));
			System.out.println(a.contains(last));
			System.out.println(b.contains(first));
			System.out.println(b.contains(last));
			ResizableArray.copyAll(b, a);
			System.out.println(b.count());
			System.out.println(a.count());
			System.out.println(a.contains(first));
			System.out.println(a.contains(last));
			System.out.println(b.contains(first));
			System.out.println(b.contains(last));
			System.out.println(b.removeElement(first));
			System.out.println(b.contains(first));
			System.out.println(b.removeElement(first));
			System.out.println(b.contains(first));

			System.out.println(a.removeElement(first));
			ResizableArray.copyAll(b, a);
			System.out.println(b.count());
			System.out.println(a.count());
			System.out.println(a.contains(first));
			System.out.println(a.contains(last));
			System.out.println(b.contains(first));
			System.out.println(b.contains(last));
		}
		if ( test == 2 ) { //test IntegerArray
			IntegerArray a = new IntegerArray();
			System.out.println(a.isEmpty());
			while ( jin.hasNextInt() ) {
				a.addElement(jin.nextInt());
			}
			jin.next();
			System.out.println(a.sum());
			System.out.println(a.mean());
			System.out.println(a.countNonZero());
			System.out.println(a.count());
			IntegerArray b = a.distinct();
			System.out.println(b.sum());
			IntegerArray c = a.increment(5);
			System.out.println(c.sum());
			if ( a.sum() > 100 )
				ResizableArray.copyAll(a, a);
			else
				ResizableArray.copyAll(a, b);
			System.out.println(a.sum());
			System.out.println(a.removeElement(jin.nextInt()));
			System.out.println(a.sum());
			System.out.println(a.removeElement(jin.nextInt()));
			System.out.println(a.sum());
			System.out.println(a.removeElement(jin.nextInt()));
			System.out.println(a.sum());
			System.out.println(a.contains(jin.nextInt()));
			System.out.println(a.contains(jin.nextInt()));
		}
		if ( test == 3 ) { //test insanely large arrays
			LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
			for ( int w = 0 ; w < 500 ; ++w ) {
				ResizableArray<Integer> a = new ResizableArray<Integer>();
				int k =  2000;
				int t =  1000;
				for ( int i = 0 ; i < k ; ++i ) {
					a.addElement(i);
				}
				
				a.removeElement(0);
				for ( int i = 0 ; i < t ; ++i ) {
					a.removeElement(k-i-1);
				}
				resizable_arrays.add(a);
			}
			System.out.println("You implementation finished in less then 3 seconds, well done!");
		}
	}
	
}

