/*package mk.ukim.finki.lab2;*/
import java.text.DecimalFormat;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;
import java.io.InputStream;

class InsufficientElementsException extends Exception
{
    public InsufficientElementsException()
    {
        super("Insufficient number of elements");
    }
}
class InvalidColumnNumberException extends Exception
{
    public InvalidColumnNumberException()
    {
        super("Invalid column number");
    }
}
class InvalidRowNumberException extends Exception
{
    public InvalidRowNumberException()
    {
        super("Invalid row number");
    }
}
final class DoubleMatrix
{
    private double [][] matrix;
    private int m;
    private int n;

    public DoubleMatrix(double[] array , int m, int n) throws InsufficientElementsException {

        this.m = m;
        this.n = n;
        if(m*n>array.length)
        {
            throw new InsufficientElementsException();
        }
        int k=0;
        this.matrix=new double[m][n];
        if(m*n==array.length)
        {
            for(int i=0;i<m;i++)
            {
                for(int j=0;j<n;j++)
                {
                    matrix[i][j]=array[k++];
                }
            }
        }
        else
        {
            k=array.length-m*n;
            for(int i=0;i<m;i++)
            {
                for(int j=0;j<n;j++)
                {
                    matrix[i][j]=array[k++];
                }
            }

        }

    }
    public String getDimensions()
    {
        return String.format("[%d x %d]",m,n);
    }
    public int rows()
    {
        return m;
    }
    public int columns()
    {
        return n;
    }
    //Proveri dali moze ako stavam tuka m,
    public double maxElementAtRow(int row) throws InvalidRowNumberException
    {
        if(row>m||row<1)
        {
            throw new InvalidRowNumberException();
        }
        double max=-20;
        for(int j=0;j<n;j++)
        {
            if(max<matrix[row-1][j])
            {
                max=matrix[row-1][j];
            }
        }
        return max;
    }
    public double maxElementAtColumn(int column) throws InvalidColumnNumberException
    {
        if(column>n||column<1)
        {
            throw new InvalidColumnNumberException();
        }

        double max=-20;
        for(int i=0;i<m;i++)
        {
            if(max<matrix[i][column-1])
            {
                max=matrix[i][column-1];
            }
        }
        return max;
    }
    public double sum()
    {
        double summary=0;
        for(int i=0;i<m;i++)
        {
            for(int j=0;j<n;j++)
            {
                summary+=matrix[i][j];
            }
        }
        return summary;
    }

    public double[] toSortedArray()
    {
        double[]result=new double[this.m*this.n];
        double[][]temp=Arrays.copyOf(this.matrix, this.m*this.n);

        for(int i=0; i<this.m; i++)
        {
            for(int j=0; j<this.n; j++)
            {
                result[i*n+j]=temp[i][j];
                //System.out.print(result[i*m+j]);System.out.print("/");System.out.print(temp[i][j]);System.out.print("|");
            }
            //System.out.println();
        }

        Arrays.sort(result);

        for(int i=0; i<this.m*this.n; i++)
        {
            /*
            System.out.print(result[i]);System.out.print("|");
            if(i%n==n-1)
                System.out.println();*/
        }

        for(int i=0; i<n*m/2; i++)
        {
            double swap;
            swap=result[i];
            result[i]=result[n*m-i-1];
            result[n*m-i-1]=swap;
        }


        return result;
    }

    @Override
    public String toString() {
        StringBuilder string=new StringBuilder();
        for(int i=0;i<m;i++)
        {
            for(int j=0;j<n;j++)
            {
                string.append(String.format("%.2f",matrix[i][j]));
                if(j!=n-1)
                {
                    string.append("\t");
                }
            }
            if(i!=m-1)
            {
                string.append("\n");
            }

        }
        return string.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DoubleMatrix other = (DoubleMatrix) obj;
        if (!Arrays.deepEquals(matrix, other.matrix))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(matrix);
        return result;
    }
}
class MatrixReader
{
    public static DoubleMatrix read(InputStream input)
            throws InsufficientElementsException
    {
        Scanner scanner=new Scanner(input);
        int m, n;
        m=scanner.nextInt();
        n=scanner.nextInt();

        double[]temp=new double[m*n];
        for(int i=0; i<m; i++)
        {
            for(int j=0; j<n; j++)
            {
                temp[i*n+j]=scanner.nextDouble();
            }
        }

        DoubleMatrix result=new DoubleMatrix(temp, m, n);

        return result;
    }
}
public class DoubleMatrixTester {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        DoubleMatrix fm = null;

        double[] info = null;

        DecimalFormat format = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            String operation = scanner.next();

            switch (operation) {
                case "READ": {
                    int N = scanner.nextInt();
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    double[] f = new double[N];

                    for (int i = 0; i < f.length; i++)
                        f[i] = scanner.nextDouble();

                    try {
                        fm = new DoubleMatrix(f, R, C);
                        info = Arrays.copyOf(f, f.length);

                    } catch (InsufficientElementsException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }

                    break;
                }

                case "INPUT_TEST": {
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    StringBuilder sb = new StringBuilder();

                    sb.append(R + " " + C + "\n");

                    scanner.nextLine();

                    for (int i = 0; i < R; i++)
                        sb.append(scanner.nextLine() + "\n");

                    fm = MatrixReader.read(new ByteArrayInputStream(sb
                            .toString().getBytes()));

                    info = new double[R * C];
                    Scanner tempScanner = new Scanner(new ByteArrayInputStream(sb
                            .toString().getBytes()));
                    tempScanner.nextDouble();
                    tempScanner.nextDouble();
                    for (int z = 0; z < R * C; z++) {
                        info[z] = tempScanner.nextDouble();
                    }

                    tempScanner.close();

                    break;
                }

                case "PRINT": {
                    System.out.println(fm.toString());
                    break;
                }

                case "DIMENSION": {
                    System.out.println("Dimensions: " + fm.getDimensions());
                    break;
                }

                case "COUNT_ROWS": {
                    System.out.println("Rows: " + fm.rows());
                    break;
                }

                case "COUNT_COLUMNS": {
                    System.out.println("Columns: " + fm.columns());
                    break;
                }

                case "MAX_IN_ROW": {
                    int row = scanner.nextInt();
                    try {
                        System.out.println("Max in row: "
                                + format.format(fm.maxElementAtRow(row)));
                    } catch (InvalidRowNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "MAX_IN_COLUMN": {
                    int col = scanner.nextInt();
                    try {
                        System.out.println("Max in column: "
                                + format.format(fm.maxElementAtColumn(col)));
                    } catch (InvalidColumnNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "SUM": {
                    System.out.println("Sum: " + format.format(fm.sum()));
                    break;
                }

                case "CHECK_EQUALS": {
                    int val = scanner.nextInt();

                    int maxOps = val % 7;

                    for (int z = 0; z < maxOps; z++) {
                        double work[] = Arrays.copyOf(info, info.length);

                        int e1 = (31 * z + 7 * val + 3 * maxOps) % info.length;
                        int e2 = (17 * z + 3 * val + 7 * maxOps) % info.length;

                        if (e1 > e2) {
                            double temp = work[e1];
                            work[e1] = work[e2];
                            work[e2] = temp;
                        }

                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(work, fm.rows(),
                                fm.columns());
                        System.out
                                .println("Equals check 1: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode()&&f1
                                        .equals(f2)));
                    }

                    if (maxOps % 2 == 0) {
                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(new double[]{3.0, 5.0,
                                7.5}, 1, 1);

                        System.out
                                .println("Equals check 2: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode()&&f1
                                        .equals(f2)));
                    }

                    break;
                }

                case "SORTED_ARRAY": {
                    double[] arr = fm.toSortedArray();

                    String arrayString = "[";

                    if (arr.length > 0)
                        arrayString += format.format(arr[0]) + "";

                    for (int i = 1; i < arr.length; i++)
                        arrayString += ", " + format.format(arr[i]);

                    arrayString += "]";

                    System.out.println("Sorted array: " + arrayString);
                    break;
                }

            }

        }

        scanner.close();
    }
}
