package it.dantar.gamehunt;

public class HuntItem extends HuntObject {

	String iconPath;
	
	public HuntItem(String name, String title) {
		super(name, title);
	}

	public String getIconPath() {
		return this.iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	
}
