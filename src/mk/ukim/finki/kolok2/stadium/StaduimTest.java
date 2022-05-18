package mk.ukim.finki.kolok2.stadium;

import java.util.Scanner;
import java.util.*;
class SeatNotAllowedException extends Exception
{
    public SeatNotAllowedException() {
    }
}
class SeatTakenException extends Exception
{
    public SeatTakenException() {
    }
}
class Sector
{
    private String code;
    private Map<Integer,Integer> boughtTicketsBySeatNumber;
    private Set<Integer> attendantTypeBySeatNumber;


    public Sector(String code, int seatNumbers) {
        this.code = code;
        boughtTicketsBySeatNumber =new HashMap<>();
        attendantTypeBySeatNumber=new HashSet<>();
        attendantTypeBySeatNumber.add(0);
        for(int i=1;i<=seatNumbers;i++)
        {
            boughtTicketsBySeatNumber.put(i,-1);

        }
    }
    public void BuySeatFromSector(int seat,int type) throws SeatTakenException,SeatNotAllowedException
    {
        if(boughtTicketsBySeatNumber.get(seat)==1)
        {
            throw new SeatTakenException();
        }
        if(Valid(type))
        {
            throw new SeatNotAllowedException();
        }
        boughtTicketsBySeatNumber.put(seat,1);
        attendantTypeBySeatNumber.add(type);
    }
    private boolean Valid(int type)
    {
        if(type==1&&attendantTypeBySeatNumber.contains(type))
        {
            return true;
        }
        else return type == 2 && attendantTypeBySeatNumber.contains(1);
    }
    public int getFreeSpaces()
    {
        return (int) boughtTicketsBySeatNumber.values()
                .stream()
                .filter(i->i==-1)
                .count();
    }

    public String getCode() {
        return code;
    }
    private int getTakenSpaces()
    {
        return boughtTicketsBySeatNumber.size()-getFreeSpaces();
    }
    private double getCoeficient()
    {
        return (getTakenSpaces()*1.0)/(boughtTicketsBySeatNumber.size()*1.0);
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%",code,getFreeSpaces(),boughtTicketsBySeatNumber.size()
        ,getCoeficient()*100);
        //E	1000/1000	0.0%
    }
}
class Stadium
{
    private String name;
    private Map<String,Sector> sectorsByName;
    ///?

    public Stadium(String name) {
        this.name = name;
        sectorsByName=new HashMap<>();
    }

    public void createSectors(String[] sectorNames, int[] sectorSizes)
    {
        for(int i=0;i<sectorNames.length;i++)
        {
            sectorsByName.put(sectorNames[i],new Sector(sectorNames[i],sectorSizes[i]));
        }

    }

    public void buyTicket(String part, int seat, int type) throws SeatTakenException,SeatNotAllowedException{
        sectorsByName.get(part).BuySeatFromSector(seat,type);
    }

    public void showSectors() {
        Comparator<Sector> sectorComparator= Comparator.comparing(Sector::getFreeSpaces).reversed()
                .thenComparing(Sector::getCode);
        sectorsByName.values()
                .stream()
                .sorted(sectorComparator)
                .forEach(System.out::println);
    }
}

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}
