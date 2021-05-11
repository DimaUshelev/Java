package service.third;

import common.Configuration;
import service.first.Ship;

import java.util.Vector;

public class Timer implements Runnable {
    public int commonTimeForAll;
    private boolean signal;
    private long counterShipInQueue;
    Vector<Ship> ships;

    public Timer(Vector<Ship> ships)
    {
        this.counterShipInQueue = 0;
        this.ships = ships;
        this.commonTimeForAll = 0;
        this.signal = false;
    }

    @Override
    public void run() {
        if (!signal)
            this.commonTimeForAll++;
        for (Ship s: ships)
        {
            if (s.getArrivalTime().getTime() <= commonTimeForAll && s.getShipDelayOnCrane().getTime() == 0)
            {
                ++counterShipInQueue;
            }
        }
        if (this.commonTimeForAll >= Configuration.getNumberDaysForSimulation() * 24 * 60)
        {
            signal = true;
        }
    }

    public int getCommonTimeForAll() {
        return commonTimeForAll;
    }
    public int getTotalAverageQueueLength()
    {
        return (int)(Math.ceil((double)counterShipInQueue / commonTimeForAll));
    }
    public boolean getSignal()
    {
        return signal;
    }
}
