package de.mfgd_karteikarten.mfgd_karteikarten.ui.topic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.base.db.ImExporter;
import de.mfgd_karteikarten.mfgd_karteikarten.base.db.TopicEditor;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import nucleus.factory.PresenterFactory;
import nucleus.presenter.Presenter;

@ActivityScope
public class TopicPresenter extends Presenter<TopicActivity> {
    private TopicEditor editor;
    private List<Deck> decks;
    private HashSet<Integer> savedSelection;
    private boolean inTestMode;

    @Inject
    public TopicPresenter(TopicEditor editor) {
        this.editor = editor;
        inTestMode = false;
    }

    @Override
    protected void onTakeView(TopicActivity topicActivity) {
        decks = editor.getDecks();

        topicActivity.setTopicTitle((inTestMode ? "Exam: " : "Learn: ") + editor.getTopicName());
        topicActivity.setDecks(decks);

        if (savedSelection != null) {
            topicActivity.setSelection(savedSelection);
        }
    }

    public void switchMode(TopicActivity view)
    {
        inTestMode = !inTestMode;
        view.setTopicTitle((inTestMode ? "Exam: " : "Learn: ") + editor.getTopicName());
    }

    public void addDeck(String name) {
        Deck deck = new Deck(name);
        editor.addDeck(deck);

        TopicActivity view = getView();
        if (view != null) {
            view.addDeck(deck);
        }
    }

    public void renameDeck(int deckId, String name) {
        Deck deck = editor.getDeck(deckId);
        deck.setName(name);
        editor.setDeck(deck);
        decks = editor.getDecks();

        TopicActivity view = getView();
        if (view != null) {
            view.setDecks(decks);
        }
    }

    public void deleteDecks(HashSet<Integer> positions) {
        for (int i = decks.size() - 1; i >= 0; --i) {
            if (positions.contains(i)) {
                editor.removeDeck(decks.remove(i));
            }
        }

        TopicActivity view = getView();
        if (view != null) {
            view.setDecks(decks);
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
                view.startCardAskActivity(deckIds, inTestMode);
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
            view.startCardAskActivity(deckIds, inTestMode);
        }
    }

    public void shareSelected(HashSet<Integer> selection, TopicActivity view)
    {
        ImExporter exporter = new ImExporter();
        ArrayList<Deck> exportDecks = new ArrayList<>();

        for (int index : selection)
        {
            exportDecks.add(decks.get(index));
        }

        try {
            File exportFile = exporter.exportDecks(exportDecks, new File(view.getExternalFilesDir(null), "decks"));
            view.startShareIntent(exportFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDeck(int position) {
        TopicActivity view = getView();
        if (view != null) {
            view.startDeckActivity(decks.get(position).getID());
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
