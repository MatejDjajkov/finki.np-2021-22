package mk.ukim.finki.ispitni.prevodi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Text
{
    private String [] parts;

    public Text(String lines) {
        this.parts = lines.split("\\n");
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(String.join("\n", parts));
        sb.append("\n");
        return sb.toString();
    }

    public void shiftTime(int ms)
    {
        String [] times=parts[1].split(" --> ");
        String timeFrom=timeToString(stringToTime(times[0])+ms);
        String timeTo=timeToString(stringToTime(times[1])+ms);
        parts[1]=String.join(" --> ",timeFrom,timeTo);


    }
    static int stringToTime ( String time ) {
        String[] partss = time.split(",");
        int res = Integer.parseInt(partss[1]);
        partss = partss[0].split(":");
        int sec = Integer.parseInt(partss[2]);
        int min = Integer.parseInt(partss[1]);
        int h = Integer.parseInt(partss[0]);
        res += sec * 1000;
        res += min * 60 * 1000;
        res += h * 60 * 60 * 1000;
        return res ;
    }
    static String timeToString ( int time ) {
        int h = time / (60 * 60 * 1000) ;
        time = time % (60 * 60 * 1000) ;
        int m = time / (60 * 1000) ;
        time = time % (60 * 1000) ;
        int s = time / 1000;
        int ms = time % 1000;
        return String.format ("%02d:%02d:%02d,%03d", h , m , s , ms );
    }


}
class Subtitles
{
    List<Text> textList;
    public Subtitles() {
        textList=new ArrayList<>();
    }

    public int loadSubtitles(InputStream in) {
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder=new StringBuilder();
        bufferedReader.lines()
                .forEach(i-> stringBuilder.append(i).append('\n'));

        String [] parts=stringBuilder.toString().split("\\n\\n");
        for(String st:parts)
        {
            textList.add(new Text(st));
        }
        return parts.length;
    }

    public void print() {
        textList
                .forEach(System.out::println);
    }

    public void shift(int shift) {
        textList
                .forEach(i->i.shiftTime(shift));

    }
}
public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}
