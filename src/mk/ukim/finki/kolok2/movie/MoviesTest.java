// mk.ukim.finki.kolok2.movie;
import java.util.*;
import java.util.stream.Collectors;

class Movie
{
    private String name;
    private List<Integer> allratings;
    static int  maxsize;

    public Movie(String name, int[] ratings) {
        this.name = name;
        this.allratings=new ArrayList<>();
        for(int rating:ratings)
        {
            allratings.add(rating);
        }
        if(maxsize<allratings.size())
        {
            maxsize=allratings.size();
        }

    }
    public double getGrade()
    {
        return allratings.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    public String getName() {
        return name;
    }
    public double getCoef()
    {
        //просечен ретјтинг на филмот x вкупно број на рејтинзи на филмот / максимален број на рејтинзи (од сите филмови во листата)
        return getGrade()*allratings.size()*1.0;
    }

    public List<Integer> getAllratings() {
        return allratings;
    }

    public static int getMaxsize() {
        return maxsize;
    }

    @Override
    public String toString() {
        //Love on the Run (1979) (7.33) of 6 ratings
        return String.format("%s (%.2f) of %d ratings",name,getGrade(),allratings.size());

    }
}
class MoviesList
{
    private List<Movie> movies;

    final Comparator<Movie> GradeAndTitle=Comparator.comparing(Movie::getGrade)
            .reversed()
            .thenComparing(Movie::getName);

    final Comparator<Movie> Coeficient=Comparator.comparing(Movie::getCoef)
            .thenComparing(Movie::getGrade)
            .thenComparing(Movie::getName);


    public MoviesList() {
        this.movies = new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings) {
        movies.add(new Movie(title,ratings));
    }

    public List<Movie> top10ByAvgRating() {
        return movies.stream()
                .sorted(GradeAndTitle)
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Movie> top10ByRatingCoef() {
        List<Movie>list =movies.stream()
                .sorted(Coeficient)
                .collect(Collectors.toList());
        Collections.reverse(list);
        return list.stream()
                .limit(10)
                .collect(Collectors.toList());
    }
}
public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}
