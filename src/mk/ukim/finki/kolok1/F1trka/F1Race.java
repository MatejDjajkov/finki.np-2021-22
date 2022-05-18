package mk.ukim.finki.kolok1.F1trka;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class F1Race {
    private List<Racers> racersList;
    public F1Race()
    {
        this.racersList=new ArrayList<>();

    }
    public void readResults(InputStream inputStream)
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        racersList =bufferedReader.lines().map(Racers::create).collect(Collectors.toList());

    }
    public void printSorted(OutputStream outputStream)
    {

        PrintWriter printWriter = new PrintWriter(outputStream);
        racersList.stream().sorted().forEach(printWriter::println);
        printWriter.flush();

    }
}
