package de.mfgd_karteikarten.mfgd_karteikarten.ui.cardAsk;

import java.util.List;

import javax.inject.Inject;

import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.base.LearnAssistant;
import nucleus.factory.PresenterFactory;
import nucleus.presenter.Presenter;

@ActivityScope
public class CardAskPresenter extends Presenter<CardAskActivity> {

    LearnAssistant learnAssistant;

    @Inject
    public CardAskPresenter(LearnAssistant learnAssistant)
    {
        this.learnAssistant = learnAssistant;
    }

    @Override
    protected void onTakeView(CardAskActivity cardAskActivity) {
        if (learnAssistant.hasNextCard())
        {
            cardAskActivity.setQuestion(learnAssistant.getNextCard());
        }
    }

    public void fertigpositv()
    {
        learnAssistant.gradePositive();
        if (learnAssistant.hasNextCard())
        {
            CardAskActivity cardAskActivity = getView();
            if (cardAskActivity != null)
            {
                cardAskActivity.setQuestion(learnAssistant.getNextCard());
            }
        }
    }

    public void fertignegativ()
    {
        learnAssistant.gradePositive();
        if (learnAssistant.hasNextCard())
        {
            CardAskActivity cardAskActivity = getView();
            if (cardAskActivity != null)
            {
                cardAskActivity.setQuestion(learnAssistant.getNextCard());
            }
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
