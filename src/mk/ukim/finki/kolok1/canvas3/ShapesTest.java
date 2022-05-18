package mk.ukim.finki.kolok1.canvas3;
import java.util.*;

enum Color {
    RED, GREEN, BLUE
}
enum Type
{
    Circle,Square
}

abstract class Shape
{
    private String id;
    private Color color;
    private Type type;

    public Shape(String id, Color color, Type type) {
        this.id = id;
        this.color = color;
        this.type = type;
    }
    public abstract Type getType();

    public abstract void scale(float scaleFactor);
    public abstract float weight();

    public String getId()
    {
        return this.id;
    }
    public Color getColor()
    {
        return this.color;
    }


}
class Circle extends Shape
{
    private float radius;

    public Circle(String id, Color color, Type type, float radius) {
        super(id, color, type);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        this.radius=radius*scaleFactor;

    }

    @Override
    public float weight() {

        return (float) (Math.pow(radius,2)*Math.PI);
    }

    @Override
    public Type getType() {
        return Type.Circle;
    }

    @Override
    public String toString() {
        //C: [id:5 места од лево] [color:10 места од десно] [weight:10.2 места од десно]
        return String.format("C: %-5s%-10s%10.2f",getId(),getColor(),weight());
    }

}
class Square extends Shape
{
    private float height;
    private float width;

    public Square(String id, Color color, Type type, float height, float width) {
        super(id, color, type);
        this.height = height;
        this.width = width;
    }

    @Override
    public void scale(float scaleFactor) {
        this.width=this.width*scaleFactor;
        this.height=this.height*scaleFactor;
    }

    @Override
    public float weight() {

        return height*width;
    }

    @Override
    public Type getType() {
        return Type.Square;
    }

    @Override
    public String toString() {
        //R: r1   BLUE           28.00
        return String.format("R: %-5s%-10s%10.2f",getId(),getColor(),weight());
    }

}

class Canvas {
    private List<Shape> shapeList;

    public Canvas() {
        this.shapeList = new ArrayList<>();
    }
    public void add(String id, Color color, float radius)
    {
        Circle circle=new Circle(id,color,Type.Circle,radius);
        if (shapeList.size() != 0) {
            for (Shape shape : shapeList) {
                if (shape.weight() < circle.weight()) {
                    shapeList.add(shapeList.indexOf(shape), circle);
                    return;
                }

            }
        }
        shapeList.add(circle);

    }


    public void add(String id, Color color, float width, float height) {
        Square square= new Square(id,color,Type.Square,height,width);
        if (shapeList.size() != 0) {
            for (Shape shape : shapeList) {
                if (shape.weight() < square.weight()) {
                    shapeList.add(shapeList.indexOf(shape), square);
                    return;
                }
            }
        }
        shapeList.add(square);
    }

    public void scale(String id, float scaleFactor) {

        for(Shape shape :shapeList)
        {
            if(shape.getId().equals(id))
            {

                shape.scale(scaleFactor);
                Shape shape1=shape;
                shapeList.remove(shape);
                for(Shape shape2:shapeList)
                {
                    if(shape2.weight()<shape1.weight())
                    {
                        shapeList.add(shapeList.indexOf(shape2),shape1);
                        return;
                    }
                }
                shapeList.add(shape1);
                break;
            }
        }



    }


    @Override
    public String toString() {
        for(Shape shape:shapeList)
        {
            System.out.println(shape);
        }
        return"";
    }
}
public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}
