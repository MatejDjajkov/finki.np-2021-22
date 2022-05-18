package mk.ukim.finki.ispitni.taskwithdecorator;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

interface Tasks
{
    //[категорија][име_на_задача],[oпис],[рок_за_задачата],[приоритет]
    String getCategory();
    String getName();
    String getDescription();
    LocalDateTime getDueDate();
    int getPriority();
}
class BasicTask implements Tasks
{
    String category;
    String name;
    String decription;

    public BasicTask(String category, String name, String decription) {
        this.category = category;
        this.name = name;
        this.decription = decription;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return decription;
    }

    @Override
    public LocalDateTime getDueDate() {
        return LocalDateTime.MAX;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        return "Task{" +
                "category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", decription='" + decription + '\'' +
                '}';
    }
}
abstract class TaskDecorator implements Tasks
{
   Tasks tasks;

    public TaskDecorator(Tasks tasks) {
        this.tasks = tasks;
    }

    @Override
    public String getCategory() {
        return tasks.getCategory();
    }

    @Override
    public String getName() {
        return tasks.getName();
    }

    @Override
    public String getDescription() {
        return tasks.getDescription();
    }

    @Override
    public LocalDateTime getDueDate() {
        return tasks.getDueDate();
    }

    @Override
    public int getPriority() {
        return tasks.getPriority();
    }

    @Override
    public String toString() {
        return tasks.toString();
    }
}
class Priority extends TaskDecorator
{
    int priority;

    public Priority(Tasks tasks, int priority) {
        super(tasks);
        this.priority = priority;
    }

    @Override
    public String getCategory() {
        return super.getCategory();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public LocalDateTime getDueDate() {
        return super.getDueDate();
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public String toString() {
        return super.toString()+String.format(",%d",priority);
    }
}
class Time extends TaskDecorator
{
    LocalDateTime localDateTime;

    public Time(Tasks tasks, LocalDateTime localDateTime) {
        super(tasks);
        this.localDateTime = localDateTime;
    }

    @Override
    public String getCategory() {
        return super.getCategory();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public LocalDateTime getDueDate() {
        return this.localDateTime;
    }

    @Override
    public int getPriority() {
        return super.getPriority();
    }

    @Override
    public String toString() {
        return super.toString()+String.format(",%s",localDateTime.toString());
    }
}
class TaskManager
{
    List<Tasks> tasksList;
    private Tasks createSimpleTask(String first,String second,String third)
    {
        return new BasicTask(first,second,third);
    }

    public TaskManager() {
        tasksList=new ArrayList<>();
    }

    public void readTasks(InputStream in) {
        BufferedReader bf=new BufferedReader(new InputStreamReader(in));
        bf.lines()
                .forEach(i->
                {
                    String [] parts=i.split("\\,");
                    Tasks basicTask=createSimpleTask(parts[0],parts[1],parts[2]);
                    if(parts.length==4)
                    {
                        if(parts[3].length()==1)
                        {
                            basicTask=new Priority(basicTask,Integer.parseInt(parts[3]));
                        }
                        else 
                        {
                            basicTask=new Time(basicTask,LocalDateTime.parse(parts[3]));
                        }
                    }
                    else if(parts.length==5)
                    {
                        basicTask=new Priority(new Time(basicTask,LocalDateTime.parse(parts[3])),Integer.parseInt(parts[4]));
                    }
                    tasksList.add(basicTask);
                });

    }

    void printTasks(OutputStream os, boolean includePriority, boolean includeCategory) {
        PrintWriter pw=new PrintWriter(os);
        Comparator<Tasks> comparator=null;
        comparator=Comparator.comparing(Tasks::getCategory);


        tasksList.stream()
                .sorted(comparator)
                .forEach(pw::println);
        pw.flush();

    }
}

public class TasksManagerTest {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        System.out.println("Tasks reading");
        manager.readTasks(System.in);
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
        System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");

    }
}
