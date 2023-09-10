package ca.myandroid.mathquiz.model;

import java.io.Serializable;

public class Question implements Serializable {
    private final String question;
    private final double correctAnswer;
    private final double userAnswer;

    public Question(String question, double correctAnswer, double userAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.userAnswer = userAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public double getCorrectAnswer() {
        return correctAnswer;
    }

    public double getUserAnswer() {
        return userAnswer;
    }
}
