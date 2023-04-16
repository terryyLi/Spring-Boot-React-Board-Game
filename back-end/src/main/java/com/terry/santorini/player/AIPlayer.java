package com.terry.santorini.player;

public class AIPlayer extends Player {

    public AIPlayer(String name) {
        super(name);
    }

    public AIPlayer(String name, GameActions godCard) {
        super(name, godCard);
    }

    public AIPlayer(Player player) {
        super(player.getName(), player.getActions());
    }

    @Override
    public Player update(GameActions godCard) {
        return new AIPlayer(super.update(godCard));
    }
    
    @Override
    public GameActions getActions() {
        GameActions action = super.getActions();
        if (action instanceof AI) {
            return action;
        }
        return new AI(action);
    }
    
}
