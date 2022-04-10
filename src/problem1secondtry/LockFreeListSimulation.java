package problem1secondtry;

import org.junit.rules.Stopwatch;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockFreeListSimulation {
    static Integer numServants = 4;
    static Integer numGifts;
    static ConcurrentLinkedQueue<Integer> unorderedGiftBag;
    static ConcurrentLinkedQueue<Integer> giftsToRemoveFromGiftList;
    static LockFreeList<Integer> giftList;
    static int giftsPerServant;
    static Instant instantStarted;
    static Instant instantStopped;

    public static void main(String[] args) {
        instantStarted = Instant.now();
        giftList = new LockFreeList<>(new Node(null, Integer.MIN_VALUE, new AtomicMarkableReference<Node>(null, false)),
                new Node(null, Integer.MAX_VALUE, new AtomicMarkableReference<Node>(null, false)));
        unorderedGiftBag = new ConcurrentLinkedQueue<Integer>();
        giftsToRemoveFromGiftList = new ConcurrentLinkedQueue<Integer>();

        numGifts = 500000;
        //load the "numgifts" gifts into our unordered gift bag.
        for(int i = 0; i < numGifts; i++){
            int giftToAdd = new Gift(i).uniqueID;   //each gift has a unique ID generated by the hashcode of the object.
            unorderedGiftBag.add(giftToAdd);
            giftsToRemoveFromGiftList.add(giftToAdd);
        }
        System.out.println("Loaded unordered gift bag");
        //although we are splitting the "work" up, threads are still adding/removing to and from the same concurrent locked list LockFreeList "giftList", juan said this is fine.
        giftsPerServant = numGifts/4 * 2; //each get 1/4 of the gifts, and for each of those 1/4 portions they add and remove 1/4, so * 2
        startServantThreads();
    }

    public static void printRuntime(){
        Duration durationBetween = Duration.between(instantStarted, instantStopped);
        System.out.println("Runtime in milliseconds: " + durationBetween.toMillis());
    }

    private static void startServantThreads(){
        for (int i = 0; i < numServants; i++) {
            Thread servant = new Thread(new Servant(i));
            servant.start();
        }
    }
}
