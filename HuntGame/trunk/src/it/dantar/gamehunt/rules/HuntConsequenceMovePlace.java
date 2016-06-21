package it.dantar.gamehunt.rules;

import it.dantar.gamehunt.HuntGame;

public class HuntConsequenceMovePlace extends HuntConsequence {

	private String fromName;
	private String toName;

	public HuntConsequenceMovePlace(String fromName, String toName) {
		this.fromName = fromName;
		this.toName = toName;
	}

	@Override
	public void runConsequence(HuntGame game) {
		game.movePlace(this.fromName, this.toName);
	}

}
