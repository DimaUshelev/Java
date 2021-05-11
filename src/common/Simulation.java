package common;

import service.first.Timetable;
import service.second.CustomAdditionToTimetable;
import service.second.Serializer;
import service.third.PortOperationSimulation;

import java.nio.file.Files;
import java.nio.file.Path;

public class Simulation {
    public static void main(String[] args) {
        try {

            Files.deleteIfExists(Path.of("result.json"));
            Files.deleteIfExists(Path.of("timetable.json"));
            Files.deleteIfExists(Path.of("timetableWithAccurate.json"));
            Timetable ourTimetable = new Timetable().createTimetableWithRandomValue();
            ourTimetable.showTimetable();
            //CustomAdditionToTimetable.addCustomShipInTheTimetable(ourTimetable);
            Serializer.serializeTimetable(ourTimetable, "timetable.json");
            PortOperationSimulation.findOptimalPortOperation(ourTimetable);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
