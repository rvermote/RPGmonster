package Tests;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.Before;

import Items.Backpack;
import Items.Purse;
import Items.Weapon;
import RPG3.Monster;


public class TestReplaceMethod {
	
	Weapon pizza;
	Backpack back;
	Monster monster1;
	
	@Before
	public void setUp() throws Exception {
		pizza = new Weapon(1, 20);
		back = new Backpack(2,20,20);
		monster1 = new Monster("Destroyer o' Hope", 13, 100, pizza, back, 5);
	}
	
	//NECESSARY TEST FOR THE METHOD THAT REPLACES A WEAPON WITH ANOTHER ONE (WORKS FOR ALL ITEMS).
	//The test can give an error if the strength generated randomly is set to 0 (chance is really low).
	
	//Test to assure that the method works. (if done right).
	@Test
	public void replaceItem1() throws Exception {
		Weapon burrito =new Weapon(1, 20);
		monster1.replaceItem(0, burrito);
		assertEquals(burrito, monster1.getAnchors()[0]);
	}
	
	//Test to assure that when replacing an item, the item replaced holder is set to null.
	@Test 
	public void weaponHolderToNull1() throws Exception {
		Weapon burrito =new Weapon(1, 20);
		monster1.replaceItem(0, burrito);
		assertEquals(null, pizza.getDirectHolder());
	}
	
	//Test to assure that when replacing a weapon, the item replaced is destroyed.
	@Test 
	public void weaponDestroyed() throws Exception {
		Weapon burrito =new Weapon(1, 20);
		monster1.replaceItem(0, burrito);
		assertEquals(true, pizza.isDestroyed());
	}
	
	//Test to assure you can not obtain a weapon/item if it would exceed the total weight.
	@Test (expected = IllegalArgumentException.class) 
	public void replaceItem2() throws Exception {
		Weapon heavyweapon = new Weapon (1000, 20);
		monster1.replaceItem(0, heavyweapon);
	}
	
	//Test to assure you can not replace in an anchor that does not exist.
	@Test (expected = IllegalArgumentException.class) 
	public void replaceItem3() throws Exception {
		Weapon baguette =new Weapon(1, 20);
		monster1.replaceItem(-1, baguette);
	}
	
	//Test to assure you can not obtain a weapon/item if is already being held by a monster.
	@Test (expected = IllegalArgumentException.class) 
	public void replaceItem4() throws Exception {
		Backpack sack = new Backpack(2,20,20);
		Weapon baguette =new Weapon(1, 20);
		@SuppressWarnings("unused")
		Monster monster2 = new Monster("Lonely little goblin", 13, 100, baguette, sack, 4);
		monster1.replaceItem(0, baguette);
	}
	
	//Test to assure you can not obtain a weapon that is destroyed.
	@Test (expected = IllegalArgumentException.class) 
	public void replaceItem5() throws Exception {
		Weapon burrito =new Weapon(1, 20);
		burrito.destroy();
		monster1.replaceItem(0, burrito);
	}
	
	//Test to assure you can not replace an item in an anchor that is empty (NullPointerException).
	@Test (expected = NullPointerException.class) 
	public void replaceItem6() throws Exception {
		Weapon burrito =new Weapon(1, 20);
		monster1.replaceItem(1, burrito);
		assertEquals(burrito, monster1.getAnchors()[1]);
	}
	
	//Test to assure the total weight of the monster is set correctly after replacing a weapon.
	//At the start the weight was 3 (1 + 2). After replacing the weapon pizza (1) with the new weapon burrito (5), the weight is 7.
	@Test 
	public void totalWeightMonster() throws Exception {
		Weapon burrito =new Weapon(5, 20);
		monster1.replaceItem(0, burrito);
		BigDecimal result = new BigDecimal("7.00");
		assertEquals(result, monster1.getTotalWeight());
	}
	
	//Test to assure that in the case there is a store item in the anchor point, the item is dropped and can be obtained later.
	@Test 
	public void replaceItem7() throws Exception {
		Purse purse = new Purse(1, 2, 4, 20);
		purse.obtain(monster1, 1);
		Weapon burrito =new Weapon(1, 20);
		monster1.replaceItem(1, burrito);
		monster1.replaceItem(1, purse);
		assertEquals(purse, monster1.getAnchors()[1]);
	}
	
}
