package RPG3;

import java.util.Random;

import Exceptions.IllegalHitpointsException;
import Items.Backpack;
import Items.Item;
import Items.Purse;
import Items.StoreItem;
import Items.Weapon;
import be.kuleuven.cs.som.annotate.Basic;

import java.math.*;

/**
 * A class of monsters with int values damage, protection, 
 * hitpoints and strength and String values name.
 * @invar	The name of each monster must be a legal
 *           name for a monster.
 * 			| isValidName(getName())
 * @invar	The hitpoints of each monster must be a legal
 *           hitpoints for a monster.
 * 			| isValidHitpoints(getHitpoints())
 * @invar	The protection of each monster must be a legal
 *           protection for a monster.
 * 			| isValidProtection(getProtection())
 * @invar	The amount of anchors must be a legal amount.
 * 			| isValidNumberAnchors(number)
 * @invar	Each item must be attachable to the monster.
 * 			| canHaveItem()
 * @invar	The total weight of items must be a legal value.
 * 			| isValidTotalWeight()
 * @invar	The protection number must be a prime.
 * 			| isPrime(number)
 * @invar	The hitpoints value must be a legal value.
 * 			|isValidHitpoints()
 * @invar	The monster must be alive to continue fighting.
 * 			|isAlive()
 * @author 	Robin and Ramon
 * @version 3.7
 *
 */

public class Monster {

	//CONSTRUCTOR

	/**
	 * Initialize this new mosnter with given name, hitpoints, weapon, backpack and number of anchors.
	 *
	 * @param 	name
	 * 			The name for this new monster.
	 * @param 	hitpoints
	 * 			The hitpoints value for this new monster.
	 * @param	weapon
	 * 			The weapon wielded by this new monster.
	 * @param	backpack
	 * 			The backpack wielded by this new monster.
	 * @param	numberAnchors
	 * 			The number of anchors of this new monster.
	 * @pre		The protection is a legal protection value
	 * 			|isValidProtection(protection)
	 * @post    The name of this new monster is equal to
	 *		    the given name.
	 *          | new.getName() == name
	 * @post    The damage of this new monster is equal to
	 *		    the damage randomly generated.
	 *          | new.getDamage() == generateDamage()
	 * @post    The hitpoints of this new monster is equal to
	 *		    the given hitpoints.
	 *          | new.getHitpoints() == hitpoints
	 * @post    The strength of this new monster is equal to
	 *		    the strength randomly generated.
	 *          | new.getStrength() == generateStrength()
	 * @post		The carrying capacity of this new monster is equal to
	 * 			the strength value times 12, expressed in kilograms.
	 * 			| new.getCarryingCapacity() == new.getStrength()*12
	 * @post		The number of anchors of this new monster is equal to
	 * 			the given number of anchors.
	 * 			| new.getAnchors().length == numberAnchors
	 * @post		The weapon of this new monster is equipped in its left hand.
	 * 			| new.getAnchors()[0] == weapon
	 * @post		The backpack of this new monster is equipped on its back.
	 * 			| new.getAnchors()[2] == backpack
	 * @throws	IllegalArgumentException
	 * 			The given name is invalid or doesn't exist.
	 * 			| !isValidName(name)
	 * @throws	IllegalArgumentException
	 * 			The number of anchors is not a positive integer.
	 * 			| !isValidNumberAnchors(numberAnchors)
	 * 
	 */
	
	public Monster(String name, int protection, int hitpoints, Weapon weapon, Backpack backpack, int numberAnchors) throws IllegalArgumentException {
		if(! isValidName(name) )
			throw new IllegalArgumentException(name);
		this.name = name;
		setDamage(generateDamage());
		assert isValidProtection(protection);
		this.protection = protection;
		this.MAX_HITPOINTS = hitpoints;
		try {
			setHitpoints(hitpoints);}
		catch(IllegalHitpointsException ae) {
			System.out.println(ae.getMessage());
			System.exit(1);
		}
		this.strength = generateStrength();
		this.carryingCapacity = new BigDecimal(String.valueOf((12*Math.abs(getStrength()))));
		if(! isValidNumberAnchors(numberAnchors) )
			throw new IllegalArgumentException();
		this.anchors = new Item[numberAnchors];
		weapon.obtain(this);
		backpack.obtain(this,2);
	}

	//2.2.1 ANCHOR
	/**
	 * List of items representing the anchors of this monster.
	 */
	private Item[] anchors;
	
	/**
	 * return the List of items slotted in the anchors of this monster.
	 */
	@Basic
	public  Item[] getAnchors() {
		return this.anchors;
	}
	
	/**
	 * @param 	numberAnchors
	 * 		  	The number of anchors to be checked.
	 * @return 	True if the number of anchors is greater than or equal to 3.
	 * 		   	|(return = (numberAnchors >= 3))
	 */
	public boolean isValidNumberAnchors(int numberAnchors) {
		return (numberAnchors >= 3);
	}
	
	/**
	 * Sets an item at an anchor point of this monster.
	 * @param 	item
	 * 		  	Item to be set.
	 * @post		If this holder has no direct holder, 
	 * 			the direct holder of the item is this monster.
	 * 			|if(item.getDirectHolder() == null)
	 * 			| 	then item.getDirectHolder() == new
	 * @post		The item will be set at the lowest free anchor index.
	 * 			|for each index in 0..getAnchors().length-1:
	 * 			|	if (getAnchors[i] == null)
	 * 			|		then new.getAnchors[i] == item
	 * @throws	IllegalArgumentException
	 * 			This monster does not have a free anchor point.
	 * 			|!canHaveItem()
	 * @throws	IllegalArgumentException
	 * 			The total weight of this monster + the new item exceeds the carrying capacity.
	 * 			|!isValidTotalWeight(item)
	 */
	public void setItem(Item item) {
		if(!canHaveItem())
			throw new IllegalArgumentException("Can not have as item");
		if(!isValidTotalWeight(item))
			throw new IllegalArgumentException("The total weight is not valid");
		if(item.getDirectHolder() == null)
			item.obtain(this);
		for(int i=0; i<anchors.length; i++) {
			if (anchors[i] == null) {
				this.anchors[i] = item; break;}
		}
	}
	
	/**
	 * Sets an item at a given anchor point of this monster.
	 * @param 	item
	 * 		  	Item to be set.
	 * @param	anchorPoint
	 * 			Anchor point at which item will be set.
	 * @post		If this holder has no direct holder, 
	 * 			the direct holder of the item is this monster.
	 * 			|if(item.getDirectHolder() == null)
	 * 			| 	then item.getDirectHolder() == this
	 * @post		The item will be set at the given anchor point.
	 * 			|this.anchors[anchorPoint] == item
	 * @throws	IllegalArgumentException
	 * 			The anchor point at this index is occupied.
	 * 			|!canHaveItem(anchorPoint)
	 * @throws	IllegalArgumentException
	 * 			The total weight of this monster + the new item exceeds the carrying capacity.
	 * 			|!isValidTotalWeight(item)
	 */
	public void setItem(Item item, int anchorPoint) {
		if(!canHaveItem(anchorPoint))
			throw new IllegalArgumentException("Can not have as item");
		if(!isValidTotalWeight(item))
			throw new IllegalArgumentException("The total weight is not valid");
		if(item.getDirectHolder() == null)
			item.obtain(this,anchorPoint);
		this.anchors[anchorPoint] = item;
	}
	
	/**
	 * Check whether this monster has a free anchor point.
	 * @return	True if this monster does not have all anchor points occupied.
	 * 			|for 1 index in 0..anchors.length-1:
	 * 			|	getAnchors[i] == null
	 */
	public boolean canHaveItem() {
		for(int i=0; i<anchors.length; i++) {
			if (anchors[i] == null)
				return true;
		}
		return false;
	}
	
	/**
	 * Check whether this monster has a free anchor point at the given index.
	 * @return	True if this monster has a free anchor point at the given index.
	 * 			|return = (anchors[anchorPoint] == null)
	 */
	public boolean canHaveItem(int anchorPoint) throws IllegalArgumentException{
		if(anchorPoint >= anchors.length || anchorPoint < 0)
			return false;
		return (anchors[anchorPoint] == null);
	}
	
	/**
	 * Check if this monster has the given item attached at one of its anchor points.
	 * @param 	item
	 * 			The item to be checked.
	 * @return	True if the monster has the given item attached to one of its anchor points.
	 * 			|for 1 index in 0..anchors.length-1:
	 * 			|	getAnchors[i] == item
	 */
	public boolean hasItem(Item item) {
		for(int i=0; i<anchors.length; i++) {
			if (anchors[i] == item) 
				return true;
		}
		return false;
	}
	
	/**
	 * Drop the given item.
	 * @param 	storeItem
	 * 			The store item to be dropped.
	 * @post		The item doesn't have a direct holder.
	 * 			|item.getDirectHolder() == null
	 * @post		This monster does not have the item attached to one of its anchors anymore.
	 * 			|for each index in 0..anchors.length-1:
	 * 			|	new.getAnchors[i] != item
	 * @throws 	IllegalArgumentException
	 * 			The item does not belong to this monster.
	 * 			|!hasItem(storeItem)
	 */
	public void drop(StoreItem storeItem) throws IllegalArgumentException{
		if (!hasItem(storeItem))
			throw new IllegalArgumentException();
		if(storeItem.getDirectHolder() != null)
			storeItem.setHolderToNull();
		setItemToNull(storeItem);
	}
	
	/**
	 * Replace an item at the given index with another item.
	 * @param 	index
	 * 			The index at which the item will be removed.
	 * @param 	replacement
	 * 			The item to be attached to this monster.
	 * @throws 	IllegalArgumentException
	 * 			The index does not refer to a legal anchor point.
	 * 			|(index > this.getAnchors().length || index < 0) 
	 * @throws	IllegalArgumentException
	 * 			The new total weight isn't valid.
	 * 			|!isValidTotalWeight(replacement,this.getAnchors()[index])
	 * @post		If the item at the given index is a store item, the item will be dropped.
	 * 			|if (StoreItem.class.isAssignableFrom(this.getAnchors()[index].getClass()))
	 * 			|	then item.getDirectHolder() == null
	 * 			|		&& 	for each index in 0..getAnchors().length-1:
	 * 			|			new.getAnchors[i] != item
	 * @post		If the item at the given index is not a store item, it will be destroyed.
	 * 			|if (! (StoreItem.class.isAssignableFrom(this.getAnchors()[index].getClass())))
	 * 			|	then item.getDirectHolder() == null
	 * 			|		 && item.isDestroyed() == true
	 * 			|		 && for each index in 0..getAnchors().length-1:
	 * 			|			new.getAnchors[i] != item
	 * @post		The replacement will be attached to monster at the given index.
	 * 			|new.getAnchors[index] == item
	 */
	public void replaceItem(int index, Item replacement) throws IllegalArgumentException{
		if (index > this.getAnchors().length || index < 0) 
			throw new IllegalArgumentException();
		if(!isValidTotalWeight(replacement,this.getAnchors()[index]))
			throw new IllegalArgumentException();
		if (StoreItem.class.isAssignableFrom(this.getAnchors()[index].getClass())) {
			drop((StoreItem) this.getAnchors()[index]);
		}
		else {
			((Weapon) this.getAnchors()[index]).destroy();
		}
		replacement.obtain(this,index);
	}
	
	/**
	 * Removes all bonds between the given item and this monster.
	 * @param 	item
	 * 			The item of which the bonds should be broken.
	 * @post		If the item is bonded with this monster, the item will become unattached from the anchor point.
	 * 			|if (item.getDirectHolder() == this)
	 * 			|	then for each index in 0..getAnchors().length-1:
	 * 			|			getAnchors()[i] != item
	 * @post		If the item is bonded with this monster, this item will not have a direct holder.
	 * 			|if (item.getDirectHolder() == this)
	 * 			|	item.getDirectHolder() == null
	 */
	public void setItemToNull(Item item) {
		if (item != null) {
			for(int i=0; i<anchors.length; i++) {
				if (anchors[i] == item) {
					this.anchors[i] = null; break;}
			}
			item.setHolderToNull();
		}
	}
	
	/**
	 * Return the total value of all items attached to this monster.
	 * @return 	The total value of all items attached to this monster.
	 * 			|for each index in 0..getAnchors().length-1:
	 * 			|	if (getAnchors()[i] != null )
	 * 			|		then totalValue += anchors[i].getTotalValue()
	 */
	public int getTotalValue() {
		int totalValue = 0;
		for(int i=0; i<anchors.length; i++) {
			if (anchors[i] != null ) {
				totalValue += anchors[i].getTotalValue();
			}
		}
		return totalValue;
	}
	
	/**
	 * Return the total weight of all items attached to this monster.
	 * @return 	The total weight of all items attached to this monster.
	 * 			|for each index in 0..getAnchors().length-1:
	 * 			|	if (getAnchors()[i] != null )
	 * 			|		then totalWeight += getAnchors[i].getTotalWeight()
	 */
	public BigDecimal getTotalWeight() {
		BigDecimal totalWeight = BigDecimal.ZERO;
		for(int i=0; i<anchors.length; i++) {
			if (anchors[i] != null ) {
				totalWeight = totalWeight.add(anchors[i].getTotalWeight());
			}
		}
		return totalWeight;
	}
	
	/**
	 * Check if the total weight of all attached items and a given item does not exceed the carrying capacity of this monster.
	 * @param 	item
	 * 			The extra item to be weighted.
	 * @return	True if the total weight does not exceed the carrying capacity.
	 * 			| return = getTotalWeight() + item.getWeight() <= getCarryingCapacity()
	 */
	public boolean isValidTotalWeight(Item item) {
		BigDecimal totalWeight = getTotalWeight();
		return (totalWeight.add(item.getWeight()).compareTo( getCarryingCapacity()) == -1 || totalWeight.add(item.getWeight()).compareTo( getCarryingCapacity()) == 0);
	}
	
	/**
	 * Check if the total weight of all attached items and a given item, substracted with another given item, does not exceed the carrying capacity of this monster.
	 * @param 	item
	 * 			The extra item to be weighted.
	 * @param	itemToRemove
	 * 			The item to be substracted from the weight.
	 * @return	True if the total weight does not exceed the carrying capacity.
	 * 			| return = getTotalWeight() + item.getWeight() - itemToRemove.getWeight() <= getCarryingCapacity()
	 */
	public boolean isValidTotalWeight(Item item, Item itemToRemove) {
		BigDecimal totalWeight = getTotalWeight().subtract(itemToRemove.getWeight());
		if(totalWeight.add(item.getWeight()).compareTo( getCarryingCapacity()) == -1 || totalWeight.add(item.getWeight()).compareTo( getCarryingCapacity()) == 0)
			return true;
		return false;
	}
	//2.1.1 NAME

	/**
	 * Variable registering the name of this monster.
	 */
	private final String name;

	/**
	 * Return the name of this monster.
	 */
	@Basic
	public String getName() {
		return this.name;
	}

	/**
	 * Variable registering the regular expresion used to specify
	 * the name of the monster.
	 */
	private final static String regExpr = "^[A-Z][A-Za-z0-9' ]+";

	/**
	 * Checks if the name of the monster is valid.
	 * @param 	name
	 * 			The name to check
	 * @return	true if the name exists and the regular expression recognizes the name.
	 * 			|return = (name.matches(regExpr) && name != null)
	 */
	public boolean isValidName(String name){
		return (name.matches(regExpr) && name != null);
	}

	//2.1.2 DAMAGE

	/**
	 * Variable registering the damage value of this monster.
	 */
	private int damage;

	/**
	 * Return the damage value of this monster.
	 */
	@Basic
	public int getDamage() {
		return this.damage;
	}

	/**
	 * Generates the damage of this monster.
	 * 
	 * @return  A generated number between the minimum damage and the
	 * 			maximum damage
	 *          | result == (random.nextInt(getMaxDamage()+MIN_DAMAGE))
	 */
	public int generateDamage() {
		Random  random = new Random();
		return random.nextInt(getMaxDamage()+MIN_DAMAGE);
	}

	/**
	 * Sets the damage of this monster to a new damage.
	 * @param 	newDamage
	 * 			The new damage value of this monster
	 * @post		If the entered value is within the accepted limits, the new damage value is the entered value.
	 * 			| if ((newDamage >= MIN_DAMAGE) && (newDamage <= getMaxDamage()))
	 * 			| 	then new.getDamage() == newDamage
	 * @post		If the entered value is lower than the minimum accepted damage, the new damage value is the minimum accepted damage.
	 * 			| if (newDamage < MIN_DAMAGE)
	 * 			| 	then new.getDamage() == MIN_DAMAGE
	 * @post		If the entered value is higher than the maximum accepted damage, the new damage value is the maximum accepted damage.
	 * 			| if (newDamage > getMaxDamage())
	 * 			| 	then new.getDamage() == getMaxDamage()
	 */
	public void setDamage(int newDamage) {
		if ((newDamage >= MIN_DAMAGE) && (newDamage <= getMaxDamage()))
			this.damage = newDamage;
		else if (newDamage < MIN_DAMAGE)
			this.damage = MIN_DAMAGE;
		else 
			this.damage = getMaxDamage();
	}

	/**
	 * Variable registering the highest possible damage value of monsters.
	 */
	private static int MAX_DAMAGE = 20;

	/**
	 * Return the maximum damage value of this monster..
	 */
	@Basic
	public static int getMaxDamage() {
		return MAX_DAMAGE;
	}

	/**
	 * Variable registering the lowest possible damage value of monsters.
	 */
	private final static int MIN_DAMAGE = 1;

	//2.1.3 PROTECTION

	/**
	 * Variable registering the protection value of a monster.
	 */
	private final int protection;

	/**
	 * Returns the protection value of this monster.
	 */
	@Basic
	public int getProtection() {
		return this.protection;
	}

	/**
	 * Checks if the value of the protection is valid.
	 * @param 	protection
	 * 			The protection value to be checked.
	 * @pre		The protection value is a prime number
	 * 			|isPrime(protection)
	 * @return	true if the protection value is within the range
	 * 			specified by the highest and lowest possible protection values.
	 * 			| return == ((protection >= MIN_PROTECTION) && (protection <= getMaxProtection()))
	 */
	public boolean isValidProtection(int protection) {
		assert isPrime(protection);
		return ((protection >= MIN_PROTECTION) && (protection <= getMaxProtection()));
	}

	/**
	 * Checks if a number given is a prime number.
	 * 
	 * @param 	num
	 * 			The number to be checked.
	 * @return	True if the number is two.
	 * 			| if (num == 2)
	 * 			|	then return == true
	 * 			True if the number is not divisible by anything other than one or itself.
	 * 			| if (for each index in 3..num/2:
	 * 			|		num % i != 0)
	 * 			|	then return == true
	 */
	public static boolean isPrime(float num) {
		if (num == 1) return false;
		if (num == 2) return true;
		if (num % 2 == 0) return false;
		for (int i = 3; i*2 < num; i += 2)
			if (num % i == 0) return false;
		return true;
	}

	/**
	 * Variable registering the highest possible protection value of a monster.
	 */
	private static int MAX_PROTECTION = 40;

	/**
	 * Returns the highest possible protection value of this monster.
	 */
	@Basic
	public int getMaxProtection() {
		return MAX_PROTECTION;
	}

	/**
	 * Variable registering the lowest possible protection value of monsters.
	 */
	private final static int MIN_PROTECTION = 1;

	//2.1.4 HITPOINTS

	/**
	 * Variable registering the hitpoints value of a monster.
	 */
	private int hitpoints;

	/**
	 * Returns the hitpoints value of this monster.
	 */
	public int getHitpoints() {
		return this.hitpoints;
	}

	/**
	 * Sets the hitpoints of this monster to a new hitpoitns.
	 * @param 	newHitpoints
	 * 			The new hitpoints value of this monster.
	 * @post		The hitpoints of this monster is equal to the given hitpoints value.
	 * 			| this.getHitpoints() == newHitpoints
	 * @throws 	IllegalNameException
	 * 			The new hitpoints isn't valid.
	 * 			| !isValidHitpoints(newHitpoints)
	 */
	public void setHitpoints(int newHitpoints) throws IllegalHitpointsException {
		if (!isValidHitpoints(newHitpoints))
			throw new IllegalHitpointsException(newHitpoints);
		this.hitpoints = newHitpoints;
	}

	/**
	 * Checks if the value of the hitpoints is valid.
	 * @param 	hitpoints
	 * 			The hitpoints value to be checked.
	 * @return	true if the hitpoints value is within the range
	 * 			specified by the highest and lowest possible hitpoints values.
	 * 			| return ==
	 * 			| 	((hitpoints >= MIN_HITPOINTS) && (hitpoints <= getMaxHitpoints()))
	 */
	public boolean isValidHitpoints(int hitpoints) {
		return ((hitpoints >= MIN_HITPOINTS) && (hitpoints <= getMaxHitpoints()));
	}

	/**
	 * Variable registering the highest possible hitpoints value of this monster.
	 */
	private int MAX_HITPOINTS;

	/**
	 * Returns the highest possible hitpoints value of this monster.
	 */
	@Basic
	public int getMaxHitpoints() {
		return this.MAX_HITPOINTS;
	}

	/**
	 * Variable registering the lowest possible hitpoints value of monsters.
	 */
	private final static int MIN_HITPOINTS = 0;

	/**
	 * Checks if the monster is alive.
	 * @return	true if the hitpoints value is strictly larger than the min hitpoints value.
	 * 			| return = (this.getHitpoints() > MIN_HITPOINTS)
	 */
	public boolean isAlive() {
		return(this.getHitpoints() > MIN_HITPOINTS);
	}

	//2.1.5 STRENGTH

	/**
	 * Variable registering the strength value of this monster.
	 */
	private final int strength;

	/**
	 * Returns the strength value of this monster.
	 */
	@Basic
	public int getStrength() {
		return this.strength;
	}

	/**
	 * Generates the protection of the monster.
	 * 
	 * @return  A generated number between -15 and 35 (average
	 * 			of 10).
	 *          | result == ((random.nextInt(51) - 25) + 10)
	 */
	public int generateStrength() {
		Random  random = new Random();
		return (((random.nextInt(51) - 25) + 10));
	}

	/**
	 * The carrying capacity of this monster.
	 */
	private final BigDecimal carryingCapacity;
	
	/**
	 * Returns the carrying capacity of this monster.
	 */
	@Basic
	public BigDecimal getCarryingCapacity() {
		return this.carryingCapacity;
	}
	//2.1.6 HITTING

	/**
	 * @param 	opponent
	 * 			The monster upon which the hit method is cast.
	 * @post		If the hitpoints value of this monster is lower than a randomly generated number between 1 and 31, the comparer will be set to the hitpoints value.
	 * 			| if (this.hitpoints < randomNumber)
	 * 			|	then comparer == getHitpoints()
	 * @post		If the comparer is larger than the protection value of the opponent,
	 * 			the opponent's hitpoints will be substracted by this monster's damage + the right handed weapon's damage + the strength value of this monster - 5, divided by 3.
	 * 			| if (comparer > opponent.getProtection())
	 * 			|	then new.getHitpoints() == (opponent.getHitpoints() - getDamage() + getAnchors()[1].getDamage() + getStrength() - 5)/3
	 * @throws	IllegalHitpointsException
	 * 			The new hitpoints isn't valid.
	 * 			| !isValidHitpoints(opponent.hitpoints - (this.damage + (this.strength-5)/3))
	 * @note 	no else statements because of inertia axiom
	 */
	public void hit(Monster opponent) throws IllegalArgumentException, IllegalHitpointsException {
		if(this.isAlive()) {
		Random  random = new Random();
		int randomNumber = random.nextInt(31);
		int weaponDamage = 0;
		if (this.anchors[1] instanceof Weapon)
			weaponDamage = ((Weapon)this.anchors[1]).getDamage();
		if (this.hitpoints < randomNumber)
			randomNumber = this.hitpoints;
		
		if (randomNumber > opponent.protection)
			try {
				System.out.println(this.name+" attacks");
				opponent.setHitpoints(opponent.hitpoints - (this.damage + weaponDamage + (this.strength-5)/3));}
			catch(IllegalHitpointsException ae) {opponent.setHitpoints(0);}
		else 
			System.out.println(this.name+" attack missed.");}

	}

	public static void main(String[] args) {
		Weapon pizza = new Weapon(1, 20);
		Weapon burrito = new Weapon(1, 20);
		Backpack back = new Backpack(2,20,20);
		Backpack sack = new Backpack(2,10,10);
		Monster monster1 = new Monster("Destroyer o' Hope", 11, 100, pizza, back, 5);
		Monster monster2 = new Monster("Lonely little goblin", 17, 100, burrito, sack, 4);
		String repeatedStar = new String(new char[60]).replace('\0', '*');
		System.out.println(repeatedStar);
		System.out.println(monster1.name+" has damage: "+monster1.damage+" ; protection: "+monster1.protection+"; strength: "+monster1.strength+"; hitpoints: "+monster1.hitpoints+".");
		System.out.println(monster1.name+" has weapon with ID: "+monster1.getAnchors()[0].getId()+"; damage: "+((Weapon)monster1.getAnchors()[0]).getDamage()+"; value: "+((Weapon)monster1.getAnchors()[0]).getValue()+"; weight: "+((Weapon)monster1.getAnchors()[0]).getWeight()+".");
		System.out.println(monster1.name+" has backpack with weight: "+((Backpack)monster1.getAnchors()[2]).getWeight()+"; capacity: "+((Backpack)monster1.getAnchors()[2]).getCapacity()+"; value: "+((Backpack)monster1.getAnchors()[2]).getValue()+".");
		System.out.println("");
		System.out.println(monster2.name+" has damage: "+monster2.damage+"; protection: "+monster2.protection+"; strength: "+monster2.strength+"; hitpoints: "+monster2.hitpoints+".");
		System.out.println(monster2.name+" has weapon with ID: "+monster2.getAnchors()[0].getId()+"; damage: "+((Weapon)monster2.getAnchors()[0]).getDamage()+"; value: "+((Weapon)monster1.getAnchors()[0]).getValue()+"; weight: "+((Weapon)monster1.getAnchors()[0]).getWeight()+".");
		System.out.println(monster2.name+" has backpack with weight: "+((Backpack)monster2.getAnchors()[2]).getWeight()+"; capacity: "+((Backpack)monster2.getAnchors()[2]).getCapacity()+"; value: "+((Backpack)monster2.getAnchors()[2]).getValue()+".");
		System.out.println(repeatedStar);
		Backpack rucksack = new Backpack(2,35,250);
		Weapon baguette = new Weapon(2, 20);
		baguette.addToBackpack(rucksack);
		Random random = new Random();
		Purse purse = new Purse(1,30,20,random.nextInt(21));
		Purse largePurse = new Purse(1,120,50,random.nextInt(51));
		purse.addToBackpack(rucksack);
		largePurse.addToBackpack(rucksack);
		monster1.replaceItem(2, rucksack);
		System.out.println(monster1.name+" has replace its backpack with a new one with weight: "+((Backpack)monster1.getAnchors()[2]).getWeight()+"; capacity: "+((Backpack)monster1.getAnchors()[2]).getCapacity()+"; value: "+((Backpack)monster1.getAnchors()[2]).getValue()+".");
		System.out.println("The contents of this new backpack are: "+rucksack.getContents()+",\nthe total value is "+rucksack.getTotalValue()+" an the total weight is "+rucksack.getTotalWeight()+".");
		System.out.println(repeatedStar);
		Monster attacker = monster1;
		Monster opponent = monster2;
		int randomNumber = random.nextInt(2);
		if (randomNumber == 0) {
			attacker = monster2;
			opponent = monster1;}
		System.out.println(attacker.name+" will be the first monster to attack");
		int count = 1;
		while (attacker.isAlive() && opponent.isAlive()) {

			System.out.println("ROUND "+count);
			try {
				attacker.hit(opponent);}
			catch(IllegalHitpointsException ae) {}
			System.out.println(opponent.name+" has "+opponent.hitpoints);
			try {
				opponent.hit(attacker);}
			catch(IllegalHitpointsException ae) {}
			System.out.println(attacker.name+" has "+attacker.hitpoints);
			count++;
		}	
		System.out.println(repeatedStar);
		
		Monster winner;
		Monster loser;
		if(attacker.isAlive()) {
			winner = attacker;
			loser = opponent;}
		else {
			winner = opponent;
			loser = attacker;}
		
		Backpack winnerBackpack = new Backpack(1,1,1);
		winnerBackpack = ((Backpack)winner.getAnchors()[2]);
		
		for(int i=0;i<loser.getAnchors().length;i++)
			if(loser.getAnchors()[i] != null) {
				loser.getAnchors()[i].addToBackpack(winnerBackpack);
			}
		
		System.out.println(winner.name+" has damage: "+winner.damage+" ; protection: "+winner.protection+"; strength: "+winner.strength+"; hitpoints: "+winner.hitpoints+".");
		System.out.println(winner.name+" has weapon with ID: "+winner.getAnchors()[0].getId()+"; damage: "+((Weapon)winner.getAnchors()[0]).getDamage()+"; value: "+((Weapon)winner.getAnchors()[0]).getValue()+"; weight: "+((Weapon)winner.getAnchors()[0]).getWeight()+".");
		System.out.println(winner.name+" has backpack with weight: "+(winnerBackpack.getTotalWeight())+"; capacity: "+(winnerBackpack.getCapacity())+"; value: "+(winnerBackpack.getTotalValue())+".");
		System.out.println("This backpack has the following objects:");		
		System.out.println(winnerBackpack.getContents());
		System.out.println("The lightest item is "+winnerBackpack.getLightestItem());
		System.out.println("The heaviest item is "+winnerBackpack.getHeaviestItem());
	}

}
