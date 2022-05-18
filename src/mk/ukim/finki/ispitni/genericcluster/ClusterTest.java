package mk.ukim.finki.ispitni.genericcluster;

import java.util.*;
import java.util.stream.Collectors;

class Point2D implements Comparable<Point2D>
{
    long id;
    float x;
    float y;
    float distance;

    public Point2D(long id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.distance=0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public float getDistance() {
        return distance;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public int compareTo(Point2D o) {
        return Float.compare(this.distance,o.distance);
    }

    @Override
    public String toString() {
        return String.format(" %d -> %.3f",id,distance);
        // 103 -> 3.046
    }
    public void setDistance(float x1, float x2, float y1, float y2)
    {
        this.distance=(float)Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
    }


}
class Cluster<T extends Point2D & Comparable<Point2D> >
{
    Map<Long,T> pointByID;

    public Cluster() {
        pointByID=new TreeMap<>();
    }

    public void addItem(T point2D) {
        pointByID.put(point2D.id,point2D);
    }

    public void near(int id, int top) {
        T element=pointByID.get((long) id);
        pointByID.values()
                .stream()
                .forEach(i->i.setDistance(element.x,i.x,element.y,i.y));
        final int[] count = {1};
        pointByID.values()
                .stream()
                .sorted()
                .skip(1)
                .limit(top)
                .map(i->String.format("%d.%s", count[0]++,i.toString()))
                .forEach(System.out::println);



    }

}

/**
 * January 2016 Exam problem 2
 */
public class ClusterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}

// your code here