package de.mfgd_karteikarten.mfgd_karteikarten.ui.deck;

import java.util.List;

import javax.inject.Inject;

import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.base.DeckEditor;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import nucleus.factory.PresenterFactory;
import nucleus.presenter.Presenter;

@ActivityScope
public class DeckPresenter extends Presenter<DeckActivity> {
    private DeckEditor editor;
    private List<Card> cards;

    @Inject
    public DeckPresenter(DeckEditor editor)
    {
        this.editor = editor;
    }

    public static class Factory implements PresenterFactory<DeckPresenter>
    {
        private App app;
        private int deckId;

        public Factory(App app, int deckId)
        {
            this.app = app;
            this.deckId = deckId;
        }

        @Override
        public DeckPresenter createPresenter()
        {
            return DaggerDeckComponent.builder()
                    .appComponent(app.component())
                    .deckModule(new DeckModule(deckId))
                    .build()
                    .getDeckPresenter();

        }
    }
}
