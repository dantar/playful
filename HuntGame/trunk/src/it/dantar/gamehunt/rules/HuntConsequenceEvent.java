package it.dantar.gamehunt.rules;

import it.dantar.gamehunt.HuntEvent;
import it.dantar.gamehunt.HuntGame;

public class HuntConsequenceEvent extends HuntConsequence {

	private HuntEvent huntEvent;
	private String huntEventName;

	public HuntConsequenceEvent(String name) {
		this.huntEventName = name;
	}

	public HuntConsequenceEvent(HuntEvent huntEvent) {
		this.huntEvent = huntEvent;
	}

	@Override
	public void runConsequence(HuntGame game) {
		if (this.huntEvent == null) {
			this.huntEvent = game.getFinder().<HuntEvent>find(this.huntEventName);
		}
		if (this.huntEvent == null) {
			game.postMessageToMonitors(String.format("Could not find event %s", this.huntEventName));
		} else {
			game.recordEvent(this.huntEvent);
		}
	}

	
}
