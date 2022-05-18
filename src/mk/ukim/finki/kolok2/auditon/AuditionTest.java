//package mk.ukim.finki.kolok2.auditon;

import java.util.*;

class Participant
{
    String city;
    String code;
    String name;
    int age;

    public Participant(String city, String code, String name, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d",code,name,age);
        //003 Аце 17
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return getCode().equals(that.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }

}
class Audition
{
    private Map<String, Set<Participant>> participantByCity;

    public Audition() {
        this.participantByCity=new HashMap<>();

    }

    public void addParticpant(String part, String part1, String part2, int parseInt) {
        Participant participant=new Participant(part,part1,part2,parseInt);
        participantByCity.putIfAbsent(part,new HashSet<>());
        participantByCity.get(part).add(participant);
    }

    public void listByCity(String city) {
        Set<Participant> participantsFromCity=participantByCity.get(city);
        final Comparator<Participant> comparator=Comparator.comparing(Participant::getName)
                .thenComparing(Participant::getAge)
                .thenComparing(Participant::getCode);
        participantsFromCity.stream()
                .sorted(comparator)
                .forEach(i-> System.out.println(i));
    }
}
public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}