package de.mfgd_karteikarten.mfgd_karteikarten.ui.topic;

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

    private OnSelectionModeChanged selectionModeChangedListener;
    private boolean showSelection;
    private List<Deck> decks;
    private HashSet<Integer> selection;
    public DeckAdapter() {
        selection = new HashSet<>();
        decks = new ArrayList<>();
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
            toogleSelection(position);
            return true;
        });

        holder.view.setOnClickListener(v ->
        {
            if (showSelection) {
                toogleSelection(position);
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
    }

    void addDeck(Deck deck) {
        decks.add(deck);
        notifyItemInserted(decks.size() - 1);
    }

    Deck getDeck(int position) {
        return decks.get(position);
    }

    void removeDeck(int position) {
        decks.remove(position);
        notifyItemRemoved(position);
    }

    public void toogleSelection(int position) {
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
    }

    public HashSet<Integer> getSeletion() {
        return selection;
    }

    public void showSelection(boolean showSelection) {
        if (this.showSelection != showSelection) {
            this.showSelection = showSelection;
            notifyDataSetChanged();

            if (selectionModeChangedListener != null) {
                selectionModeChangedListener.onSelectionModeChanged(showSelection);
            }
        }
    }

    public void clearSelection() {
        selection.clear();
        showSelection(false);
        notifyDataSetChanged();
    }

    public void setSelection(HashSet<Integer> selection) {
        this.selection = selection;
        showSelection = !selection.isEmpty();
        notifyDataSetChanged();
    }

    public void setSelectionModeChangedListener(OnSelectionModeChanged selectionModeChangedListener) {
        this.selectionModeChangedListener = selectionModeChangedListener;
    }

    public interface OnSelectionModeChanged {
        void onSelectionModeChanged(boolean inSelectionMode);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView nameView;
        public CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.nameView = (TextView) view.findViewById(R.id.deck_name);
            this.checkBox = (CheckBox) view.findViewById(R.id.selection_checkbox);
        }
    }
}
