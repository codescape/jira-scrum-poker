package net.congstar.jira.plugins.planningpoker.action;

public class PokerCard {
	String name;
	public String getName() {
		return name;
	}

	String image;
	
	public String getImage() {
		return image;
	}

	public PokerCard (String name, String image) {
		this.name = name;
		this.image = image;
	}
}
