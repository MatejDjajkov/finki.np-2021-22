package mk.ukim.finki.kolok2.faculty;

//package mk.ukim.finki.vtor_kolokvium;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class OperationNotAllowedException extends Exception {
    OperationNotAllowedException (String message)
    {
        super(message);
    }
}
abstract class Student
{
    protected String studentID;
    protected Map<Integer,List<Integer>> gradesByTerm;
    protected Set<String> coursesByName;
    public Student(String studentID)
    {
        this.studentID=studentID;
        gradesByTerm=new HashMap<>();
        coursesByName=new TreeSet<>();
    }
    public void addGradeToStudent(int term, String courseName, int grade) throws OperationNotAllowedException
    {
        if(term>gradesByTerm.size())
        {
            throw new OperationNotAllowedException(String.format("Term %d is not possible for student with ID %s",term,studentID));
        }
        if(gradesByTerm.get(term).size()>=3)
        {
            throw new OperationNotAllowedException(String.format("Student %s already has 3 grades in term %d",studentID,term));
        }
        coursesByName.add(courseName);
        gradesByTerm.get(term).add(grade);
    }
    public int getNumberOfPassedSubjects()
    {
        return(int) gradesByTerm.values()
                .stream()
                .flatMap(List::stream)
                .count();

    }
    public double getAverageGrade()
    {
        return gradesByTerm.values()
                .stream()
                .flatMap(List::stream)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(5.00);
    }

    public String getStudentID() {
        return studentID;
    }
    public boolean hasGraduated()
    {
        return 3*gradesByTerm.size()==getNumberOfPassedSubjects();
    }
    public String getDetailedInfo()
    {
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("Student: %s\n",studentID));
        final int[] count = {1};
        gradesByTerm.values()
                .stream()
                .forEach(i->
                {
                    sb.append(String.format("Term %d\n", count[0]++));
                    sb.append(String.format("Courses: %d\n",i.size()));
                    sb.append(String.format("Average grade for term: %.2f\n",i.stream().mapToInt(Integer::intValue).average().orElse(5.00)));
                });
        sb.append(String.format("Average grade: %.2f\n",getAverageGrade()));
        sb.append(String.format("Courses attended: %s\n",String.join(",",coursesByName)));
        //Term 1
        //Courses: 2
        //Average grade for term: 6.50
       //Average grade: 7.00
       //Courses attended: course1,course10,course2,course3,course4,course5,course6,course7,course8,course9


        return sb.substring(0,sb.length()-1);
    }

    @Override
    public String toString() {
        return String.format("Student: %s Courses passed: %d Average grade: %.2f",studentID,getNumberOfPassedSubjects(),getAverageGrade());

    }
}
class StudentThreeYears extends Student
{
    public StudentThreeYears(String studentID) {
        super(studentID);
        for(int i=1;i<=6;i++)
        {
            gradesByTerm.put(i,new ArrayList<>());
        }
    }
}
class StudentFourYears extends Student
{
    public StudentFourYears(String studentID) {
        super(studentID);
        for(int i=1;i<=8;i++)
        {
            gradesByTerm.put(i,new ArrayList<>());
        }

    }
}
class Course
{
    private String courseName;
    private int studentCount;
    private List<Integer> grades;

    public Course(String courseName) {
        this.courseName = courseName;
        this.studentCount = 0;
        this.grades = new ArrayList<>();
    }
    public void addGradeForCourse(int grade)
    {
        grades.add(grade);
        studentCount++;
    }

    @Override
    public String toString() {
        return String.format("%s %d %.2f",courseName,studentCount,getAverageGradeForCourse());
    }

    public String getCourseName() {
        return courseName;
    }

    public int getStudentCount() {
        return studentCount;
    }
    public double getAverageGradeForCourse()
    {
       return grades.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(5.00);
    }
}
class Faculty {

    Map<String,Student> studentsByID;
    Map<String,Course> coursesByName;

    StringBuilder faclultylogs=new StringBuilder();

    public Faculty() {
        studentsByID=new HashMap<>();
        coursesByName=new HashMap<>();

    }

    void addStudent(String id, int yearsOfStudies) {
        if(yearsOfStudies==3)
        {
            studentsByID.put(id,new StudentThreeYears(id));
        }
        else
        {
            studentsByID.put(id,new StudentFourYears(id));
        }

    }


    void addGradeToStudent(String studentId, int term, String courseName, int grade) throws OperationNotAllowedException {
        Student student=studentsByID.get(studentId);
        student.addGradeToStudent(term,courseName,grade);
        if(student.hasGraduated())
        {
            faclultylogs.append(String.format("Student with ID %s graduated with average grade %.2f in %d years.\n",studentId,student.getAverageGrade(),student.gradesByTerm.size()/2));
            studentsByID.remove(studentId);
        }
        coursesByName.putIfAbsent(courseName,new Course(courseName));
        coursesByName.get(courseName).addGradeForCourse(grade);

    }

    String getFacultyLogs() {

        return faclultylogs.substring(0,faclultylogs.length()-1);
    }

    String getDetailedReportForStudent(String id) {

        return studentsByID.get(id).getDetailedInfo();
    }

    void printFirstNStudents(int n) {
        Comparator<Student> comparingStudendt=Comparator.comparing(Student::getNumberOfPassedSubjects)
                .thenComparing(Student::getAverageGrade)
                .thenComparing(Student::getStudentID).reversed();
        studentsByID.values()
                .stream()
                .sorted(comparingStudendt)
                .limit(n)
                .forEach(System.out::println);
    }

    void printCourses() {
        Comparator<Course> courseComparator=Comparator.comparing(Course::getStudentCount)
                .thenComparing(Course::getAverageGradeForCourse)
                .thenComparing(Course::getCourseName);
        coursesByName.values()
                .stream()
                .sorted(courseComparator)
                .forEach(System.out::println);

    }
}

public class FacultyTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();

        if (testCase == 1) {
            System.out.println("TESTING addStudent AND printFirstNStudents");
            Faculty faculty = new Faculty();
            for (int i = 0; i < 10; i++) {
                faculty.addStudent("student" + i, (i % 2 == 0) ? 3 : 4);
            }
            faculty.printFirstNStudents(10);

        } else if (testCase == 2) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            try {
                faculty.addGradeToStudent("123", 7, "NP", 10);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
            try {
                faculty.addGradeToStudent("1234", 9, "NP", 8);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        } else if (testCase == 3) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("123", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("1234", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (testCase == 4) {
            System.out.println("Testing addGrade for graduation");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            int counter = 1;
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("123", i, "course" + counter, (i % 2 == 0) ? 7 : 8);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            counter = 1;
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("1234", i, "course" + counter, (j % 2 == 0) ? 7 : 10);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("PRINT STUDENTS (there shouldn't be anything after this line!");
            faculty.printFirstNStudents(2);
        } else if (testCase == 5 || testCase == 6 || testCase == 7) {
            System.out.println("Testing addGrade and printFirstNStudents (not graduated student)");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), i % 5 + 6);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            if (testCase == 5)
                faculty.printFirstNStudents(10);
            else if (testCase == 6)
                faculty.printFirstNStudents(3);
            else
                faculty.printFirstNStudents(20);
        } else if (testCase == 8 || testCase == 9) {
            System.out.println("TESTING DETAILED REPORT");
            Faculty faculty = new Faculty();
            faculty.addStudent("student1", ((testCase == 8) ? 3 : 4));
            int grade = 6;
            int counterCounter = 1;
            for (int i = 1; i < ((testCase == 8) ? 6 : 8); i++) {
                for (int j = 1; j < 3; j++) {
                    try {
                        faculty.addGradeToStudent("student1", i, "course" + counterCounter, grade);
                    } catch (OperationNotAllowedException e) {
                        e.printStackTrace();
                    }
                    grade++;
                    if (grade == 10)
                        grade = 5;
                    ++counterCounter;
                }
            }
            System.out.println(faculty.getDetailedReportForStudent("student1"));
        } else if (testCase==10) {
            System.out.println("TESTING PRINT COURSES");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            faculty.printCourses();
        } else if (testCase==11) {
            System.out.println("INTEGRATION TEST");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 2 : 3); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }

            }

            for (int i=11;i<15;i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= 3; k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("DETAILED REPORT FOR STUDENT");
            System.out.println(faculty.getDetailedReportForStudent("student2"));
            try {
                System.out.println(faculty.getDetailedReportForStudent("student11"));
                System.out.println("The graduated students should be deleted!!!");
            } catch (NullPointerException e) {
                System.out.println("The graduated students are really deleted");
            }
            System.out.println("FIRST N STUDENTS");
            faculty.printFirstNStudents(10);
            System.out.println("COURSES");
            faculty.printCourses();
        }
    }
}

