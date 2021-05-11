package service.first;

import common.Configuration;
import service.second.Serializer;

import java.io.IOException;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;


public class Timetable {

    private Vector<Ship> setShips = new Vector<>();
    private final int numberShip;

    public Timetable()
    {
        Random r = new Random();
        this.numberShip = r.nextInt(Configuration.getMaxNumberShip() + 1);
    }

    public Timetable(Timetable timetable)
    {
        this.numberShip = timetable.getNumberShip();
        this.setShips = copyVector(timetable.getSetShips());
    }

    public Timetable createTimetableWithRandomValue()
    {
        for (int i = 0; i < numberShip; ++i)
        {
            setShips.add(GeneratorRandomShip.generateRandomShip());
        }
        Collections.sort(setShips);
        return this;
    }
    public void showTimetable()
    {
        setShips.forEach(System.out::println);
    }

    public Vector<Ship> getSetShips()
    {
        return setShips;
    }

    public void addShipInTimetable(Ship ship)
    {
        setShips.addElement(ship);
        Collections.sort(setShips);
    }

    public int getNumberShip()
    {
        return numberShip;
    }

    private static Vector<Ship> copyVector(Vector<Ship> setShip)
    {
        Vector<Ship> newSetShip = new Vector<>();
        setShip.forEach((ship) -> newSetShip.add(new Ship(ship)));
        return newSetShip;
    }

    public static void creatAccurateTimetable(Timetable timetable, String fileName) throws IOException {
        Random random = new Random();
        int deltaArrivalTime = Configuration.getDeltaArrivalAtThePort() * 24 * 60;
        for (int i = 0; i < timetable.getNumberShip(); ++i)
        {
            timetable.getSetShips().elementAt(i)
                    .setArrivalTime(new Time(timetable.getSetShips().elementAt(i).getArrivalTime().getTime()
                            + ((-1) * random.nextInt(deltaArrivalTime) + deltaArrivalTime)));
            if (timetable.getSetShips().elementAt(i).getArrivalTime().getTime() < 0)
            {
                timetable.getSetShips().elementAt(i).setArrivalTime(new Time(0));
            }
        }
        Collections.sort(timetable.getSetShips());
        if (fileName != null)
        {
            Serializer.serializeVectorShip(timetable.getSetShips(), fileName);
        }
    }
}
