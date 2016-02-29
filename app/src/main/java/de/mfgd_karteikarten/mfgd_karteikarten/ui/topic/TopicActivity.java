package de.mfgd_karteikarten.mfgd_karteikarten.ui.topic;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import nucleus.view.NucleusAppCompatActivity;

public class TopicActivity extends NucleusAppCompatActivity<TopicPresenter> implements DeckAdapter.OnSelectionModeChanged {
    public static final String TOPIC_EXTRA = "TOPIC_EXTRA";

    private DeckAdapter adapter;
    private Button learnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(TOPIC_EXTRA)) {
            int topicID = extras.getInt(TOPIC_EXTRA);
            setPresenterFactory(new TopicPresenter.Factory(App.get(this), topicID));
        } else {
            Log.e("TopicActivity", "gestartet ohne Topic als Extra");
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new DeckAdapter();
        adapter.setSelectionModeChangedListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.decklist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.add_fab);
        actionButton.setOnClickListener(view -> {
            Deck deck = new Deck("deck");
            getPresenter().addDeck(deck);
            adapter.addDeck(deck);
        });

        learnButton = (Button) findViewById(R.id.learn_button);
    }

    public void setDecks(List<Deck> decks) {
        adapter.setDecks(decks);
    }

    public void setSelection(HashSet<Integer> selection) {
        adapter.setSelection(selection);
    }

    public void startCardAskActivity(List<Deck> decks) {
        ArrayList<Deck> decksParam = new ArrayList<>(decks);
    }

    @Override
    protected void onPause() {
        getPresenter().closeWithSelection(adapter.getSeletion());
        super.onPause();
    }

    @Override
    public void onSelectionModeChanged(boolean inSelectionMode) {
        if (inSelectionMode) {
            learnButton.setText(R.string.learn_selected);
            learnButton.setOnClickListener(v -> getPresenter().learnSelected(adapter.getSeletion()));
        } else {
            learnButton.setText(R.string.learn_all);
            learnButton.setOnClickListener(v -> getPresenter().learnAll());
        }
    }
}
