package ca.myandroid.mathquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ca.myandroid.mathquiz.model.Question;

public class ResultActivity extends AppCompatActivity {
    private TextView playerNameTextView;
    private TextView playerScoreTextView;
    private Button returnButton;
    private TableLayout questionsTable;
    private CheckBox sortCorrectAscCheckBox;
    private CheckBox sortIncorrectDescCheckBox;

    private String username;
    private int correctAnswersCount;
    private List<Question> questionsAndAnswers;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        playerNameTextView = findViewById(R.id.playerNameTextView);
        playerScoreTextView = findViewById(R.id.playerScoreTextView);
        returnButton = findViewById(R.id.returnButton);
        //finishButton = findViewById(R.id.finishButton);
        questionsTable = findViewById(R.id.questionsTable);
        sortCorrectAscCheckBox = findViewById(R.id.sortCorrectAscCheckBox);
        sortIncorrectDescCheckBox = findViewById(R.id.sortIncorrectDescCheckBox);

        username = getIntent().getStringExtra("username");
        correctAnswersCount = getIntent().getIntExtra("correctAnswersCount", 0);
        questionsAndAnswers = (List<Question>) getIntent().getSerializableExtra("questionsAndAnswers");

        playerNameTextView.setText(String.format("Player:"));
        playerScoreTextView.setText(String.format("Score: %d", correctAnswersCount));
        displayQuestionsAndAnswers(questionsAndAnswers);

        sortCorrectAscCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sortIncorrectDescCheckBox.setChecked(false);
                    Collections.sort(questionsAndAnswers, new Comparator<Question>() {
                        @Override
                        public int compare(Question qa1, Question qa2) {
                            return Double.compare(qa1.getCorrectAnswer(), qa2.getCorrectAnswer());
                        }
                    });
                    questionsTable.removeAllViews();
                    displayQuestionsAndAnswers(questionsAndAnswers);
                }
            }
        });

        sortIncorrectDescCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sortCorrectAscCheckBox.setChecked(false);
                    Collections.sort(questionsAndAnswers, new Comparator<Question>() {
                        @Override
                        public int compare(Question qa1, Question qa2) {
                            boolean qa1Correct = qa1.getUserAnswer() == qa1.getCorrectAnswer();
                            boolean qa2Correct = qa2.getUserAnswer() == qa2.getCorrectAnswer();
                            return Boolean.compare(qa1Correct, qa2Correct);
                        }
                    });
                    questionsTable.removeAllViews();
                    displayQuestionsAndAnswers(questionsAndAnswers);
                }
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("score", correctAnswersCount);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

//


    }

    private void displayQuestionsAndAnswers(List<Question> questionsAndAnswers) {
        for (Question qa : questionsAndAnswers) {
            TableRow row = new TableRow(this);
            TextView questionTextView = new TextView(this);
            questionTextView.setText(qa.getQuestion());
            questionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            TextView userAnswerTextView = new TextView(this);
            userAnswerTextView.setText(String.format("%.1f", qa.getUserAnswer()));
            userAnswerTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            TextView correctAnswerTextView = new TextView(this);
            correctAnswerTextView.setText(String.format("%.1f", qa.getCorrectAnswer()));
            correctAnswerTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            row.addView(questionTextView);
            row.addView(userAnswerTextView);
            row.addView(correctAnswerTextView);

            questionsTable.addView(row);
        }
    }
}

