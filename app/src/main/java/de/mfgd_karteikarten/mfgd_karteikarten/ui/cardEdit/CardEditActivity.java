package de.mfgd_karteikarten.mfgd_karteikarten.ui.cardEdit;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import nucleus.view.NucleusAppCompatActivity;

public class CardEditActivity extends NucleusAppCompatActivity<CardEditPresenter> {

    public static final String CARD_EXTRA = "CARD_EXTRA";
    public static final String DECK_EXTRA = "DECK_EXTRA";
    private EditText questionEdit;
    private EditText answerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_edit);

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(DECK_EXTRA))
        {
            int deckId = extras.getInt(DECK_EXTRA);
            int cardId = extras.getInt(CARD_EXTRA, Card.UNKNOWN_ID);
            setPresenterFactory(new CardEditPresenter.Factory(App.get(this), deckId, cardId));
        }
        else {
            Log.e("CardEditActivity", "gestartet ohne Extra");
            finish();
        }

        questionEdit = (EditText) findViewById(R.id.question_text);
        answerText = (EditText) findViewById(R.id.answer_text);

        Button saveButton = (Button) findViewById(R.id.save_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_button);

        saveButton.setOnClickListener(v -> getPresenter().saveCard(
                questionEdit.getText().toString(),
                answerText.getText().toString()));

        cancelButton.setOnClickListener(v -> closeActivity());
    }

    public void setCard(Card card)
    {
        questionEdit.setText(card.getQuestion());
        answerText.setText(card.getAnswer());
    }

    public void closeActivity()
    {
        finish();
    }

    @Override
    protected void onPause() {
        getPresenter().updateCard(
                questionEdit.getText().toString(),
                answerText.getText().toString());
        super.onPause();
    }
}
