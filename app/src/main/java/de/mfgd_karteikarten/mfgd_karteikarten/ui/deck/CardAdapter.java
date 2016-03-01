package de.mfgd_karteikarten.mfgd_karteikarten.ui.deck;

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
import de.mfgd_karteikarten.mfgd_karteikarten.data.Card;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>
{
    private OnSelectionChanged selectionChangedListener;
    private OnItemClicked itemClickedListener;

    private Context context;
    private boolean showSelection;
    private List<Card> cards;
    private HashSet<Integer> selection;

    public CardAdapter(Context context)
    {
        this.selection = new HashSet<>();
        this.cards = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.abbView.setText(card.getQuestion());

        if (showSelection)
        {
            holder.checkbox.setVisibility(View.VISIBLE);
            if(selection.contains(position))
            {
                holder.checkbox.setChecked(true);
            } else
            {
                holder.checkbox.setChecked(false);
            }
        } else
        {
            holder.checkbox.setVisibility(View.INVISIBLE);
        }

        holder.view.setOnClickListener(v ->
        {
            if(showSelection)
            {
                toggleSelection(position);
            }else if(itemClickedListener != null)
            {
                itemClickedListener.onItemClicked(position);
            }
        });
    }

    void setCards(List<Card> cards)
    {
        this.cards = cards;
        selection.clear();
        showSelection = false;

        notifySelectionChanged();
        notifyDataSetChanged();
    }

    void addCard(Card card)
    {
        cards.add(card);
        notifyItemInserted(cards.size() - 1);
    }

    Card getCard(int position)
    {
        return cards.get(position);
    }

    public void toggleSelection(int position)
    {
        if(selection.contains(position))
        {
            selection.remove(position);
        } else
        {
            selection.add(position);
        }
        if(selection.isEmpty())
        {
            showSelection(false);
        } else
        {
            notifyItemChanged(position);
        }

        notifySelectionChanged();
    }

    public HashSet<Integer> getSelection()
    {
        return selection;
    }


    public void showSelection(boolean showSelection)
    {
        if(this.showSelection != showSelection)
        {
            this.showSelection = showSelection;
            notifyDataSetChanged();
        }
    }

    public void clearSelection()
    {
        selection.clear();
        showSelection(false);

        notifySelectionChanged();
        notifyDataSetChanged();
    }

    public void setSelection(HashSet<Integer> selection)
    {
        this.selection = selection;

        showSelection(!selection.isEmpty());
        notifySelectionChanged();
    }

    private void notifySelectionChanged()
    {
        if(selectionChangedListener != null)
        {
            selectionChangedListener.onSelectionChanged(selection);
        }
    }

    public void setSelectionChangedListener(OnSelectionChanged selectionChangedListener)
    {
        this.selectionChangedListener = selectionChangedListener;
    }

    public void setItemClickedListener(OnItemClicked itemClickedListener)
    {
        this.itemClickedListener = itemClickedListener;
    }

    public interface OnSelectionChanged
    {
        void onSelectionChanged(HashSet<Integer> selection);
    }

    public interface OnItemClicked
    {
        void onItemClicked(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public View view;
        public TextView abbView;
        public CheckBox checkbox;

        public ViewHolder(View view)
        {
            super(view);
            this.view = view;
            this.abbView = (TextView) view.findViewById(R.id.card_name);
            this.checkbox = (CheckBox) view.findViewById(R.id.card_selection_checkbox);

        }
    }
}

