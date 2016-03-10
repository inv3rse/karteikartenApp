package de.mfgd_karteikarten.mfgd_karteikarten.ui.importDeck;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;
import nucleus.view.NucleusAppCompatActivity;

public class ImportActivity extends NucleusAppCompatActivity<ImportPresenter> {

    private ProgressDialog loadingDialog;
    private Spinner topicSpinner;
    private TextView deckName;
    private TextView num_cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String importUrl = getIntent().getData().toString();
        if (importUrl == null)
        {
            Log.e("ImportActivity", "url was null");
            finish();
        }
        setPresenterFactory(new ImportPresenter.Factory(App.get(this), importUrl));

        setContentView(R.layout.activity_import);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        topicSpinner = (Spinner) findViewById(R.id.topic_select_spinner);
        topicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getPresenter().onTopicSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button okButton = (Button) findViewById(R.id.ok_button);
        okButton.setOnClickListener(v -> getPresenter().onOk(this));

        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> getPresenter().onCancel(this));

        num_cards = (TextView) findViewById(R.id.deck_num_cards);
        deckName = (TextView) findViewById(R.id.deck_name);
    }

    public void showLoading(boolean loading)
    {
        if (loading)
        {
            if (loadingDialog == null)
            {
                loadingDialog = ProgressDialog.show(this, "loading", "loading deck from Server");
            }
        }
        else if (loadingDialog != null)
        {
            loadingDialog.cancel();
        }
    }

    public void setDeck(Deck deck)
    {
        deckName.setText(deck.getName());
        num_cards.setText(getString(R.string.num_cards, deck.getCards().size()));
    }

    public void setTopicOptions(List<Topic> topics, int selection)
    {
        String topicList[] = new String[topics.size()];
        for (int i = 0; i < topics.size(); ++i)
        {
            topicList[i] = topics.get(i).getName();
        }

        topicSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, topicList));
        topicSpinner.setSelection(selection);
    }

    public void closeActivity()
    {
        finish();
    }

}
