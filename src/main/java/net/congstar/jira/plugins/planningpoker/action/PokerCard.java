package net.congstar.jira.plugins.planningpoker.action;

public class PokerCard {
	String name;
	public String getName() {
		return name;
	}

	String image;
	String chosenImage;
	
	public String getChosenImage() {
		return chosenImage;
	}

	public String getImage() {
		return image;
	}

	public PokerCard (String name, String image, String chosenImage) {
		this.name = name;
		this.image = image;
		this.chosenImage = chosenImage;
	}
}
