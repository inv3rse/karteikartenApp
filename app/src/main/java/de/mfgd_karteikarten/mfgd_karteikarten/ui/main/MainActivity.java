package de.mfgd_karteikarten.mfgd_karteikarten.ui.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.GridView;

import java.util.List;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(MainPresenter.class)
public class MainActivity extends NucleusAppCompatActivity<MainPresenter> {

    private GridView topicGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topicGrid = (GridView) findViewById(R.id.topic_grid);
        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.fab);

        addButton.setOnClickListener(view -> getPresenter().onAddTopicClicked());
    }

    public void setTopics(List<Topic> topics)
    {
        topicGrid.setAdapter(new TopicAdapter(topics, this));
    }
}
