package de.mfgd_karteikarten.mfgd_karteikarten.ui.cardEdit;

import nucleus.factory.PresenterFactory;
import nucleus.presenter.Presenter;

public class CardEditPresenter extends Presenter<CardEditActivity> {

    public class Factory implements PresenterFactory<CardEditPresenter>
    {
        private int deckId;

        @Override
        public CardEditPresenter createPresenter() {
            return null;
        }
    }
}
