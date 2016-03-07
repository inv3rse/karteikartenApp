package de.mfgd_karteikarten.mfgd_karteikarten.ui.cardAsk;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.learn.LearnAssistant;
import de.mfgd_karteikarten.mfgd_karteikarten.base.learn.LearnInterface;
import de.mfgd_karteikarten.mfgd_karteikarten.base.learn.TestAssistant;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import io.realm.Realm;

@Module
public class CardAskModule {
    List<Integer> ids;
    boolean cards;
    boolean testMode;

    public CardAskModule(List<Integer> ids, boolean cards, boolean testMode) {
        this.ids = ids;
        this.cards = cards;
        this.testMode = testMode;
    }

    @Provides
    @ActivityScope
    public List<Card> provideCards(Realm realm) {
        List<Card> cards = new ArrayList<>();

        for (int id : this.ids) {
            if (this.cards) {
                Card card = realm.where(Card.class).equalTo("ID", id).findFirst();
                if (card != null) {
                    cards.add(realm.copyFromRealm(card));
                }
            } else {
                Deck deck = realm.where(Deck.class).equalTo("ID", id).findFirst();
                if (deck != null) {
                    for (Card card : deck.getCards()) {
                        cards.add(realm.copyFromRealm(card));
                    }
                }
            }
        }

        return cards;
    }

    @Provides
    @ActivityScope
    public LearnInterface provideLearnAssistant(Realm realm, List<Card> cards) {
        if (testMode) {
            return new TestAssistant(cards);
        } else {
            return new LearnAssistant(realm, cards);
        }
    }
}
