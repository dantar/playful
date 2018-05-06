package it.dantar.gamehunt.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class RegexTests {

	public static String RULE = "(item\\((\\w+)\\)|place\\((\\w+)\\))(,(item\\((\\w+)\\)|place\\((\\w+)\\)))*=event\\((\\w+)\\)";
	
	@Test
	public void testGroups() {
		Pattern p = Pattern.compile(RULE);
		Matcher m = p.matcher("item(sword)=event(win)");
		if (m.find()) {
			Assert.assertEquals(8, m.groupCount());
			Assert.assertEquals("sword", m.group(2));
		}
	}

	@Test
	public void testRule01() {
		Pattern p = Pattern.compile(RULE);
		Matcher m = p.matcher("item(sword),place(dragon)=event(win)");
		if (m.find()) {
			Assert.assertEquals(8, m.groupCount());
			Assert.assertEquals("sword", m.group(2));
			for (int index=1; index <= m.groupCount(); index++) {
//				System.out.println(m.group(index));
			}
			Assert.assertEquals("dragon", m.group(7));
		}
	}

	@Test
	public void testRule02() {
		Pattern p = Pattern.compile(RULE);
		Matcher m = p.matcher("item(sword),place(dragon),item(torch),item(gulp)=event(win)");
		if (m.find()) {
			Assert.assertEquals(8, m.groupCount());
			Assert.assertEquals("sword", m.group(2));
			for (int index=1; index <= m.groupCount(); index++) {
//				System.out.println(m.group(index));
			}
			Assert.assertEquals("dragon", m.group(7));
		}
	}

	@Test
	public void testSimple() {
		Pattern p = Pattern.compile("item-(\\w+)");
		Matcher m = p.matcher("item-sword");
		while (m.find()) {
			Assert.assertEquals(1, m.groupCount());
			Assert.assertEquals("sword", m.group(1));
		}
	}

}
