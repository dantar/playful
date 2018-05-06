package it.dantar.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternParser {

	Pattern pattern;
	
	public PatternParser(String regex) {
		this.pattern = Pattern.compile(regex);
	}

	public String parse(String cursor) {
		Matcher m = this.pattern.matcher(cursor);
		if (m.find()) return this.validMatch(m);
		return null;
	}

	protected String validMatch(Matcher m) {
		return m.group(1);
	}
	
}
