package de.mfgd_karteikarten.mfgd_karteikarten.ui.cardEdit;

import dagger.Module;
import dagger.Provides;
import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.db.DeckEditor;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import io.realm.Realm;

@Module
public class CardEditModule {

    int deckId;
    int cardId;

    public CardEditModule(int deckId, int cardId)
    {
        this.deckId = deckId;
        this.cardId = cardId;
    }

    @Provides
    @ActivityScope
    public Deck provideDeck(Realm realm)
    {
        Deck deck = realm.where(Deck.class).equalTo("ID", deckId).findFirst();
        return realm.copyFromRealm(deck);
    }

    @Provides
    @ActivityScope
    public Card provideCard(DeckEditor deckEditor)
    {
        if (cardId == Card.UNKNOWN_ID)
        {
            return new Card();
        }

        return deckEditor.getCard(cardId);
    }
}
