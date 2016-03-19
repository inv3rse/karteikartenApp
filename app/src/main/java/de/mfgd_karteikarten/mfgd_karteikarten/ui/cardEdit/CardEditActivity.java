package de.mfgd_karteikarten.mfgd_karteikarten.ui.cardEdit;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ViewFlipper;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import nucleus.view.NucleusAppCompatActivity;

public class CardEditActivity extends NucleusAppCompatActivity<CardEditPresenter>
{

    public static final String CARD_EXTRA = "CARD_EXTRA";
    public static final String DECK_EXTRA = "DECK_EXTRA";
    private EditText questionEdit;
    private EditText answerText;
    private EditText falseAnswerText;
    private EditText correctAnswerText;
    private ViewFlipper viewFlipper;
    private RadioButton defaultView;
    private RadioButton vocabView;
    private RadioButton mcView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_edit);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(DECK_EXTRA))
        {
            int deckId = extras.getInt(DECK_EXTRA);
            int cardId = extras.getInt(CARD_EXTRA, Card.UNKNOWN_ID);
            setPresenterFactory(new CardEditPresenter.Factory(App.get(this), deckId, cardId));
        } else
        {
            Log.e("CardEditActivity", "gestartet ohne Extra");
            finish();
        }

        questionEdit = (EditText) findViewById(R.id.question_text);
        answerText = (EditText) findViewById(R.id.answer_text);
        falseAnswerText = (EditText) findViewById(R.id.answer_wrong);
        correctAnswerText = (EditText) findViewById(R.id.answer_correct);

        Button saveButton = (Button) findViewById(R.id.save_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_button);

        defaultView = (RadioButton) findViewById(R.id.defaultRButton);
        vocabView = (RadioButton) findViewById(R.id.vocabRButton);
        mcView = (RadioButton) findViewById(R.id.mcRButton);

        defaultView.setChecked(true);

        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);

        saveButton.setOnClickListener(v -> saveCard());

        cancelButton.setOnClickListener(v -> closeActivity());

        defaultView.setOnClickListener(v -> switchView());

        vocabView.setOnClickListener(v -> switchView());

        mcView.setOnClickListener(v -> switchView());
    }

    public void switchView()
    {
        if (mcView.isChecked())
        {
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.mcview)));
        } else
        {
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.defaultvocview)));
        }
    }

    public void setCard(Card card)
    {
        if (card.getType() == 3)
        {
            questionEdit.setText(card.getQuestion());
            correctAnswerText.setText(card.getAnswer());
            falseAnswerText.setText(card.getFalseanswer());
            mcView.setChecked(true);
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.mcview)));
        } else if (card.getType() == 2)
        {
            questionEdit.setText(card.getQuestion());
            answerText.setText(card.getAnswer());
            vocabView.setChecked(true);
        } else
        {
            questionEdit.setText(card.getQuestion());
            answerText.setText(card.getAnswer());
            defaultView.setChecked(true);
        }
    }

    public void saveCard()
    {
        int type;
        if (mcView.isChecked())
        {
            type = 3;
            getPresenter().saveCard(
                    type,
                    questionEdit.getText().toString(),
                    correctAnswerText.getText().toString(),
                    falseAnswerText.getText().toString());
        } else if (vocabView.isChecked())
        {
            type = 2;
            getPresenter().saveCard(
                    type,
                    questionEdit.getText().toString(),
                    answerText.getText().toString());
        } else
        {
            type = 1;
            getPresenter().saveCard(
                    type,
                    questionEdit.getText().toString(),
                    answerText.getText().toString()
            );
        }
    }

    public void updateCard()
    {
        int type;
        if (mcView.isChecked())
        {
            type = 3;
            getPresenter().updateCard(
                    type,
                    questionEdit.getText().toString(),
                    correctAnswerText.getText().toString(),
                    falseAnswerText.getText().toString());
        } else if (vocabView.isChecked())
        {
            type = 2;
            getPresenter().updateCard(
                    type,
                    questionEdit.getText().toString(),
                    answerText.getText().toString());
        } else
        {
            type = 1;
            getPresenter().updateCard(
                    type,
                    questionEdit.getText().toString(),
                    answerText.getText().toString()
            );
        }
    }

    public void closeActivity()
    {
        finish();
    }

    @Override
    protected void onPause()
    {
        updateCard();
        super.onPause();
    }
}
