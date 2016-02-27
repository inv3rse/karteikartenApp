package de.mfgd_karteikarten.mfgd_karteikarten.ui.topic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;

public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.ViewHolder> {
    private boolean showSelection;
    private List<Deck> decks;
    private ArrayList<Integer> selection;

    public DeckAdapter() {
        selection = new ArrayList<>();
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

        if (showSelection)
        {
            holder.checkBox.setVisibility(View.VISIBLE);
            if (selection.contains(position))
            {
                holder.checkBox.setChecked(true);
            }
            else
            {
                holder.checkBox.setChecked(false);
            }
        }
        else
        {
            holder.checkBox.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return decks.size();
    }

    void setDecks(List<Deck> decks)
    {
        this.decks = decks;
        selection.clear();
        showSelection = false;
        notifyDataSetChanged();
    }

    void addDeck(Deck deck)
    {
        decks.add(deck);
        notifyItemInserted(decks.size() - 1);
    }

    Deck getDeck(int position)
    {
        return decks.get(position);
    }

    void removeDeck(int position)
    {
        decks.remove(position);
        notifyItemRemoved(position);
    }

    public void toogleSelection(int position) {
        if (selection.contains(position)) {
            selection.remove(position);
        } else {
            selection.add(position);
        }

        notifyItemChanged(position);
    }

    public List<Integer> getSeletion() {
        return Collections.unmodifiableList(selection);
    }

    public void showSelection(boolean showSelection)
    {
        this.showSelection = showSelection;
        notifyDataSetChanged();
    }

    public void clearSelection() {
        selection.clear();
        showSelection = false;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            nameView = (TextView) view.findViewById(R.id.deck_name);
            checkBox = (CheckBox) view.findViewById(R.id.selection_checkbox);
        }
    }
}
