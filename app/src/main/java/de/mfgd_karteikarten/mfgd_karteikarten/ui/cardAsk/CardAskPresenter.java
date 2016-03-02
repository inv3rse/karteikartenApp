package de.mfgd_karteikarten.mfgd_karteikarten.ui.cardAsk;

import java.util.List;

import javax.inject.Inject;

import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import de.mfgd_karteikarten.mfgd_karteikarten.base.LearnAssistant;
import nucleus.factory.PresenterFactory;
import nucleus.presenter.Presenter;


@ActivityScope
public class CardAskPresenter extends Presenter<CardAskActivity> {

    private LearnAssistant learnAssistant;
    private Card card;
    private boolean isAnswerVisible;

    @Inject
    public CardAskPresenter(LearnAssistant learnAssistant)
    {
        this.learnAssistant = learnAssistant;
        isAnswerVisible = false;

        card = new Card();
        card.setQuestion("eine frage");
        card.setAnswer("eine antwort");
        if (learnAssistant.hasNextCard()) {
            this.card = learnAssistant.getNextCard();
        }
    }

    @Override
    protected void onTakeView(CardAskActivity cardAskActivity) {

        cardAskActivity.setQuestion(card);
        cardAskActivity.setAnswer(card);

        cardAskActivity.setAnswerVisible(isAnswerVisible);
    }

    public void fertigpositv()
    {
        isAnswerVisible = false;
        learnAssistant.gradePositive();
        if (learnAssistant.hasNextCard())
        {
            card = learnAssistant.getNextCard();
            CardAskActivity cardAskActivity = getView();
            if (cardAskActivity != null)
            {
                cardAskActivity.setQuestion(card);
                cardAskActivity.setAnswer(card);
                cardAskActivity.setAnswerVisible(false);
            }
        }
    }

    public void fertignegativ()
    {
        isAnswerVisible = false;
        learnAssistant.bewerteNegativ();
        if (learnAssistant.hasNextCard())
        {
                card = learnAssistant.getNextCard();
                CardAskActivity cardAskActivity = getView();
                if (cardAskActivity != null)
                {
                    cardAskActivity.setQuestion(card);
                    cardAskActivity.setAnswer(card);
                    cardAskActivity.setAnswerVisible(false);
            }
        }
    }

    public void zeigeAntwort()
    {
        isAnswerVisible = true;
        CardAskActivity view = getView();
        if (view != null)
        {
            view.setAnswerVisible(true);
        }
    }


    public static class Factory implements PresenterFactory<CardAskPresenter>
    {
        private App app;
        private List<Integer> ids;
        private boolean decks;

        public Factory(App app, List<Integer> ids, boolean decks)
        {
            this.app = app;
            this.ids = ids;
        }

        @Override
        public CardAskPresenter createPresenter()
        {
            return DaggerCardAskComponent.builder()
                    .appComponent(app.component())
                    .cardAskModul(new CardAskModul(ids, decks))
                    .build()
                    .getCardAskPresenter();
        }
    }
}
