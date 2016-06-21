package it.dantar.gamehunt;

public class HuntStat extends HuntObject {

	Integer score;

	public HuntStat(String name, String title, Integer score) {
		super(name, title);
		this.score = score;
	}

	public Integer getScore() {
		return this.score;
	}

	public HuntStat setScore(Integer score) {
		this.score = score;
		return this;
	}
	
	public HuntStat gain(Integer x) {
		this.score = this.score + x;
		return this;
	}
	
	public HuntStat lose(Integer x) {
		this.score = Math.max(this.score - x, 0);
		return this;
	}
	
}
