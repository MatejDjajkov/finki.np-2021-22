package mk.ukim.finki.kolok1.generic3;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

class Triple<T extends Number>
{
    List<T> tList;

    public Triple(T first, T second, T third) {

        tList=new ArrayList<>();
        tList.add(first);
        tList.add(second);
        tList.add(third);
    }

    public double max() {
        return tList.stream()
                .mapToDouble(Number::doubleValue)
                .max()
                .getAsDouble();

    }

    public double avarage() {
        return tList.stream()
                .mapToDouble(Number::doubleValue)
                .average()
                .getAsDouble();

    }


    public void sort() {
        tList.sort(Comparator.comparing(Number::doubleValue));

    }

    @Override
    public String toString() {
        return tList.stream()
                .map(i->String.format("%.2f",i.doubleValue()))
                .collect(Collectors.joining(" "));
    }
}

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.avarage());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.avarage());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.avarage());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
