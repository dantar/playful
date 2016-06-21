package id.dantar.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTeamTest {

	ParserTeam team;
	Map<String, Integer> count;
	
	@Before
	public void beforeTest() {
		this.count = new HashMap<String, Integer>();
		this.team = new ParserTeam();
		this.team.addParser(new ColorParser(this.count, "Blue", "^[bB][lL][uU][eE] ?(.*)"));
		this.team.addParser(new ColorParser(this.count, "Red", "^[rR][eE][dD] ?(.*)"));
	}
	
	@Test
	public void testBlue1() {
		this.team.parse("BluE");
		Assert.assertNotNull(this.count.get("Blue"));
		Assert.assertEquals(1, this.count.get("Blue").intValue());
	}

	@Test
	public void testBlue3() {
		this.team.parse("BluE blue BLUE");
		Assert.assertNotNull(this.count.get("Blue"));
		Assert.assertEquals(3, this.count.get("Blue").intValue());
	}

	@Test
	public void testMixed() {
		this.team.parse("BluE rEd blue BLUE");
		Assert.assertNotNull(this.count.get("Blue"));
		Assert.assertEquals(3, this.count.get("Blue").intValue());
		Assert.assertNotNull(this.count.get("Red"));
		Assert.assertEquals(1, this.count.get("Red").intValue());
	}

	@Test
	public void testOrange() {
		this.team.parse("Orange");
		Assert.assertEquals(null, this.count.get("Blue"));
	}

}

class ColorParser extends PatternParser {

	private String name;
	private Map<String, Integer> count;

	public ColorParser(Map<String, Integer> count, String name, String regex) {
		super(regex);
		this.name = name;
		this.count = count;
	}

	@Override
	protected String validMatch(Matcher m) {
		if (this.count.containsKey(this.name))
			this.count.put(this.name, this.count.get(this.name) + 1);
		else 
			this.count.put(this.name, 1);
		return super.validMatch(m);
	}

	
}
