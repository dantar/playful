package it.dantar.gamehunt.rules;

import it.dantar.gamehunt.HuntGame;

public class HuntMove {

	HuntAction action; 
	HuntGame game;
	
	public HuntMove(HuntAction action, HuntGame game) {
		this.action = action;
		this.game = game;
	}

	public HuntAction getAction() {
		return action;
	}

	public void setAction(HuntAction action) {
		this.action = action;
	}

	public HuntGame getGame() {
		return game;
	}

	public void setGame(HuntGame game) {
		this.game = game;
	}

	public void runTriggers() {
		for (HuntConsequence c : this.game.listConsequences(this.getAction().getItemName(), this.getAction().getPlaceName())) {
			c.runConsequence(this.game);
		}
	}
	
}
