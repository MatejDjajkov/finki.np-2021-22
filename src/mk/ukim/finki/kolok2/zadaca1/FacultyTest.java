package mk.ukim.finki.kolok2.zadaca1;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
class Student
{
    String courseID;
    String id;
    int points;

    public Student(String courseID,String id, int points) {
        this.courseID=courseID;
        this.id = id;
        this.points = points;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return String.format("%s %d (%d)",id,points,getGrade());
    }
    public String toAnotherString()
    {
        return String.format("%s %d (%d)",courseID,points,getGrade());
    }
    public int getGrade()
    {
        if(points<50)
        {
            return 5;
        }
        else
        {
            return (points/10)+1;
        }
    }
}
class Faculty
{
    Map<String,List<Student>> studentsByCourse;

    public Faculty() {
        studentsByCourse=new HashMap<>();
    }
    public void addInfo (String courseId, String studentId, int totalPoints)
    {
        studentsByCourse.putIfAbsent(courseId,new ArrayList<>() );
        studentsByCourse.get(courseId).add(new Student(courseId,studentId,totalPoints));
    }

    public void printCourseReport(String courseId, String comparator, boolean descending) {
        Comparator<Student> studentComparator;
        if(comparator.equals("byId"))
        {
            studentComparator=Comparator.comparing(Student::getId)
                    .thenComparing(Student::getPoints);
        }
        else
        {
            studentComparator=Comparator.comparing(Student::getPoints)
                    .thenComparing(Student::getPoints);
        }
        if(descending)
        {
            studentComparator=studentComparator.reversed();
        }
        studentsByCourse.get(courseId)
                .stream()
                .sorted(studentComparator)
                .forEach(System.out::println);
    }

    public void printStudentReport(String studentId) {
        studentsByCourse.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(i-> i.getId().equals(studentId))
                .sorted(Comparator.comparing(Student::getCourseID))
                .forEach(i->System.out.println(i.toAnotherString()));

    }

    public Map<Integer, Long> gradeDistribution(String courseId) {
        return studentsByCourse.get(courseId)
                .stream()
                .collect(Collectors.groupingBy(Student::getGrade,Collectors.counting()));
    }
}

public class FacultyTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Faculty faculty = new Faculty();

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s++");
            if (parts[0].equals("addInfo")) {
                String courseId = parts[1];
                String studentId = parts[2];
                int totalPoints = Integer.parseInt(parts[3]);
                faculty.addInfo(courseId, studentId, totalPoints);
            } else if (parts[0].equals("printCourseReport")) {
                String courseId = parts[1];
                String comparator = parts[2];
                boolean descending = Boolean.parseBoolean(parts[3]);
                faculty.printCourseReport(courseId, comparator, descending);
            } else if (parts[0].equals("printStudentReport")) { //printStudentReport
                String studentId = parts[1];
                faculty.printStudentReport(studentId);
            } else {
                String courseId = parts[1];
                Map<Integer, Long> grades = faculty.gradeDistribution(courseId);
                grades.forEach((key, value) -> System.out.println(String.format("%2d -> %3d", key, value)));
            }
        }
    }
}

