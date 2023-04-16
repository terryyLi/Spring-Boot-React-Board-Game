package com.terry.santorini.game;

public enum GameStage {
    INIT("init", "Player %s needs to place a worker"),
    SELECT("select", "Player %s needs to select a worker"),
    MOVE("move", "Player %s needs to move a worker"),
    BUILD("build", "Player %s needs to build"),
    ENDTURN("endTurn", "Player %s's turn is finished"),
    END("end", "Game Over. Player %s is the winner!");

    private String stage;
    private String description;

    GameStage(String stage, String description) {
        this.stage = stage;
        this.description = description;
    }

    @Override
    public String toString() {
        return stage;
    }

    public String getDescription(String playerName) {
        return description.formatted(playerName);
    }
}
