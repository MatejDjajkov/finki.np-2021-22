package mk.ukim.finki.lab5.complexnumbers;
import java.text.DecimalFormat;
import java.util.*;



class ComplexNumber<T extends Number,U extends Number> implements Comparable<ComplexNumber<?,?>>
{

    private T real;
    private U imagionary;

    public ComplexNumber(T real, U imagionary) {
        this.real=real;
        this.imagionary=imagionary;
    }

    public T getReal() {
        return real;
    }
    public U getImaginary()
    {
        return imagionary;
    }

    public double modul() {
        return Math.sqrt(Math.pow(real.doubleValue(),2)+Math.pow(imagionary.doubleValue(),2));
    }

    @Override
    public String toString() {
        DecimalFormat formatter=new DecimalFormat("#0.00");
        StringBuilder sb=new StringBuilder();
        sb.append(formatter.format(real));

        if(imagionary.doubleValue()>=0.0)
            sb.append("+");

        sb.append(formatter.format(imagionary));
        sb.append("i");

        return sb.toString();
    }

    @Override
    public int compareTo(ComplexNumber<?, ?> other) {

        if(this.modul()==other.modul())
        {
            return 0;
        }
        else if(this.modul()>other.modul())
        {
            return 1;
        }
        return -1;
    }

}

public class ComplexNumberTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) { //test simple functions int
            int r = jin.nextInt();int i = jin.nextInt();
            ComplexNumber<Integer, Integer> c = new ComplexNumber<Integer, Integer>(r, i);
            System.out.println(c);
            System.out.println(c.getReal());
            System.out.println(c.getImaginary());
            System.out.println(c.modul());
        }
        if ( k == 1 ) { //test simple functions float
            float r = jin.nextFloat();
            float i = jin.nextFloat();
            ComplexNumber<Float, Float> c = new ComplexNumber<Float, Float>(r, i);
            System.out.println(c);
            System.out.println(c.getReal());
            System.out.println(c.getImaginary());
            System.out.println(c.modul());
        }
        if ( k == 2 ) { //compareTo int
            LinkedList<ComplexNumber<Integer,Integer>> complex = new LinkedList<ComplexNumber<Integer,Integer>>();
            while ( jin.hasNextInt() ) {
                int r = jin.nextInt(); int i = jin.nextInt();
                complex.add(new ComplexNumber<Integer, Integer>(r, i));
            }
            System.out.println(complex);
            Collections.sort(complex);
            System.out.println(complex);
        }
        if ( k == 3 ) { //compareTo double
            LinkedList<ComplexNumber<Double,Double>> complex = new LinkedList<ComplexNumber<Double,Double>>();
            while ( jin.hasNextDouble() ) {
                double r = jin.nextDouble(); double i = jin.nextDouble();
                complex.add(new ComplexNumber<Double, Double>(r, i));
            }
            System.out.println(complex);
            Collections.sort(complex);
            System.out.println(complex);
        }
        if ( k == 4 ) { //compareTo mixed
            LinkedList<ComplexNumber<Double,Integer>> complex = new LinkedList<ComplexNumber<Double,Integer>>();
            while ( jin.hasNextDouble() ) {
                double r = jin.nextDouble(); int i = jin.nextInt();
                complex.add(new ComplexNumber<Double, Integer>(r, i));
            }
            System.out.println(complex);
            Collections.sort(complex);
            System.out.println(complex);
        }
    }
}