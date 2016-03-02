package de.mfgd_karteikarten.mfgd_karteikarten.ui.deck;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import de.mfgd_karteikarten.mfgd_karteikarten.ui.cardEdit.CardEditActivity;
import de.mfgd_karteikarten.mfgd_karteikarten.ui.topic.DeckAdapter;
import nucleus.view.NucleusAppCompatActivity;

public class DeckActivity extends NucleusAppCompatActivity<DeckPresenter> implements ActionMode.Callback
{

    public static final String DECK_EXTRA = "DECK_EXTRA";
    public static final String CHECKBOX_VISIBLE = "CHECKBOX_VISIBLE";

    private CardAdapter adapter;
    private ActionMode actionMode = null;
    private int deckId;
    private boolean multipleSelection;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck);

        Bundle extras = getIntent().getExtras();
        if(extras.containsKey(DECK_EXTRA))
        {
            deckId = extras.getInt(DECK_EXTRA);
            setPresenterFactory(new DeckPresenter.Factory(App.get(this), deckId));
        } else
        {
            Log.e("Deck Activity", "gestartet ohne deck als Extra");
            finish();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new CardAdapter(this);
        adapter.setSelectionChangedListener(this::onSelectionChanged);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cardlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.add_card);
        actionButton.setOnClickListener(v -> getPresenter().addCard(deckId));



    }

    public void setDeckName(String name)
    {
        toolbar.setTitle(name);
    }

    public void setCards(List<Card> cards)
    {
        adapter.setCards(cards);
    }

    public void setSelection(HashSet<Integer> selection)
    {
        adapter.setSelection(selection);
    }

    @Override
    protected void onPause()
    {
        getPresenter().closeWithSelection(adapter.getSelection());
        super.onPause();
    }

    private void onSelectionChanged(HashSet<Integer> selection)
    {
        boolean wasMultiple = multipleSelection;
        multipleSelection = selection.size() > 1;

        if (!selection.isEmpty()) {
            if (actionMode == null) {
                actionMode = startSupportActionMode(this);
            } else if (wasMultiple != multipleSelection) {
                actionMode.invalidate();
            }

            actionMode.setTitle(getString(R.string.selection_title, selection.size()));
        } else if (actionMode != null) {
            actionMode.finish();
        }
    }

    public void startCardEditActivity(int deckId)
    {
        Intent intent = new Intent(this, CardEditActivity.class);
        intent.putExtra(CardEditActivity.DECK_EXTRA, deckId);
        startActivity(intent);
    }

    public void startCardEditActivity(int deckId, int cardId)
    {
        Intent intent = new Intent(this, CardEditActivity.class);
        intent.putExtra(CardEditActivity.CARD_EXTRA, cardId);
        intent.putExtra(CardEditActivity.DECK_EXTRA, deckId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu)
    {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.deck_activity_context, menu);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu)
    {
        menu.getItem(0).setVisible(!multipleSelection);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_delete:
                getPresenter().deleteCards(adapter.getSelection());
                return true;
            case R.id.action_edit:
                if (adapter.getSelection().size() == 1)
                {
                    HashSet<Integer> selection = adapter.getSelection();
                    getPresenter().editCard(deckId, selection);
                    return true;
                }
                break;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode)
    {
        this.adapter.clearSelection();
        this.actionMode = null;
    }
}
