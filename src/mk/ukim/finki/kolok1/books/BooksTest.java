package mk.ukim.finki.kolok1.books;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

class Book
{
    private String Title;
    private String Category;
    private float price;

    public Book(String title, String category, float price) {
        Title = title;
        Category = category;
        this.price = price;
    }

    public String getTitle() {
        return Title;
    }

    public String getCategory() {
        return Category;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) %.2f",Title,Category,price);
                //A Brief History of Time (A) 25.66
    }
}
class  BookCollection
{
    private List<Book> books;
    final Comparator<Book> comparingCategory= Comparator.comparing(Book::getTitle)
            .thenComparing(Book::getPrice);
    final Comparator<Book> comparingPrice= Comparator.comparing(Book::getPrice)
            .thenComparing(Book::getTitle);

    public  BookCollection() {
        books=new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void printByCategory(String category) {
        books.stream()
                .filter(i->i.getCategory().equalsIgnoreCase(category))
                .sorted(comparingCategory)
                .forEach(i->System.out.println(i));
    }

    public List<Book> getCheapestN(int n) {
        return books.stream()
                .sorted(comparingPrice)
                .limit(n)
                .collect(Collectors.toList());
    }
}
public class BooksTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}

// Вашиот код овде