package de.mfgd_karteikarten.mfgd_karteikarten.ui.main;

import java.util.ArrayList;

import de.mfgd_karteikarten.mfgd_karteikarten.base.mvp.BasePresenter;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;

public class MainPresenter extends BasePresenter<MainActivity>
{
    private ArrayList<Topic> topics;

    public MainPresenter()
    {
        topics = new ArrayList<>();
        ArrayList<Deck> decks = new ArrayList<>();

        for (int i = 1; i < 4; ++i) {
            Deck deck = new Deck("deck" + i);
            decks.add(deck);
        }

        Topic topic = new Topic("test", decks);
        Topic topic2 = new Topic("abcde", decks);
        topics.add(topic);
        topics.add(topic2);
    }

    @Override
    protected void onTakeView(MainActivity view) {
        view.setTopics(topics);
    }

    public void onAddTopicClicked()
    {
        Topic topic = new Topic("test", new ArrayList<>());
        topics.add(topic);

        if(hasView())
        {
            getView().setTopics(topics);
        }
    }
}
