package de.mfgd_karteikarten.mfgd_karteikarten.ui.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashSet;
import java.util.List;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;
import de.mfgd_karteikarten.mfgd_karteikarten.ui.topic.TopicActivity;
import nucleus.view.NucleusAppCompatActivity;

public class MainActivity extends NucleusAppCompatActivity<MainPresenter> implements ActionMode.Callback {

    private TopicAdapter topicAdapter;
    private EditText dialogInput;

    private boolean isMultipleSelection;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setPresenterFactory(new MainPresenter.Factory(App.get(this)));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        topicAdapter = new TopicAdapter(this);
        topicAdapter.setSelectionChangedListener(this::onSelectionChanged);
        topicAdapter.setItemClickedListener(position -> getPresenter().onPositionClicked(position));

        RecyclerView topicGrid = (RecyclerView) findViewById(R.id.topic_grid);
        topicGrid.setLayoutManager(new GridLayoutManager(this, 2));
        topicGrid.setAdapter(topicAdapter);

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.fab);
        addButton.setOnClickListener(view -> getPresenter().onAddTopicClicked(this));
    }

    public void setTopics(List<Topic> topics) {
        topicAdapter.setTopics(topics);
    }

    public void setSelection(HashSet<Integer> selection)
    {
        topicAdapter.setSelection(selection);
    }

    public void addTopic(Topic topic) {
        topicAdapter.addTopic(topic);
    }

    public void showCreateEditDialog(int topicId, String topicName)
    {
        dialogInput = new EditText(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        dialogInput.setLayoutParams(lp);
        dialogInput.setText(topicName);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(dialogInput)
                .setTitle(topicId == Topic.UNKNOWN_ID? "ADD TOPIC" : "EDIT TOPIC")
                .setMessage("Type in the name of the topic.")
                .setIcon(R.drawable.ic_my_add_24dp)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    if (!dialogInput.getText().toString().isEmpty()) {
                        // add the topic with the dialogInput text from the dialog
                        getPresenter().addUpdateTopicName(topicId, dialogInput.getText().toString());
                    } else {
                        // error when the textfield is free
                        Toast.makeText(this, "ERROR : The textfield was empty, topics need always a name.", Toast.LENGTH_LONG).show();
                    }
                    dialogInput = null;
                })
                .setOnCancelListener(dialog ->
                {
                    getPresenter().updateDialogStatus(false, dialogInput.getText().toString());
                    dialogInput = null;
                })
                .create();

        alertDialog.show();
    }

    public void startTopicActivity(int topicId) {
        Intent intent = new Intent(this, TopicActivity.class);
        intent.putExtra(TopicActivity.TOPIC_EXTRA, topicId);
        startActivity(intent);
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

    private void onSelectionChanged(HashSet<Integer> selection) {
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

        getPresenter().updateSelection(selection);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                getPresenter().deleteSelection(this);
                return true;
            case R.id.action_edit:
                if (topicAdapter.getSelection().size() == 1) {
                    getPresenter().editSelected(this);
                }
                break;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        this.topicAdapter.clearSelection();
        this.actionMode = null;
    }
}
