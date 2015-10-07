package net.congstar.jira.plugins.scrumpoker.model;

public class PokerCard {

    private String name;

    private String image;

    public PokerCard(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

}
