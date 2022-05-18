package mk.ukim.finki.kolok1.logprocessor;

import java.util.*;
import java.util.stream.Collectors;

interface Log {
    int getLogId();

    String getLogMessage();

    double getLogProblemValue();

    int getStatusCode();
}

class SystemALog implements Log {

    private int logId;
    private String logMessage;

    public SystemALog(int logId, String logMessage) {
        this.logId = logId;
        this.logMessage = logMessage;
    }

    @Override
    public int getLogId() {
        return logId;
    }

    @Override
    public String getLogMessage() {
        return logMessage;
    }

    @Override
    public double getLogProblemValue() {
        return logMessage.hashCode() + 2.45 * logId;
    }

    @Override
    public int getStatusCode() {
        return logMessage.contains("STATUS CODE") ? 1 : 0;
    }

    @Override
    public String toString() {
        return "SystemALog{" +
                "logId=" + logId +
                ", logMessage='" + logMessage + '\'' +
                '}';
    }
}

class SystemBLog implements Log {
    private String logId;
    private String logMessage;

    public SystemBLog(String logId, String logMessage) {
        this.logId = logId;
        this.logMessage = logMessage;
    }

    @Override
    public int getLogId() {
        return logId.hashCode();
    }

    @Override
    public String getLogMessage() {
        return logMessage;
    }

    @Override
    public double getLogProblemValue() {
        return logMessage.hashCode() * 3.45 + getLogId() * 1.35;
    }

    @Override
    public int getStatusCode() {
        return logMessage.contains("STATUS CODE") ? 1 : 0;
    }

    @Override
    public String toString() {
        return "SystemBLog{" +
                "logId='" + logId + '\'' +
                ", logMessage='" + logMessage + '\'' +
                '}';
    }
}

interface LogProcessor<R> {
    R compute(List<? extends Log> Logs);
}

class Logs<T extends Log> {
    private final List<T> logs;

    public Logs(List<T> logs) {
        this.logs = logs;
    }

    public void process(LogProcessor<?> processor) {
        System.out.println(processor.compute(logs));
    }
}

public class LogProcessorTest<T extends Log> {

    public static void main(String[] args) {

        ArrayList<SystemALog> systemALogsList = new ArrayList<>();
        ArrayList<SystemBLog> systemBLogsList = new ArrayList<>();

        String[] logTexts = {"STATUS CODE 500 SERVER ERROR", "STATUS CODE 304", "STATUS CODE 200 OK", "DATABASE DOWN ERROR", "SERVICE X NOT RUNNING",
                "STATUS CODE 201", "STATUS CODE 302", "SERVICE Z NOT RUNNING", "AUTHENTICATION ERROR", "AUTHORIZATION ERROR"};

        Scanner sc = new Scanner(System.in);
        int countSystemALogs;
        countSystemALogs = Integer.parseInt(sc.nextLine());
        while (countSystemALogs > 0) {
            int id = Integer.parseInt(sc.nextLine());
            systemALogsList.add(new SystemALog(id, logTexts[id]));
            --countSystemALogs;
        }

        int countSystemBLogs;
        countSystemBLogs = Integer.parseInt(sc.nextLine());
        while (countSystemBLogs > 0) {
            String line = sc.nextLine();
            String[] parts = line.split(":");
            String text = Arrays.stream(parts).skip(1).collect(Collectors.joining(" "));
            systemBLogsList.add(new SystemBLog(parts[0], text));
            --countSystemBLogs;
        }

        Logs<SystemALog> logsA = new Logs<>(systemALogsList);

        //todo: first processor
        LogProcessor<Long> firstProcessor = logs -> logs
                .stream()
                .filter(i -> i.getLogMessage().contains("ERROR"))
                .count();
        System.out.println("----- RESULTS FROM THE FIRST PROCESSOR -----");
        logsA.process(firstProcessor);

        //todo: second processor
        LogProcessor<String> secondProcessor = logs -> String.format("Count: %d Min: %.2f Average: %.2f Max: %.2f",
                logs.size(),
                logs
                        .stream()
                        .mapToDouble(Log::getLogProblemValue)
                        .min()
                        .orElse(0),
                logs
                        .stream()
                        .mapToDouble(Log::getLogProblemValue)
                        .average()
                        .orElse(0),
                logs
                        .stream()
                        .mapToDouble(Log::getLogProblemValue)
                        .max()
                        .orElse(0));
        System.out.println("----- RESULTS FROM THE SECOND PROCESSOR -----");
        logsA.process(secondProcessor);

        Logs<SystemBLog> logsB = new Logs<>(systemBLogsList);

        //todo: third processor
        LogProcessor<List<Log>> thirdProcessor = logs -> logs.stream().sorted(Comparator.comparing(Log::getLogId)).collect(Collectors.toList());
        System.out.println("----- RESULTS FROM THE THIRD PROCESSOR -----");
        logsB.process(thirdProcessor);

        //TODO fourth processor
        LogProcessor<Double> fourthProcessor = logs -> logs.stream().filter(i -> i.getStatusCode() != 0).mapToDouble(Log::getLogProblemValue).sum();
        System.out.println("----- RESULTS FROM THE FOURTH PROCESSOR -----");
        logsB.process(fourthProcessor);
    }
}
