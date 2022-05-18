package mk.ukim.finki.ispitni.phonebook;
import java.util.*;
import java.util.stream.Collectors;

class DuplicateNumberException extends Exception
{
    public DuplicateNumberException(String m) {
        super(m);
    }
}

class Contact
{
    String name;
    String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return String.format("%s %s",name,number);
    }
}
class PhoneBook
{
    Map<String,Set<Contact>> contactsByNumber;
    Map<String,Set<Contact>> contactsByName;
    Comparator<Contact> contactComparator=Comparator.comparing(Contact::getName)
            .thenComparing(Contact::getNumber);

    public PhoneBook() {
        contactsByNumber=new HashMap<>();
        contactsByName=new HashMap<>();
    }

    public void addContact(String name, String number) throws DuplicateNumberException {
        if(contactsByNumber.containsKey(number))
        {
            throw new DuplicateNumberException(String.format("Duplicate number: %s",number));
        }
        Contact contact=new Contact(name, number);
        contactsByName.putIfAbsent(name,new TreeSet<>(contactComparator));
        contactsByName.get(name).add(contact);
        for(int i=0;i<=number.length();i++)
        {
            for(int j=i+2;j<=number.length();j++)
            {
                contactsByNumber.putIfAbsent(number.substring(i,j),new TreeSet<>(contactComparator));
                contactsByNumber.get(number.substring(i,j)).add(contact);
            }
        }
    }

    public void contactsByNumber(String part) {
        if(!contactsByNumber.containsKey(part))
        {
            System.out.println("NOT FOUND");
            return;
        }

        contactsByNumber.get(part)
                    .stream()
                    .forEach(System.out::println);


    }

    public void contactsByName(String part) {

        if(!contactsByName.containsKey(part))
        {
            System.out.println("NOT FOUND");
            return;
        }

            contactsByName.get(part)
                    .stream()
                    .forEach(System.out::println);

    }
}

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}
