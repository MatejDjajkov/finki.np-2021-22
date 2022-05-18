package mk.ukim.finki.kolok2.airportsV2;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

class Flight implements Comparable<Flight> {


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return time == flight.time;
    }

    @Override
    public int hashCode() {
        return Objects.hash(time);
    }

    private String from;
    private String to;
    private int time;
    private int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
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

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public int compareTo(Flight o) {
        Comparator<Flight> comparator = Comparator.comparing(Flight::getTime).thenComparing(Flight::getFrom);
        return comparator.compare(this, o);
    }
}


class Airport {
    private String name;
    private String country;
    private String code;
    private int passengers;
    Set<Flight> flightSet;
    static Comparator<Flight> comparator = Comparator.comparing(Flight::getTime);

    public Airport(String name, String country, String code, int passengers) {

        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        flightSet = new TreeSet<>(comparator);
    }

    public void addFlight(Flight flight) {
        flightSet.add(flight);
    }

    public void printFlights(String to) {
        StringBuilder sb = new StringBuilder();

        flightSet.stream()
                .filter(x -> x.getTo().equals(to))
                .forEach(x -> sb.append(x));
        if (sb.toString().equals("")) {
            sb.append(String.format("No flights from %s to %s", code, to));
        }

        System.out.println(sb.toString());

        //No flights from IAH to FRA

    }

    public void printFlights() {
        printAirport();
        Comparator<Flight> comparator = Comparator.comparing(Flight::getTo).thenComparing(Flight::getTime);
        List<Flight> flightList = getFlightList().stream().sorted(comparator).collect(Collectors.toList());
        for (int i = 0; i < flightList.size(); i++) {
            System.out.printf("%-1d. ", i + 1);
            System.out.println(flightList.get(i));
        }
    }

    public List<Flight> getFlightList() {
        return new ArrayList<>(flightSet);
    }

    private void printAirport() {
        System.out.printf("%s (%s)\n", name, code);
        System.out.println(country);
        System.out.println(passengers);
    }
}


class Airports {
    //code - airport
    Map<String, Airport> airportMap;

    public Airports() {
        airportMap = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        Airport airport = new Airport(name, country, code, passengers);
        airportMap.putIfAbsent(code, airport);
    }

    public void addFlights(String from, String to, int time, int duration) {
        Flight flight = new Flight(from, to, time, duration);
        airportMap.get(from)
                .addFlight(flight);
    }

    public void showFlightsFromAirport(String code) {
        airportMap.get(code)
                .printFlights();
    }

    public void showDirectFlightsFromTo(String from, String to) {
        airportMap.get(from)
                .printFlights(to);
    }

    public void showDirectFlightsTo(String to) {

        TreeSet<Flight> flightTreeSet = airportMap.values()
                .stream()
                .map(x -> x.getFlightList())
                .flatMap(x -> x.stream())
                .filter(x -> x.getTo().equals(to)).collect(Collectors.toCollection(TreeSet::new));

        flightTreeSet.stream().forEach(x -> System.out.println(x));
    }

}


public class AirportsTest2 {
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
