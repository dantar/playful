package it.dantar.gamehunt.finder;

import it.dantar.gamehunt.HuntObject;
import it.dantar.gamehunt.rules.HuntTrigger;

import java.util.List;

public interface HuntFinder {

	public <T extends HuntObject> T find(String name);

	public List<HuntTrigger> getTriggers();

}
