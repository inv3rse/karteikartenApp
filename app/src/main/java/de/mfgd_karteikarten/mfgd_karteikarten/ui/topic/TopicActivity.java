package de.mfgd_karteikarten.mfgd_karteikarten.ui.topic;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import de.mfgd_karteikarten.mfgd_karteikarten.ui.cardAsk.CardAskActivity;
import de.mfgd_karteikarten.mfgd_karteikarten.ui.deck.DeckActivity;
import nucleus.view.NucleusAppCompatActivity;

public class TopicActivity extends NucleusAppCompatActivity<TopicPresenter> implements ActionMode.Callback {
    public static final String TOPIC_EXTRA = "TOPIC_EXTRA";
    private static final String KEY_TOPIC_DIALOG_VISIBLE = "TOPIC_DIALOG_VISIBLE";
    private static final String KEY_TOPIC_DIALOG_NAME = "TOPIC_DIALOG_NAME";

    private DeckAdapter adapter;
    private Button learnButton;
    private ActionMode actionMode = null;
    private boolean createTopicDialogVisible = false;
    private EditText topicName = null;
    private Toolbar toolbar;

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

        if (savedInstanceState != null) {
            createTopicDialogVisible = savedInstanceState.getBoolean(KEY_TOPIC_DIALOG_VISIBLE);
            if (createTopicDialogVisible) {
                showCreateTopicDialog();
                topicName.setText(savedInstanceState.getString(KEY_TOPIC_DIALOG_NAME, ""));
            }
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new DeckAdapter(this);
        adapter.setSelectionChangedListener(this::onSelectionChanged);
        adapter.setItemClickedListener(position -> getPresenter().showDeck(position));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.decklist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.add_fab);
        actionButton.setOnClickListener(v -> showCreateTopicDialog());

        learnButton = (Button) findViewById(R.id.learn_button);
        setLearnButtonAction(false);
    }

    public void setTopicName(String name)
    {
        toolbar.setTitle(name);
    }

    public void setDecks(List<Deck> decks) {
        adapter.setDecks(decks);
    }

    public void addDeck(Deck deck)
    {
        adapter.addDeck(deck);
    }

    public void setSelection(HashSet<Integer> selection) {
        adapter.setSelection(selection);
    }

    public void startCardAskActivity(ArrayList<Integer> deckIds) {
        Intent intent = new Intent(this, CardAskActivity.class);
        intent.putIntegerArrayListExtra(CardAskActivity.CARDASK_EXTRA_DECKS, deckIds);
        startActivity(intent);
    }

    public void startDeckActivity(int deckId)
    {
        Intent intent = new Intent(this, DeckActivity.class);
        intent.putExtra(DeckActivity.DECK_EXTRA, deckId);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        getPresenter().closeWithSelection(adapter.getSeletion());
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(KEY_TOPIC_DIALOG_VISIBLE, createTopicDialogVisible);
        if (createTopicDialogVisible) {
            outState.putString(KEY_TOPIC_DIALOG_NAME, topicName.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    private void onSelectionChanged(HashSet<Integer> selection) {
        setLearnButtonAction(!selection.isEmpty());

        if (!selection.isEmpty()) {
            if (actionMode == null) {
                actionMode = startSupportActionMode(this);
            }

            actionMode.setTitle(getString(R.string.selection_title, selection.size()));
        } else if (actionMode != null) {
            actionMode.finish();
        }
    }

    private void setLearnButtonAction(boolean inSelectionMode) {
        if (inSelectionMode) {
            learnButton.setText(R.string.learn_selected);
            learnButton.setOnClickListener(v -> getPresenter().learnSelected(adapter.getSeletion()));
        } else {
            learnButton.setText(R.string.learn_all);
            learnButton.setOnClickListener(v -> getPresenter().learnAll());
        }
    }

    private void showCreateTopicDialog() {
        createTopicDialogVisible = true;
        topicName = new EditText(this);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.deck_name)
                .setView(topicName)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    createTopicDialogVisible = false;
                    getPresenter().addDeck(topicName.getText().toString());
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setOnCancelListener(dialog -> createTopicDialogVisible = false)
                .create();

        alertDialog.show();
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.topic_activity_context, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.delete_action) {
            getPresenter().deleteDecks(adapter.getSeletion());
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        this.adapter.clearSelection();
        this.actionMode = null;
    }
}
