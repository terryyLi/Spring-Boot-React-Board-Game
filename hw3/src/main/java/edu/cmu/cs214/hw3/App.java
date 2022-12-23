package edu.cmu.cs214.hw3;

import java.io.IOException;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;

public class App extends NanoHTTPD {

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    private static final int PORT = 8080;

    private Game game;
    private AIProxy aiProxy;


    /**
     * Start the server at :8080 port.
     * @throws IOException
     */
    public App() throws IOException {
        super(PORT);

        this.game = new Game();

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning!\n");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> params = session.getParms();
        if (uri.equals("/aimode")) {
            this.game.setAIMode();
            this.aiProxy = new AIProxy();
        } else if (uri.equals("/newgame")) {
            this.game = new Game();
        } else if(this.game.getPlayer1() == null
            || this.game.getPlayer2() == null) {
            if (uri.equals("/defaultPlayer")) {
                if(this.game.getPlayer1() == null) {
                    this.game.setPlayer1(new DefaultPlayer(this.game.getBoard(), 1));
                    this.game.setCurrentPlayer(this.game.getPlayer1());
                } else {
                    Player defaultPlayer = new DefaultPlayer(this.game.getBoard(), 2);
                    this.game.setPlayer2(defaultPlayer);
                    if(this.game.isAIMode()) {
                        this.aiProxy.setPlayer(defaultPlayer);
                    }
                }
            } else if (uri.equals("/demeter")) {
                if(this.game.getPlayer1() == null) {
                    this.game.setPlayer1(new Demeter(this.game.getBoard(), 1));
                    this.game.setCurrentPlayer(this.game.getPlayer1());
                } else {
                    Player demeter = new Demeter(this.game.getBoard(), 2);
                    this.game.setPlayer2(demeter);
                    if(this.game.isAIMode()) {
                        this.aiProxy.setPlayer(demeter);
                    }
                }
            } else if (uri.equals("/minotaur")) {
                if(this.game.getPlayer1() == null) {
                    this.game.setPlayer1(new Minotaur(this.game.getBoard(), 1));
                    this.game.setCurrentPlayer(this.game.getPlayer1());
                } else {
                    Player minotaur = new Minotaur(this.game.getBoard(), 2);
                    this.game.setPlayer2(minotaur);
                    if(this.game.isAIMode()) {
                        this.aiProxy.setPlayer(minotaur);
                    }
                }
            } else if (uri.equals("/pan")) {
                if(this.game.getPlayer1() == null) {
                    this.game.setPlayer1(new Pan(this.game.getBoard(), 1));
                    this.game.setCurrentPlayer(this.game.getPlayer1());
                } else {
                    Player pan = new Pan(this.game.getBoard(), 2);
                    this.game.setPlayer2(pan);
                    if(this.game.isAIMode()) {
                        this.aiProxy.setPlayer(pan);
                    }
                }
            } else if (uri.equals("/artemis")) {
                if(this.game.getPlayer1() == null) {
                    this.game.setPlayer1(new Artemis(this.game.getBoard(), 1));
                    this.game.setCurrentPlayer(this.game.getPlayer1());
                } else {
                    Player artemis = new Artemis(this.game.getBoard(), 2);
                    this.game.setPlayer2(artemis);
                    if(this.game.isAIMode()) {
                        this.aiProxy.setPlayer(artemis);
                    }
                }
            } else if (uri.equals("/hephaestus")) {
                if(this.game.getPlayer1() == null) {
                    this.game.setPlayer1(new Hephaestus(this.game.getBoard(), 1));
                    this.game.setCurrentPlayer(this.game.getPlayer1());
                } else {
                    Player hephaestus = new Hephaestus(this.game.getBoard(), 2);
                    this.game.setPlayer2(hephaestus);
                    if(this.game.isAIMode()) {
                        this.aiProxy.setPlayer(hephaestus);
                    }
                }
            } else if (uri.equals("/apollo")) {
                if(this.game.getPlayer1() == null) {
                    this.game.setPlayer1(new Apollo(this.game.getBoard(), 1));
                    this.game.setCurrentPlayer(this.game.getPlayer1());
                } else {
                    Player apollo = new Apollo(this.game.getBoard(), 2);
                    this.game.setPlayer2(apollo);
                    if(this.game.isAIMode()) {
                        this.aiProxy.setPlayer(apollo);
                    }
                }
            }
        } else if (this.game.getWinner() == null) {
            if (uri.equals("/initialize")) {
                int x = Integer.parseInt(params.get("x"));
                int y = Integer.parseInt(params.get("y"));
                Player ply1 = this.game.getPlayer1();
                Player ply2 = this.game.getPlayer2();
                if(ply1.getWorker1() == null) {
                    ply1.setWorker1(new Worker(new Position(x, y), 1)); 
                } else if(ply1.getWorker2() == null) {
                    ply1.setWorker2(new Worker(new Position(x, y), 2)); 
                    if(this.game.isAIMode()) {
                        aiProxy.initialize();
                        this.game.setNotInitializable();
                    }
                } else if(ply2.getWorker1() == null) {
                    ply2.setWorker1(new Worker(new Position(x, y), 1)); 
                } else if(ply2.getWorker2() == null) {
                    ply2.setWorker2(new Worker(new Position(x, y), 2));
                    if(ply2.getWorker2() != null) {
                        this.game.setNotInitializable();
                    }
                }
            } else if (uri.equals("/select")) {
                int x = Integer.parseInt(params.get("x"));
                int y = Integer.parseInt(params.get("y")); 
                Player currPlayer = this.game.getCurrentPlayer();
                currPlayer.setCurrentWorker(currPlayer.getBoard().getWorker(new Position(x, y)));
            } else if (uri.equals("/play")) {
                int x = Integer.parseInt(params.get("x"));
                int y = Integer.parseInt(params.get("y")); 
                Player currPlayer = this.game.getCurrentPlayer();
                Worker curWorker = currPlayer.getCurrentWorker();
                if(curWorker != null) {
                    if(!curWorker.getReadyToBuild()) {
                        if(currPlayer.getRemainingMoveSteps() == 1
                            && currPlayer.getMaxMoveSteps() > 1) {
                            if(curWorker.getPosition().getX() == x
                                && curWorker.getPosition().getY() == y) {
                                    currPlayer.setRemainingMoveSteps(0);
                                }
                        }
                        currPlayer.move(curWorker, new Position(x, y));
                    } else {
                        if(currPlayer.getRemainingBuildSteps() == 1
                        && currPlayer.getMaxBuildSteps() > 1) {
                            if(curWorker.getPosition().getX() == x
                                && curWorker.getPosition().getY() == y) {
                                    currPlayer.setRemainingBuildSteps(0);
                                }
                        }
                        currPlayer.build(curWorker, new Position(x, y));
                        boolean isEndBuild = curWorker.getReadyToBuild();
                        if(!isEndBuild) {
                            this.game.resetCurrentPlayer();
                            this.game.alterCurrentPlayer();
                            if(this.game.isAIMode()) {
                                aiProxy.automate();
                                this.game.alterCurrentPlayer();
                            }
                        }
                        
                    }
                }
            }
        } 
        GameState gameplay = GameState.forGame(this.game);
        return newFixedLengthResponse(gameplay.toString());
    }

    public static class Test {
        public String getText() {
            return "Hello World!";
        }
    }
}