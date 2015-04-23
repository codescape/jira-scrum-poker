package net.congstar.jira.plugins.planningpoker.action;

public class PokerCard {
	String name;
	public String getName() {
		return name;
	}

	String image;
	String imageChosen;
	
	public String getImageChosen() {
		return imageChosen;
	}

	public String getImage() {
		return image;
	}

	public PokerCard (String name, String image, String imageChosen) {
		this.name = name;
		this.image = image;
	}
}
