package de.mfgd_karteikarten.mfgd_karteikarten.base.learn;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import io.realm.Realm;

public class LearnAssistant implements LearnInterface {
    private Realm realm;
    private List<Card> cards;
    private Card currentCard;
    private Random random;

    public LearnAssistant(Realm realm, List<Card> cards) {
        this.realm = realm;
        this.cards = cards;
        this.random = new Random();
    }

    @Override
    public boolean hasNextCard() {
        return !cards.isEmpty();
    }

    @Override
    public Card getNextCard() {

        // Alle karten mit einem negativen Rating werden mehrfach in die Liste eingetragen
        ArrayList<Integer> cardIndexes = new ArrayList<>();
        for (int i = 0; i < cards.size(); ++i)
        {
            int j = 0;
            do {
                cardIndexes.add(i);
                j--;
            } while (j > cards.get(i).getRating());
        }

        currentCard = cards.get(cardIndexes.get(random.nextInt(cardIndexes.size())));
        return currentCard;
    }

    @Override
    public Card getCard(int i)
    {
        return cards.get(i);
    }

    @Override
    public int getPosition(Card card)
    {
        return cards.indexOf(card);
    }

    @Override
    public Card shuffleCard() {
        return getNextCard();
    }

    @Override
    public void gradeCurrentCard(boolean positive) {
        if (currentCard != null)
        {
            if (positive)
            {
                currentCard.setRating(currentCard.getRating() + 1);
            }
            else
            {
                currentCard.setRating(currentCard.getRating() + 1);
            }
            if (currentCard.getID() != Card.UNKNOWN_ID)
            {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(currentCard);
                realm.commitTransaction();
            }
            else
            {
                Log.e("LearnAssistant", "Kann Karte ohne ID nicht updaten");
            }
        }
    }
}
