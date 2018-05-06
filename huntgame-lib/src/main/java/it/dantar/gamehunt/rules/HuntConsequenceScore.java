package it.dantar.gamehunt.rules;

import it.dantar.gamehunt.HuntGame;

public class HuntConsequenceScore extends HuntConsequence {

	String scoreName;
	Integer amount;
	
	public HuntConsequenceScore(String scoreName, Integer amount) {
		super();
		this.scoreName = scoreName;
		this.amount = amount;
	}

	@Override
	public void runConsequence(HuntGame game) {
		game.setScore(scoreName, game.getScore(scoreName) + this.amount);
	}

}
