package id.dantar.parser;

import java.util.ArrayList;
import java.util.List;

public class ParserTeam {

	List<PatternParser> parsers = new ArrayList<PatternParser>();
	
	public void addParser(PatternParser parser) {
		this.parsers.add(parser);
	}

	public void parse(String text) {
		String cursor = text;
		while (! cursor.isEmpty()) {
			String trail = null;
			for (PatternParser parser: this.parsers) {
				trail = parser.parse(cursor);
				if (trail != null) break;
			}
			if (trail == null) break;
			cursor = trail;
		}
	}

}
