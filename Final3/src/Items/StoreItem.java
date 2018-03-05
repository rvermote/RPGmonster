package Items;

import be.kuleuven.cs.som.annotate.Basic;
/**
 * A class of items which can be used to store things.
 * @invar	The capacity should be valid.
 * 			|isValidCapacity(capacity)
 * @author Robin and Ramon
 * @version 3.7
 * 
 */
public abstract class StoreItem extends Item {
	
	/**
	 * Initiate a new store item with given weight, value and capacity.
	 * @param 	weight
	 * 			The weight of the new store item.
	 * @param 	value
	 * 			The value of the new store item.
	 * @param 	capacity
	 * 			The carrying capacity of the new store item.
	 * @effect	The store item is initialized with given weight and value
	 * 			|super(weight, value)
	 * @effect	The capacity of the store item is equal to the given capacity.
	 * 			|new.getCapacity() == capacity
	 * @throws 	IllegalArgumentException
	 * 			The new capacity isn't valid.
	 * 			|!isValidCapacity(capacity)
	 */
	public StoreItem(double weight, int value, int capacity) throws IllegalArgumentException {
		super(weight, value);
		if(!isValidCapacity(capacity))
			throw new IllegalArgumentException("Not valid capacity");
		this.capacity = capacity;
	}
	
	/**
	 * Variable indicating the carrying capacity of this store item.
	 */
	protected int capacity;
	
	/**
	 * Return the carrying capacity of this store item.
	 */
	@Basic
	public int getCapacity() {
		return capacity;
	}
	
	/**
	 * Check if the carrying capacity is a legal amount.
	 * @param 	capacity
	 * 			The capacity to be checked.
	 * @return	True if the carrying capacity is strictly larger than 0.
	 * 			|return == (capacity > 0)
	 */
	public boolean isValidCapacity(int capacity) {
		return(capacity > 0);
	}
	
}
