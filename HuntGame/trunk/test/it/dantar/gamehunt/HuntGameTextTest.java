package it.dantar.gamehunt;

import it.dantar.gamehunt.finder.PreloadedFinder;
import it.dantar.gamehunt.rules.HuntConditionAction;
import it.dantar.gamehunt.rules.HuntConsequence;
import it.dantar.gamehunt.rules.HuntConsequenceEvent;
import it.dantar.gamehunt.rules.HuntTrigger;

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HuntGameTextTest {

	HuntGame game;
	private PreloadedFinder finder;
	
	@Before
	public void setUp() throws Exception {
		this.game = new HuntGame();
		this.finder = new PreloadedFinder();
		this.game.setFinder(this.finder);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStartingEquipment() {
		Assert.assertEquals(0, this.game.getEquipment().size());
		Assert.assertEquals(0, this.game.getDiary().size());
		Assert.assertTrue(! this.game.hasItem("sword"));
		Assert.assertTrue(! this.game.hasEvent("boot"));
	}

	@Test
	public void testGainItem() {
		this.game.gainItem(new HuntItem("sword", "A shiny sword"));
		Assert.assertEquals(1, this.game.getEquipment().size());
		Assert.assertTrue(this.game.hasItem("sword"));
	}

	@Test
	public void testRecordEvent() {
		this.game.recordEvent(new HuntEvent("boot", "You wake up in a dark room."));
		Assert.assertEquals(1, this.game.getDiary().size());
		Assert.assertTrue(this.game.hasEvent("boot"));
	}

	// Running the game with triggers

	@Test
	public void testTriggerItemPlaceNoTriggers() {
		HuntPlace dragon = new HuntPlace("dragon", "Smaug the mighty dragon");
		HuntItem sword = new HuntItem("sword", "A shiny sword");
		List<HuntConsequence> consequences = this.game.listConsequences(sword, dragon);
		Assert.assertEquals(0, consequences.size());
	}
	
	@Test
	public void testTriggerItemPlaceWithTriggerMatch() {
		HuntPlace dragon = new HuntPlace("dragon", "Smaug the mighty dragon");
		HuntItem sword = new HuntItem("sword", "A shiny sword");
		HuntTrigger t = new HuntTrigger(new HuntConditionAction("sword", "dragon"));
		t.addConsequence(new HuntConsequenceEvent(new HuntEvent("killedDragon", "You killed Smaug!")));
		this.finder.preload(t);
		List<HuntConsequence> consequences = this.game.listConsequences(sword, dragon);
		Assert.assertEquals(1, consequences.size());
	}

	@Test
	public void testTriggerItemPlaceWithTriggerNoMatch() {
		HuntPlace dragon = new HuntPlace("dragon", "Smaug the mighty dragon");
		HuntItem book = new HuntItem("book", "A leather bound book");
		HuntTrigger t = new HuntTrigger(new HuntConditionAction("sword", "dragon"));
		t.addConsequence(new HuntConsequenceEvent(new HuntEvent("killedDragon", "You killed Smaug!")));
		this.finder.preload(t);
		List<HuntConsequence> consequences = this.game.listConsequences(book, dragon);
		Assert.assertEquals(0, consequences.size());
	}

}
