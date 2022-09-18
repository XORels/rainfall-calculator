import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.DoubleStream;

import static java.util.stream.Collectors.toList;

public class Main {
    public static void main(String[] args) {
        // Each instance of Measurement is one record in the file.
        // The ArrayList below will therefore contain the whole file row by row
        ArrayList<Measurement> dataset = new ArrayList<>();
        {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader("hly3723.csv"));

                // Skips the first 24 lines of header info
                for (int i = 1; i < 25; i++) {
                    bufferedReader.readLine();
                }

                String line;
                // Imports file line by line, splits tokens at commas and stores in an array of String values
                // This array of data is passed to the Measurement constructor and the new Measurement is added
                // to dataset
                while ((line = bufferedReader.readLine()) != null) {
                    String[] data = line.split(",");
                    dataset.add(new Measurement(data));
                }

                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Calculates max among air, wet bulb and dew point temperatures
        OptionalDouble maxTemp = dataset
                .parallelStream()
                .flatMapToDouble(d -> DoubleStream.of(d.temp, d.wetb, d.dewpt))
                .max();

        // Calculates average amount of sunlight per hour
        OptionalDouble avgSun = dataset
                .parallelStream()
                .mapToDouble(d -> d.sun)
                .average();

        // Calculates total rainfall across all records
        double totalRain = dataset
                .parallelStream()
                .mapToDouble(d -> d.rain)
                .sum();

        // Calculates sunlight hours in November 2001, ie between 31 October 11.59pm and 1 December 00:00
        double nov2001Sun = dataset
                .parallelStream()
                .filter(d -> d.dateTime.isAfter(LocalDateTime.of(2001, 10, 31, 23, 59))
                        && d.dateTime.isBefore(LocalDateTime.of(2001, 12, 1, 0, 0)))
                .mapToDouble(d -> d.sun)
                .sum();

        // Calculates hours where the rainfall was greater than 5mm
        double rainGreater = dataset
                .parallelStream()
                .filter(d -> d.rain > 5)
                .count();

        // Creates a list of DateTime objects when Mean Sea Level Pressure was higher than 1047.2 hPa
        List<LocalDateTime> mslTimes = dataset
                .parallelStream()
                .filter(d -> d.msl > 1047.2)
                .map(d -> d.dateTime)
                .collect(toList());

        System.out.println("Maximum temperature across all data points: " + maxTemp.getAsDouble());

        System.out.println("Average sunshine per hour across all data points: " + avgSun.getAsDouble());

        System.out.printf("Total rainfall across all data points: %.2f %n", totalRain);

        System.out.printf("Total sunshine hours in November 2001: %.2f %n", nov2001Sun);

        System.out.printf("The number of hours where rainfall was greater than 5mm: %.0f %n", rainGreater);

        System.out.print("The list of times when Mean Sea Level Pressure exceeded 1047.2 hPa: ");
        mslTimes.forEach(d -> System.out.print(d.format(Measurement.dateFormat) + " "));
    }
}
