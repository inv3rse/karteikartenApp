package de.mfgd_karteikarten.mfgd_karteikarten.base.mvp;

import android.support.v7.app.AppCompatActivity;

/**
 * Activity that requires a presenter.
 * Lifecycle methods of the presenter are called automatically.
 * @param <P> Required presenter
 */
public abstract class PresenterActivity<P extends BasePresenter> extends AppCompatActivity {

    private P presenter;

    /**
     * Method to provide the Presenter.
     * Will be called once and then saved internally.
     * Must be "ready" before onResume.
     * @return the presenter
     */
    abstract protected P providePresenter();

    protected P getPresenter()
    {
        if (presenter == null)
        {
            throw new IllegalStateException("presenter not set");
        }
        return presenter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter = providePresenter();
        //noinspection unchecked
        getPresenter().takeView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPresenter().removeView();
    }
}
