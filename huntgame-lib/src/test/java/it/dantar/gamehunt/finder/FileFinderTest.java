package it.dantar.gamehunt.finder;

import it.dantar.gamehunt.HuntEvent;
import it.dantar.gamehunt.HuntItem;
import it.dantar.gamehunt.HuntPlace;
import it.dantar.gamehunt.rules.HuntTrigger;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FileFinderTest {

	private String hunt_dir;

	private FileFinder finder;
	
	@Before
	public void testSetup() {
		this.hunt_dir = this.getClass().getResource("data").getPath();
		File f = new File(hunt_dir + "/gameA");
		finder = new FileFinder(f);
	}
	
	@Test
	public void testSword() {
		HuntItem sword = finder.<HuntItem>find("sword");
		Assert.assertEquals("sword", sword.getName());
		Assert.assertEquals("A shiny sword", sword.getTitle());
		Assert.assertEquals(hunt_dir+"/gameA/sword/sword-icon.png", sword.getIconPath());
	}

	@Test
	public void testTorch() {
		HuntItem torch = finder.<HuntItem>find("torch");
		Assert.assertEquals("A feeble torch", torch.getTitle());
		Assert.assertEquals("torch", torch.getName());
		Assert.assertEquals(hunt_dir+"/gameA/torch/icon.png", torch.getIconPath());
	}

	@Test
	public void testDragon() {
		HuntPlace dragon = finder.<HuntPlace>find("dragon");
		Assert.assertEquals("dragon", dragon.getName());
		Assert.assertEquals("A mighty dragon", dragon.getTitle());
	}

	@Test
	public void testOrcs() {
		HuntPlace orcs = finder.<HuntPlace>find("orcs");
		Assert.assertEquals("orcs", orcs.getName());
		Assert.assertEquals("An orc warband", orcs.getTitle());
	}
	
	@Test
	public void testWin() {
		HuntEvent win = finder.<HuntEvent>find("win");
		Assert.assertEquals("win", win.getName());
		Assert.assertEquals("You win!", win.getTitle());
		Assert.assertEquals(hunt_dir+"/gameA/events/default.png", win.getIconPath());
	}

	@Test
	public void testRules() {
		List<HuntTrigger> triggers = this.finder.getTriggers();
		Assert.assertEquals(2, triggers.size());
	}

}
