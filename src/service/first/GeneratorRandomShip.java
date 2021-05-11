package service.first;

import common.Configuration;

import java.util.Random;

public class GeneratorRandomShip {
    private static Random random = new Random();
    public static Ship generateRandomShip()
    {
        final TypeCargo.TypesCargo typesCargo = TypeCargo.generateRandomTypeCargo();
        int tonnage = 0;
        int unloadTime = 0;
        if (TypeCargo.TypesCargo.BULK == typesCargo)
        {
            tonnage = Configuration.getMinWeightForBulkCargo()
                    + random.nextInt(Configuration.getMaxWeightForBulkCargo() - Configuration.getMinWeightForBulkCargo() + 1);
            unloadTime = (int)Math.ceil(tonnage / ((double)Configuration.getPerformanceCraneForBulkCargo() / 60));
        }
        else if (TypeCargo.TypesCargo.LIQUID == typesCargo)
        {
            tonnage = Configuration.getMinWeightForLiquidCargo()
                    + random.nextInt(Configuration.getMaxWeightForLiquidCargo() - Configuration.getMinWeightForLiquidCargo() + 1);
            unloadTime = (int)Math.ceil(tonnage / ((double)Configuration.getPerformanceCraneForLiquidCargo() / 60));
        }
        else
        {
            tonnage = Configuration.getMinNumberContainer()
                    + random.nextInt(Configuration.getMaxNumberContainer() - Configuration.getMinNumberContainer() + 1);
            unloadTime = (int)Math.ceil(tonnage / ((double)Configuration.getPerformanceCraneForContainer() / 60));
        }

        Ship ship = new Ship(generateRandomName(15), typesCargo, tonnage);
        ship.setArrivalTime(Time.getArrivalData(Configuration.getNumberDaysForSimulation(), 24, 60));
        ship.setUnloadTime(new Time(unloadTime));
        return ship;
    }

    private static String generateRandomName(int sizeName)
    {
        Random r = new Random();
        StringBuffer name = new StringBuffer();
        for (int i = 0; i < sizeName; ++i)
        {
            name.insert(0, (char)(65 + r.nextInt(26) + r.nextInt(2) * (7 + r.nextInt(26))));
        }
        return new String(name);
    }

}
