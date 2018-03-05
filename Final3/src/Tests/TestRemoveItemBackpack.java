package Tests;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import Items.Backpack;
import Items.Purse;
import Items.Weapon;
import RPG3.Monster;

public class TestRemoveItemBackpack {

	Weapon pizza;
	Weapon burrito;
	Weapon heavyWeapon;
	Backpack back;
	Backpack sack;
	Monster monster1;
	Purse purse;
	Purse largePurse;
	
	@Before
	public void setUp() throws Exception {
		pizza = new Weapon(1, 20);
		burrito = new Weapon(2, 20);
		heavyWeapon = new Weapon(1000, 20);
		back = new Backpack(2,20,20);
		sack = new Backpack(3, 40,40);
		purse = new Purse(1,30,20,18);
		largePurse = new Purse(1,120,50,40);
		monster1 = new Monster("Destroyer o' Hope", 13, 100, pizza, back, 5);
		pizza.addToBackpack(sack); //lightest item in sack
		purse.addToBackpack(sack);  //heaviest item in sack
		burrito.addToBackpack(back); //lightest item in back
		sack.addToBackpack(back); //heaviest item in back
		// Total value of back is 148
		// Total weight of back is 9.80
	}
	
	//NECESSARY TEST FOR THE METHOD THAT REMOVES AN ITEM FROM A BACKPACK
	
	//Test to assure that the method works. (if done right)
	//It is removing the element in position 0, so purse will be the new element in position 0.
	@Test
	public void removeFromBackpack1() throws Exception {
		pizza.removeFromBackpack();
		assertEquals(purse, sack.getContents().get(0));
	}
	
	//Test to assure that when an item is removed, its direct holder is set to null.
	@Test
	public void removeFromBackpack2() throws Exception {
		pizza.removeFromBackpack();
		assertEquals(null, pizza.getDirectHolder());
	}
	
	//Test to assure that when an item is removed from a backpack held by a monster, its indirect holder is set to null.
	@Test
	public void removeFromBackpack3() throws Exception {
		pizza.removeFromBackpack();
		assertEquals(null, pizza.getIndirectHolder(pizza));
	}
	
	//Test to assure that when a weapon is removed from a backpack, is destroyed.
	@Test
	public void removeFromBackpack4() throws Exception {
		pizza.removeFromBackpack();
		assertEquals(true, pizza.isDestroyed());
	}
	
	//Test to assure that you can not remove an item that is not contained in a backpack.
	@Test (expected = IllegalArgumentException.class) 
	public void removeFromBackpack5() throws Exception {
		heavyWeapon.removeFromBackpack();
	}
	
	//Test to assure that when removing an item, the total value is calculated well.
	@Test 
	public void removeFromBackpack6() throws Exception {
		sack.removeFromBackpack(); //sack has a value of 88
		assertEquals(40, back.getTotalValue());
	}
	
	//Test to assure that when removing an item, the total weight is calculated well.
	@Test 
	public void removeFromBackpack7() throws Exception {
		sack.removeFromBackpack(); //sack has a weight of 5.90
		BigDecimal result = new BigDecimal("4.00"); 
		assertEquals(result, back.getTotalWeight());
	}
}
