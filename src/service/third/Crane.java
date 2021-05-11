package service.third;

import common.Configuration;
import service.first.Ship;
import service.first.Time;
import service.first.TypeCargo;

import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Crane implements Runnable {

    private Ship unloadedShip;
    private final Port port;
    private final Queue<Ship> queueShip;
    private final TypeCargo.TypesCargo typesCargo;
    private static final Random random = new Random();
    private final Object mutex;
    private final CyclicBarrier barrier;
    private final Vector<Ship> setUnloadedShip;

    public Crane(Port port, TypeCargo.TypesCargo typesCargo, Queue<Ship> queue, Object mutex,CyclicBarrier barrier, Vector<Ship> res) {
        this.unloadedShip = null;
        this.port = port;
        this.typesCargo = typesCargo;
        this.queueShip = queue;
        this.mutex = mutex;
        this.unloadedShip = null;
        this.barrier = barrier;
        this.setUnloadedShip = res;
    }


    @Override
    public void run() throws IllegalArgumentException
    {
        try {
            int randomValueDelay;
            int internalDelayTimer = 0;
            do {
                if (unloadedShip == null) {
                    randomValueDelay = random.nextInt(Configuration.getMaxUnloadDelay() * 24 * 60 + 1);
                    internalDelayTimer = randomValueDelay;
                    synchronized (mutex) {
                        Ship tempShip = port.searchShipWithTypeCargo(typesCargo);
                        if (tempShip != null) {
                            int unloadTimeForTempShip = 0;
                            int waitingTimeForFirstShipInTheQueue = 0;
                            if (!queueShip.isEmpty()) {
                                unloadTimeForTempShip = (int) Math.ceil(tempShip.getTonnage()
                                        / ((double) TypeCargo.getPerformanceForThisType(tempShip.getTypeCargo())));
                                assert queueShip.peek() != null;
                                waitingTimeForFirstShipInTheQueue = port.timer.getCommonTimeForAll()
                                        - queueShip.peek().getArrivalTime().getTime();
                            }
                            if (tryConnectShipWitSecondCrane(queueShip,tempShip, waitingTimeForFirstShipInTheQueue, unloadTimeForTempShip)) {
                                unloadedShip = tempShip;
                                internalDelayTimer = unloadedShip.getShipDelayOnCrane().getTime();
                                unloadedShip.incNumberUnloadingCranes();
                            } else {
                                if (waitingTimeForFirstShipInTheQueue >= 0 && !queueShip.isEmpty()) {
                                    excludeShipFromQueue();
                                    unloadedShip.setShipDelayOnCrane(new Time(randomValueDelay));
                                }
                            }
                        } else {
                            if (!queueShip.isEmpty()) {
                                assert queueShip.peek() != null;
                                if (port.timer.getCommonTimeForAll() - queueShip.peek().getArrivalTime().getTime() >= 0) {
                                    excludeShipFromQueue();
                                    unloadedShip.setShipDelayOnCrane(new Time(randomValueDelay));
                                }
                            }
                        }
                    }
                } else {
                    synchronized (unloadedShip) {
                        unloadedShip.unload(TypeCargo.getPerformanceForThisType(typesCargo));
                        if (unloadedShip.getTonnage() == 0) --internalDelayTimer;

                        if (unloadedShip.getTonnage() == 0 && internalDelayTimer <= 0) {
                            if (unloadedShip != null && port.searchShipAmongUnloadedOnes(unloadedShip)) {
                                synchronized (port) {
                                    port.incTotalNumberUnloadedShips();
                                }
                            }
                            assert unloadedShip != null;
                            unloadedShip.decNumberUnloadingCranes(unloadedShip.getNumberUnloadingCranes());
                            unloadedShip.setEndOfUnloading(new Time(port.timer.getCommonTimeForAll()));
                            synchronized (port) {
                                port.deleteShipAmongUnloadedOnes(unloadedShip);
                            }
                            unloadedShip = null;
                        }
                    }

                }


                barrier.await();
            } while (!port.timer.getSignal());
        }
        catch (InterruptedException | BrokenBarrierException e)
        {
            e.printStackTrace();
        }
    }

    private boolean tryConnectShipWitSecondCrane(Queue<Ship> queueShip, Ship tempShip,
                                                 int waitingTimeForFirstShipInTheQueue, int unloadTimeForTempShip)
    {
        int allWaitingTime = unloadTimeForTempShip + waitingTimeForFirstShipInTheQueue;
        return (!queueShip.isEmpty()
                && waitingTimeForFirstShipInTheQueue > 0
                && (double) (Objects.requireNonNull(queueShip.peek()).getTonnage() / tempShip.getTonnage()) < 1.25
                && (double) (Objects.requireNonNull(queueShip.peek()).getTonnage() / tempShip.getTonnage()) > 0.7
                && allWaitingTime < Objects.requireNonNull(queueShip.peek()).getUnloadTime().getTime()
                && tempShip.getNumberUnloadingCranes() < 2)
                || (waitingTimeForFirstShipInTheQueue < 0 && tempShip.getNumberUnloadingCranes() < 2)
                || (tempShip.getNumberUnloadingCranes() < 2 && queueShip.isEmpty());
    }

    private synchronized void excludeShipFromQueue() {
        synchronized (queueShip) {
            unloadedShip = queueShip.poll();


            synchronized (setUnloadedShip) {
                setUnloadedShip.add(unloadedShip);
            }

            if (unloadedShip.getTypeCargo() != this.typesCargo) {
                throw new IllegalArgumentException("A ship with a different " +
                        "type of cargo has been transferred to this crane");
            }
            unloadedShip.incNumberUnloadingCranes();
            unloadedShip.addWaitingTime(port.timer.getCommonTimeForAll() - unloadedShip.getArrivalTime().getTime());
            port.addShipForUnload(unloadedShip);
        }
    }
}

