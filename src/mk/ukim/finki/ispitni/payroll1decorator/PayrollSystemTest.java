package mk.ukim.finki.ispitni.payroll1decorator;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

interface Employee
{
    String getID();
    String getLevel();
    double getRate();
    double getSalary();

}
class BasicEmployee implements Employee
{
    String ID;
    String level;
    double rate;

    public BasicEmployee(String ID, String level, double rate) {
        this.ID = ID;
        this.level = level;
        this.rate = rate;
    }


    @Override
    public String getID() {
        return this.ID;
    }

    @Override
    public String getLevel() {
        return this.level;
    }

    @Override
    public double getRate() {
        return this.rate;
    }

    @Override
    public double getSalary() {
        return 0;
    }
}
abstract class EmployeeDecorator implements Employee
{
    Employee employee;

    public EmployeeDecorator(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String getID() {
        return employee.getID();
    }

    @Override
    public String getLevel() {
        return employee.getLevel();
    }

    @Override
    public double getRate() {
        return employee.getRate();
    }

    @Override
    public double getSalary() {
        return employee.getSalary();
    }
}
class HourlyEmployee extends EmployeeDecorator
{

    double hours;

    public HourlyEmployee(Employee employee, double hours) {
        super(employee);
        this.hours = hours;
    }


    @Override
    public String getID() {
        return super.getID();
    }

    @Override
    public String getLevel() {
        return super.getLevel();
    }

    @Override
    public double getRate() {
        return super.getRate();
    }

    @Override
    public double getSalary() {
        if(hours<=40)
        {
            return hours*employee.getRate();
        }
        else {
            double difference=hours-40.0;
            return 40*getRate()+(difference*getRate()*1.5);
        }
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f",
                employee.getID(),employee.getLevel(),getSalary(), Math.min(hours, 40.0),(hours>40.0)?hours-40.0:0.0);
    }
}
class FreelanceEmployee extends EmployeeDecorator
{
    List<Integer> tickets;

    public FreelanceEmployee(Employee employee, List<Integer> tickets) {
        super(employee);
        this.tickets = tickets;
    }

    @Override
    public String getID() {
        return super.getID();
    }

    @Override
    public String getLevel() {
        return super.getLevel();
    }

    @Override
    public double getRate() {
        return super.getRate();
    }

    @Override
    public double getSalary() {
        return tickets.stream()
                .mapToDouble(Integer::doubleValue)
                .sum()*getRate();
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d",
                employee.getID(),employee.getLevel(),getSalary(),tickets.size(),tickets.stream()
        .mapToInt(Integer::intValue)
        .sum());
        //Employee ID: bca8f6 Level: level9 Salary: 1127.50 Tickets count: 9 Tickets points: 41
    }
}
class PayrollSystem
{
    Map<String,Double> hourlyRateByLevel;
    Map<String,Double> ticketRateByLevel;
    List<Employee> employeeList;

    public PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
        employeeList=new ArrayList<>();
    }

    public void readEmployees(InputStream in) {
        BufferedReader bf=new BufferedReader(new InputStreamReader(in));
        bf.lines()
                .forEach(i->
                {
                    String [] parts=i.split(";");
                    if(parts[0].equals("H"))
                    {
                        //H;ID;level;hours;
                        BasicEmployee bs=new BasicEmployee(parts[1],parts[2],hourlyRateByLevel.get(parts[2]));
                        employeeList.add(new HourlyEmployee(bs,Double.parseDouble(parts[3])));
                    }
                    else if(parts[0].equals("F"))
                    {
                        //F;ID;level;ticketPoints1;ticketPoints2;...;ticketPointsN;
                        BasicEmployee bs=new BasicEmployee(parts[1],parts[2],ticketRateByLevel.get(parts[2]));
                        List<Integer> integerList= Stream.of(parts)
                                .skip(3)
                                .map(Integer::valueOf)
                                .collect(Collectors.toList());

                        employeeList.add(new FreelanceEmployee(bs,integerList));
                    }
                });
    }

    public Map<String, Set<Employee>> printEmployeesByLevels(PrintStream out, Set<String> levels) {
        return employeeList.stream()
                .filter(i->levels.contains(i.getLevel()))
                .collect(Collectors.groupingBy(Employee::getLevel, TreeMap::new,Collectors.toCollection(
                        ()->new TreeSet<>(Comparator.comparing(Employee::getSalary).thenComparing(Employee::getLevel).reversed()))));
    }
}

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i=5;i<=10;i++) {
            levels.add("level"+i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: "+ level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });


    }
}
