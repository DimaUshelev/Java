package service.first;

import common.Configuration;

import java.util.Random;

public class TypeCargo {
    public enum TypesCargo
    {
        BULK,
        LIQUID,
        CONTAINER,
    }
    public static int getPerformanceForThisType(TypesCargo thisType)
    {
        if (thisType == TypesCargo.BULK)
        {
            return (int)Math.ceil(((double) Configuration.getPerformanceCraneForBulkCargo() / 60));
        }
        else if (thisType== TypesCargo.LIQUID)
        {
            return (int)Math.ceil(((double)Configuration.getPerformanceCraneForLiquidCargo() / 60));
        }
        else
        {
            return (int)Math.ceil(((double)Configuration.getPerformanceCraneForContainer() / 60));
        }
    }
    public static TypesCargo generateRandomTypeCargo()
    {
        Random r = new Random();
        return switch (r.nextInt(3)) {
            case 0 -> TypesCargo.BULK;
            case 1 -> TypesCargo.LIQUID;
            default -> TypesCargo.CONTAINER;
        };
    }
}
