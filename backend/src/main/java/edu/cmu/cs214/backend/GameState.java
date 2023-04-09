package edu.cmu.cs214.backend;

import java.util.Arrays;

public final class GameState {

    private final Cell[] cells;
    private final Player winner;
    private final int playerTurn;
    private final Worker workerTurn;
    private final boolean isReadyToBuild;
    private final boolean isInitializable;

    private GameState(Cell[] cells, Player winner, int playerTurn, Worker workerTurn, boolean isReadyToBuild, boolean isInitializable) {
        this.cells = cells;
        this.winner = winner;
        this.playerTurn = playerTurn;
        this.workerTurn = workerTurn;
        this.isReadyToBuild = isReadyToBuild;
        this.isInitializable = isInitializable;
    }

    public static GameState forGame(Game game) {
        Cell[] cells = getCells(game);
        boolean readyToBuild = false;
        if(game.getCurrentPlayer() != null && game.getCurrentPlayer().getCurrentWorker() != null) {
            readyToBuild = game.getCurrentPlayer().getCurrentWorker().getReadyToBuild();
        }
        int currPlayerValue = 0;
        Worker currentWorker = null;
        if(game.getCurrentPlayer() != null) {
            currPlayerValue = game.getCurrentPlayer().getValue();
            currentWorker = game.getCurrentPlayer().getCurrentWorker();
        } 
        return new GameState(cells, game.getWinner(), currPlayerValue, currentWorker, readyToBuild, game.isInitializable());
    }

    public Cell[] getCells() {
        return this.cells;
    }

    /**
     * toString() of GameState will return the string representing
     * the GameState in JSON format.
     */
    @Override
    public String toString() {
        if (this.winner == null && this.workerTurn == null) {
            return "{ \"cells\": " + Arrays.toString(this.cells) + "," +
                     "\"playerTurn\": " + this.playerTurn + "," +
                     "\"workerTurn\": " + null + "," +
                     "\"isReadyToBuild\": " + this.isReadyToBuild + "," +
                     "\"isInitializable\": " + this.isInitializable + "," +
                     "\"winner\": " + null + "}";
        } else if (this.winner == null && this.workerTurn != null) {
            return "{ \"cells\": " + Arrays.toString(this.cells) + "," +
                     "\"playerTurn\": " + this.playerTurn + "," +
                     "\"workerTurn\": " + this.workerTurn.getValue() + "," +
                     "\"isReadyToBuild\": " + this.isReadyToBuild + "," +
                     "\"isInitializable\": " + this.isInitializable + "," +
                     "\"winner\": " + null + "}";
        } else if (this.winner != null && this.workerTurn == null) {
            return "{ \"cells\": " + Arrays.toString(this.cells) + "," +
                     "\"playerTurn\": " + this.playerTurn + "," +
                     "\"workerTurn\": " + null + "," +
                     "\"isReadyToBuild\": " + this.isReadyToBuild + "," +
                     "\"isInitializable\": " + this.isInitializable + "," +
                     "\"winner\": " + this.winner.getValue() + "}";
        } else {
            return "{ \"cells\": " + Arrays.toString(this.cells) + "," +
                     "\"playerTurn\": " + this.playerTurn + "," +
                     "\"workerTurn\": " + this.workerTurn.getValue() + "," +
                     "\"isReadyToBuild\": " + this.isReadyToBuild + "," +
                     "\"isInitializable\": " + this.isInitializable + "," +
                     "\"winner\": " + this.winner.getValue() + "}";
        }
    }

    private static Cell[] getCells(Game game) {
        int n = game.getBoardSize();
        int maxLevel = game.getMaxLevel();
        Cell[] cells = new Cell[n * n];
        Player curPlayer = game.getCurrentPlayer();
        boolean initializable = game.isInitializable();
        Board board = game.getBoard();
        
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                String text = "";
                boolean selectable = false;
                boolean movable = false;
                boolean buildable = false;
                int level = board.getTowerLevel(new Position(x, y));
                for(int i = 0; i < level; i++) {
                    text += "[";
                }
                for(int i = 0; i < level; i++) {
                    text += "]";
                }

                Worker worker = board.getWorker(new Position(x, y));
                Player player = board.getPlayer(new Position(x, y));
                String pre = text.substring(0, text.length() / 2);
                String post = text.substring(text.length() / 2);
                if(level == maxLevel) {
                    text = pre + "D" + post;
                }

                if(worker != null && player != null) {
                    text = pre + "Player" + player.getValue() + "worker" + worker.getValue() + post;
                    if(initializable) {
                        cells[n * y + x] = new Cell(x, y, text, selectable, movable, buildable, initializable);
                        continue;
                    }
                    if(player.getValue() == curPlayer.getValue() && curPlayer.getCurrentWorker() == null) {
                        selectable = true;
                    } else if(player.getValue() == curPlayer.getValue() 
                                && curPlayer.getCurrentWorker().getValue() == worker.getValue()) {
                        if(!worker.getReadyToBuild()) {
                            movable = true;
                        } else {
                            buildable = true;
                        }
                        
                    }
                }

                cells[n * y + x] = new Cell(x, y, text, selectable, movable, buildable, initializable);
            }
        }
        return cells;
    }
}

class Cell {
    private final int x;
    private final int y;
    private final String text;
    private final boolean selectable;
    private final boolean movable;
    private final boolean buildable;
    private final boolean initializable;

    Cell(int x, int y, String text, boolean selectable, boolean movable, boolean buildable, boolean initializable) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.selectable = selectable;
        this.movable = movable;
        this.buildable = buildable;
        this.initializable = initializable;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getText() {
        return this.text;
    }

    public boolean isselectable() {
        return this.selectable;
    }

    public boolean ismovable() {
        return this.movable;
    }

    public boolean isbuildable() {
        return this.buildable;
    }

    public boolean isinitializable() {
        return this.initializable;
    }

    @Override
    public String toString() {
        return "{ \"text\": \"" + this.text + "\"," +
                " \"selectable\": " + this.selectable + "," +
                " \"movable\": " + this.movable + "," +
                " \"buildable\": " + this.buildable + "," +
                " \"initializable\": " + this.initializable + "," +
                " \"x\": " + this.x + "," +
                " \"y\": " + this.y + " }" ;
    }
}