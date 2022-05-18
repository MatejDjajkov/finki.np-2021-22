package mk.ukim.finki.kolok1.canvas1;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class Canvas implements Comparable<Canvas>
{
    private String id;
    private List<Integer> elements;
    private static int count=0;
    public Canvas(String id,List<Integer> elements)
    {
        this.id=id;
        this.elements=elements;
        count+=elements.size();
    }

    public static int getCount() {
        return count;
    }

    private int max()
    {
        return elements.stream()
                .mapToInt(i->i)
                .sum();
    }
    public static Canvas createCanvas(String string)
    {
        String[] temp=string.split("\\s");
        List<Integer> list=new LinkedList<>();
        for(int i=1;i<temp.length;i++)
        {
            list.add(Integer.parseInt(temp[i]));
        }
        return new Canvas(temp[0],list);
    }

    @Override
    public String toString() {
        return String.format("%s %d %d", id, elements.size(), max() * 4);
    }

    @Override
    public int compareTo(Canvas o) {
        if(this.max()>o.max())
        {
            return 1;
        }
        else if(this.max()<o.max())
        {
            return -1;
        }
        return 0;
    }
}
class ShapesApplication
{
    private List<Canvas> elements;
    public ShapesApplication()
    {
        elements= new LinkedList<>();
    }

    public int readCanvases(InputStream inputStream)
    {
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
        elements=bufferedReader.lines()
                .map(i->Canvas.createCanvas(i))
                .collect(Collectors.toList());
        return Canvas.getCount();

    }
    public void printLargestCanvasTo(OutputStream outputStream)
    {
        PrintWriter printWriter = new PrintWriter(outputStream);
        List<Canvas> sorted;
        sorted=elements.stream()
                .sorted()
                .collect(Collectors.toList());
        System.out.println(sorted.get(sorted.size()-1));
        printWriter.flush();
    }
}
public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
