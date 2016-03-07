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
        }
    }

    @Override
    public void save(Bundle state) {
        state.putString(KEY_ANSWER, card.getAnswer());
        state.putString(KEY_QUESTION, card.getQuestion());
        state.putString(KEY_FALSEANSWER, card.getFalseanswer());
    }

    @Override
    protected void onTakeView(CardEditActivity cardEditActivity) {
        cardEditActivity.setCard(card);
    }

    public void updateCard(String question, String answer)
    {
        card.setQuestion(question);
        card.setAnswer(answer);
    }

    public void saveCard(String question, String answer)
    {
        updateCard(question, answer);
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
