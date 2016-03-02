package de.mfgd_karteikarten.mfgd_karteikarten.base.learn;

import java.util.List;
import java.util.Random;

import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;

public class TestAssistant implements LearnInterface {

    private List<Card> cards;
    private Card currentCard;

    private Random random;

    public TestAssistant(List<Card> cards) {
        this.cards = cards;
        this.random = new Random();
    }

    @Override
    public boolean hasNextCard() {
        return !cards.isEmpty();
    }

    @Override
    public Card getNextCard() {
        currentCard = cards.remove(random.nextInt(cards.size()));
        return currentCard;
    }

    @Override
    public Card shuffleCard() {
        if (currentCard != null) {
            cards.add(currentCard);
        }

        currentCard = cards.remove(random.nextInt(cards.size() - 1));
        return currentCard;
    }

    @Override
    public void gradeCurrentCard(boolean positive) {
        // im TestModus bewerten wir nicht
    }
}
