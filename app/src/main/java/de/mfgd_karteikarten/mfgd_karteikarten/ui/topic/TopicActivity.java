package de.mfgd_karteikarten.mfgd_karteikarten.ui.topic;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import nucleus.view.NucleusAppCompatActivity;

public class TopicActivity extends NucleusAppCompatActivity<TopicPresenter>
{
    public static final String TOPIC_EXTRA = "TOPIC_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(TOPIC_EXTRA))
        {
            int topicID = extras.getInt(TOPIC_EXTRA);
            setPresenterFactory(new TopicPresenter.Factory(App.get(this), topicID));
        }
        else
        {
            Log.e("TopicActivity", "gestartet ohne Topic als Extra");
            finish();
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.decklist);
    }
}
