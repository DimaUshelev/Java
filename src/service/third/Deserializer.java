package service.third;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.first.Ship;
import service.first.Timetable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

public class Deserializer {
    public static Timetable deserializeTimetable(String nameFile) throws IOException {
        if (nameFile == null || nameFile.length() == 0)
        {
            throw new FileNotFoundException("Such file not exist");
        }
        ObjectMapper or = new ObjectMapper();
        return or.readValue(new File(nameFile), Timetable.class);
    }

    public static Vector<Ship> deserializeSetShip(String nameFile) throws IOException {
        if (nameFile == null || nameFile.length() == 0)
        {
            throw new FileNotFoundException("Such file not exist");
        }
        ObjectMapper or = new ObjectMapper();
        return or.readValue(new File(nameFile), new TypeReference<>() {
        });
    }

}
