package de.mfgd_karteikarten.mfgd_karteikarten.ui.cardAsk;

import java.util.List;

import javax.inject.Inject;

import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.base.learn.LearnInterface;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;
import nucleus.factory.PresenterFactory;
import nucleus.presenter.Presenter;


@ActivityScope
public class CardAskPresenter extends Presenter<CardAskActivity> {

    private LearnInterface learnAssistant;
    private Card card;
    private boolean isAnswerVisible;

    @Inject
    public CardAskPresenter(LearnInterface learnAssistant) {
        this.learnAssistant = learnAssistant;
        isAnswerVisible = false;

        if (learnAssistant.hasNextCard()) {
            this.card = learnAssistant.getNextCard();
        } else {
            card = null;

        }
    }

    @Override
    protected void onTakeView(CardAskActivity cardAskActivity) {

        if (card != null) {
            cardAskActivity.setQuestion(card);
            cardAskActivity.setAnswer(card);
            cardAskActivity.setAnswerVisible(isAnswerVisible);
        } else {
            cardAskActivity.showFinishedDialog();
        }

    }

    public void gradeCard(boolean positive) {
        CardAskActivity cardAskActivity = getView();
        isAnswerVisible = false;
        learnAssistant.gradeCurrentCard(positive);

        if (learnAssistant.hasNextCard()) {
            card = learnAssistant.getNextCard();
            if (cardAskActivity != null) {
                cardAskActivity.setQuestion(card);
                cardAskActivity.setAnswer(card);
                cardAskActivity.setAnswerVisible(false);
            }
        } else {
            card = null;
            if (cardAskActivity != null) {
                cardAskActivity.showFinishedDialog();
            }
        }
    }

    public void zeigeAntwort() {
        isAnswerVisible = true;
        CardAskActivity view = getView();
        if (view != null) {
            view.setAnswerVisible(true);
        }
    }


    public static class Factory implements PresenterFactory<CardAskPresenter> {
        private App app;
        private List<Integer> ids;
        private boolean decks;
        private boolean testMode;

        public Factory(App app, List<Integer> ids, boolean decks, boolean testMode) {
            this.app = app;
            this.ids = ids;
            this.testMode = testMode;
        }

        @Override
        public CardAskPresenter createPresenter() {
            return DaggerCardAskComponent.builder()
                    .appComponent(app.component())
                    .cardAskModule(new CardAskModule(ids, decks, testMode))
                    .build()
                    .getCardAskPresenter();
        }
    }
}
