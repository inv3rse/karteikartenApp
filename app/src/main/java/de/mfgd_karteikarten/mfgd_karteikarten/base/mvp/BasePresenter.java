package de.mfgd_karteikarten.mfgd_karteikarten.base.mvp;

/**
 * Base class for all presenters
 * @param <V> View to work with, ex MainActivity
 */
public class BasePresenter<V> {

    private V view;

    public void takeView(V view)
    {
        this.view = view;
        onTakeView(view);
    }

    public void removeView()
    {
        if (view != null)
        {
            onRemoveView(view);
            this.view = null;
        }
    }

    public boolean hasView()
    {
        return view != null;
    }

    public V getView()
    {
        return view;
    }

    protected void onTakeView(V view) {}
    protected void onRemoveView(V view) {}
}
