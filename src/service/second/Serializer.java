package service.second;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import service.first.Ship;
import service.first.Timetable;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class Serializer {
    public static void serializeTimetable(Timetable timetable, String nameFile) throws IOException
    {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ow.writeValue(new File(nameFile), timetable);
    }

    public static void serializeVectorShip(Vector<Ship> setShip, String nameFile) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ow.writeValue(new File(nameFile), setShip);
    }
}
