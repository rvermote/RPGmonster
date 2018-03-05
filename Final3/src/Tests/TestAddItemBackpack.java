package Tests;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import Items.Backpack;
import Items.Purse;
import Items.Weapon;
import RPG3.Monster;

public class TestAddItemBackpack {

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
	}
	
	//NECESSARY TEST FOR THE METHOD THAT ADDS AN ITEM TO A BACKPACK
	
	//Test to assure that the method works. (if done right)
	@Test
	public void addToBackpack1() throws Exception {
		purse.addToBackpack(back);
		assertEquals(purse, back.getContents().get(0));
	}
	
	//Test to assure that when item is added to a backpack, direct holder is this backpack.
	@Test
	public void addToBackpack2() throws Exception {
		purse.addToBackpack(back);
		assertEquals(back, purse.getDirectHolder());
	}
	
	//Test to assure that when item is added to a backpack that is held by a monster, the indirect holder is this monster.
	@Test
	public void addToBackpack3() throws Exception {
		purse.addToBackpack(back);
		assertEquals(monster1, purse.getIndirectHolder(purse));
	}
	
	//Test to assure that when item is added to a backpack that is not held by a monster, the indirect holder is null.
	@Test 
	public void addToBackpack4() throws Exception {
		purse.addToBackpack(sack);
		assertEquals(null, purse.getIndirectHolder(purse));
	}
	
	//Test to assure that a backpack that has items can be added to a backpack.
	@Test 
	public void addToBackpack5() throws Exception {
		purse.addToBackpack(sack);
		sack.addToBackpack(back);
		assertEquals(sack, back.getContents().get(0));
	}
	
	//Test to assure that a weapon that has been detroyed can not be added to a backpack.
	@Test (expected = IllegalArgumentException.class) 
	public void addToBackpack6() throws Exception {
		burrito.destroy();
		burrito.addToBackpack(back);
	}
	
	//Test to assure that an item which is already in a backpack, can not be added to another backpack.
	@Test  (expected = IllegalArgumentException.class) 
	public void addToBackpack7() throws Exception {
		purse.addToBackpack(sack);
		purse.addToBackpack(back);
	}
	
	//Test to assure that an item which is already being held by a monster, can be added to a backpack.
	@Test  
	public void addToBackpack8() throws Exception {
		pizza.addToBackpack(back);
		assertEquals(pizza, back.getContents().get(0));
	}
	
	//Test to assure that an item which is already being held by a monster, can be added to a backpack and the
	//new direct holder of the item, would be the backpack
	@Test  
	public void addToBackpack9() throws Exception {
		pizza.addToBackpack(back);
		assertEquals(back, pizza.getDirectHolder());
	}
		
	//Test to assure that you can not add an item if it would exceed the carrying capacity of the backapck
	@Test  (expected = IllegalArgumentException.class) 
	public void addToBackpack10() throws Exception {
		heavyWeapon.addToBackpack(back);
	}
	
	//Test to assure that when an item is added to a backpack, is sorted by its weight.
	@Test 
	public void addToBackpack11() throws Exception {
		burrito.addToBackpack(back); //weight is 2.00
		pizza.addToBackpack(back); //weight is 1.00
		largePurse.addToBackpack(back); //weight is 3.00
		purse.addToBackpack(back); //weight is 1.90
		assertEquals(pizza, back.getContents().get(0));
		assertEquals(purse, back.getContents().get(1));
		assertEquals(burrito, back.getContents().get(2));
		assertEquals(largePurse, back.getContents().get(3));
	}
	
	//Test to assure that when an item is added to a backpack, the total weight of the backpack is calculated correctly.
	@Test 
	public void addToBackpack12() throws Exception {
		burrito.addToBackpack(back); //weight is 2.00
		pizza.addToBackpack(back); //weight is 1.00
		largePurse.addToBackpack(back); //weight is 3.00
		purse.addToBackpack(back); //weight is 1.90
		BigDecimal result = new BigDecimal("9.90"); //the back already is 2.00
		assertEquals(result,back.getTotalWeight()); 
	}
	
	//Test to assure that when an item is added to a backpack, the total value of the backpack is calculated correctly.
	@Test 
	public void addToBackpack13() throws Exception {
		burrito.addToBackpack(back); //value of burrito is 20
		pizza.addToBackpack(sack); //value of pizza is 20
		purse.addToBackpack(sack); //value of purse is 48
		sack.addToBackpack(back); //value of sack is 40 (plus value of items inside)
		assertEquals(148,back.getTotalValue());  //the back already is 20
	}
	
	//Test to assure that the indirect holder works fine
	@Test 
	public void addToBackpack14() throws Exception {
		pizza.addToBackpack(sack); 
		sack.addToBackpack(back); 
		assertEquals(monster1,pizza.getIndirectHolder(pizza));
	}
}
