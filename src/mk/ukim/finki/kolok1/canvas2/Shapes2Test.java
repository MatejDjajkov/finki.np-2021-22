package mk.ukim.finki.kolok1.canvas2;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum Type
{
    circle,square;
}
class IrregularCanvasException extends Exception
{
    public IrregularCanvasException(String id,double max) {
        super(String.format("Canvas %s has a shape with area larger than %.2f.",id,max));
    }
}
abstract class Shape
{
    private double length;

    public Shape(double length) {
        this.length = length;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
    public abstract Type getType();
    public abstract double getArea();
}
class Cirlce extends Shape
{
    public Cirlce(double length) {
        super(length);
    }
    public Type getType()
    {
        return Type.circle;
    }
    public double getArea()
    {
        return Math.pow(getLength(),2)*Math.PI;
    }

}
class Square extends Shape
{
    public Square(double length) {
        super(length);
    }
    public Type getType()
    {
        return Type.square;
    }
    public double getArea()
    {
        return Math.pow(getLength(),2);
    }

}
class Canvas implements Comparable<Canvas>
{
    private List<Shape> list;
    private String id;
    private double max;

    public Canvas(List<Shape> list, String id, double max) {
        this.list = list;
        this.id = id;
        this.max = max;
    }
    private int getCircleNumber()
    {
        return (int) list.stream()
                .filter(i->i.getType().equals(Type.circle))
                .count();
    }
    private int getSquareNumber()
    {
        return (int)list.stream()
                .filter(i->i.getType().equals(Type.square))
                .count();
    }
    private double getSquareArea()
    {
        return list.stream().filter(i->i.getType().equals(Type.square))
                .mapToDouble(i->i.getArea())
                .sum();
    }
    private double getCircleArea()
    {
        return list.stream().filter(i->i.getType().equals(Type.circle))
                .mapToDouble(i->i.getArea())
                .sum();
    }
    private double minArea()
    {
        double max=10000;
        for(Shape shape:list)
        {
            if(max>shape.getArea())
            {
                max=shape.getArea();
            }
        }
        return max;
    }
    private double maxArea()
    {
        double max=0;
        for(Shape shape:list)
        {
            if(max<shape.getArea())
            {
                max=shape.getArea();
            }
        }
        return max;
    }
    private double getAverage()
    {
        return (getCircleArea()+getSquareArea())/list.size();
    }

    public static Canvas createCanvas(String s, double max) throws IrregularCanvasException
    {
        String [] temp=s.split("\\s");
        List<Shape> shapeList=new ArrayList<>();
        for(int i=2;i<temp.length;i+=2)
        {
            if(temp[i-1].equals("C"))
            {
                shapeList.add(new Cirlce(Double.parseDouble(temp[i])));

            }
            else
            {
                shapeList.add(new Square(Double.parseDouble(temp[i])));
            }
        }
        for(Shape shape :shapeList)
        {
            if(shape.getArea()>max)
            {
                throw new IrregularCanvasException(temp[0],max);
            }
        }
        return new Canvas(shapeList,temp[0],max);
    }

    @Override
    public String toString() {
        //ID total_shapes total_circles total_squares min_area max_area average_area
        return String.format("%s %d %d %d %.2f %.2f %.2f",
                id,getCircleNumber()+getSquareNumber(),getCircleNumber(),getSquareNumber()
        ,minArea(),maxArea(),getAverage());
    }

    @Override
    public int compareTo(Canvas other) {
        return Double.compare(other.getCircleArea()+other.getSquareArea(),this.getCircleArea()+this.getSquareArea());
    }
}
class ShapesApplication
{
    private List<Canvas> elements;
    private double max;

    public ShapesApplication(int max)
    {
        elements=new LinkedList<>();
        this.max=max;
    }
    public void readCanvases(InputStream inputStream)
    {
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
        elements=bufferedReader
                .lines()
                .map(line->
                {
                    try
                    {
                        return Canvas.createCanvas(line,max);
                    }catch (IrregularCanvasException e)
                    {
                        System.out.println(e.getMessage());
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());

    }
    public void printCanvases(OutputStream outputStream)
    {
        PrintWriter printWriter=new PrintWriter(outputStream);
        elements.stream().sorted().forEach(i->printWriter.println(i));
        printWriter.flush();
    }

}

public class Shapes2Test {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}