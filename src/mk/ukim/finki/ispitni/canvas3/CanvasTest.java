package mk.ukim.finki.ispitni.canvas3;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
class InvalidIDException extends Exception
{
    public InvalidIDException(String messega) {
        super(messega);
    }
}
class InvalidDimensionException extends Exception
{
    public InvalidDimensionException() {
        super("Dimension 0 is not allowed!");
    }
}
abstract class Shape
{
    String id;
    double side;

    public String getId() {
        return id;
    }

    public Shape(String id, double side) {
        this.id=id;
        this.side = side;
    }
    public abstract double getArea();
    public abstract void scale(double coef);
    public abstract String toString();
    public abstract double getParametar();
}
class Square extends Shape
{
    public Square(String id,double side) {
        super(id,side);
    }

    @Override
    public double getArea() {
        return Math.pow(side,2);
    }

    @Override
    public void scale(double coef) {
        this.side=side*coef;

    }

    @Override
    public String toString() {
        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f",side,getArea(),side*4.0);
    }

    @Override
    public double getParametar() {
        return this.side*4.0;
    }
}
class Circle extends Shape
{
    public Circle(String id,double side) {
        super(id,side);
    }

    @Override
    public double getArea() {
        return Math.pow(side,2)*Math.PI;
    }

    @Override
    public void scale(double coef) {
        this.side=side*coef;

    }

    @Override
    public String toString() {
        return String.format("Circle -> Radius: %.2f Area: %.2f Perimeter: %.2f",side,getArea(),side*2.0*Math.PI);
    }

    @Override
    public double getParametar() {
        return side*2.0*Math.PI;
    }


}
class Rectangle extends Shape
{
    double height;

    public Rectangle(String id,double side, double height) {
        super(id,side);
        this.height = height;
    }


    @Override
    public double getArea() {
        return this.height*this.side;
    }

    @Override
    public void scale(double coef) {
        this.side=this.side*coef;
        this.height=this.height*coef;
    }

    @Override
    public String toString() {
        return String.format("Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f",side,height,getArea(),2.0*(side+height));
    }

    @Override
    public double getParametar() {
        return 2.0*(this.side+this.height);
    }
}
class Canvas
{
    Set<Shape> shapesById;
    Comparator<Shape> shapeComparator=Comparator.comparing(Shape::getArea);

    public Canvas() {
        this.shapesById=new TreeSet<>(shapeComparator);
    }

    public void readShapes(InputStream in) throws InvalidIDException,InvalidDimensionException{
        BufferedReader bf=new BufferedReader(new InputStreamReader(in));
                bf.lines()
                        .forEach(i ->
                        {
                            try {
                                String[] parts = i.split("\\s");
                                if (parts[1].length() != 6 || parts[1].matches(".*\\W.*")) {
                                    throw new InvalidIDException(String.format("ID %s is not valid", parts[1]));
                                }
                                if (parts[0].equals("1")) {

                                    shapesById.add(new Circle(parts[1], Double.parseDouble(parts[2])));
                                }
                                if (parts[0].equals("2")) {

                                    shapesById.add(new Square(parts[1], Double.parseDouble(parts[2])));
                                }
                                if (parts[0].equals("3")) {
                                    shapesById.add(new Rectangle(parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3])));
                                }
                            } catch (InvalidIDException invalidIDException) {
                                System.out.println(invalidIDException.getMessage());
                            }

                        });

    }

    void scaleShapes (String userID, double coef)
    {
        shapesById
        .stream()
                .filter(i->i.getId().equals(userID))
        .forEach(i->i.scale(coef));
    }
    void printAllShapes (OutputStream os)
    {

        PrintWriter pw=new PrintWriter(os);
        shapesById.stream()
                .forEach(i->pw.println(i));
       pw.flush();

    }
    void printByUserId (OutputStream os)
    {
        PrintWriter pw=new PrintWriter(os);
        Comparator <Set<Shape>> comparator=((o1, o2) ->
        {
            int value=Integer.compare(o2.size(),o1.size());
            if(value==0)
            {
                return Double.compare(o1.stream().mapToDouble(i->i.getArea()).sum(),
                        o2.stream().mapToDouble(i->i.getArea()).sum());
            }
            return value;
        });
        Map<String,Set<Shape>> shapes=shapesById.stream()
                .collect(Collectors.groupingBy(i->i.getId(),
                        ()->new  TreeMap<>(),
                        Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Shape::getParametar)))));
        shapes.values()
                .stream()
                .sorted(comparator)
                .forEach(i->
                {
                    List<Shape> list=shapesById.stream().collect(Collectors.toList());

                    pw.printf("Shapes of user: %s\n",list.get(0).getId());
                    i.stream().forEach(j->pw.println(j));
                });
        pw.flush();

    }
    void statistics (OutputStream os)
    {
        DoubleSummaryStatistics dbs=shapesById
                .stream()
                .mapToDouble(Shape::getArea)
                .summaryStatistics();
        PrintWriter pw=new PrintWriter(os);
        pw.printf("count: %d\nsum: %.2f\n" +
                "min: %.2f\n" +
                "average: %.2f\n" +
                "max: %.2f",dbs.getCount(),dbs.getSum(),dbs.getMin(),dbs.getAverage(),dbs.getMax());
        pw.flush();
        //count: 5
        //sum: 852.06
        //min: 51.86
        //average: 170.41
        //max: 306.99
    }
}
public class CanvasTest {

    public static void main(String[] args) throws InvalidIDException{
        Canvas canvas = new Canvas();

        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
        try {
            canvas.readShapes(System.in);
        } catch (InvalidDimensionException invalidDimensionException) {
            System.out.println(invalidDimensionException.getMessage());
        }
        System.out.println("BEFORE SCALING");
        canvas.printAllShapes(System.out);
        canvas.scaleShapes("123456", 1.5);
        System.out.println("AFTER SCALING");
        canvas.printAllShapes(System.out);

        System.out.println("PRINT BY USER ID TESTING");
        canvas.printByUserId(System.out);

        System.out.println("PRINT STATISTICS");
        canvas.statistics(System.out);
    }
}
