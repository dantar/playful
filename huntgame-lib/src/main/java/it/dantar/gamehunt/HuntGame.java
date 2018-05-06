package it.dantar.gamehunt;

import it.dantar.gamehunt.finder.HuntFinder;
import it.dantar.gamehunt.rules.HuntAction;
import it.dantar.gamehunt.rules.HuntConsequence;
import it.dantar.gamehunt.rules.HuntMove;
import it.dantar.gamehunt.rules.HuntTrigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuntGame {

	HuntStat life = new HuntStat("life", "Vita", 10);
	HuntStat points = new HuntStat("points", "Punti", 0);
	List<HuntItem> equipment = new ArrayList<HuntItem>();
	List<HuntEvent> diary = new ArrayList<HuntEvent>();
	private HuntFinder finder;
	private List<HuntEventsObserver> observers = new ArrayList<HuntEventsObserver>();
	private Map<String, Integer> score = new HashMap<String, Integer>();
	private List<HuntGameMonitor> monitors = new ArrayList<HuntGameMonitor>();
	private Map<String, String> displaced = new HashMap<String, String>();
	
	public void gainItem(HuntItem huntItem) {
		this.equipment.add(huntItem);
	}
	public void gainItem(String name) {
		if (! this.hasItem(name)) {
			HuntItem item = this.finder.<HuntItem>find(name);
			if (item==null) {
				this.postMessageToMonitors(String.format("Gain item refs invalid item %s", name));
			}
			this.gainItem(item);
		}
	}
	public void loseItem(String itemName) {
		this.equipment.remove(this.finder.find(itemName));
	}
	
	public void recordEvent(HuntEvent huntEvent) {
		this.diary.add(huntEvent);
		for (HuntEventsObserver observer : this.observers) {
			observer.observeHuntEvent(huntEvent);
		}
	}
	public void recordEvent(String name) {
		this.recordEvent(this.finder.<HuntEvent>find(name));
	}
	
	public HuntStat getLife() {
		return life;
	}
	public void setLife(HuntStat life) {
		this.life = life;
	}
	public List<HuntItem> getEquipment() {
		return equipment;
	}
	public void setEquipment(List<HuntItem> equipment) {
		this.equipment = equipment;
	}
	public List<HuntEvent> getDiary() {
		return diary;
	}
	public void setDiary(List<HuntEvent> diary) {
		this.diary = diary;
	}
	public HuntFinder getFinder() {
		return finder;
	}
	public void setFinder(HuntFinder finder) {
		this.finder = finder;
	}
	
	public boolean hasItem(String string) {
		for (HuntItem item : this.equipment) {
			if (string.equals(item.getName()))
				return true;
		}
		return false;
	}
	
	public boolean hasEvent(String string) {
		for (HuntEvent event : this.diary) {
			if (event==null) {
				this.postMessageToMonitors("There is a null event!");
				continue;
			}
			if (string.equals(event.getName()))
				return true;
		}
		return false;
	}
	
	public List<HuntConsequence> listConsequences(HuntItem item, HuntPlace place) {
		List<HuntConsequence> result = new ArrayList<HuntConsequence>();
		if (item == null || place == null) return result;
		HuntMove move = new HuntMove(new HuntAction(item.getName(), place.getName()), this);
		addMoveConsequences(result, move);
		return result;
	}
	
	private void addMoveConsequences(List<HuntConsequence> consequences,
			HuntMove move) {
		for (HuntTrigger trigger : this.finder.getTriggers()) {
			if (trigger.getCondition().checkOn(move)) {
				for (HuntConsequence c : trigger.getConsequences()) {
					consequences.add(c);
				}
			}
		}
	}
	
	public List<HuntConsequence> listConsequences(String itemName, String placeName) {
		HuntItem item = this.finder.<HuntItem>find(itemName);
		HuntPlace place = this.resolvePlace(placeName);
		if (item == null) {
			this.postMessageToMonitors(String.format("Item %s cannot be found", itemName));
		}
		if (place == null) {
			this.postMessageToMonitors(String.format("Place %s cannot be found", placeName));
		}
		return this.listConsequences(item, place);
	}

	public void registerObserver(HuntEventsObserver observer) {
		this.observers.add(observer);
	}
	
	public void setScore(String scoreName, Integer i) {
		this.score.put(scoreName, i);
	}
	
	public Integer getScore(String scoreName) {
		return this.score.get(scoreName);
	}
	
	public void runTriggers(HuntMove move) {
		String itemName = move.getAction().getItemName();
		if (this.hasItem(itemName) || "game".equals(itemName)) {
			List<HuntConsequence> consequences = this.listConsequences(itemName, move.getAction().getPlaceName());
			runConsequences(consequences);
		} else {
			String message = String.format("You don't have the following item: %s", itemName);
			this.postMessageToMonitors(message);
		}
	}
	public void postMessageToMonitors(String message) {
		for (HuntGameMonitor monitor: this.monitors) {
			monitor.warning(new RuntimeException(message));
		}
	}
	private void runConsequences(List<HuntConsequence> consequences) {
		for(HuntConsequence c: consequences) {
			c.runConsequence(this);
		}
	}
	
	public void runTriggers(String itemName, String placeName) {
		this.runTriggers(new HuntMove(new HuntAction(itemName, placeName), this));	
	}
	
	public void registerMonitor(HuntGameMonitor monitor) {
		this.monitors.add(monitor);
	}
	
	public void resetGame() {
		List<HuntConsequence> consequences = new ArrayList<HuntConsequence>();
		HuntMove move = new HuntMove(new HuntAction("game", "reset"), this);
		this.addMoveConsequences(consequences, move);
		this.runConsequences(consequences);
	}
	
	public HuntPlace resolvePlace(String placeName) {
		String actualName = this.displaced.get(placeName);
		if (actualName == null) actualName = placeName;
		HuntPlace result = this.finder.<HuntPlace>find(actualName);
		if (result==null) {
			this.postMessageToMonitors(String.format("Cannot find place %s", actualName));
		}
		return result;
	}
	
	public void movePlace(String fromName, String toName) {
		this.displaced.put(fromName, toName);
	}
	
}
