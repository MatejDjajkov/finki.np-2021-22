package mk.ukim.finki.kolok2.weather;
import java.util.*;

interface Subscriber
{
    void update(float temperature, float humidity, float pressure);
}
class WeatherDispatcher
{
    List<Subscriber> subscriberList;
    public WeatherDispatcher() {
        subscriberList=new ArrayList<>();
    }

    public void remove(Subscriber subscriber) {
        subscriberList.remove(subscriber);
    }

    public void register(Subscriber subscriber) {
        subscriberList.add(subscriber);
    }

    public void setMeasurements(float parseFloat, float parseFloat1, float parseFloat2) {
        subscriberList.forEach(i->i.update(parseFloat, parseFloat1, parseFloat2));
    }
}
class CurrentConditionsDisplay implements Subscriber
{
    public CurrentConditionsDisplay(WeatherDispatcher weatherDispatcher) {
        weatherDispatcher.register(this);
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        System.out.printf("Temperature: %.1fF\n" +
                "Humidity: %.1f%%\n", temperature, humidity);

    }
}
class ForecastDisplay implements Subscriber
{
    static float prevPressure = (float) 0.0;
    public ForecastDisplay(WeatherDispatcher weatherDispatcher) {
        weatherDispatcher.register(this);
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        float res = Float.compare(pressure, prevPressure);
        if (res == 1) {
            System.out.println("Forecast: Improving");
        } else if (res == 0) {
            System.out.println("Forecast: Same");
        } else {
            System.out.println("Forecast: Cooler");
        }
        System.out.print("\n");
        prevPressure = pressure;
    }
}
public class WeatherApplication {

    public static void main(String[] args) {
        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            if(parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if(operation==1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if(operation==2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if(operation==3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if(operation==4) {
                    weatherDispatcher.register(currentConditions);
                }

            }
        }
    }
}
