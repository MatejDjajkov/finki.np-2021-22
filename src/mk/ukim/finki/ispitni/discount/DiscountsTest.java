package mk.ukim.finki.ispitni.discount;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Discounts
 */
class Item implements Comparable<Item>
{
    int originalPrice;
    int currentPrice;

    public Item(int currentPrice,int originalPrice) {
        this.originalPrice = originalPrice;
        this.currentPrice = currentPrice;
    }
    public int getDiscountAmountPerItem()
    {
        return originalPrice-currentPrice;
    }
    public double getDiscountPercentPerItem()
    {
        return (1.0-((currentPrice*1.0)/(originalPrice*1.0)))*100.0;
    }
    public int getProcent()
    {
        return (int) getDiscountPercentPerItem();
    }


    @Override
    public String toString() {
        return String.format("%2d%% %d/%d\n",getProcent(),currentPrice,originalPrice);
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    @Override
    public int compareTo(Item o) {
        Comparator<Item> comparator=Comparator.comparing(Item::getProcent)
                .thenComparing(Item::getCurrentPrice).reversed();
        return comparator.compare(this,o);

    }
}
class Store
{
    String name;
    List<Item> itemsList;

    public Store(String s) {
        itemsList=new ArrayList<>();
        String [] parts=s.split("\\s");
        this.name=parts[0];
        for(int i=1;i<parts.length;i++)
        {
            if(parts[i].contains(":"))
            {
                String [] prices=parts[i].split(":");
                itemsList.add(new Item(Integer.parseInt(prices[0]),Integer.parseInt(prices[1])));
            }

        }

    }
    public int GetTotalDiscount()
    {
        return itemsList.stream()
                .mapToInt(Item::getDiscountAmountPerItem)
                .sum();
    }
    public double getAverageDiscount()
    {
        return itemsList.stream()
                .mapToInt(Item::getProcent)
                .average()
                .orElse(0.0);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("%s\n" +
                "Average discount: %.1f%%\n" +
                "Total discount: %d\n",name,getAverageDiscount(),GetTotalDiscount()));
        itemsList.stream()
                .sorted()
                .forEach(i->sb.append(i.toString()));
        //Levis
        //Average discount: 35.8%
        //        Total discount: 21137
        return sb.substring(0,sb.length()-1);
    }
}
class Discounts
{
    List<Store> storeList;
    public Discounts() {
        storeList=new ArrayList<>();

    }

    public int readStores(InputStream in) {
        BufferedReader bf=new BufferedReader(new InputStreamReader(in));
        bf.lines()
                .forEach(i->
                {
                    storeList.add(new Store(i));
                });
        return storeList.size();
    }

    public List<Store> byAverageDiscount() {
        return storeList.stream()
                .sorted(Comparator.comparing(Store::getAverageDiscount).reversed()
                .thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Store> byTotalDiscount() {
        return storeList
                .stream()
                .sorted(Comparator.comparing(Store::GetTotalDiscount)
                .thenComparing(Store::getName))
                .limit(3)
                .collect(Collectors.toList());
    }
}
public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

// Vashiot kod ovde
