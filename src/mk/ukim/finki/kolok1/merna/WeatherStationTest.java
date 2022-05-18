package mk.ukim.finki.kolok1.merna;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Weather implements Comparable<Weather>
{
    private float temp;
    private float wind;
    private float hum;
    private float vis;
    private Date date;

    public Weather(float temp, float wind, float hum, float vis, Date date) {
        this.temp = temp;
        this.wind = wind;
        this.hum = hum;
        this.vis = vis;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public float getTemp() {
        return temp;
    }

    @Override
    public String toString() {
        //41.8 9.4 km/h 40.8% 20.7 km Tue Dec 17 23:35:15 GMT 2013
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s",temp,wind,hum,vis,date);
    }

    @Override
    public int compareTo(Weather other) {
        return this.date.compareTo(other.date);
    }
}
class WeatherStation
{
    private int days;
    private List<Weather> elements;
    public WeatherStation(int n)
    {
        this.days=n;
        elements=new ArrayList<>();
    }
    private boolean isValid(Weather weather)
    {
        if(elements.size()==0)
        {
            return true;
        }
        for(Weather w:elements)
        {
            if(Math.abs(w.getDate().getTime()-weather.getDate().getTime())<150000)
            {
                return false;
            }

        }
        return true;
    }

    public void addMeasurment(float temp, float wind, float hum, float vis, Date date) {

        Weather weather=new Weather(temp, wind, hum, vis, date);
        if(!isValid(weather))
        {
            return;
        }
        List<Weather> temp1=elements.stream()
                .filter(i->date.getTime()-i.getDate().getTime()>24*60*60*1000*days)
                .collect(Collectors.toList());
        elements.removeAll(temp1);
        elements.add(weather);
    }

    public int total() {
        return elements.size();
    }

    public void status(Date from, Date to) throws RuntimeException{
        List<Weather> list=elements.stream()
                .filter(i->i.getDate().equals(from)||(
                        i.getDate().after(from)&&i.getDate().before(to))
                ||i.getDate().equals(to))
                .collect(Collectors.toList());
        if(list.size()==0)
        {
            throw new RuntimeException();
        }
        list.stream()
                .forEach(i-> System.out.println(i));
        double average=list.stream()
                .mapToDouble(i->i.getTemp())
                .sum();
        average=average/list.size()*1.0;
        System.out.println(String.format("Average temperature: %.2f",average));

    }
}

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

// vashiot kod ovde