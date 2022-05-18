package mk.ukim.finki.kolok2.onlineshop;

import java.time.LocalDateTime;
import java.util.*;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(message);
    }
}


class Product {
    String id;
    String name;
    LocalDateTime created;
    double price;
    int quantitySold;

    public Product(String id, String name, LocalDateTime localDateTime, double price) {
        this.id = id;
        this.name = name;
        this.created = localDateTime;
        this.price = price;
        this.quantitySold=0;
    }

    public double getPrice(int n) {
        quantitySold+=n;
        return price*n*1.0;


    }

    public LocalDateTime getLocalDateTime() {
        return created;
    }

    public double getPrice() {
        return price;
    }

    public int getQuanity() {
        return quantitySold;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + created +
                ", price=" + price +
                ", quantitySold=" + quantitySold +
                '}';
    }
}


class OnlineShop {

    Map<String,Product> productByID;
    Map<String,List<Product>> productcsByCategory;
    OnlineShop() {
        productByID=new HashMap<>();
        productcsByCategory=new HashMap<>();

    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price){
        Product product= new Product(id,name,createdAt,price);
        productByID.put(id,product);
        productcsByCategory.putIfAbsent(category,new ArrayList<>());
        productcsByCategory.get(category).add(product);

    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException{
        if(productByID.get(id)==null)
        {
            throw  new ProductNotFoundException(String.format("Product with id %s does not exist in the online shop!",id));
        }
        return productByID.get(id).getPrice(quantity);
    }
    private Comparator<Product> getComparator (COMPARATOR_TYPE comparator_type)
    {

        Comparator<Product> comparingPrice=Comparator.comparing(Product::getPrice);
        Comparator <Product> comparatorDate=Comparator.comparing(Product::getLocalDateTime);
        Comparator<Product> comparingQuanity= Comparator.comparing(Product::getQuanity);
        return switch (comparator_type) {
            case HIGHEST_PRICE_FIRST -> comparingPrice.reversed();
            case LOWEST_PRICE_FIRST -> comparingPrice;
            case LEAST_SOLD_FIRST -> comparingQuanity;
            case MOST_SOLD_FIRST -> comparingQuanity.reversed();
            case OLDEST_FIRST -> comparatorDate;
            default -> comparatorDate.reversed();
        };

    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        Comparator<Product> comparator=getComparator(comparatorType);
        List<List<Product>> result = new ArrayList<>();
        List<Product> allProduct= new ArrayList<>();
        if(category==null)
        {
            allProduct.addAll(productByID.values());
        }
        else
        {
            allProduct.addAll(productcsByCategory.get(category));
        }
        allProduct.sort(comparator);
        for(int i=0;i<allProduct.size();i+=pageSize)
        {
            if(i+pageSize>allProduct.size())
            {
                result.add(allProduct.subList(i,i+allProduct.size()-i));
                break;
            }
            result.add(allProduct.subList(i,i+pageSize));
        }



        return result;
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category=null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}


