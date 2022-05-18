//package mk.ukim.finki.kolok1.timetable;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class UnsupportedFormatException extends Exception {
    public UnsupportedFormatException(String s) {
        super();
    }
}
class InvalidTimeException extends Exception
{
    public InvalidTimeException(String message) {
        super();
    }
}

class Time
{
    private int hour;
    private int minutes;

    public Time(int hour, int minutes) {
        this.hour = hour;
        this.minutes = minutes;
    }

    public static Time createTime(String s) throws InvalidTimeException,UnsupportedFormatException
    {
        if(!s.contains("\\.")||!s.contains("\\:"))
        {
            throw new UnsupportedFormatException(s);
        }
        String [] temp;
        if(s.contains(":"))
        {
            temp=s.split("\\:");
        }
        else
        {
            temp=s.split("\\.");
        }
        if(Integer.parseInt(temp[0])>24||Integer.parseInt(temp[1])>60)
        {
            throw new InvalidTimeException("Lele mamo be");
        }
        return new Time (Integer.parseInt(temp[0]),Integer.parseInt(temp[1]));
    }

}
class TimeTable
{
    private List<Time> timeList;
    private List<String> stringList;

    public TimeTable() {
        this.timeList=new ArrayList<>();
        this.stringList=new ArrayList<>();
    }


    public void readTimes(InputStream inputStream) throws InvalidTimeException,UnsupportedFormatException{
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
        stringList=bufferedReader.lines()
                .collect(Collectors.toList());
        String [] temp=stringList.toArray(new String[0]);
        stringList=new ArrayList<>();
        for(int i=0;i<temp.length;i++)
        {
            String [] useless=temp[i].split("\\s");
            for(int j=0;j< useless.length;j++)
            {
                stringList.add(useless[j]);
            }
        }
        timeList= stringList.stream()
                .map(i->
                {
                    try
                    {
                        return Time.createTime(i);
                    }
                    catch (UnsupportedFormatException | InvalidTimeException e)
                    {
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
        System.out.println(timeList);
    }

    public void writeTimes(PrintStream out, TimeFormat format24) {
        PrintWriter printWriter= new PrintWriter(out);
        timeList
                .forEach(printWriter::println);
        printWriter.flush();

    }
}


public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);

    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}
