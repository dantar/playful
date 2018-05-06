package it.dantar.gamehunt.rules;

import it.dantar.parser.ParserTeam;
import it.dantar.parser.PatternParser;
import it.dantar.gamehunt.HuntGame;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class HuntTrigger {

	private HuntCondition condition;
	private List<HuntConsequence> consequences;
	private String consequenceConfig = "";
	
	public static HuntTrigger parse(String trigger) {
		return new HuntTrigger(trigger);
	}

	public HuntTrigger(HuntCondition condition) {
		this.condition = condition;
	}

	public void setCondition(HuntCondition condition) {
		this.condition = condition;
	}

	private void setCondition(String config) {
		ParserTeam team = new ParserTeam();
		team.addParser(new PatternParserAction(this));
		team.addParser(new PatternParserHas(this));
		team.addParser(new PatternParserGame(this));
		this.condition = new HuntConditionFalse();
		team.parse(config);
	}
	
	public HuntTrigger(String config) {
		String[] parts = config.split("=");
		this.setCondition(config);
		this.consequenceConfig = parts[1];
	}

	public HuntTrigger addConsequence(HuntConsequence huntConsequence) {
		if (this.consequences == null) {
			this.consequences = new ArrayList<HuntConsequence>();
		}
		this.consequences.add(huntConsequence);
		return this;
	}

	public HuntCondition getCondition() {
		return condition;
	}

	public void runConsequences(HuntGame game) {
		for (HuntConsequence c: this.getConsequences()) {
			c.runConsequence(game);
		}
	}

	public List<HuntConsequence> getConsequences() {
		if (this.consequences == null) {
			this.consequences = new ArrayList<HuntConsequence>();
			ParserTeam team = new ParserTeam();
			team.addParser(new PatternParserEvent(this.consequences));
			team.addParser(new PatternParserGainItem(this.consequences));
			team.addParser(new PatternParserLoseItem(this.consequences));
			team.addParser(new PatternParserScore(this.consequences));
			team.addParser(new PatternParserMovePlace(this.consequences));
			team.parse(this.consequenceConfig);
		}
		return this.consequences;
	}

}

class PatternParserAction extends PatternParser {

	HuntTrigger huntTrigger;
	
	public PatternParserAction(HuntTrigger huntTrigger) {
		super("^action\\((\\w+),(\\w+)\\)(.*)");
		this.huntTrigger = huntTrigger;
	}

	@Override
	protected String validMatch(Matcher m) {
		this.huntTrigger.setCondition(new HuntConditionAction(m.group(1), m.group(2)));
		return m.group(3);
	}
	
}

class PatternParserGame extends PatternParser {

	HuntTrigger huntTrigger;
	
	public PatternParserGame(HuntTrigger huntTrigger) {
		super("^(reset|end)(.*)");
		this.huntTrigger = huntTrigger;
	}

	@Override
	protected String validMatch(Matcher m) {
		this.huntTrigger.setCondition(new HuntConditionGame(m.group(1)));
		return m.group(2);
	}
	
}

class PatternParserHas extends PatternParser {

	HuntTrigger huntTrigger;
	
	public PatternParserHas(HuntTrigger huntTrigger) {
		super("^has\\((\\w+)\\)(.*)");
		this.huntTrigger = huntTrigger;
	}

	@Override
	protected String validMatch(Matcher m) {
		this.huntTrigger.setCondition(new HuntConditionHasItem(m.group(1)));
		return m.group(2);
	}
	
}

class PatternParserEvent extends PatternParser {

	List<HuntConsequence> consequences;
	
	public PatternParserEvent(List<HuntConsequence> consequences) {
		super("^event\\((\\w+)\\),?(.*)");
		this.consequences = consequences;
	}

	@Override
	protected String validMatch(Matcher m) {
		this.consequences.add(new HuntConsequenceEvent(m.group(1)));
		return m.group(2);
	}
	
}

class PatternParserGainItem extends PatternParser {

	List<HuntConsequence> consequences;
	
	public PatternParserGainItem(List<HuntConsequence> consequences) {
		super("^gain\\((\\w+)\\),?(.*)");
		this.consequences = consequences;
	}

	@Override
	protected String validMatch(Matcher m) {
		this.consequences.add(new HuntConsequenceGainItem(m.group(1)));
		return m.group(2);
	}
	
}

class PatternParserLoseItem extends PatternParser {

	List<HuntConsequence> consequences;
	
	public PatternParserLoseItem(List<HuntConsequence> consequences) {
		super("^lose\\((\\w+)\\),?(.*)");
		this.consequences = consequences;
	}

	@Override
	protected String validMatch(Matcher m) {
		this.consequences.add(new HuntConsequenceLoseItem(m.group(1)));
		return m.group(2);
	}
	
}

class PatternParserScore extends PatternParser {

	List<HuntConsequence> consequences;
	
	public PatternParserScore(List<HuntConsequence> consequences) {
		super("^score\\((\\w+)\\,(-?\\d+)\\),?(.*)");
		this.consequences = consequences;
	}

	@Override
	protected String validMatch(Matcher m) {
		this.consequences.add(new HuntConsequenceScore(m.group(1), Integer.parseInt(m.group(2))));
		return m.group(3);
	}
	
}

class PatternParserMovePlace extends PatternParser {
	
	List<HuntConsequence> consequences;
	
	public PatternParserMovePlace(List<HuntConsequence> consequences) {
		super("^move\\((\\w+),(\\w+)\\),?(.*)");
		this.consequences = consequences;
	}

	@Override
	protected String validMatch(Matcher m) {
		this.consequences.add(new HuntConsequenceMovePlace(m.group(1), m.group(2)));
		return m.group(3);
	}

}