package de.mfgd_karteikarten.mfgd_karteikarten.ui.main;

import android.content.Intent;

import java.util.ArrayList;

import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;
import de.mfgd_karteikarten.mfgd_karteikarten.ui.topic.TopicActivity;
import nucleus.presenter.Presenter;

public class MainPresenter extends Presenter<MainActivity>
{
    private ArrayList<Topic> topics;

    public MainPresenter()
    {
        topics = new ArrayList<>();
        ArrayList<Deck> decks = new ArrayList<>();

        for (int i = 1; i < 5; ++i) {
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

    public void onPositionClicked(int position)
    {
        MainActivity view = getView();

        if (view != null)
        {
            Topic topic = topics.get(position);

            Intent intent = new Intent(view, TopicActivity.class);
            intent.putExtra(TopicActivity.TOPIC_EXTRA, topic.getTitle());
            getView().startActivity(intent);
        }
    }

    public void onAddTopicClicked()
    {
        ArrayList<Deck> decks = new ArrayList<>();

        for (int i = 1; i < 5; ++i) {
            Deck deck = new Deck("deck" + i);
            decks.add(deck);
        }

        Topic topic = new Topic("test", decks);
        topics.add(topic);

        if(getView() != null)
        {
            getView().setTopics(topics);
        }
    }
}
