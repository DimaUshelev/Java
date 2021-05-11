package service.third;

import common.Configuration;
import service.first.Ship;
import service.first.Timetable;
import service.first.TypeCargo;

import java.util.Vector;

public class Port {
    private int totalNumberUnloadedShips;
    private long totalWeightUnloadedCargo;
    private final Vector<Ship> setOfUnloadedShipsInPort;
    private final Vector<Ship> setShipInPort;
    public Timer timer;


    public Port(Timetable timetable) {
        this.setShipInPort = new Vector<>();
        timetable.getSetShips().forEach((ship) -> this.setShipInPort.add(new Ship(ship)));
        this.totalNumberUnloadedShips = 0;
        this.setOfUnloadedShipsInPort = new Vector<>(10);
        this.timer = new Timer(this.setShipInPort);
    }

    public int getTotalNumberUnloadedShips() {
        return totalNumberUnloadedShips;
    }

    public void incTotalNumberUnloadedShips() {
        ++this.totalNumberUnloadedShips;
    }

    public long getTotalWeightUnloadedCargo() {
        return totalWeightUnloadedCargo;
    }

    public void addTotalWeightUnloadedCargo(long weightUnloadedCargo) {
        this.totalWeightUnloadedCargo += weightUnloadedCargo;
    }

    public void addShipForUnload(Ship ship)
    {
        setOfUnloadedShipsInPort.add(ship);
    }
    public boolean searchShipAmongUnloadedOnes(Ship ship)
    {
        return setOfUnloadedShipsInPort.contains(ship);
    }

    public Ship searchShipWithTypeCargo(TypeCargo.TypesCargo typesCargo) {
        Ship tempShip = new Ship("",typesCargo, Integer.MAX_VALUE);
        if (!setOfUnloadedShipsInPort.isEmpty())
            synchronized (setOfUnloadedShipsInPort) {
                for (Ship s : setOfUnloadedShipsInPort) {
                    if (s.getTypeCargo() == typesCargo && s.getTonnage() > TypeCargo.getPerformanceForThisType(typesCargo)) {
                        if (s.getTonnage() < tempShip.getTonnage() && s.getNumberUnloadingCranes() < Configuration.getMaxCraneOnOneShip()) {
                            tempShip = s;
                        }
                    }
                }
            }
        if (tempShip.getName().equals("")) return null;
        return tempShip;

    }

    public void deleteShipAmongUnloadedOnes(Ship ship)
    {
        setOfUnloadedShipsInPort.remove(ship);
    }

    public Vector<Ship> getSetShipInPort() {
        return setShipInPort;
    }
}
