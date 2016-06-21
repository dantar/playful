package it.dantar.gamehunt;

import java.util.ArrayList;
import java.util.List;

public class HuntEventsLogger implements HuntEventsObserver {

	List<HuntEvent> huntEvents = new ArrayList<HuntEvent>();
	
	@Override
	public void observeHuntEvent(HuntEvent huntEvent) {
		this.huntEvents.add(huntEvent);
	}

	public List<HuntEvent> getHuntEvents() {
		return huntEvents;
	}

	public void setHuntEvents(List<HuntEvent> huntEvents) {
		this.huntEvents = huntEvents;
	}

	public void flushHuntEvents() {
		this.huntEvents.clear();
	}
	
}
