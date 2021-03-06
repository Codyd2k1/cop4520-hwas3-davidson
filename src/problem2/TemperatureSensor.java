package problem2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TemperatureSensor implements Runnable{
    static final int millisecondsBetweenReadings = 50; //reports generate every 3 seconds, 60 readings per report, 3/60 = 50 milliseconds.
    public int sensorID;
    public int largestTempDifferenceBetweenReadings;
    public int largestTempDifferenceTime;
    private int previousReading;

    public TemperatureSensor(int sensorID) {
        this.sensorID = sensorID;
        largestTempDifferenceBetweenReadings = 0;
        largestTempDifferenceTime = 0;
        previousReading = Integer.MIN_VALUE;
    }

    @Override
    public void run() {
        System.out.println("started temp sensor: " + sensorID);
        int time = 0;
        while(true) {
            //generate random number between -100 to 70
            int currentReading = (int) ((Math.random() * (71 - (-101))) + -101);
            AtomicTempModule.temperatureList.add(currentReading);
            System.out.println("sensor # " + sensorID +  " added temp: " + currentReading);
            //check if this is the largest difference
            if(previousReading != Integer.MIN_VALUE) {
                int currentDifference = Math.abs(currentReading - previousReading);
                if (currentDifference > largestTempDifferenceBetweenReadings) {
                    largestTempDifferenceBetweenReadings = currentDifference;
                    largestTempDifferenceTime= time;
                    System.out.println("Largest Temp difference on thread " + sensorID + " updated to: " + largestTempDifferenceBetweenReadings);
                }
            }
            previousReading = currentReading;
            //wait 1 minute between scans:
            try {
                TimeUnit.MILLISECONDS.sleep(millisecondsBetweenReadings);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time++;
        }
    }
}
