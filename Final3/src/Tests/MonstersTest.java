package Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import Exceptions.IllegalHitpointsException;
import Items.Backpack;
import Items.Weapon;
import RPG3.Monster;

public class MonstersTest {

	@Test(expected = IllegalArgumentException.class)
	public final void checkName1() throws Exception {
		Weapon pizza = new Weapon(1, 20);
		Backpack back = new Backpack(2,20,20);
		new Monster("al capo", 1, 200, pizza, back, 3);
	}
	@Test(expected = IllegalArgumentException.class)
	public final void checkName2() throws Exception {
		Weapon pizza = new Weapon(1, 20);
		Backpack back = new Backpack(2,20,20);
		new Monster("a", 1, 200, pizza, back, 3);
	}
	@Test(expected = IllegalArgumentException.class)
	public final void checkName3() throws Exception {
		Weapon pizza = new Weapon(1, 20);
		Backpack back = new Backpack(2,20,20);
		new Monster("Al 3*", 1, 200, pizza, back, 3);
	}
	@Test
	public final void checkName4() throws Exception {
		Weapon pizza = new Weapon(1, 20);
		Backpack back = new Backpack(2,20,20);
		new Monster("Al capo35", 1, 200, pizza, back, 3);
	}
	@Test(expected = IllegalHitpointsException.class)
	public final void checkHitpoints1 () throws Exception{
		Weapon pizza = new Weapon(1, 20);
		Backpack back = new Backpack(2,20,20);
		Monster Test = new Monster("Al capo35", 1, 200, pizza, back, 3);
		Test.setHitpoints(-5);
	}
	@Test(expected = IllegalHitpointsException.class)
	public final void checkHitpoints2 () throws Exception{
		Weapon pizza = new Weapon(1, 20);
		Backpack back = new Backpack(2,20,20);
		Monster Test = new Monster("Al capo35", 1, 200, pizza, back, 3);
		Test.setHitpoints(250);
	}
	@Test
	public final void checkHitpoints3 () throws Exception{
		Weapon pizza = new Weapon(1, 20);
		Backpack back = new Backpack(2,20,20);
		Monster Test = new Monster("Al capo35", 1, 100, pizza, back, 3);
		Test.setHitpoints(0);
	}
	@Test
	public final void checkHitpoints4 () throws Exception{
		Weapon pizza = new Weapon(1, 20);
		Backpack back = new Backpack(2,20,20);
		Monster Test = new Monster("Al capo35", 1, 100, pizza, back, 3);
		Test.setHitpoints(20);
	}
	@Test
	public final void setDamage1 () throws Exception{
		Weapon pizza = new Weapon(1, 20);
		Backpack back = new Backpack(2,20,20);
		Monster Test = new Monster("Al capo35", 1, 100, pizza, back, 3);
		Test.setDamage(30);
		assertEquals(20,Test.getDamage());
	}
	@Test
	public final void setDamage2 () throws Exception{
		Weapon pizza = new Weapon(1, 20);
		Backpack back = new Backpack(2,20,20);
		Monster Test = new Monster("Al capo35", 1, 100, pizza, back, 3);
		Test.setDamage(12);
		assertEquals(12,Test.getDamage());
	}
	@Test
	public final void setDamage3 () throws Exception{
		Weapon pizza = new Weapon(1, 20);
		Backpack back = new Backpack(2,20,20);
		Monster Test = new Monster("Al capo35", 1, 100, pizza, back, 3);
		Test.setDamage(-40);
		assertEquals(1,Test.getDamage());
	}
	@Test
	public final void isAlive1 () throws Exception{
		Weapon pizza = new Weapon(1, 20);
		Backpack back = new Backpack(2,20,20);
		Monster Test = new Monster("Al capo35", 1, 100, pizza, back, 3);
		Test.setHitpoints(20);
		assertTrue(Test.isAlive());
	}
	@Test
	public final void isAlive2 () throws Exception{
		Weapon pizza = new Weapon(1, 20);
		Backpack back = new Backpack(2,20,20);
		Monster Test = new Monster("Al capo35", 1, 100, pizza, back, 3);
		Test.setHitpoints(0);
		assertFalse(Test.isAlive());
	}
}
