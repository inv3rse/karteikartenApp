package de.mfgd_karteikarten.mfgd_karteikarten.ui.topic;

import android.util.Log;
import android.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.base.db.ImExporter;
import de.mfgd_karteikarten.mfgd_karteikarten.base.db.TopicEditor;
import de.mfgd_karteikarten.mfgd_karteikarten.base.online.IcasyApi;
import de.mfgd_karteikarten.mfgd_karteikarten.base.online.OnlineMapper;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import nucleus.factory.PresenterFactory;
import nucleus.presenter.Presenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

@ActivityScope
public class TopicPresenter extends Presenter<TopicActivity> {
    private OnlineMapper onlineMapper;
    private IcasyApi api;

    private TopicEditor editor;
    private List<Deck> decks;
    private HashSet<Integer> savedSelection;
    private boolean inTestMode;

    @Inject
    public TopicPresenter(TopicEditor editor, OnlineMapper onlineMapper, IcasyApi api) {
        this.editor = editor;
        this.onlineMapper = onlineMapper;
        this.api = api;
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

    public void switchMode(TopicActivity view) {
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

    public void exportSelected(HashSet<Integer> selection, TopicActivity view) {
        ImExporter exporter = new ImExporter();
        ArrayList<Deck> exportDecks = new ArrayList<>();

        for (int index : selection) {
            exportDecks.add(decks.get(index));
        }

        try {
            File exportFile = exporter.exportDecks(exportDecks, new File(view.getExternalFilesDir(null), "decks"));
            view.startExportIntent(exportFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shareSelected(HashSet<Integer> selection, TopicActivity view) {
        ImExporter exporter = new ImExporter();
        ArrayList<Deck> shareDecks = new ArrayList<>();

        for (int index : selection) {
            shareDecks.add(decks.get(index));
        }

        Observable.from(shareDecks)
                .flatMap(deck -> Observable.zip(api.createDeck(deck), Observable.just(deck), (id, deck1) -> new Pair<>(deck1, id)))
                .collect((Func0<ArrayList<Pair<Deck,String>>>) ArrayList::new, ArrayList::add)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pairList -> {
                    String shareString = "";
                    for (Pair<Deck, String> pair : pairList) {
                        onlineMapper.addMapping(pair.first.getID(), pair.second);
                        shareString = shareString + pair.first.getName() + ": https://icasy-pro.herokuapp.com/deck/" + pair.second + "\n";
                    }
                    view.startShareIntent(shareString);
                }, Throwable::printStackTrace);
    }

    public void importDecks(String fileString, TopicActivity view) {
        File file = new File(fileString);
        ImExporter importer = new ImExporter();
        try {
            List<Deck> importedDecks = importer.importDecks(file);

            for (Deck deck : importedDecks) {
                editor.addDeck(deck);
            }
            decks = editor.getDecks();
            view.setDecks(decks);
        } catch (IOException e) {
            Log.d("TopicPresenter", "failed to import decks");
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
