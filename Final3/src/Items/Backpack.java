package Items;

import java.util.ArrayList;
import java.util.HashSet;

import be.kuleuven.cs.som.annotate.Basic;

import java.util.*;

import java.math.*;
/**
 * A class of backpacks which can be equipped by monsters or put in backpacks.
 * @invar	The total weight of a backpack should be lower or equal to the carrying capacity.
 * 			|hasValidTotalWeight(Item item)
 * @author Robin and Ramon
 * @version 3.7
 * 
 */
public class Backpack extends StoreItem {
	
	/**
	 * Initialize a new purse.
	 * @param 	weight
	 * 			The weight of the new backpack.
	 * @param 	value
	 * 			The value of the new backpack.
	 * @param 	capacity
	 * 			The capacity of the new backpack.
	 * @param 	content
	 * 			The content of the new backpack.
	 * @effect	The weight, value and capacity are initialized.
	 * 			|super(weight, value, capacity)
	 */
	public Backpack(double weight, int value, int capacity) {
		super(weight, value, capacity);
		this.carryingCapacity = new BigDecimal(Integer.toString(capacity));
	}

	//2.1.1: IDENTIFICATION

	/**
	 * Variable used in creating the ID.
	 */
	private static long idCounter=0;

	/**
	 * Generates the ID of the backpack.
	 * 
	 * @return  A generated odd number that has is unique to each backpack.
	 *          | result == (idCounter + 2)
	 */
	@Override
	protected long createId() {
		while (idCounter % 2 == 0)
			idCounter++;
		return idCounter++;
	}
	
	/**
	 * ArrayList defining the contents of this backpack.
	 */
	private ArrayList<Item> contents = new ArrayList<Item>();
	
	/**
	 * Return the contents of this backpack.
	 */
	@Basic
	public ArrayList<Item> getContents() {
		return this.contents;
	}
	
	//WEIGHT
	
	/**
	 * Return the total weight.
	 * @return 	The weights across all items in the backpack.
	 * 			|for each index in 0..getContents().size():
	 * 			|	if (getContents().get(i) != null)
	 * 			|		then totalWeight += getContents().get(i).getTotalWeight()
	 */
	public BigDecimal getTotalWeight() {
		BigDecimal totalWeight = this.getWeight();
		for(int i=0;i<getContents().size();i++) {
			if (getContents().get(i) != null)
				totalWeight = totalWeight.add(getContents().get(i).getTotalWeight());
		}
		return totalWeight;
	}
	
	//obtain lightest/heaviest item method 1 (non-constant time part is hidden in addToBackpack()).
	
	/**
	 * Sort the content on weights.
	 * @post		The contents of this backpack are sorted from lowest to highest weight
	 * 			|for each index in 0..getContents().size():
	 * 			|	getContents().get(i).getTotalWeight() < getContents().get(i+1).getTotalWeight()
	 */
	public void sortContentWeight() {
		contents.sort((o1, o2) -> o1.getTotalWeight().compareTo(o2.getTotalWeight()));
	}
	
	/**
	 * Return the lightest item of a sorted backpack.
	 * @return 	The lightest item of a sorted backpack.
	 * 			|return == (getContents().get(0))
	 */
	public Item getLightestItem() {
		return getContents().get(0);
	}
	
	/**
	 * Return the heaviest item of a sorted backpack.
	 * @return 	The heaviest item of a sorted backpack.
	 * 			|return == (getContents().get(getContents().size()-1)
	 */
	public Item getHeaviestItem() {
		return getContents().get(getContents().size()-1);
	}
	
	//obtain lightest/heaviest item method 2 (introduce HashSet to iterate over elements in constant time).
	
	/**
	 * HashSet defining the contents of this backpack.
	 */
	private HashSet<Item> constantContent = new HashSet<Item>();
	
	/**
	 * Return the contents of this backpack.
	 */
	@Basic
	public HashSet<Item> getConstantContent() {
		return this.constantContent;
	}
	
	/**
	 * Return the lightest item of this backpack.
	 * @return 	The lightest item of this backpack.
	 * 			|for each item in constantContent;
	 * 			|	if(item.getTotalWeight() < lightestItem.getTotalWeight())
	 * 			|		then lightestItem == item
	 * @throws 	NoSuchElementException
	 * 			The backpack contains no lightest item.
	 * 			|lightestItem == null
	 */
	public Item getLightestItem2() throws NoSuchElementException{
		Item lightestItem = null;
		for(Item item : constantContent) {
			if(lightestItem == null)
				lightestItem = item;
			else if(item.getTotalWeight().compareTo(lightestItem.getTotalWeight()) == -1)
				lightestItem = item;
		}
		if(lightestItem == null)
			throw new NoSuchElementException("Backpack contains no such items.");
		return lightestItem;
	}
	
	/**
	 * Return the heaviest item of this backpack.
	 * @return 	The heaviest item of this backpack.
	 * 			|for each item in constantContent;
	 * 			|	if(item.getTotalWeight() > lightestItem.getTotalWeight())
	 * 			|		then heaviestItem == item
	 * @throws 	NoSuchElementException
	 * 			The backpack contains no heaviest item.
	 * 			|heaviestItem == null
	 */
	public Item getHeaviestItem2() throws NoSuchElementException{
		Item heaviestItem = null;
		for(Item item : constantContent) {
			if(heaviestItem == null)
				heaviestItem = item;
			else if(item.getTotalWeight().compareTo(heaviestItem.getTotalWeight()) ==1)
				heaviestItem = item;
		}
		if(heaviestItem == null)
			throw new NoSuchElementException("Backpack contains no such items.");
		return heaviestItem;
	}
	
	/**
	 * Return the total value of this backpack.
	 * @return	The value of this backpack
	 * 			| totalValue = getValue()
	 * 			together with the total value of all items in the backpack
	 * 			|for each index in 0..getContents().size()-1:
	 * 			|	if(getContents().get(i) != null)
	 * 			|		then totalValue += getContents().get(i).getTotalValue()
	 */
	public int getTotalValue() {
		int totalValue = this.getValue();
		for(int i=0;i<getContents().size();i++) {
			if (getContents().get(i) != null)
				totalValue += getContents().get(i).getTotalValue();
		}
		return totalValue;
	}
	
	//CAPACITY
	
	/**
	 * Variable indicating the carrying capacity.
	 */
	private final BigDecimal carryingCapacity;
	
	/**
	 * Return the carrying capacity.
	 */
	@Basic
	public BigDecimal getCarryingCapacity() {
		return this.carryingCapacity;
	}
	
	/**
	 * Check if the total weight of the contents of the backpack + an extra item doesn't exceed the carrying capacity.
	 * @param 	item
	 * 			The item to be added to the weight.
	 * @return	True if the total weight doesn't exceed the carrying capacity.
	 * 			|return (getTotalWeight() - getWeight() <= getCarryingCapacity())
	 */
	public boolean hasValidTotalWeight(Item item) {
		BigDecimal totalWeight = getTotalWeight();
		totalWeight.subtract(getWeight());
		return(totalWeight.add(item.getWeight()).compareTo( getCarryingCapacity()) == -1 || totalWeight.add(item.getWeight()).compareTo( getCarryingCapacity()) == 0);
	}
	
	/**
	 * Transfer all items to a given backpack.
	 * @param 	backpack
	 * 			The backpack to transfer to.
	 * @post		The given backpack has all items from this backpack added to it.
	 * 			|for each index in 0..this.getContents().size()-1:
	 * 			|	for 1 index in 0..(new backpack).getContents().size()-1:
	 * 			|		(new backpack).getContents().get(j) == this.getContents(i)
	 * @post		This backpack has all its item removed.
	 * 			|new.getContents().size() == 0	
	 * @throws	IllegalArgumentException
	 * 			The backpack doesn't exist, or is the same as this backpack.
	 * 			|backpack == null || this == backpack
	 * @throws	IllegalArgumentException
	 * 			The item shouldn't be destroyed.
	 * 			|item.isDestroyed()==true
	 * @throws	IllegalArgumentException
	 * 			The new total weight of the backpack should be valid.
	 * 			|!backpack.hasValidTotalWeight(item)
	 */
	public void transferContents(Backpack backpack) throws IllegalArgumentException{
		if(backpack == null || this == backpack)
			throw new IllegalArgumentException();
		int size = getContents().size();
		for(int i=0;i<size;i++) {
			Item item = getContents().get(0);
			if(item instanceof Weapon)
				if(((Weapon) item).isDestroyed() == true)
					throw new IllegalArgumentException("Weapon is destroyed.");
			if(!backpack.hasValidTotalWeight(item)) 
				throw new IllegalArgumentException("Exceeds the total weight.");
			item.removeFromBackpack();
			item.addToBackpack(backpack);
		}	
	}
	
	/**
	 * Add the specified items to this backpack.
	 * @param 	items
	 * 			The items to be added.
	 * @post		This backpack has the specified items added to it.
	 * 			|for all items in items:
	 * 			|	for 1 index in 0..new.getContents().size()-1:
	 * 			|		new.getContents().get(i) == item
	 * 
	 */
	public void addItems (Item...items) {
		for (Item item : items) {
			item.addToBackpack(this);
		}
	}
	
	/**
	 * Remove the specified items from this backpack.
	 * @param 	items
	 * 			The items to be removed.
	 * @post		This backpack has the specified items removed from it.
	 * 			|for all items in items:
	 * 			|	for each index in 0..getContents().size()-1:
	 * 			|		getContents().get(i) != item
	 * 
	 */
	public void removeItems (Item...items) {
		for (Item item : items) {
			item.removeFromBackpack();
		}
	}
	
}