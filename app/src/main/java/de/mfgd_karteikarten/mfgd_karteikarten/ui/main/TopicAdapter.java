package de.mfgd_karteikarten.mfgd_karteikarten.ui.main;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;

public class TopicAdapter extends BaseAdapter {

    private List<Topic> topics;
    private Context context;

    public TopicAdapter(List<Topic> topics, Context context)
    {
        this.topics = topics;
        this.context = context;
    }

    @Override
    public int getCount() {
        return topics.size();
    }

    @Override
    public Object getItem(int position) {
        return topics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Topic topic = topics.get(position);
        View topicView;
        if (convertView != null)
        {
            topicView = convertView;
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) context.
                                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            topicView = inflater.inflate(R.layout.topic_grid_item, parent, false);
        }

        TextView topicHeader = (TextView) topicView.findViewById(R.id.topic_header);
        LinearLayout topicLayout = (LinearLayout) topicView.findViewById(R.id.deck_layout);

        topicLayout.removeAllViewsInLayout();

        topicHeader.setText(topic.getName());
        for(Deck deck : topic.getDecks())
        {
            Button deckButton = new Button(context);
            deckButton.setText(deck.getName());

            topicLayout.addView(deckButton);
        }

        return topicView;
    }
}
