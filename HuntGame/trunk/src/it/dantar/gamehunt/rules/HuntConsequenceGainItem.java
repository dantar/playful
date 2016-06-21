package it.dantar.gamehunt.rules;

import it.dantar.gamehunt.HuntGame;

public class HuntConsequenceGainItem extends HuntConsequence{

	String itemName;
	
	public HuntConsequenceGainItem(String itemName) {
		super();
		this.itemName = itemName;
	}

	@Override
	public void runConsequence(HuntGame game) {
		game.gainItem(this.itemName);
	}
	
}
