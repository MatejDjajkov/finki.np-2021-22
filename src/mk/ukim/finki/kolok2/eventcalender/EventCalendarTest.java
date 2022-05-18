package mk.ukim.finki.kolok2.eventcalender;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

class WrongDateException extends Exception {
    WrongDateException(String messege) {
        super(messege);
    }
}
class Event implements Comparable<Event>
{
    private String name;
    private String location;
    private Date date;

    public Event(String name, String location, Date date) {
        this.name = name;
        this.location = location;
        this.date = date;

    }
    public int getMonth()
    {
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        DateFormat dateFormat=new SimpleDateFormat("dd MMM, YYY HH:mm");
        return String.format("%s at %s, %s",dateFormat.format(date),location,name);
    }

    @Override
    public int compareTo(Event other) {
        Comparator<Event> eventComparator=Comparator.comparing(Event::getDate)
                .thenComparing(Event::getName);
        return eventComparator.compare(this,other);
    }
}
class EventCalendar {
    private int year;
    private Map<String, Set<Event>> eventsByDate;


    public EventCalendar(int year) {
        this.year = year;
        eventsByDate =new HashMap<>();
    }

    public void addEvent(String name, String location, Date date) throws WrongDateException{
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        if(calendar.get(Calendar.YEAR)!=year)
        {

            throw new WrongDateException(String.format("Wrong date: %s",date.toString()));
        }
        Event event=new Event(name,location,date);
        eventsByDate.putIfAbsent(date.toString().substring(0,10),new TreeSet<>());
        eventsByDate.get(date.toString().substring(0,10)).add(event);

    }

    public void listEvents(Date date) {
        if(eventsByDate.get(date.toString().substring(0,10))==null)
        {
            System.out.println("No events on this day!");
            return;
        }
        List<Event> eventList=eventsByDate.get(date.toString().substring(0,10))
                .stream()
                .collect(Collectors.toList());
        eventList.stream()
                .forEach(System.out::println);

    }

    public void listByMonth() {
        Map<Integer,Long> map=eventsByDate.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Event::getMonth,Collectors.counting()));

        for(int i=0;i<12;i++)
        {
            if(map.containsKey(i))
            {
                System.out.println(String.format("%d : %d",i+1,map.get(i)));
            }
            else
            {
                System.out.println(String.format("%d : 0",i+1));
            }
        }
    }
}
public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}
