package service.first;

import java.util.Random;

public class Time {
    private Integer time;

    public Time()
    {
        this.time = 0;
    }

    public Time(Integer minutes)
    {
        this.time = minutes;
    }

    public Time(Integer days, Integer hours, Integer minutes)
    {
        this.time = days * 24 * 60 + hours * 60 + minutes;
    }

    public void addTime(Integer minutes)
    {
        this.time += minutes;
    }

    public static Time toTime(String time)
    {
        time = time.trim();
        int firstDelimiter = time.indexOf(":");
        int secondDelimiter = time.lastIndexOf(":");
        if (secondDelimiter == -1 || firstDelimiter == secondDelimiter || firstDelimiter == -1)
            throw new StringIndexOutOfBoundsException("Invalid time was passed to transfer to the time object");
        String day = time.substring(0, firstDelimiter);
        String hour = time.substring(firstDelimiter + 1, secondDelimiter);
        String minutes = time.substring(secondDelimiter + 1);
        return new Time(Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minutes));
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    @Override
    public String toString() {
        int days = time / (24 * 60);
        int hours = (time - days * 24 * 60) / 60;
        int minutes = time - (days * 24 * 60 + hours * 60);
        return days + ":" + hours + ":" + minutes;
    }

    public static Time getArrivalData(int borderForDay, int borderForHours, int borderForMinutes)
    {
        try
        {
            if (borderForHours > 24 || borderForMinutes > 60)
                throw new IllegalArgumentException("Going beyond the logical boundary of time");
        }
        catch (IllegalArgumentException e)
        {
            e.getStackTrace();
        }
        return new Time(generateRandomDay(borderForDay), generateRandomHour(borderForHours),
                generateRandomMinutes(borderForMinutes));
    }

    private static int generateRandomDay(int borderForDay)
    {
        return new Random().nextInt(borderForDay);
    }
    private static int generateRandomHour(int borderForHours)
    {
        return new Random().nextInt(borderForHours);
    }
    private static int generateRandomMinutes(int borderForMinutes)
    {
        return new Random().nextInt(borderForMinutes);
    }
}
