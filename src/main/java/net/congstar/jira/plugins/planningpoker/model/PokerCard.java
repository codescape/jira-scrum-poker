package net.congstar.jira.plugins.planningpoker.model;

public class PokerCard {

    String name;

    String image;

    String chosenImage;

    public PokerCard(String name, String image, String chosenImage) {
        this.name = name;
        this.image = image;
        this.chosenImage = chosenImage;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getChosenImage() {
        return chosenImage;
    }

}
