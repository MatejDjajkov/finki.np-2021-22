package mk.ukim.finki.kolok1.dailytemp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

enum Type {
    C,F;
}

class Temperature {
    private Type type;
    private int temp;

    public Temperature(Type type, int temp) {
        this.type = type;
        this.temp = temp;
    }

    public double getFerenheit() {
        if (this.type.equals(Type.F)) {
            return temp;
        }
        //T∗95+32
        return ((temp * 9.0) / 5.0) + 32;
    }

    public double getCelcius() {
        if (this.type.equals(Type.C)) {
            return temp;
        }
        return ((temp - 32) * 5.0) / 9.0;
    }
}

    class Day implements Comparable<Day>{
        private int day;
        private List<Temperature> temperatureList;

        public Day(int day, List<Temperature> temperatureList) {
            this.day = day;
            this.temperatureList = temperatureList;
        }
        public static Day createDay(String s)
        {
            String [] temp=s.split(" ");
            List<Temperature> tempList=new ArrayList<>();
            for(int i=1;i<temp.length;i++) {

                String first = temp[i];
                if (Character.compare(first.charAt(first.length() - 1), 'C') == 0) {
                    tempList.add(new Temperature(Type.C,Integer.parseInt(first.substring(0,first.length()-1))));

                }
                else
                {
                    tempList.add(new Temperature(Type.F,Integer.parseInt(first.substring(0,first.length()-1))));
                }
            }
            return new Day(Integer.parseInt(temp[0]),tempList);
        }
        public void printCelcius()
        {
            DoubleSummaryStatistics doubleSummaryStatistics=new DoubleSummaryStatistics();
            doubleSummaryStatistics=temperatureList
                    .stream()
                    .mapToDouble(i->i.getCelcius())
                    .summaryStatistics();
            //11: Count:   7 Min:  38.33C Max:  40.56C Avg:  39.44C

            System.out.println(String.format("%d: Count:%6d Min:%6.2fC Max:%6.2fC Avg:%6.2fC",
                    day,
                    doubleSummaryStatistics.getCount(),
                    doubleSummaryStatistics.getMin(),
                    doubleSummaryStatistics.getMax(),
                    doubleSummaryStatistics.getAverage()));
        }
        public void printFerenheit()
        {
            DoubleSummaryStatistics doubleSummaryStatistics=new DoubleSummaryStatistics();
            doubleSummaryStatistics=temperatureList
                    .stream()
                    .mapToDouble(i->i.getFerenheit())
                    .summaryStatistics();
            //11: Count:   7 Min:  38.33C Max:  40.56C Avg:  39.44C
            System.out.println(String.format("%d: Count:%6d Min:%6.2fF Max:%6.2fF Avg:%6.2fF",
                    day,
                    doubleSummaryStatistics.getCount(),
                    doubleSummaryStatistics.getMin(),
                    doubleSummaryStatistics.getMax(),
                    doubleSummaryStatistics.getAverage()));
        }

        @Override
        public int compareTo(Day other) {
            return Integer.compare(this.day,other.day);
        }
    }

    class DailyTemperatures {
        private List<Day> elements;


        public void readTemperatures(InputStream inputStream) {
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            elements=bufferedReader
                    .lines()
                    .map(i->Day.createDay(i))
                    .collect(Collectors.toList());
        }

        public void writeDailyStats(PrintStream out, char c) {
            if(Character.compare(c,'C')==0)
            {
                elements.stream()
                        .sorted()
                        .forEach(i->i.printCelcius());
            }
            else
            {
                elements.stream()
                        .sorted()
                        .forEach(i->i.printFerenheit());
            }
        }
    }

    public class DailyTemperatureTest {
        public static void main(String[] args) {
            DailyTemperatures dailyTemperatures = new DailyTemperatures();
            dailyTemperatures.readTemperatures(System.in);
            System.out.println("=== Daily temperatures in Celsius (C) ===");
            dailyTemperatures.writeDailyStats(System.out, 'C');
            System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
            dailyTemperatures.writeDailyStats(System.out, 'F');
        }
    }

