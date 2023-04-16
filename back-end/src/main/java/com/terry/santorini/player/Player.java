package com.terry.santorini.player;

public class Player {
    private final String name;
    private final GameActions actions; 

    public Player(String name) {
        this.name = name;
        this.actions = new Human();
    }

    public Player(String name, GameActions godCard) {
        this.name = name;
        this.actions = godCard;
    }

    /**
     * Update the player's god card
     * 
     * @param godCard object to be updated
     * 
     * @return updated {@link Player}
     */
    public Player update(GameActions godCard) {
        return new Player(this.name, godCard);
    }

    public GameActions getActions() {
        return actions;
    }

    public String getName() {
        return name;
    }
}
