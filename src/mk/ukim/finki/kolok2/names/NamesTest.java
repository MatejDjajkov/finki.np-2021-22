package mk.ukim.finki.kolok2.names;

import java.util.*;
import java.util.stream.Collectors;

class UniqueName
{
    private String name;
    private int occurances;

    public UniqueName(String name) {
        this.name = name;
        this.occurances=0;
    }
    public void incraseOccurances()
    {
        this.occurances++;
    }
    public int getNumberOfUniqueLetters()
    {
        Set<String> lettersInName=new TreeSet<>();
        String [] letters=name.split("");
        for (String letter : letters) {
            lettersInName.add(letter.toLowerCase());
        }
        return lettersInName.size();

    }

    public String getName() {
        return name;
    }

    public int getOccurances() {
        return occurances;
    }

    @Override
    public String toString() {
        return String.format("%s (%d) %d",name,occurances,getNumberOfUniqueLetters());
        //Abigail (10) 5
    }
}

class Names
{
    private Map<String,UniqueName> uniqueNameByName;
    private List<UniqueName> uniqueNameList;


    public Names() {
        uniqueNameByName=new TreeMap<>();
        uniqueNameList=new ArrayList<>();
    }

    public void addName(String name) {
        uniqueNameByName.putIfAbsent(name,new UniqueName(name));
        uniqueNameByName.get(name).incraseOccurances();

    }

    public void printN(int n) {
        uniqueNameByName.values()
                .stream()
                .filter(i->i.getOccurances()>=n)
                .forEach(System.out::println);
    }

    public String findName(int len, int index) {
        uniqueNameList=uniqueNameByName
                .values()
                .stream()
                .filter(i->i.getName().length()<len)
                .collect(Collectors.toList());
        return uniqueNameList.get(index%uniqueNameList.size()).getName();


    }
}
public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}