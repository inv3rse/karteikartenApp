package de.mfgd_karteikarten.mfgd_karteikarten.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;

import java.util.List;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;
import de.mfgd_karteikarten.mfgd_karteikarten.ui.topic.TopicActivity;
import nucleus.view.NucleusAppCompatActivity;

public class MainActivity extends NucleusAppCompatActivity<MainPresenter> {

    private GridView topicGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setPresenterFactory(new MainPresenter.Factory(App.get(this)));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        topicGrid = (GridView) findViewById(R.id.topic_grid);
        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.fab);

        addButton.setOnClickListener(view -> getPresenter().onAddTopicClicked());
        topicGrid.setOnItemClickListener((parent, view, position, id) -> getPresenter().onPositionClicked(position));
    }

    public void setTopics(List<Topic> topics)
    {
        topicGrid.setAdapter(new TopicAdapter(topics, this));
    }

    public void startTopicActivity(int topicId)
    {
        Intent intent = new Intent(this, TopicActivity.class);
        intent.putExtra(TopicActivity.TOPIC_EXTRA, topicId);
        startActivity(intent);
    }
}
