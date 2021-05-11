package service.third;

import common.Configuration;
import service.first.Ship;
import service.first.Time;
import service.first.Timetable;
import service.first.TypeCargo;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class PortOperationSimulation {
    public static void findOptimalPortOperation(Timetable timetable) throws IOException {
        int numberCraneForBULK = 1;
        int numberCraneForLIQUID = 1;
        int numberCraneForCONTAINER = 1;

        Object bulkMutex = new Object();
        Object liquidMutex = new Object();
        Object containerMutex = new Object();

        ResultSaver resultSaver = new ResultSaver();
        Timetable t = new Timetable(timetable);
        if (t.getSetShips().size() == 0)
        {
            ResultSaver.saveResults(resultSaver, "result.json");
            return;
        }
        Timetable.creatAccurateTimetable(t, "timetableWithAccurate.json");

        while (true) {
            AtomicInteger totalCostForBULK = new AtomicInteger(0);
            AtomicInteger totalCostForLIQUID = new AtomicInteger(0);
            AtomicInteger totalCostForCONTAINER = new AtomicInteger(0);


            Port port = new Port(t);
            Vector<Ship> tempVectorOfShip = port.getSetShipInPort();

            Queue<Ship> queueForBULK = addThisTypeShipsInQueue(tempVectorOfShip, TypeCargo.TypesCargo.BULK);
            Queue<Ship> queueForLIQUID = addThisTypeShipsInQueue(tempVectorOfShip, TypeCargo.TypesCargo.LIQUID);
            Queue<Ship> queueForCONTAINERS = addThisTypeShipsInQueue(tempVectorOfShip, TypeCargo.TypesCargo.CONTAINER);

            Vector<Ship> resultSetOfShip = new Vector<>();

            CyclicBarrier barrier
                    = new CyclicBarrier(numberCraneForBULK + numberCraneForLIQUID + numberCraneForCONTAINER, port.timer);

            Vector<Thread> threads = new Vector<>();

            for (int i = 0; i < numberCraneForBULK; ++i) {
                threads.add(new Thread(new Crane(port, TypeCargo.TypesCargo.BULK, queueForBULK, bulkMutex, barrier, resultSetOfShip)));
            }

            for (int i = 0; i < numberCraneForLIQUID; ++i) {
                threads.add(new Thread(new Crane(port, TypeCargo.TypesCargo.LIQUID, queueForLIQUID, liquidMutex, barrier, resultSetOfShip)));
            }

            for (int i = 0; i < numberCraneForCONTAINER; ++i) {
                threads.add(new Thread(new Crane(port, TypeCargo.TypesCargo.CONTAINER, queueForCONTAINERS, containerMutex, barrier, resultSetOfShip)));
            }
            threads.forEach(Thread::start);

            threads.forEach((thread -> {
                try {
                    thread.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));

            System.out.println("\n\n");
            Collections.sort(resultSetOfShip);

            resultSetOfShip.forEach((ship) -> {
                if (ship.getTonnage() == 0) {
                    ship.showDataForStatistic();
                }
            });

            showTotalNumberOfShipsUnloaded(port, resultSaver);

            showAverageQueueForUnloading(port, resultSaver);

            showAverageWaitingTimeInTheQueue(port, resultSetOfShip, resultSaver);

            showDelay(port, resultSetOfShip, resultSaver);

            int totalCost;
            AtomicInteger numberBULKShip = new AtomicInteger(0);
            AtomicInteger numberLIQUIDShip = new AtomicInteger(0);
            AtomicInteger numberCONTAINERShip = new AtomicInteger(0);

            resultSetOfShip.forEach((ship -> {
                if (ship.getTonnage() == 0) {
                    if (ship.getTypeCargo() == TypeCargo.TypesCargo.BULK) {
                        totalCostForBULK.addAndGet(ship.calculateAmountFine());
                        numberBULKShip.getAndIncrement();
                    }
                    else if (ship.getTypeCargo() == TypeCargo.TypesCargo.LIQUID) {
                        totalCostForLIQUID.addAndGet(ship.calculateAmountFine());
                        numberLIQUIDShip.getAndIncrement();
                    }
                    else if (ship.getTypeCargo() == TypeCargo.TypesCargo.CONTAINER) {
                        totalCostForCONTAINER.addAndGet(ship.calculateAmountFine());
                        numberCONTAINERShip.getAndIncrement();
                    }
                }
            }));

            resultSaver.addNumberBulkShip(numberBULKShip.get());
            resultSaver.addNumberLiquidShip(numberLIQUIDShip.get());
            resultSaver.addNumberContainerShip(numberCONTAINERShip.get());


            totalCost = totalCostForBULK.get() + totalCostForLIQUID.get() + totalCostForCONTAINER.get();
            totalCost += Configuration.getCostOneCrane() * (numberCraneForBULK + numberCraneForLIQUID + numberCraneForCONTAINER);
            System.out.println("Total cost " + totalCost);
            resultSaver.addTotalCost(totalCost);

            System.out.println("BULK - " + totalCostForBULK.get() + " crane " + numberCraneForBULK * Configuration.getCostOneCrane());
            System.out.println("LIQUID - " + totalCostForLIQUID.get() + " crane " + numberCraneForLIQUID * Configuration.getCostOneCrane());
            System.out.println("CONTAINER - " + totalCostForCONTAINER.get() + " crane " + numberCraneForCONTAINER * Configuration.getCostOneCrane());

            resultSaver.addCostBulk(totalCostForBULK.get() + numberCraneForBULK * Configuration.getCostOneCrane());
            resultSaver.addCostLiquid(totalCostForLIQUID.get() + numberCraneForLIQUID * Configuration.getCostOneCrane());
            resultSaver.addCostContainer(totalCostForCONTAINER.get() + numberCraneForCONTAINER * Configuration.getCostOneCrane());

            resultSaver.addNumberBulkCrane(numberCraneForBULK);
            resultSaver.addNumberLiquidCrane(numberCraneForLIQUID);
            resultSaver.addNumberContainerCrane(numberCraneForCONTAINER);

                System.out.println("Current statistic cranes :\n" + "BULK - " + numberCraneForBULK
                        + "\nLIQUID - " + numberCraneForLIQUID + "\nCONTAINER - " + numberCraneForCONTAINER);
                if (totalCostForBULK.get() > Configuration.getCostOneCrane()) {
                    if (numberCraneForBULK == 1 && totalCostForBULK.get() / Configuration.getCostOneCrane() > 2)
                    {
                        numberCraneForBULK += (int)(Math.floor((double)totalCostForBULK.get() / ( 2 * Configuration.getCostOneCrane()) - 2));
                    }
                    else
                    {
                        numberCraneForBULK++;
                    }
                }
                if (totalCostForLIQUID.get() > Configuration.getCostOneCrane()) {
                    if (numberCraneForLIQUID == 1 && totalCostForLIQUID.get() / Configuration.getCostOneCrane() > 2)
                    {
                        numberCraneForLIQUID += (int)(Math.floor((double)totalCostForLIQUID.get() / ( 2 * Configuration.getCostOneCrane()) - 2));
                    }
                    else
                    {
                        numberCraneForLIQUID++;
                    }
                }
                if (totalCostForCONTAINER.get() > Configuration.getCostOneCrane()) {
                    if (numberCraneForCONTAINER == 1 && totalCostForCONTAINER.get() / Configuration.getCostOneCrane() > 2)
                    {
                        numberCraneForCONTAINER += (int)(Math.floor((double)totalCostForCONTAINER.get() / ( 2 * Configuration.getCostOneCrane()) - 2));
                    }
                    else
                    {
                        numberCraneForCONTAINER++;
                    }
                }

            if (totalCostForBULK.get() < Configuration.getCostOneCrane()
                    && totalCostForLIQUID.get() < Configuration.getCostOneCrane()
                    && totalCostForCONTAINER.get() < Configuration.getCostOneCrane()) {
                System.out.println("The number of cranes need :\n" + "BULK - " + numberCraneForBULK
                        + "\nLIQUID - " + numberCraneForLIQUID + "\nCONTAINER - " + numberCraneForCONTAINER);
                ResultSaver.saveResults(resultSaver, "result.json");
                break;
            }
        }
    }
    
    private static void showTotalNumberOfShipsUnloaded(Port port,  ResultSaver resultSaver)
    {
        int countUnloadedShip = port.getTotalNumberUnloadedShips();
        System.out.println("total number of ships unloaded - " + countUnloadedShip);
        resultSaver.addTotalNumberShipsUnloaded(countUnloadedShip);
    }

    private static void showAverageQueueForUnloading(Port port, ResultSaver resultSaver)
    {
        System.out.println("average queue for unloading - " + port.timer.getTotalAverageQueueLength());
        resultSaver.addAverageLengthUnloadingQueue(port.timer.getTotalAverageQueueLength());
    }

    private static void showAverageWaitingTimeInTheQueue(Port port, Vector<Ship> resultSetOfShip, ResultSaver resultSaver)
    {
        int countUnloadedShip = port.getTotalNumberUnloadedShips();
        long fullWaitingTime = resultSetOfShip.stream().filter(ship -> ship.getTonnage() == 0)
                .mapToLong(ship -> ship.getWaitingTime().getTime()).sum();
        System.out.println("average waiting time in the queue - " + new Time((int) Math.ceil((double) fullWaitingTime / countUnloadedShip)));
        resultSaver.addAverageWaitingTimeInQueue(new Time((int) Math.ceil((double) fullWaitingTime / countUnloadedShip)));
    }

    private static void showDelay(Port port, Vector<Ship> resultSetOfShip, ResultSaver resultSaver)
    {

        int maxDelay = resultSetOfShip.stream().filter(ship -> ship.getTonnage() == 0)
                .mapToInt(ship -> ship.getShipDelayOnCrane().getTime()).max().orElse(Integer.MIN_VALUE);
        int averageDelay = resultSetOfShip.stream().filter(ship -> ship.getTonnage() == 0)
                .mapToInt(ship -> ship.getShipDelayOnCrane().getTime()).sum();
        averageDelay = (int) Math.ceil((double) averageDelay / port.getTotalNumberUnloadedShips());
        System.out.println("average delay - " + new Time(averageDelay));
        resultSaver.addAverageDelay(new Time(averageDelay));
        System.out.println("Max delay - " + new Time(maxDelay));
        resultSaver.addMaxDelay(new Time(maxDelay));
    }

    private static Queue<Ship> addThisTypeShipsInQueue(Vector<Ship> ships, TypeCargo.TypesCargo comparator)
    {
        Queue<Ship> queue = new LinkedList<>();
        ships.forEach((ship) -> {
            if (ship.getTypeCargo() == comparator)
            {
                queue.add(ship);
            }
        });
        return queue;
    }
}
