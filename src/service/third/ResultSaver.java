package service.third;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import service.first.Time;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

@JsonAutoDetect
public class ResultSaver {
    public Vector<Integer> averageLengthUnloadingQueue;
    public Vector<Time> averageWaitingTimeInQueue;
    public Vector<Time> averageDelay;
    public Vector<Time> maxDelay;
    public Vector<Integer> totalNumberShipsUnloaded;
    public Vector<Integer> numberBulkShip;
    public Vector<Integer> numberLiquidShip;
    public Vector<Integer> numberContainerShip;
    public Vector<Integer> totalCost;
    public Vector<Integer> costBulk;
    public Vector<Integer> costLiquid;
    public Vector<Integer> costContainer;
    public Vector<Integer> numberBulkCrane;
    public Vector<Integer> numberLiquidCrane;
    public Vector<Integer> numberContainerCrane;

    ResultSaver()
    {
        averageLengthUnloadingQueue = new Vector<>();
        averageDelay = new Vector<>();
        averageWaitingTimeInQueue = new Vector<>();
        maxDelay = new Vector<>();
        totalNumberShipsUnloaded = new Vector<>();
        numberBulkShip = new Vector<>();
        numberLiquidShip = new Vector<>();
        numberContainerShip = new Vector<>();
        totalCost = new Vector<>();
        costBulk = new Vector<>();
        costLiquid = new Vector<>();
        costContainer = new Vector<>();
        numberBulkCrane = new Vector<>();
        numberLiquidCrane = new Vector<>();
        numberContainerCrane = new Vector<>();
    }

    public void addAverageDelay(Time averageDelay) {
        this.averageDelay.add(averageDelay);
    }
    public void addAverageLengthUnloadingQueue(int averageLengthUnloadingQueue) {
        this.averageLengthUnloadingQueue.add(averageLengthUnloadingQueue);
    }
    public void addAverageWaitingTimeInQueue(Time averageWaitingTimeInQueue) {
        this.averageWaitingTimeInQueue.add(averageWaitingTimeInQueue);
    }
    public void addMaxDelay(Time maxDelay) {
        this.maxDelay.add(maxDelay);
    }
    public void addTotalCost(int totalCost) {
        this.totalCost.add(totalCost);
    }
    public void addTotalNumberShipsUnloaded(int totalNumberShipsUnloaded) {
        this.totalNumberShipsUnloaded.add(totalNumberShipsUnloaded);
    }
    public void addNumberBulkCrane(int numberBulkCrane) {
        this.numberBulkCrane.add(numberBulkCrane);
    }
    public void addNumberContainerCrane(int numberContainerCrane) {
        this.numberContainerCrane.add(numberContainerCrane);
    }
    public void addNumberLiquidCrane(int numberLiquidCrane) {
        this.numberLiquidCrane.add(numberLiquidCrane);
    }

    public void addCostBulk(int costBulk) {
        this.costBulk.add(costBulk);
    }

    public void addCostLiquid(int costLiquid) {
        this.costLiquid.add(costLiquid);
    }

    public void addCostContainer(int costContainer) {
        this.costContainer.add(costContainer);
    }

    public void addNumberBulkShip(int numberBulkShip) {
        this.numberBulkShip.add(numberBulkShip);
    }

    public void addNumberLiquidShip(int numberLiquidShip) {
        this.numberLiquidShip.add(numberLiquidShip);
    }

    public void addNumberContainerShip(int numberContainerShip) {
        this.numberContainerShip.add(numberContainerShip);
    }

    public static void saveResults(ResultSaver result, String nameFile) throws IOException {
        if (result.totalNumberShipsUnloaded.size() == 0) return;
        int index = 0;
        int mimCost = Integer.MAX_VALUE;
        for (int i = 0; i < result.totalCost.size(); ++i) {
            if (result.totalCost.elementAt(i) < mimCost) {
                mimCost = result.totalCost.elementAt(i);
                index = i;
            }
        }
        Vector<ResultSaver> res = new Vector<>();
        ResultSaver minRes = new ResultSaver();
        ResultSaver optimalRes = new ResultSaver();

        minRes.totalCost.add(result.totalCost.elementAt(index));
        minRes.costBulk.add(result.costBulk.elementAt(index));
        minRes.costLiquid.add(result.costLiquid.elementAt(index));
        minRes.costContainer.add(result.costContainer.elementAt(index));
        minRes.numberContainerCrane.add(result.numberContainerCrane.elementAt(index));
        minRes.numberBulkCrane.add(result.numberBulkCrane.elementAt(index));
        minRes.numberLiquidCrane.add(result.numberLiquidCrane.elementAt(index));
        minRes.numberBulkShip.add(result.numberBulkShip.elementAt(index));
        minRes.numberLiquidShip.add(result.numberLiquidShip.elementAt(index));
        minRes.numberContainerShip.add(result.numberContainerShip.elementAt(index));
        minRes.maxDelay.add(result.maxDelay.elementAt(index));
        minRes.averageDelay.add(result.averageDelay.elementAt(index));
        minRes.averageWaitingTimeInQueue.add(result.averageWaitingTimeInQueue.elementAt(index));
        minRes.averageLengthUnloadingQueue.add(result.averageLengthUnloadingQueue.elementAt(index));
        minRes.totalNumberShipsUnloaded.add(result.totalNumberShipsUnloaded.elementAt(index));

        optimalRes.totalCost.add(result.totalCost.elementAt(result.totalCost.size() - 1));
        optimalRes.costBulk.add(result.costBulk.elementAt(result.totalCost.size() - 1));
        optimalRes.costLiquid.add(result.costLiquid.elementAt(result.totalCost.size() - 1));
        optimalRes.costContainer.add(result.costContainer.elementAt(result.totalCost.size() - 1));
        optimalRes.numberContainerCrane.add(result.numberContainerCrane.elementAt(result.numberContainerCrane.size() - 1));
        optimalRes.numberBulkCrane.add(result.numberBulkCrane.elementAt(result.numberBulkCrane.size() - 1));
        optimalRes.numberLiquidCrane.add(result.numberLiquidCrane.elementAt(result.numberLiquidCrane.size() - 1));
        optimalRes.numberBulkShip.add(result.numberBulkShip.elementAt(result.numberBulkShip.size() - 1));
        optimalRes.numberLiquidShip.add(result.numberLiquidShip.elementAt(result.numberLiquidShip.size() - 1));
        optimalRes.numberContainerShip.add(result.numberContainerShip.elementAt(result.numberContainerShip.size() - 1));
        optimalRes.maxDelay.add(result.maxDelay.elementAt(result.maxDelay.size() - 1));
        optimalRes.averageDelay.add(result.averageDelay.elementAt(result.averageDelay.size() - 1));
        optimalRes.averageWaitingTimeInQueue.add(result.averageWaitingTimeInQueue.elementAt(result.averageWaitingTimeInQueue.size() - 1));
        optimalRes.averageLengthUnloadingQueue.add(result.averageLengthUnloadingQueue.elementAt(result.averageLengthUnloadingQueue.size() -1));
        optimalRes.totalNumberShipsUnloaded.add(result.totalNumberShipsUnloaded.elementAt(result.totalNumberShipsUnloaded.size() - 1));

        res.add(minRes);
        res.add(optimalRes);


        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ow.writeValue(new File(nameFile), res);
    }

}
