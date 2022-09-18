import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class Measurement {
    // Creating a date format object
    static DateTimeFormatter dateFormat = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("dd-MMM-yyyy HH:mm")
            .toFormatter();
    public LocalDateTime dateTime;
    // Precipitation Amount (mm)
    public double rain;
    // Air Temperature (C)
    public double temp;
    // Wet Bulb Temperature (C)
    public double wetb;
    // Dew Point Temperature (C)
    public double dewpt;
    // Vapour Pressure (hPa)
    public double vappr;
    // Relative Humidity (%)
    public double rhum;
    // Mean Sea Level Pressure (hPa)
    public double msl;
    // Mean Wind Speed (knot)
    public double wdsp;
    // Predominant Wind Direction (degree)
    public double wddir;
    // Sunshine duration (hours)
    public double sun;

    public Measurement(String[] data) {
        // Parses date and time into DateTime object
        this.dateTime = LocalDateTime.parse(data[0], dateFormat);

        // Creates new array to hold converted double variables. The first element will be blank, as this was the
        // DateTime in the old array. Every other element is converted and mapped onto the new array
        // Data points are trimmed to remove whitespace. If empty (blank in the original file) they are set to 0
        // Every other variable is converted to a double
        double[] numData = new double[data.length];
        for (int i = 1; i < data.length; i++) {
            if (data[i].trim().isEmpty())
                numData[i] = 0;
            else
                numData[i] = Double.parseDouble(data[i]);
        }

        // Sets our data fields to the converted doubles in numData. Only some of the data points are needed
        this.rain = numData[2];
        this.temp = numData[4];
        this.wetb = numData[6];
        this.dewpt = numData[7];
        this.vappr = numData[8];
        this.rhum = numData[9];
        this.msl = numData[10];
        this.wdsp = numData[12];
        this.wddir = numData[14];
        this.sun = numData[17];
    }
}
