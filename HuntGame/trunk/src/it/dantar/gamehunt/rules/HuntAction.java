package it.dantar.gamehunt.rules;

public class HuntAction {

	String itemName;
	String placeName;
	
	public HuntAction(String itemName, String placeName) {
		this.itemName = itemName;
		this.placeName = placeName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}


	
}
