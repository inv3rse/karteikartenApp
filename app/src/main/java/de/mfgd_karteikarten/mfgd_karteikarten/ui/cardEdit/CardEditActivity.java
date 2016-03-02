package de.mfgd_karteikarten.mfgd_karteikarten.ui.cardEdit;

import android.os.Bundle;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import nucleus.view.NucleusAppCompatActivity;

public class CardEditActivity extends NucleusAppCompatActivity<CardEditPresenter> {

    public static final String CARD_EXTRA = "CARD_EXTRA";
    public static final String DECK_EXTRA = "DECK_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_edit);

        Bundle extras = getIntent().getExtras();
    }
}
