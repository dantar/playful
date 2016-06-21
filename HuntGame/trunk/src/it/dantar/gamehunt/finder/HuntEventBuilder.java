package it.dantar.gamehunt.finder;

import it.dantar.gamehunt.HuntEvent;

import java.io.File;
import java.util.Properties;

public class HuntEventBuilder extends HuntObjectBuilder<HuntEvent> {

	public static boolean match(File path) {
		return path.exists() && path.isFile();
	}

	@Override
	protected File getPropertiesFile() {
		return this.source;
	}

	@Override
	protected HuntEvent createItem(String name, Properties p) {
		return new HuntEvent(name, p.getProperty("title", name));
	}

	public HuntEventBuilder(File source, String name) {
		super();
		this.source = FileFinder.stepFile(source, String.format("%s.properties", name));
		Properties p = this.initObject(name);
		File iconFile = FileFinder.stepFile(source, p.getProperty("icon", name + ".png"));
		if (! iconFile.exists()) {
			iconFile = FileFinder.stepFile(source, "default.png");
		}
		this.item.setIconPath(iconFile.getAbsolutePath());
	}

}
