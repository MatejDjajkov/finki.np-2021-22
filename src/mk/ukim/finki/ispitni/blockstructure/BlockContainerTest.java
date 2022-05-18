package mk.ukim.finki.ispitni.blockstructure;
import java.util.*;
import java.util.stream.Collectors;

class BlockContainer<T extends Comparable<T>>
{
    Map<Integer,Set<T>> elementsByBlock;
    int size;

    public BlockContainer(int size) {
        elementsByBlock=new TreeMap<>();
        elementsByBlock.put(1,new TreeSet<>());
        this.size=size;
    }


    public void add(T element) {

            int lastblock=elementsByBlock.size();
            if(elementsByBlock.get(lastblock).size()==size)
            {
                elementsByBlock.put(lastblock+1,new TreeSet<>());
                lastblock++;
            }
            elementsByBlock.get(lastblock).add(element);


    }

    public void remove(T lastInteger) {
        int lastblock=elementsByBlock.size();
        elementsByBlock.get(lastblock).remove(lastInteger);
        if(elementsByBlock.get(lastblock).size()==0)
        {
            elementsByBlock.remove(lastblock);
        }
    }

    public void sort() {
        List<T> list=elementsByBlock.values()
                .stream()
                .flatMap(Collection::stream)
                .sorted()
                .collect(Collectors.toList());
        elementsByBlock=new TreeMap<>();
        elementsByBlock.put(1,new TreeSet<>());
        list.stream()
                .forEach(i->add(i));

    }

    @Override
    public String toString() {


        return elementsByBlock.keySet()
                .stream()
                .map(i->
                {
                    StringBuilder stringBuilder=new StringBuilder();
                    stringBuilder.append("[");
                    Set<T> set=elementsByBlock.get(i);
                    String st=set.stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(", "));
                    stringBuilder.append(st);
                    stringBuilder.append("]");
                    return stringBuilder.toString();
                })
                .collect(Collectors.joining(","));
    }
}

public class BlockContainerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int size = scanner.nextInt();
        BlockContainer<Integer> integerBC = new BlockContainer<Integer>(size);
        scanner.nextLine();
        Integer lastInteger = null;
        for(int i = 0; i < n; ++i) {
            int element = scanner.nextInt();
            lastInteger = element;
            integerBC.add(element);
        }
        System.out.println("+++++ Integer Block Container +++++");
        System.out.println(integerBC);
        System.out.println("+++++ Removing element +++++");
        integerBC.remove(lastInteger);
        System.out.println("+++++ Sorting container +++++");
        integerBC.sort();
        System.out.println(integerBC);
        BlockContainer<String> stringBC = new BlockContainer<String>(size);
        String lastString = null;
        for(int i = 0; i < n; ++i) {
            String element = scanner.next();
            lastString = element;
            stringBC.add(element);
        }
        System.out.println("+++++ String Block Container +++++");
        System.out.println(stringBC);
        System.out.println("+++++ Removing element +++++");
        stringBC.remove(lastString);
        System.out.println("+++++ Sorting container +++++");
        stringBC.sort();
        System.out.println(stringBC);
    }
}
