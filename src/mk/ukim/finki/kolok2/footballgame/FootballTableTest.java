package mk.ukim.finki.kolok2.footballgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Partial exam II 2016/2017
 */
class Team
{
    public String getName() {
        return name;
    }

    String name;
    int wins=0,loses=0,draws=0,goalsGiven=0,goalsRecieved=0;

    public Team(String name) {
        this.name = name;
    }

    public void increaseWins()
    {
        wins++;
    }
    public void increaseLoses()
    {
        loses++;
    }
    public void increaseDraws()
    {
        draws++;
    }
    public void incraseGoalsGiven(int n)
    {
        goalsGiven+=n;
    }
    public void incraseGoalsRecieved(int n)
    {
        goalsRecieved+=n;
    }
    public int getPoints()
    {
        return wins*3+draws;
    }
    public int getDifference()
    {
        return goalsGiven-goalsRecieved;
    }


    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d",name,wins+draws+loses,wins,draws,loses,getPoints());
    }
}

class FootballTable
{
    Map<String,Team> valuesofName;
    public FootballTable() {
        valuesofName=new HashMap<>();

    }
    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals)
    {
        valuesofName.putIfAbsent(homeTeam,new Team(homeTeam));
        valuesofName.putIfAbsent(awayTeam,new Team(awayTeam));
        if(homeGoals>awayGoals)
        {
            valuesofName.get(homeTeam).increaseWins();
            valuesofName.get(awayTeam).increaseLoses();
        }
        else if(homeGoals<awayGoals)
        {
            valuesofName.get(homeTeam).increaseLoses();
            valuesofName.get(awayTeam).increaseWins();
        }
        else
        {
            valuesofName.get(awayTeam).increaseDraws();
            valuesofName.get(homeTeam).increaseDraws();
        }
        valuesofName.get(homeTeam).incraseGoalsGiven(homeGoals);
        valuesofName.get(homeTeam).incraseGoalsRecieved(awayGoals);
        valuesofName.get(awayTeam).incraseGoalsGiven(awayGoals);
        valuesofName.get(awayTeam).incraseGoalsRecieved(homeGoals);
    }

    public void printTable() {
        Comparator<Team> comparator=Comparator.comparing(Team::getPoints)
                .thenComparing(Team::getDifference).reversed()
                .thenComparing(Team::getName);
        final int[] count = {1};
        valuesofName.values()
                .stream()
                .sorted(comparator)
                .map(i-> String.format("%2d.%s", count[0]++,i.toString()))
                .forEach(System.out::println);
    }
}
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here

