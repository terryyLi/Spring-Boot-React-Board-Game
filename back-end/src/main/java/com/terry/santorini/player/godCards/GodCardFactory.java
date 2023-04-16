package com.terry.santorini.player.godCards;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.terry.santorini.player.GameActions;
import com.terry.santorini.player.Human;

public class GodCardFactory {
    public static GameActions getCard(String godCardName) {
        GameActions basicCard = new Human();
        if(godCardName.equalsIgnoreCase("Human")) {
            return basicCard;
        }
        else if (godCardName.equalsIgnoreCase("Demeter")) {
            return new Demeter(basicCard);
        }
        else if (godCardName.equalsIgnoreCase("Pan")) {
            return new Pan(basicCard);
        }
        else if (godCardName.equalsIgnoreCase("Minotaur")) {
            return new Minotaur(basicCard);
        }
        else if (godCardName.equalsIgnoreCase("Apollo")) {
            return new Apollo(basicCard);
        }
        else if (godCardName.equalsIgnoreCase("Artemis")) {
            return new Artemis(basicCard);
        }
        else if (godCardName.equalsIgnoreCase("Athena")) {
            return new Athena(basicCard);
        }
        else if (godCardName.equalsIgnoreCase("Atlas")) {
            return new Atlas(basicCard);
        }
        else if (godCardName.equalsIgnoreCase("Hephaestus")) {
            return new Hephaestus(basicCard);
        }
        else if (godCardName.equalsIgnoreCase("Hermes")) {
            return new Hermes(basicCard);
        }
        else if (godCardName.equalsIgnoreCase("Prometheus")) {
            return new Prometheus(basicCard);
        }

        return basicCard; 
    }

    public static String[] getCardList() {
        return new String[] {
            "Human", "Demeter", "Pan", "Minotaur", "Apollo", "Artemis", 
            "Athena", "Atlas", "Hephaestus", "Hermes", "Prometheus"
        };
    }

    public static String getCardListJSON() {
        List<String> cardList = Arrays.asList(getCardList()).stream()
                    .map(option -> "\"" + option + "\"")
                    .collect(Collectors.toList());
        String jsonArray = String.join(",", cardList);
        return """
                {
                    "cardList": [%s]
                }
                """.formatted(jsonArray);
    }
}

