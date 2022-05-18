package mk.ukim.finki.kolok1.dropka;
import java.util.*;
class ZeroDenominatorException extends Exception
{
    public ZeroDenominatorException(String msg) {
        super(msg);

    }
}
class GenericFraction<T extends Number,U extends Number>
{
    T numerator;
    U denominator;

    public GenericFraction(T numerator, U denominator) throws ZeroDenominatorException{
        if(denominator.doubleValue()==0.0)
        {
            throw new ZeroDenominatorException("Denominator cannot be zero");
        }
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public double toDouble() {
        return numerator.doubleValue()/denominator.doubleValue();
    }

    public GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException {
        double resultdenominator=this.denominator.doubleValue()*gf.denominator.doubleValue();
        double resultnumerator=this.numerator.doubleValue()*gf.denominator.doubleValue()+this.denominator.doubleValue()*gf.numerator.doubleValue();


        return new GenericFraction<>(resultnumerator,resultdenominator);
    }

    @Override
    public String toString() {
        double devider=gfd(numerator.doubleValue(),denominator.doubleValue());
        return String.format("%.2f / %.2f",numerator.doubleValue()/devider,denominator.doubleValue()/devider);
        //19.00 / 35.00
    }
    private double gfd(double n1,double n2)
    {
        double max=1;
        for(int i=1;i<n2;i++)
        {
            if(n1%i==0&&n2%i==0)
            {
                max=(double)i;
            }
        }
        return max;
    }
}
public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}

