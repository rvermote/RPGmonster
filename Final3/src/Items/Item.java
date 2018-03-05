package Items;

import java.math.BigDecimal;

import RPG3.Monster;
import be.kuleuven.cs.som.annotate.Basic;
/**
 * A class of items which are equipable to monsters or can be put in backpacks.
 * @invar	The weight of this item should be valid.
 * 			|isValidWeight(weight)
 * @invar	The item should be linked to a monster upon transfer.
 * 			|canBeTransferred()
 * @invar	The holder should be a monster or backpack
 * 			|isPossibleHolder()
 * @invar	When setting a holder, this item shouldn't be linked with a holder already.
 * 			|canHaveDirectHolder()
 * @author 	Robin and Ramon
 * @version 3.7
 *
 */
public abstract class Item {

	/**
	 * Initialize new Item with given weight and value.
	 * @param 	weight
	 * 			Weight of the new item.
	 * @param 	value
	 * 			Value of the new item.
	 * @post		The ID of this new item is equal to the generated ID.
	 * 			|new.getId() == createId()
	 * @post		The weight of this new item is equal to the given weight.
	 * 			|new.getWeight() == weight
	 * @post		The value of this new item is equal to the given value.
	 * 			|new.getValue() == value
	 * @throws 	IllegalArgumentException
	 * 			The weight of the new item isn't valid.
	 * 			|!isValidWeight(weight)
	 */
	public Item (double weight, int value) throws IllegalArgumentException {
		this.id = createId();
		if(!isValidWeight(weight))
			throw new IllegalArgumentException("Not a valid weight");
		this.weight = new BigDecimal(String.valueOf(weight)).setScale(2);
		setValue(value);
	}

	//ID
	
	/**
	 * Variable indicating the ID of this item.
	 */
	private final long id; 
	
	/**
	 * Create the ID of this item.
	 * @return The ID of this item.
	 */
	protected abstract long createId();

	/**
	 * Return the id of this item.
	 */
	@Basic
	public long getId() {
		return this.id;
	}
	
	/**
	 * Variable indicating the weight of this item.
	 */
	protected final BigDecimal weight;
	
	/**
	 * Return the weight of this item.
	 */
	@Basic
	public BigDecimal getWeight() {
		return this.weight;
	}
	
	/**
	 * Return the total weight of this item.
	 */
	@Basic
	public abstract BigDecimal getTotalWeight();
	
	/**
	 * Check if the weight of this item is valid.
	 * @param 	weight
	 * 			The weight to be checked.
	 * @return 	True if the weight is higher than or equal to 0 kg.
	 * 			|return == (weight >= 0)
	 */
	public boolean isValidWeight(double weight) {
		return (weight >= 0);
	}
	
	/**
	 * Variable indicating the value of this item.
	 */
	protected int value;
	
	/**
	 * Return the value of this item.
	 */
	@Basic
	public int getValue() {
		return this.value;
	}
	
	/**
	 * Set the value of this item to a new value.
	 * @param 	newValue
	 * 			The new value of the item.
	 * @post	The new value of this item is the given value.
	 * 			|new.value == newValue
	 * @throws 	IllegalArgumentException
	 * 			The new value isn't valid.
	 * 			|!isValidValue(newValue)
	 */
	public void setValue(int newValue) throws IllegalArgumentException{
		if(!isValidValue(newValue)) 
			throw new IllegalArgumentException("Not a valid value");
		this.value = newValue;
	}
	
	/**
	 * Check if the value of this item is valid.
	 * @param 	value
	 * 			The weight to be checked.
	 * @return 	True if the value is higher than or equal to 0.
	 * 			|return == (value >= 0)
	 */
	public boolean isValidValue(int value) {
		return(value >= 0);
	}
	
	/**
	 * Return the total value of this item.
	 */
	@Basic
	public abstract int getTotalValue();
	
	/**
	 * Object indicating the direct holder of this item.
	 */
	protected Object directHolder = null;
	
	/**
	 * Return the direct holder of this item.
	 */
	@Basic
	public Object getDirectHolder() {
		return this.directHolder;
	}
	
	/**
	 * Set the new direct holder of this item to the object.
	 * @param 	object	
	 * 			The new direct holder of this item.
	 * @post		The new direct holder of this item is the given object.
	 * 			|new.directHolder == object
	 * @throws	IllegalArgumentException
	 * 			The object doesn't exist.
	 * 			|object == null
	 * @throws	IllegalArgumentException
	 * 			The object isn't a backpack or monster.
	 * 			|!isPossibleHolder(object)
	 * @throws	IllegalArgumentException
	 * 			The object is already linked to a holder.
	 * 			|!canHaveDirectHolder()
	 */
	public void setDirectHolder (Object object) throws IllegalArgumentException {
		if(object == null)
			throw new IllegalArgumentException();
		if (!isPossibleHolder(object))
			throw new IllegalArgumentException();
		if (!canHaveDirectHolder())
			throw new IllegalArgumentException();
		this.directHolder = object;
			
	}
	
	/**
	 * Get the indirect holder of the given object.
	 * @param 	object
	 * 			The item of which the indirect holder will be returned.
	 * @return	Null if the item doesn't have a direct holder.
	 * 			|if (! (object.getDirectHolder() instanceof Backpack) && ! (object.getDirectHolder() instanceof Monster))
	 * 			|	then return == null
	 * 			The direct holder of the object if that direct holder is a Monster.
	 * 			|if(object.getDirectHolder().getClass().isAssignableFrom(Monster.class))
	 * 			|	then return == object.getDirectHolder()
	 * 			The method itself if the direct holder is not null and not a Monster.
	 * 			|return == getIndirectHolder(object.getDirectHolder())
	 */
	public Monster getIndirectHolder(Item object) {
		if(object == null || ! (object.getDirectHolder() instanceof Backpack) && ! (object.getDirectHolder() instanceof Monster))
			return null;
		if(object.getDirectHolder().getClass().isAssignableFrom(Monster.class))
			return ((Monster) object.getDirectHolder());
		return getIndirectHolder((Item) (object.getDirectHolder()));
	}
	
	/**
	 * Attach this item to the given monster.
	 * @param 	monster
	 * 			The monster to which the item will be attached.
	 * @post		If this item has no direct holder, 
	 * 			the direct holder of the item is the monster.
	 * 			|if(item.getDirectHolder() == null)
	 * 			| 	then new.getDirectHolder() == monster
	 * @post		The item will be set at the lowest free anchor index.
	 * 			|for each index in 1..getAnchors().length:
	 * 			|	if (monster.getAnchors[i] == null)
	 * 			|		then monster.getAnchors[i] == this
	 * @throws	IllegalArgumentException()
	 * 			The monster doesn't exist or the monster doesn't have any free anchor points or a valid new weight, or this item is already linked with another item.
	 * 			|monster == null || !canHaveDirectHolder() || !monster.canHaveItem() || !monster.isValidTotalWeight(this)
	 * @throws	IllegalArgumentException()
	 * 			This item is destroyed.
	 * 			|isDestroyed() == true
	 */
	public void obtain(Monster monster) {
		if (monster == null || !canHaveDirectHolder() || !monster.canHaveItem() || !monster.isValidTotalWeight(this))
			throw new IllegalArgumentException();
		if(this instanceof Weapon)
			if(((Weapon) this).isDestroyed() == true)
				throw new IllegalArgumentException();
		setDirectHolder(monster);
		monster.setItem(this);
	}
	
	/**
	 * Attach this item to the given monster at the given index.
	 * @param 	monster
	 * 			The monster to which the item will be attached.
	 * @param	index
	 * 			The position in the list of anchors where the weapon will be attached.
	 * @post		If this item has no direct holder, 
	 * 			the direct holder of the item is the monster.
	 * 			|if(item.getDirectHolder() == null)
	 * 			| 	then new.getDirectHolder() == monster
	 * @post		The item will be set at the given anchor point.
	 * 			|monster.getAnchors()[index] == this
	 * @throws	IllegalArgumentException()
	 * 			The monster doesn't exist or the anchor point at the index is occupied or the monster doesn't have a valid new weight, or this item is already linked with another item.
	 * 			|monster == null || !canHaveDirectHolder() || !monster.canHaveItem(index) || !monster.isValidTotalWeight(this)
	 * @throws	IllegalArgumentException()
	 * 			...
	 * 			|isDestroyed() == true
	 */
	public void obtain(Monster monster, int index) {
		if (monster == null || !canHaveDirectHolder() || !monster.canHaveItem(index) || !monster.isValidTotalWeight(this))
			throw new IllegalArgumentException();
		if(this instanceof Weapon)
			if(((Weapon) this).isDestroyed() == true)
				throw new IllegalArgumentException();
		setDirectHolder(monster);
		monster.setItem(this, index);
	}
	
	/**
	 * Transfer this item to the given monster at the first free anchor.
	 * @param 	monster
	 * 			The monster to which the item will be attached.
	 * @post		The direct holder of this item is the new monster.
	 * 			new.getDirectHolder() == monster
	 * @post		The item will be set at the lowest free anchor index of the new monster.
	 * 			|for each index in 1..monster.getAnchors().length:
	 * 			|	if (monster.getAnchors[i] == null)
	 * 			|		then monster.getAnchors[i] == this
	 * @throws 	IllegalArgumentException
	 * 			The monster is null or doesn't have any free anchor points, or this item isn't attached to an anchor point.
	 * 			|monster == null || !monster.canHaveItem() || !canBeTransferred(this)
	 */
	public void transfer(Monster monster) throws IllegalArgumentException{
		if(monster == null || !monster.canHaveItem() || !canBeTransferred())
			throw new IllegalArgumentException();
		((Monster) getDirectHolder()).setItemToNull(this);
		setDirectHolder(monster);
		monster.setItem(this);
	}
	
	/**
	 * Transfer this item to the given monster at the first free anchor.
	 * @param 	monster
	 * 			The monster to which the item will be attached.
	 * @param	index
	 * 			The position in the list of anchors where the weapon will be attached.
	 * @post		The direct holder of this item is the new monster.
	 * 			new.getDirectHolder() == monster
	 * @post		The item will be set at the given anchor point.
	 * 			|monster.getAnchors()[index] == this
	 * @throws 	IllegalArgumentException
	 * 			The monster is null or the anchor point at the index is occupied, or this item isn't attached to an anchor point.
	 * 			|monster == null || !monster.canHaveItem(index) || !canBeTransferred(this)
	 */
	public void transfer(Monster monster, int index) throws IllegalArgumentException{
		if(monster == null || !monster.canHaveItem(index) || !canBeTransferred())
			throw new IllegalArgumentException();
		((Monster) getDirectHolder()).setItemToNull(this);
		setDirectHolder(monster);
		monster.setItem(this, index);
	}
	
	/**
	 * Check if the item belongs to a monster in the first place.
	 * @return 	True if the direct holder of this item is a Monster.
	 * 			|return == (getDirectHolder() instanceof Monster)
	 */
	public boolean canBeTransferred() {
		return (getDirectHolder() instanceof Monster);
			
	}

	//NEW METHODS FOR PART 3
	
	/**
	 * Add an item to a backpack.
	 * @param 	backpack
	 * @post		The direct holder of this item is the new backpack.
	 * 			|new.getDirectHolder() == backpack
	 * @post		The item is added to the backpack.
	 * 			|for an index in 0..backpack.getContents().size()-1:
	 * 			|	backpack.getContents().get(i) == item
	 * @post		The contents of the backpack are sorted from lowest to highest weight.
	 * 			|for each index in 0..backpack.getContents().size()-2:
	 * 			|	backpack.getContents().get(i).getTotalWeight() < backpack.getContents().get(i+1).getTotalWeight()
	 * @throws 	IllegalArgumentException
	 * 			This item is destroyed.
	 * 			|this.isDestroyed() == true
	 * @throws	IllegalArgumentException
	 * 			The new total weight of the backpack isn't valid.
	 * 			|!backpack.hasValidTotalWeight(this)
	 * @throws	IllegalArgumentException
	 * 			The item already belongs to a backpack.
	 * 			|this.getDirectHolder() instanceof Backpack
	 * @throws	IllegalArgumentException
	 * 			The backpack doesn't exist.
	 * 			|backpack == null
	 */
	public void addToBackpack(Backpack backpack) throws IllegalArgumentException{
		if(backpack == null)
			throw new IllegalArgumentException("Backpack does not exist.");
		if(this instanceof Weapon)
			if(((Weapon) this).isDestroyed() == true)
				throw new IllegalArgumentException("Weapon is destroyed.");
		if(!backpack.hasValidTotalWeight(this)) 
			throw new IllegalArgumentException("Exceeds the total weight.");
		if(this.directHolder instanceof Backpack)
			throw new IllegalArgumentException("Cannot add items from backpack.");
		if(this.directHolder instanceof Monster) {
			((Monster) this.directHolder).setItemToNull(this);
			if(this.directHolder != null)
				this.directHolder = null;
		}	
		setDirectHolder(backpack);
		((Backpack) getDirectHolder()).getContents().add(this);
		backpack.sortContentWeight();
		((Backpack) getDirectHolder()).getConstantContent().add(this);
	}
	
	/**
	 * Remove an item from a backpack.
	 * @post		The direct holder of this item is null.
	 * 			|new.getDirectHolder() == null
	 * @post		The backpack to which the item belonged does not contain the item anymore.
	 * 			|for each index in 0..backpack.getContents().size()-1:
	 * 			|	backpack.getContents().get(i) != this
	 * @throws 	IllegalArgumentException
	 * 			The item doesn't belong to a backpack.
	 * 			|!(this.getDirectHolder() instanceof Backpack)
	 */
	public void removeFromBackpack() throws IllegalArgumentException{
		if(! (this.directHolder instanceof Backpack))
			throw new IllegalArgumentException("Item is not contained in this backpack.");
		for(int i=0; i<((Backpack)this.directHolder).getContents().size();i++) {
			if (((Backpack)this.directHolder).getContents().get(i) == this)
				((Backpack)this.directHolder).getContents().remove(i);
		}	
		((Backpack) getDirectHolder()).getConstantContent().remove(this);
		this.directHolder = null;
	}
	
	/**
	 * Check if the object can be a possible holder of the item.
	 * @param	object
	 * 			The object to be checked.
	 * @return	True if the object is a backpack or a monster.
	 * 			|return == (object instanceof Backpack || object instanceof Monster)
	 */
	public boolean isPossibleHolder(Object object) {
		return(object instanceof Backpack || object instanceof Monster);
	}
	
	/**
	 * Check if the item is not bound to a direct holder.
	 * @return 	True if the object is not bound to a direct holder.
	 * 			|return == (getDirectHolder() == null)
	 */
	public boolean canHaveDirectHolder() {
		return(getDirectHolder() == null);
	}
	
	/**
	 * Remove the direct holder of this item.
	 * @post		The direct holder of this item is removed.
	 * 			|this.getDirectHolder() == null
	 */
	public void setHolderToNull() {
		this.directHolder = null;
	}
}
