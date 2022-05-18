package mk.ukim.finki.kolok1.minmax;
import java.util.*;
import java.util.stream.Collectors;

class MinMax<T extends Comparable<T>> {
    private T max;
    private T min;
    private int processedElements;
    private int maxCount;
    private int minCount;

    public MinMax() {
        max = null;
        min = null;
        processedElements = 0;
        maxCount = 0;
        minCount = 0;
    }

    public T max() {
        return max;
    }

    public T min() {
        return min;
    }

    public void update(T element) {
        if (processedElements == 0)
            max = min = element;
        if (max.compareTo(element) == 0)
            ++maxCount;
        if (min.compareTo(element) == 0)
            ++minCount;
        if (max.compareTo(element) < 0) {
            max = element;
            maxCount = 1;
        }
        if (min.compareTo(element) > 0) {
            min = element;
            minCount = 1;
        }
        ++processedElements;
    }

    @Override
    public String toString() {
        return min.toString() + " " + max.toString() + " " + (processedElements - (maxCount + minCount)) + "\n";
    }
}
public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}
