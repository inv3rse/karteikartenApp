package de.mfgd_karteikarten.mfgd_karteikarten.ui.deck;

import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.base.db.DeckEditor;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import de.mfgd_karteikarten.mfgd_karteikarten.ui.topic.TopicActivity;
import nucleus.factory.PresenterFactory;
import nucleus.presenter.Presenter;

@ActivityScope
public class DeckPresenter extends Presenter<DeckActivity> {
    private DeckEditor editor;
    private List<Card> cards;
    private HashSet<Integer> savedSelection;

    @Inject
    public DeckPresenter(DeckEditor editor)
    {
        this.editor = editor;
    }

    @Override
    public void onTakeView(DeckActivity deckactivity)
    {
        cards = editor.getCards();

        deckactivity.setDeckName(editor.getDeckName());
        deckactivity.setCards(cards);

        if(savedSelection != null)
        {
            deckactivity.setSelection(savedSelection);
        }

    }

    public void addCard(int deckId)
    {
        DeckActivity view = getView();
        if(view != null)
        {
            view.startCardEditActivity(deckId);
        }
    }

    public void editCard(int deckId, HashSet<Integer> selection)
    {
        DeckActivity view = getView();
        int cardId = selection.toArray(new Integer [1])[0];
        if(view != null)
        {
        view.startCardEditActivity(deckId, cardId);
        }

    }

    public void deleteCards(HashSet<Integer> positions)
    {
        for (int i = cards.size() - 1; i >= 0; --i)
        {
            if (positions.contains(i)) {
                editor.removeCard(cards.remove(i));
            }
        }
        DeckActivity view = getView();
        if (view != null) {
            view.setCards(cards);
        }
    }

    public void closeWithSelection(HashSet<Integer> selection) {
        savedSelection = selection;
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
