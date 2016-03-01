package de.mfgd_karteikarten.mfgd_karteikarten.ui.main;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {

    private Context context;
    private List<Topic> topics;

    private OnItemClicked onItemClickedListener;
    private OnSelectionChanged onSelectionChangedListener;

    public TopicAdapter(Context context) {
        this.context = context;
        this.topics = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.topic_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Topic topic = topics.get(position);

        holder.topicName.setText(topic.getName());
        holder.deckLayout.removeAllViews();

        for (int i = 0; i < Math.min(topic.getDecks().size(), 3); ++i) {
            Button button = new Button(context);
            button.setText(topic.getDecks().get(i).getName());

            holder.deckLayout.addView(button);
        }

        holder.view.setOnClickListener(v -> {
            if (onItemClickedListener != null)
            {
                onItemClickedListener.onItemClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
        notifyDataSetChanged();
    }

    public void addTopic(Topic topic) {
        topics.add(topic);
        notifyItemInserted(topics.size() - 1);
    }

    public void setOnItemClickedListener(OnItemClicked onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public void setOnSelectionChangedListener(OnSelectionChanged onSelectionChangedListener) {
        this.onSelectionChangedListener = onSelectionChangedListener;
    }

    public interface OnSelectionChanged {
        void onSelectionChanged(HashSet<Integer> selection);
    }

    public interface OnItemClicked {
        void onItemClicked(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView topicName;
        public LinearLayout deckLayout;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.topicName = (TextView) view.findViewById(R.id.topic_header);
            this.deckLayout = (LinearLayout) view.findViewById(R.id.deck_layout);
        }
    }
}
