package mk.ukim.finki.kolok2.filesystem;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Partial exam II 2016/2017
 */
class File implements Comparable<File>
{
    private char folder;
    private String name;
    private Integer size;
    private LocalDateTime localDateTime;

    public File(char folder,String name, Integer size, LocalDateTime localDateTime) {
        this.folder=folder;
        this.name = name;
        this.size = size;
        this.localDateTime = localDateTime;
    }
    public int getYear()
    {
        return localDateTime.getYear();

    }
    public File getFile()
    {
        return this;
    }
    public String getMonthAndDay()
    {
        return String.format("%s-%d",localDateTime.getMonth().toString(),localDateTime.getDayOfMonth());
    }
            //APRIL-15

    public Integer getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public char getFolder() {
        return folder;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    @Override
    public String toString() {
        return String.format("%-10s %5sB %s",name,size,localDateTime.toString());
    }

    @Override
    public int compareTo(File other) {
        Comparator<File> fileComparator=Comparator.comparing(File::getLocalDateTime)
                .thenComparing(File::getName)
                .thenComparing(File::getSize);
        return fileComparator.compare(this,other);
    }
}
class FileSystem
{
    Map<String,File> filesByName;
    public FileSystem() {
        filesByName=new HashMap<>();
    }
    public void addFile(char folder, String name, int size, LocalDateTime createdAt)
    {
        filesByName.put(name,new File(folder,name,size,createdAt));

    }

    public List<File> findAllHiddenFilesWithSizeLessThen(int size) {
        Comparator<File> comparator=Comparator.comparing(File::getFolder)
                .thenComparing(File::getLocalDateTime)
                .thenComparing(File::getName)
                .thenComparing(File::getSize);
        return filesByName.values()
                .stream()
                .filter(i->i.getSize()<size&&i.getName().charAt(0)=='.')
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public int totalSizeOfFilesFromFolders(List<Character> collect) {
        return filesByName.values()
                .stream()
                .filter(i->collect.contains(i.getFolder()))
                .mapToInt(File::getSize)
                .sum();
    }

    public Map<Integer, Set<File>> byYear() {
        return filesByName.values()
                .stream()
                .collect(Collectors.groupingBy(File::getYear,Collectors.toSet()));
    }

    public Map<String, Long> sizeByMonthAndDay() {
        return filesByName.values()
                .stream()
                .collect(Collectors.groupingBy(File::getMonthAndDay,Collectors.summingLong(File::getSize)));
    }
}
public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}
