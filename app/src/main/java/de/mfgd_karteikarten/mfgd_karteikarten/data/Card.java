package de.mfgd_karteikarten.mfgd_karteikarten.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Card extends RealmObject {

    public static final int UNKNOWN_ID = -1;

    @PrimaryKey
    private int id = UNKNOWN_ID;
    private int rating;
    private String answer;
    private String question;

    public Card() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
