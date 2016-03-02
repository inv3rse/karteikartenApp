package de.mfgd_karteikarten.mfgd_karteikarten.ui.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import javax.inject.Inject;

import de.mfgd_karteikarten.mfgd_karteikarten.R;
import de.mfgd_karteikarten.mfgd_karteikarten.base.App;
import de.mfgd_karteikarten.mfgd_karteikarten.base.TopicManager;
import de.mfgd_karteikarten.mfgd_karteikarten.data.Topic;
import nucleus.factory.PresenterFactory;
import nucleus.presenter.Presenter;

public class MainPresenter extends Presenter<MainActivity> {
    private TopicManager topicManager;

    @Inject
    public MainPresenter(TopicManager topicManager) {
        this.topicManager = topicManager;
    }

    @Override
    protected void onTakeView(MainActivity view) {
        view.setTopics(topicManager.getTopics());
    }

    public void onAddTopicClicked() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getView());

        final EditText input = new EditText(getView());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        // Setting Dialog Title
        alertDialog.setTitle("add topic");

        // Setting Dialog Message
        alertDialog.setMessage("Type in the name of the new topic.");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_my_add_24dp);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("            Create           ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (!input.getText().toString().isEmpty()) {
                    Topic topic = new Topic(input.getText().toString());
                    topicManager.addTopic(topic);
                    // Write your code here to invoke YES event
                    Toast.makeText(getView().getApplicationContext(), "Created the topic : " + input.getText().toString(), Toast.LENGTH_LONG).show();
                    update();
                }
                if (input.getText().toString().isEmpty()) {
                    Toast.makeText(getView().getApplicationContext(), "ERROR : The textfield was empty, topics need always a name.", Toast.LENGTH_LONG).show();
                }
            }
        });


        // Showing Alert Message
        alertDialog.show();


        //Topic topic = new Topic("topic " + topicManager.getTopics().size());
        //topicManager.addTopic(topic);
        update();

    }

    private void update() {
        MainActivity view = getView();
        if (view != null) {
            getView().setTopics(topicManager.getTopics());
        }
    }

    public void onPositionClicked(int position) {
        MainActivity view = getView();
        if (view != null) {
            view.startTopicActivity(topicManager.getTopics().get(position).getID());
        }
    }

    public static class Factory implements PresenterFactory<MainPresenter> {
        private App app;

        public Factory(App app) {
            this.app = app;
        }

        @Override
        public MainPresenter createPresenter() {
            return DaggerMainComponent.builder()
                    .appComponent(app.component())
                    .build()
                    .getMainPresenter();
        }
    }
}
