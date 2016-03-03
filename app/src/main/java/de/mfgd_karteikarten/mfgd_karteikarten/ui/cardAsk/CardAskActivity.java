package de.mfgd_karteikarten.mfgd_karteikarten.ui.cardAsk;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import nucleus.view.NucleusAppCompatActivity;

public class CardAskActivity extends NucleusAppCompatActivity<CardAskPresenter> {

    public static final String CARDASK_EXTRA_DECKS = "CARDASK_EXTRA_DECKS";
    public static final String CARDASK_EXTRA_CARDS = "CARDASK_EXTRA_CARDS";
    private TextView frageText;
    private TextView antwortText;
    private Button zeigeAntwortButton;
    private Button bewertenButton1;
    private Button bewertenButton2;
    private TextView antwortHead;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_ask);

        Bundle extras = getIntent().getExtras();

        ArrayList<Integer> ids = extras.getIntegerArrayList(CARDASK_EXTRA_CARDS);
        boolean cards = true;
        if (ids == null) {
            cards = false;
            ids = extras.getIntegerArrayList(CARDASK_EXTRA_DECKS);
        }

        if (ids != null) {
            setPresenterFactory(new CardAskPresenter.Factory(App.get(this), ids, cards, false));
        } else {
            Log.e("CardaskActivity", "gestartet ohne Cardask als Extra");
            finish();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frageText = (TextView) findViewById(R.id.FrageStellen);
        antwortText = (TextView) findViewById(R.id.AntwortEdit);
        zeigeAntwortButton = (Button) findViewById(R.id.zeigAnwortButton);
        bewertenButton1 = (Button) findViewById(R.id.bewertenButton1);
        bewertenButton2 = (Button) findViewById(R.id.bewertenButton2);
        antwortHead = (TextView) findViewById(R.id.AntwortText);

        bewertenButton1.setOnClickListener(v -> getPresenter().gradeCard(false));
        bewertenButton2.setOnClickListener(v -> getPresenter().gradeCard(true));
        zeigeAntwortButton.setOnClickListener(v -> getPresenter().zeigeAntwort());
    }

    public void setCard(Card card)
    {
        frageText.setText(card.getQuestion());
        antwortText.setText(card.getAnswer());
    }

    public void setAnswerVisible(boolean answerVisible) {
        if (answerVisible) {
            bewertenButton1.setVisibility(View.VISIBLE);
            bewertenButton2.setVisibility(View.VISIBLE);
            zeigeAntwortButton.setVisibility(View.GONE);
            antwortText.setVisibility(View.VISIBLE);
            antwortHead.setVisibility(View.VISIBLE);

        } else {
            bewertenButton1.setVisibility(View.GONE);
            bewertenButton2.setVisibility(View.GONE);
            zeigeAntwortButton.setVisibility(View.VISIBLE);
            antwortText.setVisibility(View.INVISIBLE);
            antwortHead.setVisibility(View.INVISIBLE);
        }
    }
    public void showFinishedDialog(){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setTitle("Meldung");
        msg.setMessage("No more card!");
        msg.setCancelable(true);
        msg.setPositiveButton(android.R.string.ok, (dialog, which) -> this.finish());
        msg.create().show();
    }
}