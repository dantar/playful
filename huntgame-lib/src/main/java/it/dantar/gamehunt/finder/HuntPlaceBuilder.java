package it.dantar.gamehunt.finder;

import it.dantar.gamehunt.HuntPlace;

import java.io.File;
import java.util.Properties;

public class HuntPlaceBuilder extends HuntObjectBuilder<HuntPlace> {

	private static final String PROPERTIES_FILENAME = "place.properties";

	public static boolean match(File path) {
		return path.exists() && path.isFile();
	}

	@Override
	protected File getPropertiesFile() {
		return this.source;
	}

	@Override
	protected HuntPlace createItem(String name, Properties p) {
		return new HuntPlace(name, p.getProperty("title", name));
	}

	public HuntPlaceBuilder(File source, String name) {
		super();
		this.source = source;
		Properties p = this.initObject(name);
	}

}
