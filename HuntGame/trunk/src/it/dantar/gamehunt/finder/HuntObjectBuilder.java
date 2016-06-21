package it.dantar.gamehunt.finder;

import it.dantar.gamehunt.HuntObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public abstract class HuntObjectBuilder<T extends HuntObject> {

	public static boolean match(File path, String filename) {
		File f = new File(path + "/" + filename);
		return f.exists() && f.isFile();
	}

	abstract protected File getPropertiesFile();
	
	abstract protected T createItem(String name, Properties p);

	public static boolean match(File path) {
		return match(path, null);
	}

	protected File source;
	protected T item;

	public HuntObjectBuilder() {
		super();
	}

	public T getBuilt() {
		return item;
	}

	protected Properties initObject(String name) {
		Properties p = new Properties();
		if (source.exists()) {
			try {
				p.load(new FileInputStream(getPropertiesFile()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			item = this.createItem(name, p);
		}
		return p;
	}


}