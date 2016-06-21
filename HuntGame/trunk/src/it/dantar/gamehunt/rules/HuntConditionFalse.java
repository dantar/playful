package it.dantar.gamehunt.rules;

public class HuntConditionFalse extends HuntCondition {

	@Override
	public boolean checkOn(HuntMove move) {
		return false;
	}
	
}
