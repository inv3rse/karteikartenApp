package de.mfgd_karteikarten.mfgd_karteikarten.ui.topic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;

public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.ViewHolder> {

    private OnSelectionChanged selectionChangedListener;
    private OnItemClicked itemClickedListener;

    private Context context;
    private boolean showSelection;
    private List<Deck> decks;
    private HashSet<Integer> selection;

    public DeckAdapter(Context context) {
        this.selection = new HashSet<>();
        this.decks = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.deck_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Deck deck = decks.get(position);
        holder.nameView.setText(deck.getName());
        holder.numCards.setText(context.getString(R.string.num_cards, deck.getCards().size()));

        if (showSelection) {
            holder.checkBox.setVisibility(View.VISIBLE);
            if (selection.contains(position)) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
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
        return decks.size();
    }

    void setDecks(List<Deck> decks) {
        this.decks = decks;
        selection.clear();
        showSelection = false;

        notifyDataSetChanged();
        notifySelectionChanged();
    }

    void addDeck(Deck deck) {
        decks.add(deck);
        notifyItemInserted(decks.size() - 1);
    }

    Deck getDeck(int position) {
        return decks.get(position);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView nameView;
        public TextView numCards;
        public CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.nameView = (TextView) view.findViewById(R.id.deck_name);
            this.numCards = (TextView) view.findViewById(R.id.deck_num_cards);
            this.checkBox = (CheckBox) view.findViewById(R.id.selection_checkbox);
        }
    }
}
