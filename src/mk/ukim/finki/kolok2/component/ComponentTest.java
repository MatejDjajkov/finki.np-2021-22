package mk.ukim.finki.kolok2.component;
import java.util.*;
import java.util.Scanner;
class InvalidPositionException extends Exception
{
    public InvalidPositionException(String messege) {
        super(messege);
    }
}
class Component
{
    String color;
    int weight;
    Set<Component> componentSet;
    int level;
    Comparator<Component> componentComparator=Comparator.comparing(Component::getWeight)
            .thenComparing(Component::getColor);

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        componentSet=new TreeSet<>(componentComparator);
        this.level=0;
    }

    public String getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }
    public void ChangeColor(String color,int weight)
    {
        if(this.weight<weight)
        {
            this.color=color;
        }
        componentSet.stream()
                .forEach(i->i.ChangeColor(color,weight));
    }

    public void addComponent(Component component) {
        component.setLevel(level+1);
        componentSet.add(component);
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(String.format("%d:%s\n",weight,color));
        componentSet.forEach(i->{
            for(int j=1;j<=i.level;j++)
            {
                stringBuilder.append("---");
            }
            stringBuilder.append(i);
        });
        return stringBuilder.toString();
        //StringBuilder sb = new StringBuilder();
        //sb.append(weight);
        //sb.append(:"");
        //sb.append(colour);
        //sb.append("\n");

//
//
        //internal.forEach(e -> {for(int i=0; i<=level; i++){sb.append("---");} sb.append(e);});
        ////System.out.println("DEBUG: "+internal.size());
        //return sb.toString();
    }
}
class Window
{
    String name;
    Map<Integer,Component> componentByPlace;

    public Window(String name)
    {
        this.name = name;
        componentByPlace=new TreeMap<>();
    }

    public void addComponent(int position, Component prev) throws InvalidPositionException
    {
        if(componentByPlace.containsKey(position))
        {
            throw new InvalidPositionException(String.format("Invalid position %d, alredy taken!",position));
        }
        componentByPlace.put(position,prev);
    }

    public void changeColor(int weight, String color) {
        componentByPlace.values()
                .stream()
                .forEach(i->i.ChangeColor(color,weight));

    }

    public void swichComponents(int pos1, int pos2) {
        Component temp=componentByPlace.get(pos1);
        componentByPlace.put(pos1,componentByPlace.get(pos2));
        componentByPlace.put(pos2,temp);
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(String.format("WINDOW %s\n",name));
        final int[] count = {1};
        componentByPlace.values()
                .stream()
                .forEach(i->stringBuilder.append(String.format("%d:%s", count[0]++,i.toString())));

        return stringBuilder.toString();
    }
}
public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}