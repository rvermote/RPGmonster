package Items;

import java.math.BigDecimal;

import be.kuleuven.cs.som.annotate.Basic;
/**
 * A class of purses which can be equipped by monsters or put in backpacks.
 * @invar	The amount of dukats removed should be valid.
 * 			|isValidRemovalAmount(dukats)
 * @author Robin and Ramon
 * @version 3.7
 * 
 */
public class Purse extends StoreItem {

	/**
	 * Initialize a new purse.
	 * @param 	weight
	 * 			The weight of the new purse.
	 * @param 	value
	 * 			The value of the new purse.
	 * @param 	capacity
	 * 			The capacity of the new purse.
	 * @param 	content
	 * 			The content of the new purse.
	 * @effect	The weight, value and capacity are initialized.
	 * 			|super(weight, value, capacity)
	 * @post		The content of the new purse is equal to the given content.
	 * 			|new.getContent() = content
	 */
	public Purse(double weight, int value, int capacity, int content) {
		super(weight, value, capacity);
		addDukats(content);
	}

	//2.3.1 IDENTIFICATION

	/**
	 * Variable used in creating the ID.
	 */
	private static long idCounter1=1;
	private static long idCounter2=0;

	/**
	 * Generates the id of the purse.
	 * 
	 * @return  A generated number that is a part of the Fibbonaci series  
	 * 			and is unique to each purse.
	 */
	@Override
	public long createId() {
		long result;
		result = idCounter2 + idCounter1;
		idCounter2 = idCounter1;
		idCounter1 = result;
		return result;
	}

	//2.3.2 CONTENT

	/**
	 * Variable indicating the content of this purse.
	 */
	private int content;
	
	/**
	 * Return the content of this purse.
	 */
	@Basic
	public int getContent() {
		return this.content;
	}

	/**
	 * Add the specified amount of dukats to the purse.
	 * @param 	dukats
	 * 			The amount of dukats to be added
	 * @pre		The purse is not torn and the amount of dukats is equal to or large than 0.
	 * 			|assert (isTorn() == false && dukats >= 0)
	 * @post		If the total amount of dukats does not exceed the capacity of the purse, the dukats are added.
	 * 			|if(getContent() + dukats < getCapacity())
	 * 			|	then new.getContent() == this.getContent() + dukats
	 * @post		If the total amount of dukats exceeds the capacity of the purse, the purse tears.
	 * 			|if(getContent() + dukats > getCapacity())
	 * 			|	then new.isTorn() == true
	 */
	public void addDukats(int dukats){
		assert (this.torn == false && dukats >= 0);
		if(getContent() + dukats > getCapacity())
			this.tear();
		else
			this.content = getContent() + dukats;
	}

	/**
	 * Remove a given amount of dukats from the purse.
	 * @param 	dukats
	 * 			The amount of dukats to be removed.
	 * @pre		The amount of dukats removed from the purse is a legal amount.
	 * 			|assert(isValidRemovalAmount(dukats))
	 * @post		The given amount of dukats are removed from the purse.
	 * 			|new.getContent() == this.getContent() - dukats
	 */
	public void removeDukats(int dukats) {
		assert isValidRemovalAmount(dukats);
		this.content = getContent() - dukats;
	}
	
	/**
	 * Check if the removal amount is legal.
	 * @param 	dukats
	 * 			The amount to be checked.
	 * @return	True if the amount of dukats are equal to or larger than 0, and equal to or lower than the amount of dukats in the purse.
	 * 			|return == (dukats >= 0 && dukats <= getContent())
	 */
	public boolean isValidRemovalAmount(int dukats) {
		return(dukats >= 0 && dukats <= getContent());
	}
	
	/**
	 * Transfer an amount of dukats from this purse to a specified purse.
	 * @param 	amount
	 * 			The amount of dukats to be transferred.
	 * @param 	purse
	 * 			The purse to which the dukats should be transferred.
	 * @pre		The new purse exists and isn't this purse.
	 * 			|assert (purse != null && this != purse)
	 * @pre		The new purse is not torn.
	 * 			|assert (purse.isTorn() == false)
	 * @pre		The amount of dukats removed from the purse is a legal amount.
	 * 			|assert(isValidRemovalAmount(dukats))
	 */
	public void transferDukats(int amount, Purse purse){
		assert (purse != null && this != purse);
		assert (purse.torn == false);
		assert isValidRemovalAmount(amount);
		this.removeDukats(amount);
		purse.addDukats(amount);
	}

	//2.3.3 VALUE
	
	/**
	 * Set the value of this purse to a new value.
	 * @param	newValue
	 * 			The new value of this purse.
	 * @throws	IllegalArgumentException
	 * 			The new value isn't valid.
	 * 			|!isValidValue(newValue)
	 * @post		The value of this purse is equal to the given value.
	 * 			|new.getValue() == newValue
	 */
	public void setValue(int newValue) throws IllegalArgumentException{
		if (!isValidValue(newValue))
			throw new IllegalArgumentException("Not valid value for purse");
		value = newValue;
	}
	
	/**
	 * Return the total value of this purse.
	 * @Return 	The value of this purse and the dukats in the purse.
	 * 			|return == (getValue() + getContent())
	 */
	public int getTotalValue() {
		return(getValue() + getContent());
	}

	//2.3.4 CAPACITY
	
	/**
	 * Set the capacity of this purse to a new capacity.
	 * @param 	newCapacity
	 * 			The new capacity of this purse.
	 * @post		The capacity of this purse is equal to the given capacity.
	 * 			|new.getCapacity() == newCapacity
	 * @throws 	IllegalArgumentException
	 * 			The new capacity isn't valid.
	 * 			|!isValidCapacity(newCapacity)
	 */
	public void setCapacity(int newCapacity) throws IllegalArgumentException {
		if (!isValidCapacity(newCapacity))
			throw new IllegalArgumentException("Not valid capacity for purse");
		capacity = newCapacity;
	}
	
	/**
	 * Variable indicating the state of this purse.
	 */
	private boolean torn = false;
	
	/**
	 * Returns the state of this purse.
	 */
	@Basic
	public boolean isTorn() {
		return(this.torn);
	}
	
	/**
	 * Tear this purse.
	 * @post		The state of this purse is torn.
	 * 			|new.isTorn() == true
	 * @post		The value of this purse is 0.
	 * 			|new.getValue() == 0
	 * @post		The content of this purse is 0.
	 * 			|new.getContent() == 0.
	 */
	public void tear() {
		this.torn = true;
		this.value = 0;
		this.content = 0;
	}
	
	//2.3.4 WEIGHT
	
	/**
	 * Return the total weight of this purse.
	 * @Return 	the weight of the purse together with the weight of the dukats in the purse.
	 * 			|return == (getWeight() + 0.05*getContent())
	 */
	public BigDecimal getTotalWeight() {
		BigDecimal weightCoins;
		weightCoins = new BigDecimal(String.valueOf(0.05 * getContent()));
		return getWeight().add(weightCoins).setScale(2, 2);
	}
	
}
