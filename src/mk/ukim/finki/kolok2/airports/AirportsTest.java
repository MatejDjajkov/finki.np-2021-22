package mk.ukim.finki.kolok2.airports;



import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

class Flight
{
    String from;
    String to;
    int time;
    int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    public String getTo() {
        return to;
    }

    public int getTime() {
        return time;
    }

    public String getFrom() {
        return from;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        LocalTime ltStart = LocalTime.of(0, 0);
        ltStart = ltStart.plusMinutes(time);
        LocalTime ltEnd = ltStart.plusMinutes(duration);
        LocalTime ltDuration = LocalTime.of(0, 0);
        ltDuration = ltDuration.plusMinutes(duration);

        String nextDay = "";
        if (ltEnd.getHour() < ltStart.getHour()) {
            nextDay = " +1d";
        }
        return String.format("%s-%s %s-%s%s %dh%02dm", from, to, ltStart, ltEnd, nextDay, ltDuration.getHour(), ltDuration.getMinute());
    }
}
class Airport
{
    private String name;
    private String country;
    private String code;
    private int passengers;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(String.format("%s (%s)\n",name,code));
        stringBuilder.append(String.format("%s\n",country));
        stringBuilder.append(String.format("%d\n",passengers));
        return stringBuilder.toString();
    }
   //Kuala Lumpur International (KUL)¶
   //Malaysia¶
   //39887866¶
}
class Airports
{

    Set<Flight> allFlightSet;
    private Map<String,Airport> airPortName;
    Comparator<Flight> flightComparator=Comparator.comparing(Flight::getTo)
            .thenComparing(Flight::getTime)
            .thenComparing(Flight::getDuration);
    public Airports()
    {
        allFlightSet=new TreeSet<>(flightComparator);
        airPortName=new HashMap<>();
    }
    public void addAirport(String name, String country, String code, int passengers)
    {
        airPortName.putIfAbsent(code,new Airport(name, country, code, passengers));
    }
    public void addFlights(String from, String to, int time, int duration)
    {
        allFlightSet.add(new Flight(from,to,time,duration));
    }
    public void showFlightsFromAirport(String code)
    {
        final int[] count = {1};
        System.out.print(airPortName.get(code));
        allFlightSet.stream()
                .filter(i->i.getFrom().equals(code))
                .forEach(i->
                {

                    System.out.println(String.format("%d. %s", count[0]++,i));
                });
    }
    public void showDirectFlightsFromTo(String from, String to)
    {
        List<Flight> flightList=allFlightSet.stream()
                .filter(i->i.getFrom().equals(from)&&i.getTo().equals(to))
                .collect(Collectors.toList());
        if(flightList.size()==0)
        {
            System.out.println(String.format("No flights from %s to %s",from,to));
        }
        else
        {
            flightList.stream()
                    .forEach(System.out::println);
        }
    }
    public void showDirectFlightsTo(String to)
    {
        allFlightSet.stream()
                .filter(i->i.getTo().equals(to))
                .forEach(System.out::println);
    }

}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}



