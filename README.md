# cop4520-hwas3-davidson

How to compile & run:

problem 1:  
1. navigate to <path_to_this_project>/src/problem1secondtry
2. run command:   
"javac LockFreeListSimulation.java Gift.java LockFreeList.java Node.java Servant.java Window.java"

3. do cd .. (go back one dir)  
4. run command   
"Java problem1secondtry.LockFreeListSimulation"  

problem 2:  
1. navigate to <path_to_this_project>/src  
2. run command:  
"javac problem2\AtomicTempModule.java problem2\TemperatureSensor.java problem1secondTry\LockFreeList.java problem1secondtry\Node.java"  
3. run command:  
"java problem2.AtomicTempModule"

**Documentation:**  
**Problem 1**  
**Statements/Proof of Correctness**  
Problem 1 was tricky, eventually I decided on using a lock free list implementation, specifically the one we learned about in ch. 9.
I chose this because a. I didn't want to have to deal with locking, and b. I read through problem 2 before starting problem 1, and decided
a non-lock based list would be applicable to both problems 1 and 2.  
Here's how I have met all of the requirements from problem 1:  
Requirement: sorted gift list - met through lockFreeList implementation
Requirement: unsorted bag of gifts to pull from when adding gifts to sorted list - met through "unsortedGiftList" in Simulation class.
Requirement: every gift has a unique tag - met through gift object, using object hashcode.
Requirement: add/remove/contains - met through LFL implementation.
Requirement: alternate adding/removing gifts - met through servant class "run" method
Requirement: dont stop until all thank yous written   - met through servant class "run" method while condition  
**Efficiency**  
My LFL has add/remove/contains that run fairly quickly, they go through and find the required nodes to link/unlink/search for by traversing
the linked list, so o(n) time. The simulation is running in a while loop that calls these add/remove/contains calls, which does increase the runtime.  
However, on average running the simulation the program is able to finish within 5000-6000 milliseconds, or 5-6 seconds.  
**Experimental Evaluation**  
This was a trial/error process, as you can see my package for problem 1 is named "problem1secondtry"... yes I did have a first try.
I had many tries, mostly for trying things to get the code from the book to "work" as the book left so much out. (Everything used from
the book is referenced and cited in the code where it's used.) I tried using a regular static list at first, didn't work because too many things
were adding/removing at once, ended up going with the LFL implementation, for reasons mentioned in "Statements/Proof of Correctness".
  
    
**Problem 2**  
**Statements/Proof of Correctness**  
Thankfully after tackling problem 1, I knew I could reuse my LFL implementation for problem 2. This would allow me to 
easily get the top 5 highest and lowest readings for the reports by displaying the last 5 and first 5 nodes containing temps 
for my LFL. After that, all that was left to do was to get the highest temp difference between intervals per sensor, and compare them all.
As you can see, I have each temp sensor record their own highest temp difference between intervals, and then have the ATM
compare each of those to find the Max highest temp difference and display it. This seperation of work is legal because we're
told that the ATM is responsible for compiling and displaying all of the data, and the sensors are responsible for collecting it.  
**Efficiency**  
I feel like efficiency isn't a huge part of this program, because the time intervals limit data collection so much.. However, 
I feel this implementation for problem 2 is fairly efficient, I'd say the most "inefficient" part of the implementation is where
I count the size of the list then iterate through it using the count.. but this is just what you accept when dealing with 
linked list, traversal is necessary and annoying. These counts/traversals run in o(n), and once again the simulation is running in a while loop
which runs reports every 6 minutes, and readings every 1.  
**Experimental Evaluation**  
Fortunately problem 2 wasn't as awful as problem 1, the only "experiments" I had to try were for things like maintaining
all of the data - didn't want to lose any readings so I had to refresh the linked list in a specific place asap, and another for
things like counting when the 6/1 minute interval(s) were up.
