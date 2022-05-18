package mk.ukim.finki.kolok1.ddv2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

enum Type
{
    A,B,V
}
class AmountNotAllowedException extends Exception
{
    public AmountNotAllowedException(int amount) {
        super(String.format("Receipt with amount %d is not allowed to be scanned",amount));
    }
}
abstract class Item
{
    private final int price;
    public Item(int price)
    {
        this.price=price;
    }

    public int getPrice() {
        return price;
    }

    public abstract Type getType();
    public abstract double getReturn();

}
class ItemA extends Item
{
    public ItemA(int price) {
        super(price);
    }

    @Override
    public Type getType() {
        return Type.A;
    }

    @Override
    public double getReturn() {
        return getPrice()*0.18;
    }
}
class ItemB extends Item
{
    public ItemB(int price) {
        super(price);
    }

    @Override
    public Type getType() {
        return Type.B;
    }

    @Override
    public double getReturn() {
        return getPrice()*0.05;
    }
}
class ItemV extends Item
{
    public ItemV(int price) {
        super(price);
    }

    @Override
    public Type getType() {
        return Type.V;
    }

    @Override
    public double getReturn() {
        return 0;
    }
}

class User implements Comparable<User>
{
    private final String id;
    private final List<Item> items;
    private final int sum;

    public User(String id, List<Item> items,int sum) {
        this.id = id;
        this.items = items;
        this.sum=sum;
    }
    public static User createUser(String s) throws AmountNotAllowedException
    {
        List<Item> itemList=new ArrayList<>();
        String [] temp=s.split("\\s");
        for(int i=2;i< temp.length;i+=2)
        {
            if(temp[i].equals("A"))
            {
                itemList.add(new ItemA(Integer.parseInt(temp[i-1])));
            }
            else if (temp[i].equals("B"))
            {
                itemList.add(new ItemB(Integer.parseInt(temp[i-1])));
            }
            else
            {
                itemList.add(new ItemV(Integer.parseInt(temp[i-1])));
            }
        }
        int sum=itemList.stream()
                .mapToInt(Item::getPrice)
                .sum();
        if(sum>30000)
        {
            throw new AmountNotAllowedException(sum);
        }
        return new User(temp[0],itemList,sum);
    }
    public double sumOfAmounts()
    {
        return items.stream()
                .mapToDouble(Item::getReturn)
                .sum()*0.15;
    }

    @Override
    public String toString() {
        //“ID SUM_OF_AMOUNTS TAX_RETURN”
        return String.format("%s %d %.5f",id,sum,sumOfAmounts());
    }

    @Override
    public int compareTo(User o) {
        return Comparator.comparing(User::sumOfAmounts)
                .thenComparing(User::sumOfAmounts)
                .compare(this,o);
    }
}
class MojDDV
{
    private List<User> userList;

    public MojDDV() {
        this.userList = new ArrayList<>();
    }

    public void readRecords(InputStream inputStream) {
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
        userList=bufferedReader
                .lines()
                .map(lines->
                {
                    try
                    {
                        return User.createUser(lines);
                    }
                    catch (AmountNotAllowedException e)
                    {
                        System.out.println(e.getMessage());
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void printTaxReturns(PrintStream out) {
        PrintWriter printWriter= new PrintWriter(out);
        userList
                .forEach(printWriter::println);
        printWriter.flush();
    }

    public void printStatistics(PrintStream out) {
        PrintWriter printWriter= new PrintWriter(out);
        DoubleSummaryStatistics doubleSummaryStatistics
                =userList.stream()
                .mapToDouble(User::sumOfAmounts)
                .summaryStatistics();
        printWriter.println(String.format("min:\t%05.03f\nmax:\t%05.03f\nsum:\t%05.03f\ncount:\t%-5d\navg:\t%05.03f\n",
                doubleSummaryStatistics.getMin(),
                doubleSummaryStatistics.getMax(),
                doubleSummaryStatistics.getSum(),
                doubleSummaryStatistics.getCount(),
                doubleSummaryStatistics.getAverage()));
        printWriter.flush();

    }
}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}