package de.mfgd_karteikarten.mfgd_karteikarten.ui.cardEdit;

import android.os.Bundle;

import javax.inject.Inject;

import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.base.db.DeckEditor;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import nucleus.factory.PresenterFactory;
import nucleus.presenter.Presenter;

@ActivityScope
public class CardEditPresenter extends Presenter<CardEditActivity> {

    private static final String KEY_QUESTION = "KEY_QUESTION";
    private static final String KEY_ANSWER = "KEY_ANSWER";
    private static final String KEY_FALSEANSWER = "KEY_FALSEANSWER";
    private static final String KEY_TYPE = "KEY_TYPE";

    private DeckEditor deckEditor;
    private Card card;

    @Inject
    public CardEditPresenter(DeckEditor editor, Card card)
    {
        this.deckEditor = editor;
        this.card = card;
    }

    @Override
    protected void onCreate(Bundle savedState) {
        if (savedState != null)
        {
            card.setQuestion(savedState.getString(KEY_QUESTION));
            card.setAnswer(savedState.getString(KEY_ANSWER));
            card.setFalseanswer(savedState.getString(KEY_FALSEANSWER));
            card.setType(savedState.getInt(KEY_TYPE));
        }
    }

    @Override
    public void save(Bundle state) {
        state.putString(KEY_ANSWER, card.getAnswer());
        state.putString(KEY_QUESTION, card.getQuestion());
        state.putString(KEY_FALSEANSWER, card.getFalseanswer());
        state.putInt(KEY_TYPE, card.getType());
    }

    @Override
    protected void onTakeView(CardEditActivity cardEditActivity) {
        cardEditActivity.setCard(card);
    }

    public void updateCard(int type, String question, String answer, String falseAnswer)
    {
        card.setType(type);
        card.setQuestion(question);
        card.setAnswer(answer);
        card.setFalseanswer(falseAnswer);
    }

    public void updateCard(int type, String question, String answer)
    {
        card.setType(type);
        card.setQuestion(question);
        card.setAnswer(answer);
    }

    public void saveCard(int type, String question, String answer)
    {
        updateCard(type, question, answer);
        if (card.getID() != Card.UNKNOWN_ID)
        {
            deckEditor.setCard(card);
        }
        else
        {
            deckEditor.addCard(card);
        }

        CardEditActivity view = getView();
        if (view != null)
        {
            view.closeActivity();
        }
    }

    public void saveCard(int type, String question, String answer, String falseanswer)
    {
        updateCard(type, question, answer, falseanswer);
        if (card.getID() != Card.UNKNOWN_ID)
        {
            deckEditor.setCard(card);
        }
        else
        {
            deckEditor.addCard(card);
        }

        CardEditActivity view = getView();
        if (view != null)
        {
            view.closeActivity();
        }
    }



    static class Factory implements PresenterFactory<CardEditPresenter>
    {
        private App app;
        private int deckId;
        private int cardId;

        public Factory(App app, int deckId, int cardId)
        {
            this.app = app;
            this.deckId = deckId;
            this.cardId = cardId;
        }

        @Override
        public CardEditPresenter createPresenter() {
            return DaggerCardEditComponent.builder()
                    .appComponent(app.component())
                    .cardEditModule(new CardEditModule(deckId, cardId))
                    .build().getCardEditPresenter();
        }
    }
}
