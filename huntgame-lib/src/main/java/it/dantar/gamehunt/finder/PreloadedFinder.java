package it.dantar.gamehunt.finder;

import it.dantar.gamehunt.HuntObject;
import it.dantar.gamehunt.rules.HuntConsequence;
import it.dantar.gamehunt.rules.HuntTrigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreloadedFinder implements HuntFinder {

	protected Map<String, HuntObject> preloads = new HashMap<String, HuntObject>();
	protected List<HuntTrigger> triggers = new ArrayList<HuntTrigger>();
	protected Map<String, HuntConsequence> consequences = new HashMap<String, HuntConsequence>();
	
	@SuppressWarnings("unchecked")
	public <T extends HuntObject> T find(String name) {
		return (T) this.preloads.get(name);
	}
	
	public void preload(HuntObject huntObject) {
		this.preloads.put(huntObject.getName(), huntObject);
	}

	public void preload(HuntTrigger trigger) {
		this.triggers.add(trigger);
	}

	public List<HuntTrigger> getTriggers() {
		return this.triggers;
	}

}
