package mk.ukim.finki.ispitni.taskmenager;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
class TaskHasPassed extends Exception
{
    public TaskHasPassed(String messege) {
        super(messege);
    }
}

abstract class Task
{
    String category;
    String name;
    String description;


    public Task(String category, String name, String description) {
        this.category = category;
        this.name = name;
        this.description = description;
    }
    abstract public String toString();
    public String getCategory()
    {
        return this.category;
    }
    abstract public int GetPriority();
    abstract public LocalDateTime GetTime();

}
class TaskBasic extends Task
{
    public TaskBasic(String category, String name, String description) {
        super(category, name, description);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int GetPriority() {
        return 200;
    }

    @Override
    public LocalDateTime GetTime() {
        return LocalDateTime.now();
    }
}
class TaskWithTime extends Task
{
    LocalDateTime localDateTime;

    public TaskWithTime(String category, String name, String description, LocalDateTime localDateTime) {
        super(category, name, description);
        this.localDateTime = localDateTime;
    }


    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + localDateTime +
                '}';
    }

    @Override
    public int GetPriority() {
        return 200;
    }

    @Override
    public LocalDateTime GetTime() {
        return localDateTime;
    }
}
class TaskWithPriority extends Task
{
    int priority;

    public TaskWithPriority(String category, String name, String description, int priority) {
        super(category, name, description);
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                '}';
    }

    @Override
    public int GetPriority() {
        return priority;
    }

    @Override
    public LocalDateTime GetTime() {
        return LocalDateTime.now();
    }
}
class TaskWithPriorityTime extends Task
{
    LocalDateTime localDateTime;
    int prirority;

    public TaskWithPriorityTime(String category, String name, String description, LocalDateTime localDateTime, int prirority) {
        super(category, name, description);
        this.localDateTime = localDateTime;
        this.prirority = prirority;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + localDateTime +
                ", priority=" + prirority +
                '}';
    }

    @Override
    public int GetPriority() {
        return prirority;
    }

    @Override
    public LocalDateTime GetTime() {
        return localDateTime;
    }
}
class TaskManager
{
    List<Task> taskList;
    public TaskManager() {
        taskList=new ArrayList<>();
    }

    public void readTasks(InputStream in)throws TaskHasPassed {
        BufferedReader bf=new BufferedReader(new InputStreamReader(in));
        bf.lines()
                .forEach(i->
                {
                    //[категорија][име_на_задача],[oпис],[рок_за_задачата],[приоритет]
                    String [] parts=i.split(",");
                    if(parts.length==4)
                    {
                        if(parts[3].length()==1)
                        {
                           taskList.add(new TaskWithPriority(parts[0],parts[1],parts[2],Integer.parseInt(parts[3])));
                        }
                        else
                        {
                            taskList.add(new TaskWithTime(parts[0],parts[1],parts[2],LocalDateTime.parse(parts[3])));
                        }

                    }
                    else if(parts.length==3)
                    {
                        taskList.add(new TaskBasic(parts[0],parts[1],parts[2]));
                    }
                    else
                    {
                        try {
                            LocalDateTime localDateTime = LocalDateTime.parse("2020-06-01T23:59:59");
                            if (LocalDateTime.parse(parts[3]).equals(localDateTime)) {

                                throw new TaskHasPassed(String.format("The deadline %s has already passed", localDateTime.toString()));

                            }
                            taskList.add(new TaskWithPriorityTime(parts[0], parts[1], parts[2], LocalDateTime.parse(parts[3]), Integer.parseInt(parts[4])));
                        } catch (TaskHasPassed taskHasPassed) {
                            System.out.println(taskHasPassed.getMessage());
                        }


                    }




                });
    }

    public void printTasks(OutputStream out, boolean includePriority, boolean includeCategory) {
        PrintWriter pw=new PrintWriter(out);
                Comparator<Task> taskComparator=getComparator(includePriority);
        if(includeCategory)
        {
            Map<String,List<Task>>tasksByCategory=taskList.stream()
                .collect(Collectors.groupingBy(Task::getCategory,Collectors.toList()));
            tasksByCategory.keySet()
                    .forEach(i->
                    {
                        pw.println(i.toUpperCase());
                        tasksByCategory.get(i)
                                .stream()
                                .sorted(taskComparator)
                                .forEach(j-> pw.println(j));
                    });
        }
        else
        {
            taskList.stream()
                    .sorted(taskComparator)
                    .forEach(i-> pw.println(i));
        }



        pw.flush();
    }
    private Comparator<Task> getComparator(boolean includePriority)
    {
        if(includePriority)
        {
            return Comparator.comparing(Task::GetPriority)
                    .thenComparing(Task::GetTime);
        }
        return Comparator.comparing(Task::GetTime);
    }
}
public class TasksManagerTest {

    public static void main(String[] args) throws TaskHasPassed {

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
