package it.dantar.gamehunt;

public class HuntEvent extends HuntObject {

	private String iconPath;

	public HuntEvent(String name, String title) {
		super(name, title);
	}

	public String getIconPath() {
		return this.iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

}
