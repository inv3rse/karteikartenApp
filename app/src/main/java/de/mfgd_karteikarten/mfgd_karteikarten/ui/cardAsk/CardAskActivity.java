package de.mfgd_karteikarten.mfgd_karteikarten.ui.cardAsk;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import nucleus.view.NucleusAppCompatActivity;

public class CardAskActivity extends NucleusAppCompatActivity<CardAskPresenter> {

    public static final String CARDASK_EXTRA_DECKS = "CARDASK_EXTRA_DECKS";
    public static final String CARDASK_EXTRA_CARDS = "CARDASK_EXTRA_CARDS";
    private EditText fragenstellen;
    private EditText antwortEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_ask);

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(CARDASK_EXTRA_CARDS)) {
            ArrayList<Integer> cards = extras.getIntegerArrayList(CARDASK_EXTRA_CARDS);
            setPresenterFactory(new CardAskPresenter.Factory(App.get(this), cards));
        } else {
            Log.e("CardaskActivity", "gestartet ohne Cardask als Extra");
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragenstellen = (EditText) findViewById(R.id.FrageStellen);
        antwortEditText = (EditText) findViewById(R.id.AntwortEdit);
        Button zeigeAntowrt = (Button) findViewById(R.id.zeigAntowrtButton);
        Button bewertenButton1 = (Button) findViewById(R.id.bewertenButton1);
        Button bewertenButton2 = (Button) findViewById(R.id.bewertenButton2);


        antwortEditText.setVisibility(View.VISIBLE);

        bewertenButton1.setOnClickListener(v -> getPresenter().fertigpositv());

    }

    public void setQuestion(Card card) {
        fragenstellen.setText(card.getQuestion());
    }
    public void setAnswer(Card card) {
        fragenstellen.setText(card.getAnswer());
    }

}