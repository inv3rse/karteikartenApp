package de.mfgd_karteikarten.mfgd_karteikarten.ui.main;

import java.util.ArrayList;

import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;
import io.realm.RealmList;
import nucleus.presenter.Presenter;

public class MainPresenter extends Presenter<MainActivity> {
    private ArrayList<Topic> topics;

    public MainPresenter() {
        topics = new ArrayList<>();
        RealmList<Deck> decks = new RealmList<>();

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

    public void onAddTopicClicked() {
        Topic topic = new Topic("test", new RealmList<>());
        topics.add(topic);

        if (getView() != null) {
            getView().setTopics(topics);
        }
    }

    public void onPositionClicked(int position)
    {

    }
}
