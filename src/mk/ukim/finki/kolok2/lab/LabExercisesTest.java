package mk.ukim.finki.kolok2.lab;

//package mk.ukim.finki.kolok2.laborotoriski;
import java.util.*;
import java.util.stream.Collectors;

class Student
{
    private String index;
    private List<Integer> points;

    public Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
    }

    public String getIndex() {
        return index;
    }

    public List<Integer> getPoints() {
        return points;
    }
    public boolean hasSigniture()
    {
        return points.size()>=8;
    }
    public double getLabPoints()
    {
        return points.stream()
                .mapToInt(i->i)
                .sum()/10.0;
    }

    @Override
    public String toString() {
        return String.format("%s %s %.2f",index,hasSigniture()?"YES":"NO",getLabPoints());
        //135042 NO 0.00
    }
    public int getYear()
    {
        return 21-Integer.parseInt(index.substring(0,2));
    }
}
class LabExercises
{
    private List<Student> studentList;

    public LabExercises() {
        this.studentList = new ArrayList<>();
    }

    public void addStudent(Student student) {
        studentList.add(student);
    }
    public List<Student> failedStudents ()
    {
        Comparator<Student> comparator=Comparator.comparing(Student::getIndex)
                .thenComparing(Student::getLabPoints);
        return studentList.stream()
                .filter(i-> !i.hasSigniture())
                .sorted(comparator)
                .collect(Collectors.toList());
    }
    public Map<Integer,Double> getStatisticsByYear()
    {
        return studentList.stream()
                .filter(Student::hasSigniture)
                .collect(Collectors.groupingBy(Student::getYear,Collectors.averagingDouble(Student::getLabPoints)));
    }
    public void printByAveragePoints (boolean ascending, int n)
    {
       Comparator<Student> comparator= Comparator.comparing(Student::getLabPoints)
               .thenComparing(Student::getIndex);
       if(!ascending)
       {
           comparator=comparator.reversed();
       }
       studentList.stream()
               .sorted(comparator)
               .limit(n)
               .forEach(System.out::println);
    }


}
public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}


