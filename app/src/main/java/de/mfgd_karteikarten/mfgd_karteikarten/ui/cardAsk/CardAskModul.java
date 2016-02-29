package de.mfgd_karteikarten.mfgd_karteikarten.ui.cardAsk;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import io.realm.Realm;

@Module
public class CardAskModul {
    List<Integer> ids;
    boolean cards;

    public CardAskModul(List<Integer> ids, boolean cards) {
        this.ids = ids;
        this.cards = cards;
    }

    @Provides
    @ActivityScope
    public List<Card> provideCards(Realm realm) {
        List<Card> cards = new ArrayList<>();

        for (int id : this.ids)
        {
            if (this.cards)
            {
                Card card = realm.where(Card.class).equalTo("ID", id).findFirst();
                if(card != null)
                {
                    cards.add(realm.copyFromRealm(card));
                }
            }
            else
            {
                Deck deck = realm.where(Deck.class).equalTo("ID", id).findFirst();
                if(deck != null)
                {
                    for (Card card : deck.getCards())
                    {
                        cards.add(realm.copyFromRealm(card));
                    }
                }
            }
        }

        return cards;
    }
}
