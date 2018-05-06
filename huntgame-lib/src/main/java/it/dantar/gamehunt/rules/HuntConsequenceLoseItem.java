package it.dantar.gamehunt.rules;

import it.dantar.gamehunt.HuntGame;

public class HuntConsequenceLoseItem extends HuntConsequence{

	String itemName;
	
	public HuntConsequenceLoseItem(String itemName) {
		super();
		this.itemName = itemName;
	}

	@Override
	public void runConsequence(HuntGame game) {
		game.loseItem(this.itemName);
	}
	
}
