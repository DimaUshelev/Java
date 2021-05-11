package service.first;

import com.fasterxml.jackson.annotation.JsonIgnore;
import common.Configuration;

public class Ship implements Comparable<Ship> {
    private final String name;
    private final TypeCargo.TypesCargo typeCargo;
    private Integer tonnage;
    private Time arrivalTime;
    private Time unloadTime;
    @JsonIgnore
    private Time waitingTime;
    @JsonIgnore
    private int numberUnloadingCranes;
    @JsonIgnore
    private Time shipDelayOnCrane;
    @JsonIgnore
    private Time endOfUnloading;

    public Ship(String name, TypeCargo.TypesCargo typeCargo, int tonnage) {
        this.name = name;
        this.typeCargo = typeCargo;
        this.tonnage = tonnage;
        this.waitingTime = new Time();
        this.numberUnloadingCranes = 0;
        this.shipDelayOnCrane = new Time();
        this.endOfUnloading = new Time();
    }
    public Ship(Ship ship)
    {
        this.name = ship.name;
        this.typeCargo = ship.typeCargo;
        this.tonnage = ship.tonnage;
        this.arrivalTime = new Time(ship.arrivalTime.getTime());
        this.unloadTime = new Time(ship.unloadTime.getTime());
        this.waitingTime = new Time(ship.waitingTime.getTime());
        this.numberUnloadingCranes = ship.numberUnloadingCranes;
        this.shipDelayOnCrane = new Time(ship.shipDelayOnCrane.getTime());
        this.endOfUnloading = new Time(ship.endOfUnloading.getTime());
    }

    public String getName() {
        return name;
    }

    public TypeCargo.TypesCargo getTypeCargo() {
        return typeCargo;
    }

    public Integer getTonnage() {
        return tonnage;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public Time getUnloadTime() {
        return unloadTime;
    }

    public Time getWaitingTime() {
        return waitingTime;
    }

    public int getNumberUnloadingCranes() {
        return numberUnloadingCranes;
    }

    public Time getShipDelayOnCrane() {
        return shipDelayOnCrane;
    }

    public Time getEndOfUnloading() {
        return endOfUnloading;
    }

    public void setTonnage(Integer tonnage) {
        this.tonnage = tonnage;
    }

    public void setArrivalTime(Time arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setUnloadTime(Time unloadTime) {
        this.unloadTime = unloadTime;
    }

    public void setWaitingTime(Time waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setNumberUnloadingCranes(int numberUnloadingCranes) {
        this.numberUnloadingCranes = numberUnloadingCranes;
    }

    public void setShipDelayOnCrane(Time shipDelayOnCrane) {
        this.shipDelayOnCrane = shipDelayOnCrane;
    }

    public void setEndOfUnloading(Time endOfUnloading) {
        this.endOfUnloading = endOfUnloading;
    }

    public void addWaitingTime(int minutes) {
        this.waitingTime.addTime(minutes);
    }

    public void decNumberUnloadingCranes(int dec)
    {
        if (this.numberUnloadingCranes - dec < 0)
        {
            throw new IllegalArgumentException("One ship can be unloaded by one or two cranes");
        }
        this.numberUnloadingCranes -= dec;
    }

    public void incNumberUnloadingCranes() throws IllegalArgumentException {
        if (this.numberUnloadingCranes == 2)
        {
            throw new IllegalArgumentException("One crane cannot be unloaded by more than two cranes");
        }
        ++this.numberUnloadingCranes;
    }

    public void unload(int tonnageUnload)
    {
        if (this.tonnage - tonnageUnload > 0)
        {
            this.tonnage -= tonnageUnload;
        }
        else
        {
            int resTonnage = this.tonnage;
            this.tonnage = 0;
        }
    }

    public int calculateAmountFine()
    {
        if (waitingTime.getTime() - (int)Math.ceil(((double)unloadTime.getTime())) > 0)
        {
            int temp = ((int)(Math.ceil((double)(waitingTime.getTime()
                    - (int)Math.ceil(((double)unloadTime.getTime()))) / 60)));
            temp *= Configuration.getFineForOneHour();
            return temp;

        }
        return 0;
    }

    public void showDataForStatistic()
    {
        String timeStartUnload = new Time(this.getArrivalTime().getTime() + this.getWaitingTime().getTime()).toString();
        String str = this.name + " " + this.getTypeCargo() + " " + this.getArrivalTime().toString()
                + " " + this.getWaitingTime().toString()
                + " " + timeStartUnload + " " + endOfUnloading.toString() + " " + this.shipDelayOnCrane.toString();
        System.out.println(str);
    }


    @Override
    public String toString() {
        return name + " " + typeCargo + " " + tonnage + " " + arrivalTime + " " + unloadTime;
    }

    @Override
    public int compareTo(Ship o) {
        return Integer.compare(this.arrivalTime.getTime(), o.arrivalTime.getTime());
    }
}
