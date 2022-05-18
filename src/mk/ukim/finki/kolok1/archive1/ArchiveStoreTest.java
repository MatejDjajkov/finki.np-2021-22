package mk.ukim.finki.kolok1.archive1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

enum Type
{
    locked,special
}
class NonExistingItemException extends Exception{
    public NonExistingItemException(int id) {
        super(String.format("Item with id %d doesn't exist",id));
    }
}
abstract class Archive
{
    private int id;
    private Date date;

    public Archive(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }
    abstract String open(int id,Date date);


}
class SpecialArchive extends Archive
{
    int opened;
    int maxOpen;
    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.opened=0;
        this.maxOpen=maxOpen;
    }

    @Override
    String open (int id, Date date) {
        if(opened>=maxOpen)
        {
            return String.format("Item %d cannot be opened more than %d times\n",id,maxOpen);
        }
        opened++;
        return String.format("Item %d opened at %s\n",getId(),date);
    }
}
class LockedArchive extends Archive
{
    private Date date;
    public LockedArchive(int id, Date dateToOpen) {
        super(id);
        this.date=dateToOpen;
    }

    @Override
    String open (int id, Date date) {
        if(date.after(this.date))
        {
            return String.format("Item %d opened at %s\n",getId(),date);
        }
        return String.format("Item %d cannot be opened before %s\n",id,this.date);
    }
}
class ArchiveStore {
    private List<Archive> archiveList;
    private StringBuilder stringBuilder;

    public ArchiveStore() {
        this.archiveList=new ArrayList<>();
        this.stringBuilder=new StringBuilder();
    }

    public void archiveItem(Archive archive, Date date)
    {
        archive.setDate(date);
        archiveList.add(archive);
        //Item 1 archived at Thu Nov 07 00:00:00 UTC 2013
        stringBuilder.append(String.format("Item %d archived at %s\n",archive.getId(),date));
    }
    public String getLog() {
        return stringBuilder.toString();
    }


    public void openItem(int open, Date date) throws NonExistingItemException{

        List<Archive> temp=archiveList.stream()
                .filter(i->Integer.compare(i.getId(),open)==0)
                .collect(Collectors.toList());
        if(temp.size()==0)
        {
            throw new NonExistingItemException(open);
        }
        stringBuilder.append(temp.get(0).open(open,date));

    }
}

public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        Date date = new Date(113, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();
            Date dateToOpen = new Date(date.getTime() + (days * 24 * 60
                    * 60 * 1000));
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}

// вашиот код овде



