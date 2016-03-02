package de.mfgd_karteikarten.mfgd_karteikarten.ui.cardAsk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

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
    private Button zeigeAntwortButton;
    private Button bewertenButton1;
    private Button bewertenButton2;
    private ImageView imageView;
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
            setPresenterFactory(new CardAskPresenter.Factory(App.get(this), ids, cards));
        } else {
            Log.e("CardaskActivity", "gestartet ohne Cardask als Extra");
            finish();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragenstellen = (EditText) findViewById(R.id.FrageStellen);
        antwortEditText = (EditText) findViewById(R.id.AntwortEdit);
        imageView = (ImageView) findViewById(R.id.smiley);
        zeigeAntwortButton = (Button) findViewById(R.id.zeigAnwortButton);
        bewertenButton1 = (Button) findViewById(R.id.bewertenButton1);
        bewertenButton2 = (Button) findViewById(R.id.bewertenButton2);
        antwortEditText.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.GONE);
        bewertenButton1.setVisibility(View.GONE);
        bewertenButton2.setVisibility(View.GONE);


        bewertenButton1.setOnClickListener(v -> getPresenter().fertigpositv());
        bewertenButton2.setOnClickListener(v -> getPresenter().fertigpositv());

        zeigeAntwortButton.setOnClickListener(v -> getPresenter().zeigeAntwort());
    }

    public void setQuestion(Card card) {
        fragenstellen.setText(card.getQuestion());
    }

    public void setAnswer(Card card) {
        fragenstellen.setText(card.getAnswer());
    }

    public void setAnswerVisible(boolean answerVisible)
    {
        if (answerVisible)
        {
            bewertenButton1.setVisibility(View.VISIBLE);
            bewertenButton2.setVisibility(View.VISIBLE);
            zeigeAntwortButton.setVisibility(View.GONE);
            antwortEditText.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
        }
        else
        {
            bewertenButton1.setVisibility(View.GONE);
            bewertenButton2.setVisibility(View.GONE);
            zeigeAntwortButton.setVisibility(View.VISIBLE);
            antwortEditText.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.GONE);
        }
    }
}