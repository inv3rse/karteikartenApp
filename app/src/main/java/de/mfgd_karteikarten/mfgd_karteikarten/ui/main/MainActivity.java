package de.mfgd_karteikarten.mfgd_karteikarten.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;
import de.mfgd_karteikarten.mfgd_karteikarten.ui.topic.TopicActivity;
import nucleus.view.NucleusAppCompatActivity;

public class MainActivity extends NucleusAppCompatActivity<MainPresenter> {

    private TopicAdapter topicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setPresenterFactory(new MainPresenter.Factory(App.get(this)));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        topicAdapter = new TopicAdapter(this);
        topicAdapter.setOnItemClickedListener(position -> getPresenter().onPositionClicked(position));

        RecyclerView topicGrid = (RecyclerView) findViewById(R.id.topic_grid);
        topicGrid.setLayoutManager(new GridLayoutManager(this, 2));
        topicGrid.setAdapter(topicAdapter);

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.fab);
        addButton.setOnClickListener(view -> getPresenter().onAddTopicClicked());
    }

    public void setTopics(List<Topic> topics) {
        topicAdapter.setTopics(topics);
    }

    public void addTopic(Topic topic) {
        topicAdapter.addTopic(topic);
    }

    public void startTopicActivity(int topicId) {
        Intent intent = new Intent(this, TopicActivity.class);
        intent.putExtra(TopicActivity.TOPIC_EXTRA, topicId);
        startActivity(intent);
    }
}
