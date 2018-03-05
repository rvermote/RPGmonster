package Items;

import java.math.BigDecimal;
import java.util.Random;

import RPG3.Monster;
import be.kuleuven.cs.som.annotate.Basic;

/**
 * A class of weapons which can be equipped by monsters or put in backpacks.
 * @invar	The damage of a weapon must be valid.
 * 			| isValidDamage(getDamage())
 * @author Robin and Ramon
 * @version 3.7
 *
 */
public class Weapon extends Item {

	/**
	 * Initialize a new weapon.
	 * @param 	weight
	 * 			The weight of the new weapon.
	 * @param 	value
	 * 			The value of the new weapon.
	 * @effect	The weight and value are initialized.
	 * 			|super(weight, value)
	 * @post		The damage of this new weapon is equal to
	 *		    the damage randomly generated.
	 *          | new.getDamage() == generateDamage()
	 */
	public Weapon (double weight, int value) throws IllegalArgumentException{
		super(weight, value);	
		this.damage = generateDamage();
	}

	//2.1.1: IDENTIFICATION

	/**
	 * Variable used in creating the ID.
	 */
	private static long idCounter=0;

	/**
	 * Generates the id of the weapon.
	 * 
	 * @return  A generated odd number that has is unique to each purse.
	 *          | result == (idCounter + 2)
	 */
	@Override
	protected long createId() {
		while (idCounter % 2 == 0)
			idCounter++;
		return idCounter++;
	}

	/**
	 * Boolean variable indicanting if a weapon is destroyed.
	 */
	private boolean destroyed = false;

	/**
	 * Returns wheteher the weapon is destroyed.
	 */
	@Basic
	public boolean isDestroyed() {
		return this.destroyed;
	}

	//2.1.2: WEIGHT
	
	/**
	* Return the total weight of this weapon.
	*/
	@Basic @Override
	public BigDecimal getTotalWeight() {
		return weight;
	}
	
	//2.1.3: VALUE
	/**
	* Return the total value of this weapon.
	*/
	@Basic @Override
	public int getTotalValue() {
		return value;
	}

	//2.1.4 DAMAGE

	/**
	 * Variable registering the damage value of this weapon.
	 */
	private int damage;

	/**
	 * Return the damage value of this weapon.
	 */
	@Basic
	public int getDamage() {
		return this.damage;
	}

	/**
	 * Generates the damage of this weapon.
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
	 * Checks if the damage of the weapon is valid.
	 * @param 	damage
	 * 			The damage to check
	 * @return	true if the damage value is within the range
	 * 			specified by the highest and lowest possible damage values.
	 * 			| return == ((damage >= MIN_DAMAGE) && (damage <= getMaxDamage()))
	 */
	public boolean isValidDamage(int damage) {
		return(damage >= MIN_DAMAGE && damage <= getMaxDamage());
	}

	/**
	 * Sets a new damage to the weapon.
	 * @param 	newDamage
	 * 			The new damage value of this weapon
	 * @pre		The new damage is a legal damage value
	 * 			|isValidDamage(newDamage)
	 */
	public void setDamage(int newDamage) {
		assert isValidDamage(newDamage);
		this.damage=newDamage;
	}

	/**
	 * Variable registering the highest possible damage value of the weapon.
	 */
	private static int MAX_DAMAGE = 20;

	/**
	 * Return the maximum damage value of the weapon.
	 */
	@Basic
	public static int getMaxDamage() {
		return MAX_DAMAGE;
	}

	/**
	 * Variable registering the lowest possible damage value of the weapon.
	 */
	private final static int MIN_DAMAGE = 1;

	//2.1.5 HOLDER
	/**
	 * Check if the weapon is not bound to a direct holder and if it is not destroyed.
	 * @return 	True if the object is not bound to a direct holder and is not destroyed.
	 * 			|return == (getDirectHolder() == null && isDestroyed() == false)
	 */
	@Override
	public boolean canHaveDirectHolder() {
		return (getDirectHolder() == null && isDestroyed() == false);
	}

	/**
	 * Destroy the weapon.
	 * @post		The weapon does not have a direct owner.
	 * 			|getDirectHolder() == null
	 * @post		The weapon is not attached to the monster it belonged to anymore.
	 * 			|for each index in 0..monster.getAnchors():
	 * 			|	monster.getAnchors()[i] != this
	 * @throws 	IllegalArgumentException
	 * 			The item is not directly anchored to the monster.
	 * 			|!(this.getDirectHolder() instanceof Monster)
	 */
	public void destroy() throws IllegalArgumentException {
		if(!(this.getDirectHolder() instanceof Monster))
			throw new IllegalArgumentException("The weapon is not held by a monster");
		((Monster) this.getDirectHolder()).setItemToNull(this);
		this.directHolder= null;
		this.destroyed = true;
	}
	
}
