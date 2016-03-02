package de.mfgd_karteikarten.mfgd_karteikarten.base.learn;

import java.util.List;

import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import io.realm.Realm;

public class LearnAssistant implements LearnInterface {
    private Realm realm;
    private List<Card> cards;
    private int nextID = 1;

    public LearnAssistant(Realm realm, List<Card> cards) {
        this.realm = realm;
        this.cards = cards;

    }

    @Override
    public boolean hasNextCard() {
        return false;
    }

    @Override
    public Card getNextCard() {
        return null;
    }

    @Override
    public Card shuffleCard() {
        return null;
    }

    @Override
    public void gradeCurrentCard(boolean positive) {

    }
}
