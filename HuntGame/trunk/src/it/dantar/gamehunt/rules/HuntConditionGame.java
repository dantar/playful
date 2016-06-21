package it.dantar.gamehunt.rules;

public class HuntConditionGame extends HuntCondition {

	private String specialName;
	
	public HuntConditionGame(String specialName) {
		this.specialName = specialName;
	}

	@Override
	public boolean checkOn(HuntMove move) {
		return move.getAction().getItemName().equals("game") && 
				move.getAction().getPlaceName().equals(this.specialName);
	}

}
