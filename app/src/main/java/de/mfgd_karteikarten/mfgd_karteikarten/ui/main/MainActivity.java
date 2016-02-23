package de.mfgd_karteikarten.mfgd_karteikarten.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Deck> decks = new ArrayList<>();

        for (int i = 1; i < 4; ++i)
        {
            Deck deck = new Deck("deck" + i);
            decks.add(deck);
        }

        Topic topic = new Topic("test", decks);
        Topic topic2 = new Topic("abcde", decks);
        ArrayList<Topic> topics = new ArrayList<>();
        topics.add(topic);
        topics.add(topic2);

        TopicAdapter topicAdapter = new TopicAdapter(topics, this);

        GridView topicGrid = (GridView) findViewById(R.id.topic_grid);
        topicGrid.setAdapter(topicAdapter);

    }
}
