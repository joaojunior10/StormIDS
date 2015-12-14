package networkmonitor.bolts.slidingwindowcounter.tools;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class provides per-slot counts of the occurrences of objects.
 * 
 * It can be used, for instance, as a building block for implementing sliding window counting of objects.
 * 
 * @param <T>
 *            The type of those objects we want to count.
 */
public final class SlotBasedCounter<T> implements Serializable {

    private static final long serialVersionUID = 4858185737378394432L;

    private final Map<T, double[]> objToCounts = new HashMap<T, double[]>();
    private final int numSlots;
    private final Map<T, long[]> numTuplesPerSlot = new HashMap<T, long[]>();
    
    public SlotBasedCounter(int numSlots) {
        if (numSlots <= 0) {
            throw new IllegalArgumentException("Number of slots must be greater than zero (you requested " + numSlots
                + ")");
        }
        this.numSlots = numSlots;
    }
 
    public void increaseCount(T obj, Double amount, int slot) {
    	double[] counts = objToCounts.get(obj);
        long[] numTuples = numTuplesPerSlot.get(obj);
        if (counts == null) {
            counts = new double[this.numSlots];
            numTuples = new long[this.numSlots];
            objToCounts.put(obj, counts);
            numTuplesPerSlot.put(obj, numTuples);
        }
        counts[slot] += amount;
        numTuples[slot] ++;
    }

    public double getCount(T obj, int slot) {
    	double[] counts = objToCounts.get(obj);
        if (counts == null) {
            return 0;
        }
        else {
            return counts[slot];
        }
    }

    public Map<T, Double> getCounts() {
        Map<T, Double> result = new HashMap<T, Double>();
        for (T obj : objToCounts.keySet()) {
            result.put(obj, computeAverageCount(obj));
        }
        return result;
    }

    
    private double computeTotalCount(T obj) {
    	double[] curr = objToCounts.get(obj);
    	double total = 0;
        for (double l : curr) {
            total += l;
        }
        return total;
    }
    
    private double computeAverageCount(T obj) {
    	double total = computeTotalCount(obj);
        long[] curr = numTuplesPerSlot.get(obj);
        long numberOfTuples = 0;
        for (long l : curr) {
            numberOfTuples += l;
        }
        if(numberOfTuples!=0)return (total/numberOfTuples);
        else return total;
       
    }


    /**
     * Reset the slot count of any tracked objects to zero for the given slot.
     * 
     * @param slot
     */
    public void wipeSlot(int slot) {
        for (T obj : objToCounts.keySet()) {
            resetSlotCountToZero(obj, slot);
        }
    }

    private void resetSlotCountToZero(T obj, int slot) {
        double[] counts = objToCounts.get(obj);
        counts[slot] = 0;
        long[] numTuples = numTuplesPerSlot.get(obj);
        numTuples[slot] = 0;
    }

    private boolean shouldBeRemovedFromCounter(T obj) {
        return computeTotalCount(obj) == 0;
    }

    /**
     * Remove any object from the counter whose total count is zero (to free up memory).
     */
    public void wipeZeros() {
        Set<T> objToBeRemoved = new HashSet<T>();
        for (T obj : objToCounts.keySet()) {
            if (shouldBeRemovedFromCounter(obj)) {
                objToBeRemoved.add(obj);
            }
        }
        for (T obj : objToBeRemoved) {
            objToCounts.remove(obj);
            numTuplesPerSlot.remove(obj);
        }
    }

}
