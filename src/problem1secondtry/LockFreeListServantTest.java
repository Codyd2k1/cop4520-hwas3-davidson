package problem1secondtry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicMarkableReference;
import org.junit.Test.None;

import static org.junit.jupiter.api.Assertions.assertAll;


class LockFreeListServantTest{

    static Integer numServants = 4;
    static Integer numGifts;
    static ConcurrentLinkedQueue<Integer> unorderedGiftBag;
    static ConcurrentLinkedQueue<Integer> giftsToRemoveFromGiftList;
    static LockFreeList<Integer> giftList;
    int giftsPerServant;
    private Random random = new Random();

    @Test
    public void testAddRemoveContainsThankYouCardWriting5k(){
        giftList = new LockFreeList<>(new Node(null, Integer.MIN_VALUE, new AtomicMarkableReference<Node>(null, false)),
                new Node(null, Integer.MAX_VALUE, new AtomicMarkableReference<Node>(null, false)));
        unorderedGiftBag = new ConcurrentLinkedQueue<Integer>();

        numGifts = 5000;
        //load the "numgifts" gifts into our unordered gift bag.
        for(int i = 0; i < numGifts; i++){
            //gen random id for the gifts
            int giftToAdd = random.nextInt(numGifts);
            if(!unorderedGiftBag.contains(giftToAdd)) unorderedGiftBag.add(giftToAdd);
        }
        giftsToRemoveFromGiftList = unorderedGiftBag;
        giftsPerServant = numGifts/4;

        assertAll(()-> {    //no exception expected. runs without error.
                startServantThreads();
            }
        );
    }

    @Test
    public void testAddRemoveContainsThankYouCardWriting500k(){
        giftList = new LockFreeList<>(new Node(null, Integer.MIN_VALUE, new AtomicMarkableReference<Node>(null, false)),
                new Node(null, Integer.MAX_VALUE, new AtomicMarkableReference<Node>(null, false)));

        numGifts = 500000;
        giftsPerServant = numGifts/4;
        assertAll(()-> {    //no exception expected. runs without error.
                    startServantThreads();
                }
            );
    }

    private void startServantThreads(){
        int startInt = 0;
        int endInt = giftsPerServant;
        for (int i = 0; i < numServants; i++) {
            Thread servant = new Thread(new Servant(i));
            startInt = endInt + 1;
            endInt = endInt + giftsPerServant;
            servant.start();
        }
    }

}

