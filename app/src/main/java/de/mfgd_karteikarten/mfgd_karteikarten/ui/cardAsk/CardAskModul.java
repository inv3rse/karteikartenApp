package de.mfgd_karteikarten.mfgd_karteikarten.ui.cardAsk;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.DeckEditor;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import io.realm.Realm;

@Module
public class CardAskModul {
    List<Integer> cards;

    public CardAskModul(List<Integer> cards) {
        this.cards = cards;
    }

    @Provides
    @ActivityScope
    public List<Card> provideCards(Realm realm) {
        List<Card> cards = new ArrayList<>();
        for (int cardID : this.cards)
        {
            Card card = realm.where(Card.class).equalTo("ID", cardID).findFirst();
            if(card != null)
            {
                cards.add(realm.copyFromRealm(card));
            }
        }
        return cards;
    }
}
