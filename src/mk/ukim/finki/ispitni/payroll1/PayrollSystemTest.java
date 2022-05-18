package mk.ukim.finki.ispitni.payroll1;
import java.io.*;
import java.security.spec.ECField;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

interface Employee
{
    double getSalary();
    int getLevels();
    String getLevel();
}
class HourlyEmployee implements Employee
{
    String id;
    String level;
    String hours;
    double rate;

    public HourlyEmployee(String id, String level, String hours,double rate) {
        this.id = id;
        this.level = level;
        this.hours = hours;
        this.rate=rate;
    }
    public static HourlyEmployee createHourlyEmoloyee(String line,Map<String,Double> rateByLevel)
    {
        //H;1ff293;level7;41.93
        String [] parts=line.split(";");
        return new HourlyEmployee(parts[1],parts[2],parts[3],rateByLevel.get(parts[2]));
    }

    public String getLevel() {
        return level;
    }

    @Override
    public double getSalary() {
        if(Double.parseDouble(hours)<=40)
        {
            return Double.parseDouble(hours)*rate;
        }
        return (40.0*rate)+(Double.parseDouble(hours)-40.0)*rate*1.5;
    }

    @Override
    public int getLevels() {
        return Integer.parseInt(String.valueOf(level.charAt(level.length()-1)));
    }

    @Override
    public String toString() {
        double hoursvalue=Double.parseDouble(this.hours);
        return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f",id
        ,level,getSalary(),(hoursvalue>40.0)?40.00:hoursvalue,hoursvalue<40?0.0:hoursvalue-40.0);
        //Employee ID: 1ff293 Level: level7 Salary: 1089.53 Regular hours: 40.00 Overtime hours: 1.93
    }

}
class FreelanceEmployee implements Employee
{
    String id;
    String level;
    double rate;
    List<Integer> ticketList;

    public FreelanceEmployee(String id, String level, List<Integer> ticketList,double rate) {
        this.id = id;
        this.level = level;
        this.ticketList = ticketList;
        this.rate=rate;
    }
    public static  FreelanceEmployee createFreelanceEmployee(String s,Map<String,Double> rateBylevel)
    {
        String [] parts=s.split(";");
        List<Integer> list=new ArrayList<>();
        for(int i=3;i<parts.length;i++)
        {
            list.add(Integer.parseInt(parts[i]));
        }
        return new FreelanceEmployee(parts[1],parts[2],list,rateBylevel.get(parts[2]));
    }


    public String getLevel() {
        return level;
    }

    @Override
    public double getSalary() {
        return ticketList.stream()
                .mapToDouble(i->i*rate)
                .sum();
    }

    @Override
    public int getLevels() {
        return Integer.parseInt(String.valueOf(level.charAt(level.length()-1)));
    }

    @Override
    public String toString() {
        //Employee ID: bca8f6 Level: level9 Salary: 1127.50 Tickets count: 9 Tickets points: 41
        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d",id
        ,level,getSalary(),ticketList.size(),ticketList.stream().mapToInt(Integer::intValue).sum());
    }

}
class PayrollSystem
{
    List<Employee> employeeList;
    Map<String,Double> hourlyRateByLevel;
    Map<String,Double> ticketRateByLevel;


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
                    if(i.charAt(0)=='H')
                    {
                        employeeList.add(HourlyEmployee.createHourlyEmoloyee(i,hourlyRateByLevel));
                    }
                    else if(i.charAt(0)=='F')
                    {
                        employeeList.add(FreelanceEmployee.createFreelanceEmployee(i,ticketRateByLevel));
                    }
                });


    }

    public Map<String, Collection<Employee>> printEmployeesByLevels (OutputStream os, Set<String> levels) {


        return employeeList.stream()
                .filter(i->levels.contains(i.getLevel()))
                .collect(Collectors.groupingBy(Employee::getLevel,
                        TreeMap::new
                        ,Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Employee::getSalary)
                                .thenComparing(Employee::getLevel).reversed()))));
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
        Map<String, Collection<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: "+ level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });


    }
}
