package de.mfgd_karteikarten.mfgd_karteikarten.ui.topic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.base.TopicEditor;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import nucleus.factory.PresenterFactory;
import nucleus.presenter.Presenter;

@ActivityScope
public class TopicPresenter extends Presenter<TopicActivity> {
    private TopicEditor editor;
    private List<Deck> decks;
    private HashSet<Integer> savedSelection;

    @Inject
    public TopicPresenter(TopicEditor editor) {
        this.editor = editor;
    }

    @Override
    protected void onTakeView(TopicActivity topicActivity) {
        decks = editor.getDecks();

        topicActivity.setTopicName(editor.getTopicName());
        topicActivity.setDecks(decks);

        if (savedSelection != null) {
            topicActivity.setSelection(savedSelection);
        }
    }

    public void addDeck(Deck deck) {
        editor.addDeck(deck);
    }

    public void deleteDecks(HashSet<Integer> selection) {
        for (int i = decks.size() - 1; i >= 0; --i) {
            if (selection.contains(i)) {
                editor.removeDeck(decks.remove(i));
            }
        }
    }

    public void learnAll() {
        TopicActivity view = getView();
        if (view != null) {
            ArrayList<Integer> deckIds = new ArrayList<>();
            for (Deck deck : editor.getDecks()) {
                deckIds.add(deck.getID());
            }

            if (!deckIds.isEmpty()) {
                view.startCardAskActivity(deckIds);
            }
        }
    }

    public void learnSelected(HashSet<Integer> selection) {
        TopicActivity view = getView();
        if (view != null) {
            ArrayList<Integer> deckIds = new ArrayList<>();
            for (int pos : selection) {
                deckIds.add(decks.get(pos).getID());
            }
            view.startCardAskActivity(deckIds);
        }
    }

    /**
     * Die Activity stoppt mit selection.
     * Methode dient zum speichern der Auswahl.
     */
    public void closeWithSelection(HashSet<Integer> selection) {
        savedSelection = selection;
    }


    public static class Factory implements PresenterFactory<TopicPresenter> {
        private App app;
        private int topicId;

        public Factory(App app, int topicId) {
            this.app = app;
            this.topicId = topicId;
        }

        @Override
        public TopicPresenter createPresenter() {
            return DaggerTopicComponent.builder()
                    .appComponent(app.component())
                    .topicModule(new TopicModule(topicId))
                    .build()
                    .getTopicPresenter();
        }
    }
}
