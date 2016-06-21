package it.dantar.gamehunt.rules;

public class HuntConditionHasItem extends HuntCondition {

	private String itemName;

	public HuntConditionHasItem(String itemName) {
		super();
		this.itemName = itemName;
	}

	@Override
	public boolean checkOn(HuntMove move) {
		return move.getGame().hasItem(this.itemName);
	}
	
}
