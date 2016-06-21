package it.dantar.gamehunt.finder;

import it.dantar.gamehunt.HuntObject;
import it.dantar.gamehunt.rules.HuntTrigger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileFinder implements HuntFinder {

	private File file;

	public FileFinder(File file) {
		this.file = file;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends HuntObject> T find(String name) {
		File source = stepFile(this.file, name);
		if (HuntItemBuilder.match(source)) {
			HuntItemBuilder builder = new HuntItemBuilder(source, name);
			return (T) builder.getBuilt();
		}
		source = stepFile(this.file, String.format("%s/place.properties", name));
		if (HuntPlaceBuilder.match(source)) {
			HuntPlaceBuilder builder = new HuntPlaceBuilder(source, name);
			return (T) builder.getBuilt();
		}
		source = stepFile(this.file, String.format("events/%s.properties", name));
		if (HuntEventBuilder.match(source)) {
			HuntEventBuilder builder = new HuntEventBuilder(stepFile(this.file, "events"), name);
			return (T) builder.getBuilt();
		}
		source = stepFile(this.file, String.format("places/%s.properties", name));
		if (HuntPlaceBuilder.match(source)) {
			HuntPlaceBuilder builder = new HuntPlaceBuilder(source, name);
			return (T) builder.getBuilt();
		}
		return null;
	}

	public static File stepFile(File source, String name) {
		return new File(String.format("%s/%s", source.getAbsolutePath(), name));
	}

	@Override
	public List<HuntTrigger> getTriggers() {
		File triggersFile = stepFile(this.file, "triggers.rules");
		List<HuntTrigger> result = new ArrayList<HuntTrigger>();
		if (triggersFile.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(triggersFile)));
				String line;
				while ((line = reader.readLine()) != null) {
					result.add(HuntTrigger.parse(line));
				}
			} catch (FileNotFoundException e) {
				throw new RuntimeException("Should not happen: FileNotFoundException of existing file", e);
			} catch (IOException e) {
				throw new RuntimeException("Error reading rules file", e);
			}
		}
		return result;
	}

}
