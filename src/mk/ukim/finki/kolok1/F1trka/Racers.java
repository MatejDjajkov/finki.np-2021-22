package mk.ukim.finki.kolok1.F1trka;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.spi.LocaleServiceProvider;

public class Racers implements Comparable<Racers>{
    private String name;
    private String besttime;
    static int count=0;


    public Racers(String name, String lap1, String lap2, String lap3) {
        this.name = name;
        besttime=lap1;
        if(compareTime(besttime,lap2)==1)
        {
            besttime=lap2;
        }
        if(compareTime(besttime,lap3)==1)
        {
            besttime=lap3;
        }

    }
    private Integer compareTime(String lap1, String lap2)
    {
       String [] first=lap1.split(":");
       String [] second=lap2.split(":");

       for(int i=0;i<3;i++)
       {
           if(Integer.parseInt(first[i])<Integer.parseInt(second[i]))
           {
               return -1;
           }
           else if(Integer.parseInt(first[i])>Integer.parseInt(second[i]))
           {
               return 1;
           }
       }
        return 0;
    }

    public static Racers create(String line) {
        String[] parts =line.split("\\s+");
        return new Racers(parts[0],parts[1],parts[2],parts[3]);
    }

    @Override
    public int compareTo(Racers o) {
        return compareTime(this.besttime,o.besttime);
    }

    @Override
    public String toString() {

        return String.format("%d. %-10s%10s",count++,name,besttime);
    }
}
