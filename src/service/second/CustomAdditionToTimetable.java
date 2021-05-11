package service.second;

import common.Configuration;
import service.first.Ship;
import service.first.Time;
import service.first.Timetable;
import service.first.TypeCargo;

import java.util.Scanner;

public class CustomAdditionToTimetable {
    public static void addCustomShipInTheTimetable(Timetable timetable)
    {
        String input;
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("Want to add a ship to the queue?");
            do {
                System.out.println("Y or N ?");
                input = in.nextLine();
            } while (!input.equalsIgnoreCase("n") && !input.equalsIgnoreCase("y"));
            if (input.equalsIgnoreCase("n")) {
                return;
            }
            String shipName;
            TypeCargo.TypesCargo cargo;
            int tonnage;
            Time arrivalTime;
            Time unloadTime;
            try {
                System.out.println("Enter the name of the ship :");
                shipName = in.nextLine();
                System.out.println("Enter the type of ship");
                input = in.nextLine();
                if (input.equalsIgnoreCase("BULK")) cargo = TypeCargo.TypesCargo.BULK;
                else if (input.equalsIgnoreCase("LIQUID")) cargo = TypeCargo.TypesCargo.LIQUID;
                else if (input.equalsIgnoreCase("CONTAINER")) cargo = TypeCargo.TypesCargo.CONTAINER;
                else
                {
                    throw new IllegalArgumentException("A nonexistent cargo type is entered");
                }
                System.out.println("Enter the tonnage for ship");
                tonnage = in.nextInt();
                Ship newShip = new Ship(shipName, cargo, tonnage);
                System.out.println("Enter the arrival time in the format dd:hh:mm");
                in.nextLine();
                input = in.nextLine();
                arrivalTime = Time.toTime(input);
                if (TypeCargo.TypesCargo.BULK == cargo)
                {
                    unloadTime = new Time((int)Math.ceil(tonnage / ((double) Configuration.getPerformanceCraneForBulkCargo() / 60)));
                }
                else if (TypeCargo.TypesCargo.LIQUID == cargo)
                {
                    unloadTime = new Time((int)Math.ceil(tonnage / ((double)Configuration.getPerformanceCraneForLiquidCargo() / 60)));
                }
                else
                {
                    unloadTime = new Time((int)Math.ceil(tonnage / ((double)Configuration.getPerformanceCraneForContainer() / 60)));
                }
                newShip.setArrivalTime(arrivalTime);
                newShip.setUnloadTime(unloadTime);
                timetable.addShipInTimetable(newShip);
            } catch (Exception e) {
                in.close();
                e.printStackTrace();
            }
        }

    }
}
