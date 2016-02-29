package de.mfgd_karteikarten.mfgd_karteikarten.ui.cardAsk;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.base.LearnAssistant;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import de.mfgd_karteikarten.mfgd_karteikarten.ui.main.DaggerMainComponent;
import de.mfgd_karteikarten.mfgd_karteikarten.ui.topic.DeckAdapter;
import de.mfgd_karteikarten.mfgd_karteikarten.ui.topic.TopicPresenter;
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
        cardAskActivity.setQuestion(learnAssistant.getNextCard());
    }

    public void fertigpositv()
    {
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
        private List<Integer> cards;

        public Factory(App app, List<Integer> cards)
        {
            this.app = app;
        }

        @Override
        public CardAskPresenter createPresenter()
        {
            return DaggerCardAskComponent.builder()
                    .appComponent(app.component())
                    .cardAskModul(new CardAskModul(cards))
                    .build()
                    .getCardAskPresenter();
        }
    }
}
