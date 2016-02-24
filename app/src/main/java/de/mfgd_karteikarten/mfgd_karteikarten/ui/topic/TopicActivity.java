package de.mfgd_karteikarten.mfgd_karteikarten.ui.topic;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(TopicPresenter.class)
public class TopicActivity extends NucleusAppCompatActivity<TopicPresenter>
{
    public static final String TOPIC_EXTRA = "TOPIC_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.decklist);
    }
}
