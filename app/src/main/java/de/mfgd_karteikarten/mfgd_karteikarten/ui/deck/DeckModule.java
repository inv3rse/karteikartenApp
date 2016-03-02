package de.mfgd_karteikarten.mfgd_karteikarten.ui.deck;

import dagger.Module;
import dagger.Provides;
import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.db.TopicEditor;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import io.realm.Realm;

@Module
public class DeckModule
{
    int deck;

    public DeckModule(int deck)
    {
        this.deck = deck;
    }

    @Provides
    @ActivityScope
    public Deck provideDeck(Realm realm)
    {
        return TopicEditor.getDeck(realm, deck);
    }

}
