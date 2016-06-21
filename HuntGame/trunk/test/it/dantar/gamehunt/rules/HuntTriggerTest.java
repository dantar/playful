package it.dantar.gamehunt.rules;

import it.dantar.gamehunt.HuntEvent;
import it.dantar.gamehunt.HuntGame;
import it.dantar.gamehunt.HuntItem;
import it.dantar.gamehunt.HuntPlace;
import it.dantar.gamehunt.rules.HuntTrigger;
import it.dantar.gamehunt.finder.PreloadedFinder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HuntTriggerTest {

	HuntGame game;
	PreloadedFinder finder;
	
	@Before
	public void setup() {
		this.game = new HuntGame();
		this.finder = new PreloadedFinder();
		this.game.setFinder(this.finder);
		// some game stuff
		this.finder.preload(new HuntPlace("dragon", "Smaug the mighty dragon"));
		this.finder.preload(new HuntItem("sword", "A shiny sword"));
		this.finder.preload(new HuntEvent("win", "You killed Smaug!"));
		this.game.gainItem("sword");
	}
	
	@Test
	public void testGameReset() {
		this.finder.preload(new HuntItem("book", "A leather bound book"));
		HuntMove move = new HuntMove(new HuntAction("game", "reset"), this.game);
		HuntTrigger trigger = HuntTrigger.parse("reset=gain(book)");
		HuntCondition condition = trigger.getCondition();
		Assert.assertTrue(condition.checkOn(move));
	}

	@Test
	public void testHasItemMatch() {
		HuntMove move = new HuntMove(new HuntAction("sword", "dragon"), this.game);
		HuntTrigger trigger = HuntTrigger.parse("has(sword)=event(win)");
		HuntCondition condition = trigger.getCondition();
		Assert.assertTrue(condition.checkOn(move));
	}

	@Test
	public void testHasItemNoMatch() {
		HuntMove move = new HuntMove(new HuntAction("sword", "dragon"), this.game);
		HuntTrigger trigger = HuntTrigger.parse("has(book)=event(win)");
		HuntCondition condition = trigger.getCondition();
		Assert.assertTrue(! condition.checkOn(move));
	}

	@Test
	public void testUseItemMatch() {
		HuntMove move = new HuntMove(new HuntAction("sword", "dragon"), this.game);
		HuntTrigger trigger = HuntTrigger.parse("action(sword,dragon)=event(win)");
		HuntCondition condition = trigger.getCondition();
		Assert.assertTrue(condition.checkOn(move));
	}

	@Test
	public void testUseItemNoMatch() {
		HuntMove move = new HuntMove(new HuntAction("sword", "dragon"), this.game);
		HuntTrigger trigger = HuntTrigger.parse("action(other,dragon)=event(win)");
		HuntCondition condition = trigger.getCondition();
		Assert.assertTrue(!condition.checkOn(move));
	}

	@Test
	public void testRunConsequencesOneEvent() {
		HuntTrigger trigger = HuntTrigger.parse("action(sword,dragon)=event(win)");
		Assert.assertTrue(!game.hasEvent("win"));
		trigger.runConsequences(game);
		Assert.assertTrue(game.hasEvent("win"));
	}

	@Test
	public void testRunGainItemConsequences() {
		this.finder.preload(new HuntItem("book", "A leather bound book"));
		HuntTrigger trigger = HuntTrigger.parse("action(sword,dragon)=gain(book)");
		Assert.assertTrue(!game.hasItem("book"));
		trigger.runConsequences(game);
		Assert.assertTrue(game.hasItem("book"));
	}

	@Test
	public void testRunLoseItemConsequences() {
		HuntTrigger trigger = HuntTrigger.parse("action(sword,dragon)=lose(sword)");
		Assert.assertTrue(game.hasItem("sword"));
		trigger.runConsequences(game);
		Assert.assertTrue(!game.hasItem("sword"));
	}

	@Test
	public void testRunScoreConsequence() {
		this.game.setScore("points", 0);
		HuntTrigger trigger = HuntTrigger.parse("action(sword,dragon)=score(points,10)");
		Assert.assertEquals(0, game.getScore("points").intValue());
		trigger.runConsequences(game);
		Assert.assertEquals(10, game.getScore("points").intValue());
	}

	@Test
	public void testRunScoreNegConsequence() {
		this.game.setScore("points", 10);
		HuntTrigger trigger = HuntTrigger.parse("action(sword,dragon)=score(points,-3)");
		Assert.assertEquals(10, game.getScore("points").intValue());
		trigger.runConsequences(game);
		Assert.assertEquals(7, game.getScore("points").intValue());
	}

	@Test
	public void testRunConsequencesTwoEvents() {
		this.finder.preload(new HuntEvent("hurra", "Hurra!"));
		HuntTrigger trigger = HuntTrigger.parse("action(sword,dragon)=event(hurra),event(win)");
		trigger.runConsequences(game);
		Assert.assertTrue(game.hasEvent("hurra"));
		Assert.assertTrue(game.hasEvent("win"));
	}

	@Test
	public void testMovePlace() {
		this.finder.preload(new HuntPlace("wounded", "Smaug the mighty dragon is wounded!"));
		HuntTrigger trigger = HuntTrigger.parse("action(sword,dragon)=move(dragon,wounded)");
		Assert.assertEquals("dragon", this.game.resolvePlace("dragon").getName());
		trigger.runConsequences(game);
		Assert.assertEquals("wounded", this.game.resolvePlace("dragon").getName());
	}
	
}
