package de.mfgd_karteikarten.mfgd_karteikarten.ui.importDeck;

import javax.inject.Inject;

import de.mfgd_karteikarten.mfgd_karteikarten.base.ActivityScope;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.base.db.TopicEditor;
import de.mfgd_karteikarten.mfgd_karteikarten.base.db.TopicManager;
import de.mfgd_karteikarten.mfgd_karteikarten.base.online.IcasyApi;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Deck;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;
import io.realm.Realm;
import nucleus.factory.PresenterFactory;
import nucleus.presenter.Presenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@ActivityScope
public class ImportPresenter extends Presenter<ImportActivity> {
    private TopicManager topicManager;
    private Realm realm;
    private String importId;

    private boolean loadFailed = false;
    private int selectedTopic = 0;
    private Deck loadedDeck = null;

    @Inject
    public ImportPresenter(TopicManager topicManager, Realm realm, IcasyApi api, String importUrl) {
        this.topicManager = topicManager;
        this.realm = realm;

        String split[] = importUrl.split("/");
        if (split.length > 1) {
            this.importId = split[split.length - 1];
            api.getDeck(importId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(deck -> {
                        loadedDeck = deck;
                        ImportActivity view = getView();
                        if (view != null) {
                            view.setDeck(deck);
                        }
                    }, throwable -> {
                        throwable.printStackTrace();
                        loadFailed = true;
                        ImportActivity view = getView();
                        if (view != null) {
                            view.closeActivity();
                        }
                    }, () -> {
                        ImportActivity view = getView();
                        if (view != null) {
                            view.showLoading(false);
                        }
                    });
        } else {
            this.importId = null;
        }
    }

    @Override
    protected void onTakeView(ImportActivity importActivity) {
        if (importId == null || loadFailed) {
            importActivity.closeActivity();
        } else {
            importActivity.setTopicOptions(topicManager.getTopics(), selectedTopic);

            if (loadedDeck == null) {
                importActivity.showLoading(true);
            } else {
                importActivity.setDeck(loadedDeck);
            }
        }
    }

    public void onTopicSelected(int pos) {
        selectedTopic = pos;
    }

    public void onOk(ImportActivity view) {
        if (loadedDeck != null && topicManager.getTopics().size() > 0) {
            Topic topic = topicManager.getTopics().get(selectedTopic);
            TopicEditor editor = new TopicEditor(realm, topic);
            editor.addDeck(loadedDeck);

            view.closeActivity();
        }
    }

    public void onCancel(ImportActivity view) {
        view.closeActivity();
    }

    public static class Factory implements PresenterFactory<ImportPresenter> {
        private App app;
        private String importUrl;

        public Factory(App app, String importUrl) {
            this.app = app;
            this.importUrl = importUrl;
        }

        @Override
        public ImportPresenter createPresenter() {
            return DaggerImportComponent.builder()
                    .appComponent(app.component())
                    .importModule(new ImportModule(importUrl))
                    .build()
                    .getImportPresenter();
        }
    }
}
