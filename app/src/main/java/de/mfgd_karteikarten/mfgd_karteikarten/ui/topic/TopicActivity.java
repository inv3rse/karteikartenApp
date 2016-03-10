package de.mfgd_karteikarten.mfgd_karteikarten.ui.topic;

import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxMenuItem;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import de.mfgd_karteikarten.mfgd_karteikarten.ui.cardAsk.CardAskActivity;
import de.mfgd_karteikarten.mfgd_karteikarten.ui.deck.DeckActivity;
import nucleus.view.NucleusAppCompatActivity;
import rx.Observable;

public class TopicActivity extends NucleusAppCompatActivity<TopicPresenter> implements ActionMode.Callback {
    public static final String TOPIC_EXTRA = "TOPIC_EXTRA";
    private static final String KEY_DECK_DIALOG_VISIBLE = "TOPIC_DIALOG_VISIBLE";
    private static final String KEY_DECK_DIALOG_NAME = "TOPIC_DIALOG_NAME";
    private static final String KEY_DECK_DIALOG_ID = "TOPIC_DIALOG_ID";

    private static final int REQUEST_FILE = 1;

    private DeckAdapter adapter;
    private Button learnButton;
    private ActionMode actionMode = null;
    private boolean createTopicDialogVisible = false;
    private EditText topicName = null;
    private int deckId;
    private boolean isMultipleSelection;
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
            createTopicDialogVisible = savedInstanceState.getBoolean(KEY_DECK_DIALOG_VISIBLE);
            deckId = savedInstanceState.getInt(KEY_DECK_DIALOG_ID, Deck.UNKNOWN_ID);
            if (createTopicDialogVisible) {
                showCreateTopicDialog(deckId);
                topicName.setText(savedInstanceState.getString(KEY_DECK_DIALOG_NAME, ""));
            }
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        adapter = new DeckAdapter(this);
        adapter.setSelectionChangedListener(this::onSelectionChanged);
        adapter.setItemClickedListener(position -> getPresenter().showDeck(position));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.decklist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.add_fab);
        actionButton.setOnClickListener(v -> showCreateTopicDialog(Deck.UNKNOWN_ID));

        learnButton = (Button) findViewById(R.id.learn_button);
        setLearnButtonAction(false);
    }

    public void setTopicTitle(String name) {
        toolbar.setTitle(name);
    }

    public void setDecks(List<Deck> decks) {
        adapter.setDecks(decks);
    }

    public void addDeck(Deck deck) {
        adapter.addDeck(deck);
    }

    public void setSelection(HashSet<Integer> selection) {
        adapter.setSelection(selection);
    }

    public void startCardAskActivity(ArrayList<Integer> deckIds, boolean inTestMode) {
        Intent intent = new Intent(this, CardAskActivity.class);
        intent.putIntegerArrayListExtra(CardAskActivity.CARDASK_EXTRA_DECKS, deckIds);
        intent.putExtra(CardAskActivity.CARDASK_EXTRA_LEARNMODE, inTestMode);
        startActivity(intent);
    }

    public void startDeckActivity(int deckId) {
        Intent intent = new Intent(this, DeckActivity.class);
        intent.putExtra(DeckActivity.DECK_EXTRA, deckId);
        startActivity(intent);
    }

    public void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, REQUEST_FILE);
    }

    public void startShareIntent(File file) {
//        Uri contentUri = FileProvider.getUriForFile(this, "de.mfgd_karteikarten.fileprovider", file);
        Uri contentUri = Uri.fromFile(file);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "deckExport.json");
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        shareIntent.setType("*/*");
        startActivity(Intent.createChooser(shareIntent, "select share method"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topic_activity, menu);

        Observable<Void> importClicked = RxMenuItem.clicks(menu.getItem(1));

        RxPermissions.getInstance(this)
                .request(importClicked, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        showFileChooser();
                    }
                });


        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (searchView == null) {
            Log.d("tag", "Failed to get search view");
        }


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);


        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                showList("");
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // showList(newText);
                showList(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_swap:
                getPresenter().switchMode(this);
                return true;
            case R.id.action_import:
                showFileChooser();
                return true;
            case R.id.action_search:
                Intent intent = getIntent();
                if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                    String query = intent.getStringExtra(SearchManager.QUERY);
                    showList(query);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra((SearchManager.QUERY));
            showList(query);
            Log.d("TopicAcitivity", "searchQuery:" + query);
        }
    }

    private void showList(String newText) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.decklist);
        recyclerView.setVisibility(View.VISIBLE);
        int itemCount = adapter.getItemCount();

        for (int i = 0; i < adapter.getItemCount(); i++) {
            Deck deck = adapter.getDeck(i);
            View view = recyclerView.getChildAt(i);

            if (newText.equals(adapter.getDeck(i).getName())) {

                Toast.makeText(this, adapter.getDeck(i).getName() + "gut", Toast.LENGTH_SHORT).show();
                view.setVisibility(View.INVISIBLE);


            }
            Toast.makeText(this, "leer", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    protected void onPause() {
        getPresenter().closeWithSelection(adapter.getSelection());
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(KEY_DECK_DIALOG_VISIBLE, createTopicDialogVisible);
        if (createTopicDialogVisible) {
            outState.putString(KEY_DECK_DIALOG_NAME, topicName.getText().toString());
            outState.putInt(KEY_DECK_DIALOG_ID, deckId);
        }
        super.onSaveInstanceState(outState);
    }


    private void onSelectionChanged(HashSet<Integer> selection) {
        setLearnButtonAction(!selection.isEmpty());

        boolean wasMultiple = isMultipleSelection;
        isMultipleSelection = selection.size() > 1;

        if (!selection.isEmpty()) {
            if (actionMode == null) {
                actionMode = startSupportActionMode(this);
            } else if (wasMultiple != isMultipleSelection) {
                actionMode.invalidate();
            }

            actionMode.setTitle(getString(R.string.selection_title, selection.size()));
        } else if (actionMode != null) {
            actionMode.finish();
        }
    }

    private void setLearnButtonAction(boolean inSelectionMode) {
        if (inSelectionMode) {
            learnButton.setText(R.string.learn_selected);
            learnButton.setOnClickListener(v -> getPresenter().learnSelected(adapter.getSelection()));
        } else {
            learnButton.setText(R.string.learn_all);
            learnButton.setOnClickListener(v -> getPresenter().learnAll());
        }
    }

    private void showCreateTopicDialog(int deckId) {
        this.deckId = deckId;
        createTopicDialogVisible = true;
        topicName = new EditText(this);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.deck_name)
                .setView(topicName)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    createTopicDialogVisible = false;
                    if (deckId != Deck.UNKNOWN_ID) {
                        getPresenter().renameDeck(deckId, topicName.getText().toString());
                    } else {
                        getPresenter().addDeck(topicName.getText().toString());
                    }
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> createTopicDialogVisible = false)
                .setOnCancelListener(dialog -> createTopicDialogVisible = false)
                .create();

        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_FILE && resultCode == RESULT_OK) {
            getPresenter().importDecks(data.getData().getPath(), this);
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.topic_activity_context, menu);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        menu.getItem(0).setVisible(!isMultipleSelection);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                getPresenter().deleteDecks(adapter.getSelection());
                return true;
            case R.id.action_edit:
                if (adapter.getSelection().size() == 1) {
                    int pos = adapter.getSelection().iterator().next();
                    Deck deck = adapter.getDeck(pos);
                    showCreateTopicDialog(deck.getID());
                    topicName.setText(deck.getName());
                    return true;
                }
                break;
            case R.id.action_share:
                getPresenter().shareSelected(adapter.getSelection(), this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        this.adapter.clearSelection();
        this.actionMode = null;
    }


}
