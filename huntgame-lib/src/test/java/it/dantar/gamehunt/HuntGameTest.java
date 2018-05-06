package it.dantar.gamehunt;

import it.dantar.gamehunt.finder.PreloadedFinder;
import it.dantar.gamehunt.rules.HuntAction;
import it.dantar.gamehunt.rules.HuntConditionAction;
import it.dantar.gamehunt.rules.HuntConsequence;
import it.dantar.gamehunt.rules.HuntConsequenceEvent;
import it.dantar.gamehunt.rules.HuntConsequenceGainItem;
import it.dantar.gamehunt.rules.HuntMove;
import it.dantar.gamehunt.rules.HuntTrigger;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HuntGameTest {

	HuntGame game;
	PreloadedFinder finder;
	
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
		this.finder.preload(new HuntItem("sword", "A shiny sword"));
		Assert.assertEquals(0, this.game.getEquipment().size());
		this.game.gainItem("sword");
		Assert.assertEquals(1, this.game.getEquipment().size());
		Assert.assertTrue(this.game.hasItem("sword"));
		this.game.gainItem("sword");
		Assert.assertEquals(1, this.game.getEquipment().size());
	}

	@Test
	public void testLoseItem() {
		this.finder.preload(new HuntItem("sword", "A shiny sword"));
		Assert.assertEquals(0, this.game.getEquipment().size());
		this.game.loseItem("sword");
		Assert.assertEquals(0, this.game.getEquipment().size());
		this.game.gainItem("sword");
		Assert.assertEquals(1, this.game.getEquipment().size());
		this.game.loseItem("sword");
		Assert.assertEquals(0, this.game.getEquipment().size());
	}

	@Test
	public void testRecordEvent() {
		this.finder.preload(new HuntEvent("boot", "You wake up in a dark room."));
		this.game.recordEvent("boot");
		Assert.assertEquals(1, this.game.getDiary().size());
		Assert.assertTrue(this.game.hasEvent("boot"));
	}

	// Running the game with triggers

	@Test
	public void testTriggerItemPlaceNoTriggers() {
		this.finder.preload(new HuntPlace("dragon", "Smaug the mighty dragon"));
		this.finder.preload(new HuntItem("sword", "A shiny sword"));
		List<HuntConsequence> consequences = this.game.listConsequences("sword", "dragon");
		Assert.assertEquals(0, consequences.size());
	}
	
	@Test
	public void testTriggerItemPlaceWithInvalidPlace() {
		this.finder.preload(new HuntPlace("dragon", "Smaug the mighty dragon"));
		this.finder.preload(new HuntItem("sword", "A shiny sword"));
		this.finder.preload(new HuntEvent("win", "You killed Smaug!"));
		HuntTrigger t = new HuntTrigger(new HuntConditionAction("sword", "dragon"));
		t.addConsequence(new HuntConsequenceEvent("win"));
		this.finder.preload(t);
		List<HuntConsequence> consequences = this.game.listConsequences("sword", "somethingelse");
		Assert.assertEquals(0, consequences.size());
	}

	@Test
	public void testResetGameAction() {
		this.finder.preload(new HuntItem("sword", "A shiny sword"));
		this.finder.preload(HuntTrigger.parse("reset=gain(sword)"));
		this.game.resetGame();
		Assert.assertTrue(this.game.hasItem("sword"));
	}

	@Test
	public void testTriggerItemPlaceWithInvalidItem() {
		final List<Exception> warnings = new ArrayList<Exception>();
		HuntGameMonitor monitor = new HuntGameMonitor() {
			public void warning(Exception e) {
				warnings.add(e);
			}
			public void exception(Exception e) {
				// TODO Auto-generated method stub
			}
		};
		this.game.registerMonitor(monitor);
		this.game.runTriggers("something", "somethingelse");
		Assert.assertEquals(1, warnings.size());
	}

	@Test
	public void testTriggerItemPlaceWithTriggerMatch() {
		this.finder.preload(new HuntPlace("dragon", "Smaug the mighty dragon"));
		this.finder.preload(new HuntItem("sword", "A shiny sword"));
		this.finder.preload(new HuntEvent("win", "You killed Smaug!"));
		this.finder.preload(new HuntTrigger("action(sword,dragon)=event(win)"));
		List<HuntConsequence> consequences = this.game.listConsequences("sword", "dragon");
		Assert.assertEquals(1, consequences.size());
	}

	@Test
	public void testRunTriggersOnGame() {
		this.finder.preload(new HuntPlace("dragon", "Smaug the mighty dragon"));
		this.finder.preload(new HuntItem("sword", "A shiny sword"));
		this.finder.preload(new HuntEvent("win", "You killed Smaug!"));
		this.finder.preload(new HuntTrigger("action(sword,dragon)=event(win)"));
		Assert.assertFalse(this.game.hasEvent("win"));
		this.game.gainItem("sword");
		this.game.runTriggers(new HuntMove(new HuntAction("sword", "dragon"), this.game));
		Assert.assertTrue(this.game.hasEvent("win"));
	}

	@Test
	public void testRunTriggersOnMove() {
		this.finder.preload(new HuntPlace("dragon", "Smaug the mighty dragon"));
		this.finder.preload(new HuntItem("sword", "A shiny sword"));
		this.finder.preload(new HuntEvent("win", "You killed Smaug!"));
		this.finder.preload(new HuntTrigger("action(sword,dragon)=event(win)"));
		Assert.assertFalse(this.game.hasEvent("win"));
		new HuntMove(new HuntAction("sword", "dragon"), this.game).runTriggers();
		Assert.assertTrue(this.game.hasEvent("win"));
	}

	@Test
	public void testTriggerItemPlaceWithTriggerNoMatch() {
		this.finder.preload(new HuntPlace("dragon", "Smaug the mighty dragon"));
		this.finder.preload(new HuntItem("sword", "A shiny sword"));
		this.finder.preload(new HuntItem("book", "A leather bound book"));
		this.finder.preload(new HuntEvent("win", "You killed Smaug!"));
		HuntTrigger t = new HuntTrigger(new HuntConditionAction("sword", "dragon"));
		t.addConsequence(new HuntConsequenceEvent("win"));
		this.finder.preload(t);
		List<HuntConsequence> consequences = this.game.listConsequences("book", "dragon");
		Assert.assertEquals(0, consequences.size());
	}

	@Test
	public void testTriggerItemPlaceWithTextTrigger() {
		this.finder.preload(new HuntPlace("dragon", "Smaug the mighty dragon"));
		this.finder.preload(new HuntItem("sword", "A shiny sword"));
		this.finder.preload(new HuntItem("book", "A leather bound book"));
		this.finder.preload(new HuntEvent("win", "You killed Smaug!"));
		HuntTrigger t = new HuntTrigger(new HuntConditionAction("sword", "dragon"));
		t.addConsequence(new HuntConsequenceEvent("win"));
		this.finder.preload(t);
		List<HuntConsequence> consequences = this.game.listConsequences("book", "dragon");
		Assert.assertEquals(0, consequences.size());
	}

	// Consequences
	
	@Test
	public void testRunHuntConsequenceEvent() {
		this.finder.preload(new HuntEvent("first", "First consequence event!"));
		Assert.assertEquals(0, this.game.getDiary().size());
		(new HuntConsequenceEvent("first")).runConsequence(this.game);
		Assert.assertEquals(1, this.game.getDiary().size());
	}

	@Test
	public void testRunHuntConsequenceGainItem() {
		this.finder.preload(new HuntItem("book", "A leather bound book"));
		Assert.assertEquals(0, this.game.getEquipment().size());
		(new HuntConsequenceGainItem("book")).runConsequence(this.game);
		Assert.assertEquals(1, this.game.getEquipment().size());
	}

	@Test
	public void testConsequencesObserver() {
		this.finder.preload(new HuntItem("book", "A leather bound book"));
		this.finder.preload(new HuntEvent("first", "First consequence event!"));
		this.finder.preload(new HuntEvent("second", "First consequence event!"));
		HuntEventsLogger observer = new HuntEventsLogger();
		this.game.registerObserver(observer);
		(new HuntConsequenceEvent("first")).runConsequence(this.game);
		(new HuntConsequenceEvent("second")).runConsequence(this.game);
		(new HuntConsequenceGainItem("book")).runConsequence(this.game);
		Assert.assertEquals(2, observer.getHuntEvents().size());
		observer.flushHuntEvents();
		Assert.assertEquals(0, observer.getHuntEvents().size());
	}

}
