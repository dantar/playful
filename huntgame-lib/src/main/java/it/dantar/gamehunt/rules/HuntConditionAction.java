package it.dantar.gamehunt.rules;

public class HuntConditionAction extends HuntCondition {

	private String itemName;
	private String placeName;
	
	public HuntConditionAction(String itemName, String placeName) {
		this.itemName = itemName;
		this.placeName = placeName;
	}

	@Override
	public boolean checkOn(HuntMove move) {
		return move.getAction().getItemName().equals(this.itemName) && 
				move.getAction().getPlaceName().equals(this.placeName);
	}

}
