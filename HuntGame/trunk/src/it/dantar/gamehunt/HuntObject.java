package it.dantar.gamehunt;

public class HuntObject {

	protected String name;
	protected String title;

	public HuntObject(String name) {
		this.name = name;
		this.title = name;
	}

	public HuntObject(String name, String title) {
		this.name = name;
		this.title = title;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}