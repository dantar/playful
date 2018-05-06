package it.dantar.gamehunt.rules;

import java.util.regex.Pattern;

public class RegexConfigParser {
	
	Pattern pattern;
	
	public RegexConfigParser (String pattern) {
		this.pattern = Pattern.compile(pattern);
	}

	
}
