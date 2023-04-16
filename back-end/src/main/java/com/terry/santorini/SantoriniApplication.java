package com.terry.santorini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.terry.santorini.game.Game;
import com.terry.santorini.player.godCards.GodCardFactory;

@Controller
@SpringBootApplication
public class SantoriniApplication {

	private Game game;

    public static void main(String[] args) {
        SpringApplication.run(SantoriniApplication.class, args);
    }

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Running!";
    }

    @GetMapping("/newGame")
    @ResponseBody
    public String serveNewGame(@RequestParam("godCard0") String godCard0,
                                @RequestParam("godCard1") String godCard1,
                                @RequestParam("AI0") boolean ai0,
                                @RequestParam("AI1") boolean ai1) {
        String[] godCardNames = {godCard0, godCard1};
        this.newGame(godCardNames);
        if (ai0) {
            game = game.setUpAI(0);
        }
        if (ai1) {
            game = game.setUpAI(1);
        }
        return game.toString();
    }

    @GetMapping("/initWorker")
    @ResponseBody
    public String serveInitWorker(@RequestParam("field_id") int field_id) {
        game = game.initWorker(field_id);
        return game.toString();
    }

    @GetMapping("/select")
    @ResponseBody
    public String serveSelect(@RequestParam("field_id") int field_id) {
        game = game.select(field_id);
        return game.toString();
    }

    @GetMapping("/move")
    @ResponseBody
    public String serveMove(@RequestParam("field_id") int field_id) {
        game = game.move(field_id);
        return game.toString();
    }

    @GetMapping("/build")
    @ResponseBody
    public String serveBuild(@RequestParam("field_id") int field_id) {
        game = game.build(field_id);
        return game.toString();
    }

    @GetMapping("/endTurn")
    @ResponseBody
    public String serveEndTurn() {
        game = game.newRound();
        return game.toString();
    }

    @GetMapping("/undo")
    @ResponseBody
    public String serveUndo() {
        game = game.undo();
        return game.toString();
    }

    @GetMapping("/ability")
    @ResponseBody
    public String serveAbility() {
        game = game.useAbility();
        return game.toString();
    }

    @GetMapping("/cardList")
    @ResponseBody
    public String getCardList() {
        return GodCardFactory.getCardListJSON();
    }

    private void newGame(String[] godCardNames) {
        this.game = new Game(godCardNames);
    }

}
