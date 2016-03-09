package de.mfgd_karteikarten.mfgd_karteikarten.ui.cardAsk;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import nucleus.view.NucleusAppCompatActivity;

public class CardAskActivity extends NucleusAppCompatActivity<CardAskPresenter> {

    public static final String CARDASK_EXTRA_DECKS = "CARDASK_EXTRA_DECKS";
    public static final String CARDASK_EXTRA_CARDS = "CARDASK_EXTRA_CARDS";
    public static final String CARDASK_EXTRA_LEARNMODE = "CARDASK_EXTRA_LEARNMODE";
    public static final String KEY_CARD_POSITION = "KEY_CARD_POSITION";
    public static final String KEY_SHOW_ANSWER = "KEY_SHOWANSWER";
    public static final String KEY_SELECTION1 = "KEY_SELECTION1";
    public static final String KEY_SELECTION2 = "KEY_SELECTION2";
    public static final String KEY_SELECTION1_TEXT = "KEY_SELECTION1_TEXT";
    public static final String KEY_SELECTION2_TEXT = "KEY_SELECTION2_TEXT";
    public static final String KEY_EDIT_ANSWER = "KEY_EDIT_ANSWER";

    private TextView frageText;
    private TextView antwortText;
    private Button zeigeAntwortButton;
    private Button bewertenButton1;
    private Button bewertenButton2;
    private TextView antwortHead;
    private TextView vocCorrectTitle;
    private TextView vocCorrectAnswer;
    private EditText vocYourAnswer;
    private RadioButton mcAnswer1;
    private RadioButton mcAnswer2;
    private ImageView mcCorrect1;
    private ImageView mcCorrect2;
    private ImageView mcFalse1;
    private ImageView mcFalse2;
    private Button nextButton;
    private ViewFlipper viewFlipper;
    private TextView number;
    private Toolbar toolbar;
    private int type;
    private String mcCorrectAnswer;
    private boolean gradeCard;
    private Context context;
    private boolean answerVisible;
    private Card card;
    private List<Integer> ids;
    private int position;
    private int rightanswer;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_ask);
        rightanswer = 0;
        count = 0;

        Bundle extras = getIntent().getExtras();

        boolean learnMode = extras.getBoolean(CARDASK_EXTRA_LEARNMODE, true);
        ids = new ArrayList<>();
        ids = extras.getIntegerArrayList(CARDASK_EXTRA_CARDS);
        boolean cards = true;
        if (ids == null) {
            cards = false;
            ids = extras.getIntegerArrayList(CARDASK_EXTRA_DECKS);
        }

        if (ids != null) {
            setPresenterFactory(new CardAskPresenter.Factory(App.get(this), ids, cards, learnMode));
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
        vocCorrectTitle = (TextView) findViewById(R.id.titlecorrectanswer);
        vocCorrectAnswer = (TextView) findViewById(R.id.correctanswer);
        vocYourAnswer = (EditText) findViewById(R.id.vocedittext);
        mcAnswer1 = (RadioButton) findViewById(R.id.answer1);
        mcAnswer2 = (RadioButton) findViewById(R.id.answer2);
        mcCorrect1 = (ImageView) findViewById(R.id.correct1);
        mcCorrect2 = (ImageView) findViewById(R.id.correct2);
        mcFalse1 = (ImageView) findViewById(R.id.false1);
        mcFalse2 = (ImageView) findViewById(R.id.false2);
        nextButton = (Button) findViewById(R.id.nextButton);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipperask);
        number = (TextView) findViewById(R.id.shownumber);

        context = this.getApplicationContext();

        if (savedInstanceState != null) {
            setAnswerVisible(false);
            int i = savedInstanceState.getInt(KEY_CARD_POSITION);
            answerVisible = savedInstanceState.getBoolean(KEY_SHOW_ANSWER);

            card = getPresenter().getSavedCard(i);

            setCard(card);

            vocYourAnswer.setText(savedInstanceState.getString(KEY_EDIT_ANSWER));
            mcAnswer1.setText(savedInstanceState.getString(KEY_SELECTION1_TEXT));
            mcAnswer2.setText(savedInstanceState.getString(KEY_SELECTION2_TEXT));

            if(savedInstanceState.getBoolean(KEY_SELECTION1))
            {
                mcAnswer1.setChecked(true);
            }else if(savedInstanceState.getBoolean(KEY_SELECTION2))
            {
                mcAnswer2.setChecked(true);
            }

            if(answerVisible) {
                setAnswerVisible(true);
                gradeCard();
            }
        }

        bewertenButton1.setOnClickListener(v -> getPresenter().gradeCard(false));
        bewertenButton2.setOnClickListener(v -> getPresenter().gradeCard(true));
        zeigeAntwortButton.setOnClickListener(v -> {
            getPresenter().zeigeAntwort();
            gradeCard();
        });
        nextButton.setOnClickListener(v -> getPresenter().gradeCard(gradeCard));


        ScrollView BG = (ScrollView) findViewById(R.id.scrollview);

        BG.setOnTouchListener(new Gesture(this) {

            public boolean onSwipeRight() {
                //es wurde nach rechts gewischt
                if (View.VISIBLE == antwortText.getVisibility()) {
                    rightanswer = rightanswer +1;
                    count = count +1;
                    Toast.makeText(getApplicationContext(), "you agreed to the answer", Toast.LENGTH_SHORT).show();

                    getPresenter().gradeCard(true);
                    return false;
                }
                return false;
            }

            public boolean onSwipeLeft() {
                //es wurde nach links gewischt
                if (View.VISIBLE == antwortText.getVisibility()) {
                    count = count+1;
                    Toast.makeText(getApplicationContext(), "you disagreed to the answer", Toast.LENGTH_SHORT).show();

                    getPresenter().gradeCard(false);
                    return false;
                }

                return false;
            }
            public boolean onSwipeBottom(){
                //es wurde nach unten gewischt
                if (View.VISIBLE == antwortText.getVisibility()) {
                    Toast.makeText(getApplicationContext(), "swipe to the right = you agree with the statement" +
                            "\nswipe to the left = you disagree with the statement" +
                            "\nswipe down = information", Toast.LENGTH_LONG).show();

                    return false;
                }
                return false;
            }
        });
    }

    public void setCard(Card c) {
        card = c;
        position = getPresenter().getPosition(card);

        List<String> list = new ArrayList<>();

        if(card.getFalseanswer() != null) {
            list.add(card.getAnswer());
            list.add(card.getFalseanswer());

            String s = list.get(new Random().nextInt(list.size()));

            if (s != null) {
                if (s.equals(card.getAnswer())) {
                    mcAnswer1.setText(card.getAnswer());
                    mcAnswer2.setText(card.getFalseanswer());
                } else {
                    mcAnswer1.setText(card.getFalseanswer());
                    mcAnswer2.setText(card.getAnswer());
                }
            }
        }

            frageText.setText(card.getQuestion());
            antwortText.setText(card.getAnswer());
            vocCorrectAnswer.setText(card.getAnswer());
            mcCorrectAnswer = card.getAnswer();
            type = card.getType();

        if (type == 3) {
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.mcviewquestion)));
        } else if (type == 2) {
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.vocviewquestion)));
        } else if (type == 1 || type == 0) {
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.defaultview)));
        }
    }
    public int getRightanswer(){
        return rightanswer;
    }

    public void setAnswerVisible(boolean answerVisib) {
        answerVisible = answerVisib;
        if (answerVisible && type <= 1) {
            bewertenButton1.setVisibility(View.VISIBLE);
            bewertenButton2.setVisibility(View.VISIBLE);
            zeigeAntwortButton.setVisibility(View.GONE);
            antwortText.setVisibility(View.VISIBLE);
            antwortHead.setVisibility(View.VISIBLE);
            number.setText(Integer.toString(rightanswer) + " of " + Integer.toString(count)+" cards were right.");
            number.setVisibility(View.VISIBLE);

        }else if(answerVisible && type == 2)
        {
            nextButton.setVisibility(View.VISIBLE);
            zeigeAntwortButton.setVisibility(View.GONE);
            vocCorrectTitle.setVisibility(View.VISIBLE);
            vocCorrectAnswer.setVisibility(View.VISIBLE);
            number.setText(Integer.toString(rightanswer) + " of " + Integer.toString(count)+" cards were right.");
            number.setVisibility(View.VISIBLE);
            vocYourAnswer.setEnabled(false);
        }else if(answerVisible && type == 3)
        {
            nextButton.setVisibility(View.VISIBLE);
            number.setText(Integer.toString(rightanswer) + " of " + Integer.toString(count)+" cards were right.");
            number.setVisibility(View.VISIBLE);
            zeigeAntwortButton.setVisibility(View.GONE);
            mcAnswer1.setEnabled(false);
            mcAnswer1.setFocusable(false);
            mcAnswer2.setEnabled(false);
            mcAnswer2.setFocusable(false);

        }else
        {
            bewertenButton1.setVisibility(View.GONE);
            bewertenButton2.setVisibility(View.GONE);
            zeigeAntwortButton.setVisibility(View.VISIBLE);
            antwortText.setVisibility(View.INVISIBLE);
            number.setVisibility(View.INVISIBLE);
            antwortHead.setVisibility(View.INVISIBLE);
            vocCorrectTitle.setVisibility(View.INVISIBLE);
            vocCorrectAnswer.setVisibility(View.INVISIBLE);
            vocYourAnswer.setEnabled(true);
            vocYourAnswer.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            vocYourAnswer.setText(null);
            mcCorrect1.setVisibility(View.INVISIBLE);
            mcCorrect2.setVisibility(View.INVISIBLE);
            mcFalse1.setVisibility(View.INVISIBLE);
            mcFalse2.setVisibility(View.INVISIBLE);
            mcAnswer1.setEnabled(true);
            mcAnswer2.setEnabled(true);
            mcAnswer1.setChecked(false);
            mcAnswer2.setChecked(false);
            mcAnswer1.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            mcAnswer2.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));

            nextButton.setVisibility(View.GONE);

            number.setText(Integer.toString(rightanswer) + " of " + Integer.toString(count)+" cards were right.");
            number.setVisibility(View.VISIBLE);

        }
    }

    public void gradeCard()
    {
        if(type == 2)
        {
            gradeCardVoc();
        }else if (type == 3)
        {
            gradeCardMc();
        }
    }

    public void gradeCardVoc()
    {
        if(vocYourAnswer.getText() != null) {
            String yAnswer = vocYourAnswer.getText().toString();
            String cAnswer = vocCorrectAnswer.getText().toString();
            gradeCard = yAnswer.equals(cAnswer);
            if(gradeCard)
            {
                rightanswer = rightanswer +1;
                count = count + 1;

                vocYourAnswer.setTextColor(ContextCompat.getColor(context, R.color.colorAnswerCorrect));
            }else
            {
                count = count + 1;
                vocYourAnswer.setTextColor(ContextCompat.getColor(context, R.color.colorAnswerFalse));
            }
        }else
        {
            count = count + 1;
            gradeCard = false;
        }
    }

    public void gradeCardMc()
    {
        String s;
        if(mcAnswer1.isChecked())
        {
            s = mcAnswer1.getText().toString();
            gradeCard = s.equals(mcCorrectAnswer);

            if(gradeCard)
            {
                rightanswer = rightanswer +1;
                count = count + 1;
                mcAnswer1.setTextColor(ContextCompat.getColor(context, R.color.colorAnswerCorrect));
                mcCorrect1.setVisibility(View.VISIBLE);
            }else
            {
                count = count + 1;
                mcAnswer1.setTextColor(ContextCompat.getColor(context, R.color.colorAnswerFalse));
                mcFalse1.setVisibility(View.VISIBLE);
                mcCorrect2.setVisibility(View.VISIBLE);
            }
        }else if(mcAnswer2.isChecked())
        {
            s = mcAnswer2.getText().toString();
            gradeCard = s.equals(mcCorrectAnswer);
            if(gradeCard)
            {
                rightanswer = rightanswer +1;
                count = count + 1;
                mcAnswer2.setTextColor(ContextCompat.getColor(context, R.color.colorAnswerCorrect));
                mcCorrect2.setVisibility(View.VISIBLE);
            }else
            {
                count = count + 1;
                mcAnswer2.setTextColor(ContextCompat.getColor(context, R.color.colorAnswerFalse));
                mcFalse2.setVisibility(View.VISIBLE);
                mcCorrect1.setVisibility(View.VISIBLE);
            }
        }else {
            s = mcAnswer1.getText().toString();
            gradeCard = false;
            count = count + 1;
            if(s.equals(mcCorrectAnswer))
            {
                mcCorrect1.setVisibility(View.VISIBLE);
            }else
            {
                mcCorrect2.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onPause(){
        getPresenter().onSavedPause(true);
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putInt(KEY_CARD_POSITION, position);
        outState.putBoolean(KEY_SHOW_ANSWER, answerVisible);
        outState.putBoolean(KEY_SELECTION1, mcAnswer1.isChecked());
        outState.putBoolean(KEY_SELECTION2, mcAnswer2.isChecked());
        outState.putString(KEY_SELECTION1_TEXT, mcAnswer1.getText().toString());
        outState.putString(KEY_SELECTION2_TEXT, mcAnswer2.getText().toString());
        outState.putString(KEY_EDIT_ANSWER, vocYourAnswer.getText().toString());

        super.onSaveInstanceState(outState);
    }

    public void showFinishedDialog() {
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setTitle("Alert");
        msg.setMessage("No more cards!  "+Integer.toString(rightanswer)+"/"+Integer.toString(count));
        msg.setCancelable(true);
        msg.setPositiveButton(android.R.string.ok, (dialog, which) -> this.finish());
        msg.create().show();
    }
}