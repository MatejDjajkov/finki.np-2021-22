package mk.ukim.finki.ispitni.studentskodosie;

import java.io.*;
import java.util.*;

/**
 * January 2016 Exam problem 1
 */
class Student
{
    String index;
    String nasoka;
    List<Integer> ocenkiList;

    public Student(String index, String nasoka, List<Integer> ocenkiList) {
        this.index = index;
        this.nasoka = nasoka;
        this.ocenkiList = ocenkiList;
    }

    public List<Integer> getOcenkiList() {
        return ocenkiList;
    }

    public String getIndex() {
        return index;
    }

    public String getNasoka() {
        return nasoka;
    }
    public double GetAverage()
    {
        return ocenkiList.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    @Override
    public String toString() {

        return String.format("%s %.2f",index,GetAverage());
    }
}
class Course implements Comparable<Course>
{
    private String name;
    private List<List<Integer>> grades;

    public Course(String name) {
        this.name = name;
        this.grades=new ArrayList<>();
    }
    public void addGrades(List<Integer> integerList)
    {
        grades.add(integerList);
    }

    @Override
    public int compareTo(Course o) {
        return Integer.compare(o.getNumberOf10s(),this.getNumberOf10s());
    }

    private int getNumber(int m)
    {
        return (int)grades.stream()
                .flatMap(Collection::stream)
                .filter(i->i==m)
                .count();
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(name+'\n');
        for(int i=6;i<=10;i++)
        {
            sb.append(String.format("%2d | ",i));
            int count=getNumber(i);
            for(int j=0;j<=count/10;j++)
            {
                sb.append("*");
            }
            if(count%10==0)
            {
                sb.deleteCharAt(sb.length()-1);
            }
            sb.append(String.format("(%d)\n",count));
        }
        return sb.toString();
    }
    public int getNumberOf10s()
    {
        return getNumber(10);
    }
}
class StudentRecords
{
    Map<String,List<Student>> studentByNasoka;
    Map<String,Course> courseByName;
    public StudentRecords() {
        studentByNasoka =new TreeMap<>();
        courseByName =new TreeMap<>();
    }

    public int readRecords(InputStream in) {
        BufferedReader bf=new BufferedReader(new InputStreamReader(in));
        bf.lines()
                .forEach(i->
                {
                    String [] parts=i.split("\\s");
                    List<Integer> ocenkilist=new ArrayList<>();
                    for(int j=2;j< parts.length;j++)
                    {
                        ocenkilist.add(Integer.parseInt(parts[j]));
                    }
                    studentByNasoka.putIfAbsent(parts[1],new ArrayList<>());
                    studentByNasoka.get(parts[1]).add(new Student(parts[0],parts[1],ocenkilist));
                    courseByName.putIfAbsent(parts[1],new Course(parts[1]));
                    courseByName.get(parts[1]).addGrades(ocenkilist);


                });
        return (int) studentByNasoka.values()
                .stream()
                .flatMap(Collection::stream)
                .count();

    }

    public void writeTable(PrintStream out) {
        Comparator<Student> studentComparator=Comparator.comparing(Student::GetAverage)
                .reversed()
                .thenComparing(Student::getIndex);
        studentByNasoka.keySet()
                .forEach(i->
                {
                    System.out.println(i);
                    studentByNasoka.get(i).stream()
                            .sorted(studentComparator)
                            .forEach(System.out::println);
                });
    }

    public void writeDistribution(PrintStream out) {
        courseByName.values()
                .stream()
                .sorted()
                .forEach(System.out::print);
    }
}
public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}

// your code here
