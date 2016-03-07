package de.mfgd_karteikarten.mfgd_karteikarten.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Card extends RealmObject {

    public static final int UNKNOWN_ID = -1;
    @PrimaryKey
    private int ID = UNKNOWN_ID;
    private int rating;
    private String answer;
    private String question;
    private int type;
    private String falseone;
    private String falsetwo;

    public Card() {

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFalseOne() {
        return falseone;
    }

    public void setFalseOne(String falseOne) {
        this.falseone = falseOne;
    }

    public String getFalseTwo()  {return falsetwo;}

    public void setFalseTwo(String falsetwo){this.falsetwo = falsetwo;}

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
