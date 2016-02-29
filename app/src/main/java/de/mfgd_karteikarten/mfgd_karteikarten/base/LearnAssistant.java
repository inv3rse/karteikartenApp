package de.mfgd_karteikarten.mfgd_karteikarten.base;

import android.content.SharedPreferences;

import java.util.List;

import javax.inject.Inject;

import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;
import io.realm.Realm;

@ActivityScope
public class LearnAssistant {
    private Realm realm;
    private List<Card> cards;
    private int nextID = 1;

    @Inject
    public LearnAssistant(Realm realm, List<Card> cards) {
        this.realm = realm;
        this.cards = cards;

    }

    public boolean hasNextCard() {
        return !cards.isEmpty();
    }

    public Card getNextCard() {
        return cards.remove(0);
    }

    public void gradePositive() {

    }

    public void bewerteNegativ() {

    }
}
