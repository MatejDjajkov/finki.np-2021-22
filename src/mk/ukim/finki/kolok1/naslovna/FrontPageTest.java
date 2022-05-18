package mk.ukim.finki.kolok1.naslovna;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class CategoryNotFoundException extends Exception
{
    public CategoryNotFoundException(String s) {
        super(String.format("Category %s was not found",s));
    }
}
abstract class NewsItem
{
    protected String title;
    protected Date date;
    protected Category category;

    public NewsItem(String title, Date date, Category category) {
        this.title = title;
        this.date = date;
        this.category = category;
    }
    public String getCategory()
    {
        return category.getCategory();
    }
    abstract String getTeaser();
}
class TextNewsItem extends NewsItem
{
    private String text;

    public TextNewsItem(String title, Date date, Category category, String text) {
        super(title, date, category);
        this.text = text;
    }

    @Override
    String getTeaser() {
        StringBuilder stringBuilder= new StringBuilder();
        Date nowtime=new Date();
        int minutes=(int)(nowtime.getTime()-date.getTime())/1000/60;
        String returntext;
        if(text.length()<=80)
        {
            returntext=text;
        }
        else
        {
            returntext=text.substring(0,80);
        }
        return String.format("%s\n%d\n%s\n",title,minutes,returntext);
    }
}
class MediaNewsItem extends NewsItem
{
    private String url;
    private int views;

    public MediaNewsItem(String title, Date date, Category category, String url, int views) {
        super(title, date, category);
        this.url = url;
        this.views = views;
    }

    @Override
    String getTeaser() {
        Date now=new Date();
        int minutes=(int)(now.getTime()-date.getTime())/1000/60;

        return String.format("%s\n%d\n%s\n%d\n",title,minutes,url,views);
    }
}
class Category
{
    private String categoryname;

    public Category(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getCategory() {
        return categoryname;
    }

}
class FrontPage
{
    private List<NewsItem> newsItemList;
    private Category[] categories;

    public FrontPage(Category[] categories) {
        this.categories = categories;
        newsItemList=new ArrayList<>();
    }

    public void addNewsItem(NewsItem newsItem) {
        this.newsItemList.add(newsItem);
    }


    public List<NewsItem> listByCategory(Category c) {
        return newsItemList.stream()
                .filter(i->i.getCategory().equals(c.getCategory()))
                .collect(Collectors.toList());
    }

    public List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException{

        int count=(int) Arrays.stream(categories)
                .filter(i->i.getCategory().equals(category))
                .count();
        if(count==0)
        {
            throw new CategoryNotFoundException(category);
        }
        List<NewsItem> temp=newsItemList.stream()
                .filter(i->i.getCategory().equals(category))
                .collect(Collectors.toList());

        return temp;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        for(NewsItem newsItem:newsItemList)
        {
            stringBuilder.append(newsItem.getTeaser());
        }
        return stringBuilder.toString();
    }
}
public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
