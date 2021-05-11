package common;

public class Configuration {
    public static int fineForOneHour = 100;
    public static int costOneCrane = 30000;

    public static int maxCraneOnOneShip = 2;

    public static int numberDaysForSimulation = 30;

    public static int deltaArrivalAtThePort = 7;
    public static int maxUnloadDelay = 1;

    public static int maxNumberShip = 2000;

    public static int maxWeightForBulkCargo = 1000;
    public static int minWeightForBulkCargo = 700;

    public static int maxWeightForLiquidCargo = 1000;
    public static int minWeightForLiquidCargo = 700;

    public static int maxNumberContainer = 1000;
    public static int minNumberContainer = 700;

    public static int performanceCraneForBulkCargo = 12;
    public static int performanceCraneForLiquidCargo = 12;
    public static int performanceCraneForContainer = 12;

    public static int getFineForOneHour() {
        return fineForOneHour;
    }
    public static int getCostOneCrane() {
        return costOneCrane;
    }
    public static int getMaxCraneOnOneShip() {
        return maxCraneOnOneShip;
    }
    public static int getNumberDaysForSimulation() {
        return numberDaysForSimulation;
    }
    public static int getDeltaArrivalAtThePort() {
        return deltaArrivalAtThePort;
    }
    public static int getMaxUnloadDelay() {
        return maxUnloadDelay;
    }
    public static int getMaxNumberShip() {
        return maxNumberShip;
    }
    public static int getMaxWeightForBulkCargo() {
        return maxWeightForBulkCargo;
    }
    public static int getMinWeightForBulkCargo() {
        return minWeightForBulkCargo;
    }
    public static int getMaxWeightForLiquidCargo() {
        return maxWeightForLiquidCargo;
    }
    public static int getMinWeightForLiquidCargo() {
        return minWeightForLiquidCargo;
    }
    public static int getMaxNumberContainer() {
        return maxNumberContainer;
    }
    public static int getMinNumberContainer() {
        return minNumberContainer;
    }
    public static int getPerformanceCraneForBulkCargo() {
        return performanceCraneForBulkCargo;
    }
    public static int getPerformanceCraneForLiquidCargo() {
        return performanceCraneForLiquidCargo;
    }
    public static int getPerformanceCraneForContainer() {
        return performanceCraneForContainer;
    }
}
