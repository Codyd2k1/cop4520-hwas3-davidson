package problem1secondtry;


import java.time.Instant;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

class Servant implements Runnable{
    Random random = new Random();
    int servantID;

    public Servant(int servantID) {
        this.servantID = servantID;
    }


    /*
        As the Minotaur was impatient to get this task done quickly, he instructed his servants not
        to wait until all of the presents from the unordered bag are placed in the chain of linked
        and ordered presents. Instead, every servant was asked to alternate adding gifts to the
        ordered chain and writing Thank you cards.
         */
    //also servants need to randomly do contains at the minotaurs request.
    @Override
    public void run() {
        int numThankYousWritten = 0;
        int task = 0;
        //alternate adding and removing gifts, 500k/4 is 125k, if each servant does 125k adds, and 125k removes all
        //thank you cards will be written.
        //although we are splitting the "work" up, threads are still adding/removing to and from the same concurrent locked list LockFreeList "giftList"
        while(numThankYousWritten < LockFreeListSimulation.numGifts/4){
            if(task == 0){
                Integer giftToAdd = LockFreeListSimulation.unorderedGiftBag.poll();
                if(giftToAdd == null){
                    task = 1;
                    continue;
                }
                LockFreeListSimulation.giftList.add(giftToAdd);
                //System.out.println("Servant " + servantID + " added gift: " + giftToAdd);
                task = 1;
            }
            else if(task == 1){
                Integer giftToRemove = LockFreeListSimulation.giftsToRemoveFromGiftList.poll();
                if(giftToRemove == null)continue;
                LockFreeListSimulation.giftList.remove(giftToRemove);
                //System.out.println("Servant " + servantID + " removed gift: " + giftToRemove);
                numThankYousWritten++;
                task = 0;
            }

            //1/1000 chance the minotaur asks a servant to run a contains check.
            int performContains = random.nextInt(1000);
            if(performContains == 1){ //
                LockFreeListSimulation.giftList.contains(random.nextInt(500000));
            }
        }
        System.out.println("Servant " + servantID + " Completed All Tasks");
        if(LockFreeListSimulation.giftsToRemoveFromGiftList.size() == 0){
            System.out.println("All thank you cards written.");
            LockFreeListSimulation.instantStopped = Instant.now();
            LockFreeListSimulation.printRuntime();
        }
    }

}
