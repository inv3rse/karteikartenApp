package de.mfgd_karteikarten.mfgd_karteikarten.ui.deck;

import android.os.Bundle;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import nucleus.view.NucleusAppCompatActivity;

public class DeckActivity extends NucleusAppCompatActivity<DeckPresenter> {

    public static final String DECK_EXTRA = "DECK_EXTRA";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck);
    }
}
