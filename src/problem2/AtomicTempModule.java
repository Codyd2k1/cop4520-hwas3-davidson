package problem2;

import problem1secondtry.LockFreeList;
import problem1secondtry.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class AtomicTempModule{
    private static final int numTemperatureSensors = 8;
    private static final int minutesBetweenReports = 60;
    private static List<TemperatureSensor> temperatureSensorList = new ArrayList<TemperatureSensor>();
    public static LockFreeList<Integer> temperatureList;

    public static void main(String[] args) {
        initializeSensors();
        //temp sensors just record temps, the atm module actually compiles/prints report.
        temperatureList = new LockFreeList<>(new Node(null, Integer.MIN_VALUE, new AtomicMarkableReference<Node>(null, false)),
                new Node(null, Integer.MAX_VALUE, new AtomicMarkableReference<Node>(null, false)));
        startSensors();
        while(true){


            //wait minutes to collect reports:
            try {
                TimeUnit.SECONDS.sleep(minutesBetweenReports);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //get top 5 and bottom 5 from sorted lockfreelist:
            //last 5 nodes, first five nodes.
            System.out.println("Temperature Reading Report:");
            LockFreeList<Integer> currentTemperatureListCopy = temperatureList;

            //we put this here so that we don't lose any data from sensors while we're running through our copy of the previous list for its report.
            temperatureList = new LockFreeList<>(new Node(null, Integer.MIN_VALUE, new AtomicMarkableReference<Node>(null, false)),
                    new Node(null, Integer.MAX_VALUE, new AtomicMarkableReference<Node>(null, false)));

            int sizeList = countSizeList(currentTemperatureListCopy);
            printTopFiveReadings(sizeList, currentTemperatureListCopy);
            printBottomFiveReadings(currentTemperatureListCopy);
            List<TemperatureSensor> currentTemperatureSensorList = temperatureSensorList;
            printHighestDifference(currentTemperatureSensorList);


        }

    }

    private static void printHighestDifference(List<TemperatureSensor> temperatureSensors){
        int currentMax = 0;
        int currentMaxIndex = 0;
        int currentMaxSensorNumber = 0;
        for(TemperatureSensor temperatureSensor: temperatureSensors){
            if(temperatureSensor.largestTempDifferenceBetweenReadings > currentMax){
                currentMax = temperatureSensor.largestTempDifferenceBetweenReadings;
                currentMaxIndex = temperatureSensor.largestTempDifferenceTime;
                currentMaxSensorNumber = temperatureSensor.sensorID;
            }
        }
        System.out.println("The Highest Recorded difference between readings was: " + currentMax + " on interval minute: " + currentMaxIndex + " from sensor " + currentMaxSensorNumber);
    }

    private static void printBottomFiveReadings(LockFreeList<Integer> temperatureListCopy){
        System.out.println("5 Lowest Readings:");
        Node curr = temperatureListCopy.head;
        for(int i = 0; i < 6; i++){
            if(i > 0){
                System.out.println("Temperature Reading: " + curr.toString());
            }
            curr = (Node) curr.next.getReference();
        }
    }

    private static void printTopFiveReadings(int sizeList, LockFreeList<Integer> temperatureListCopy){
        int startWriting = sizeList-6;
        int endWriting = sizeList-1;
        System.out.println("5 Highest Readings:");
        Node curr = temperatureListCopy.head;
        int i = 0;
        while(curr != null){
            if(i >= startWriting && i < endWriting){
                System.out.println("Temperature Reading: " + curr.toString());
            }
            curr = (Node) curr.next.getReference();
            i++;
        }
    }

    private static int countSizeList(LockFreeList<Integer> temperatureListCopy){
        int size = 0;
        Node curr = temperatureListCopy.head;
        while(curr != null){
            //System.out.println("i: " + size + " " + curr.toString());
            curr = (Node) curr.next.getReference();
            size++;
        }
        return size;
    }

    //initialize temp sensors:
    private static void initializeSensors(){
        for(int i = 0; i < numTemperatureSensors; i++){
            temperatureSensorList.add(new TemperatureSensor(i));
        }
    }

    //start temp sensors:
    private static void startSensors(){
        for(TemperatureSensor temperatureSensor: temperatureSensorList){
            Thread thread = new Thread(temperatureSensor);
            thread.start();
        }
    }

}
