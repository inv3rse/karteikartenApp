package de.mfgd_karteikarten.mfgd_karteikarten.ui.main;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
    private boolean showSelection;
    private HashSet<Integer> selection;

    private OnItemClicked itemClickedListener;
    private OnSelectionChanged selectionChangedListener;

    public TopicAdapter(Context context) {
        this.context = context;
        this.selection = new HashSet<>();
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
            button.setClickable(false);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(3, 3, 3, 3);

            button.setLayoutParams(params);

            holder.deckLayout.addView(button);
        }

        if (showSelection) {
            holder.checkBox.setVisibility(View.VISIBLE);
            if (selection.contains(position)) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }

        holder.view.setOnLongClickListener(v ->
        {
            showSelection(true);
            toggleSelection(position);
            return true;
        });

        holder.view.setOnClickListener(v ->
        {
            if (showSelection) {
                toggleSelection(position);
            } else if (itemClickedListener != null) {
                itemClickedListener.onItemClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    void setTopics(List<Topic> topics) {
        this.topics = topics;
        selection.clear();
        showSelection = false;

        notifyDataSetChanged();
        notifySelectionChanged();
    }

    void addTopic(Topic topic) {
        topics.add(topic);
        notifyItemInserted(topics.size() - 1);
    }


    public void toggleSelection(int position) {
        if (selection.contains(position)) {
            selection.remove(position);
        } else {
            selection.add(position);
        }
        if (selection.isEmpty()) {
            showSelection(false);
        } else {
            notifyItemChanged(position);
        }

        notifySelectionChanged();
    }

    public HashSet<Integer> getSelection() {
        return selection;
    }

    public void showSelection(boolean showSelection) {
        if (this.showSelection != showSelection) {
            this.showSelection = showSelection;
            notifyDataSetChanged();
        }
    }

    public void clearSelection() {
        selection.clear();
        showSelection(false);

        notifySelectionChanged();
        notifyDataSetChanged();
    }

    public void setSelection(HashSet<Integer> selection) {
        this.selection = selection;

        showSelection(!selection.isEmpty());
        notifySelectionChanged();
    }

    private void notifySelectionChanged() {
        if (selectionChangedListener != null) {
            selectionChangedListener.onSelectionChanged(selection);
        }
    }

    public void setSelectionChangedListener(OnSelectionChanged selectionChangedListener) {
        this.selectionChangedListener = selectionChangedListener;
    }

    public void setItemClickedListener(OnItemClicked itemClickedListener) {
        this.itemClickedListener = itemClickedListener;
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
        public CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.topicName = (TextView) view.findViewById(R.id.topic_header);
            this.deckLayout = (LinearLayout) view.findViewById(R.id.deck_layout);
            this.checkBox = (CheckBox) view.findViewById(R.id.topic_checkbox);
        }
    }
}
