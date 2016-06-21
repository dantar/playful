package it.dantar.gamehunt.finder;

import it.dantar.gamehunt.HuntItem;

import java.io.File;
import java.util.Properties;

public class HuntItemBuilder extends HuntObjectBuilder<HuntItem> {

	private static final String PROPERTIES_FILENAME = "item.properties";

	public static boolean match(File path) {
		return match(path, PROPERTIES_FILENAME);
	}

	@Override
	protected File getPropertiesFile() {
		return FileFinder.stepFile(source, PROPERTIES_FILENAME);
	}

	@Override
	protected HuntItem createItem(String name, Properties p) {
		return new HuntItem(name, p.getProperty("title", name));
	}

	public HuntItemBuilder(File source, String name) {
		super();
		this.source = source;
		Properties p = this.initObject(name);
		this.item.setIconPath(FileFinder.stepFile(source, p.getProperty("icon", "icon.png")).getAbsolutePath());
	}

}
