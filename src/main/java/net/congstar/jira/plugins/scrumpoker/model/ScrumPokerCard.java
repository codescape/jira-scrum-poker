package net.congstar.jira.plugins.scrumpoker.model;

public class ScrumPokerCard {

    private String name;

    private String image;

    public ScrumPokerCard(String name, String image) {
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
