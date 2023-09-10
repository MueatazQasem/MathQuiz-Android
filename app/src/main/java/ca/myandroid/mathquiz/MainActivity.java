package ca.myandroid.mathquiz;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import ca.myandroid.mathquiz.R;
import ca.myandroid.mathquiz.model.FloatingAndDots;
import ca.myandroid.mathquiz.model.Question;

public class MainActivity extends AppCompatActivity {
    private TextView questionTextView;
    private Button generateButton;
    private Button validateButton;
    private Button resultsButton;
    private Button buttonClear;
    private Button buttonMinus;
    private Button buttonFinish; // Added buttonFinish
    private EditText resultEditText;
    private Button[] numberButtons;
    private Button buttonDot;

    private String username;
    private double correctAnswer;
    private int correctAnswersCount;
    private DecimalFormat decimalFormat;

    private List<Question> questionsAndAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionsAndAnswers = new ArrayList<>();

        questionTextView = findViewById(R.id.questionTextView);
        generateButton = findViewById(R.id.generateButton);
        validateButton = findViewById(R.id.validateButton);
        resultsButton = findViewById(R.id.ScoreButton);
        buttonClear = findViewById(R.id.buttonClear);
        buttonMinus = findViewById(R.id.buttonMinus);
        resultEditText = findViewById(R.id.resultEditText);
        username = getIntent().getStringExtra("username");
        TextView playerNameTextView = findViewById(R.id.playerNameTextView);
        TextView playerScoreTextView = findViewById(R.id.playerScoreTextView);
        playerNameTextView.setText(String.format("Math Quiz"));
        playerScoreTextView.setText(String.format("Score: %d", correctAnswersCount));

        // Limit the input to one digit after the decimal point
        resultEditText.setFilters(new InputFilter[]{new FloatingAndDots(1)});

        // Initialize the number buttons array
        numberButtons = new Button[10];
        for (int i = 0; i < 10; i++) {
            int buttonId = getResources().getIdentifier("button" + i, "id", getPackageName());
            numberButtons[i] = findViewById(buttonId);

            final int number = i;
            numberButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resultEditText.append(String.valueOf(number));
                }
            });
        }

        // Initialize the decimal point button
        buttonDot = findViewById(R.id.buttonDot);
        buttonDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = resultEditText.getText().toString();
                if (!inputText.contains(".")) {
                    resultEditText.append(".");
                }
            }
        });

        // Initialize the clear button
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultEditText.setText("");
            }
        });

        // Initialize the minus button
        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = resultEditText.getText().toString();
                if (inputText.startsWith("-")) {
                    resultEditText.setText(inputText.substring(1));
                } else {
                    resultEditText.setText("-" + inputText);
                }
            }
        });

        // Initialize the finish button
        buttonFinish = findViewById(R.id.buttonFinish);
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Terminate the app
            }
        });

        username = getIntent().getStringExtra("username");
        decimalFormat = new DecimalFormat("#.##");
        generateNewQuestion();

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNewQuestion();
            }
        });

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredResult = resultEditText.getText().toString();
                if (!enteredResult.isEmpty()) {
                    double enteredResultAsNumber = Double.parseDouble(enteredResult);

                    // Store the question and answers in the list
                    questionsAndAnswers.add(new Question(questionTextView
                            .getText().toString(), correctAnswer, enteredResultAsNumber));

                    if (Math.abs(enteredResultAsNumber - correctAnswer) < 0.01) {
                        Toast.makeText(MainActivity.this, "Correct Answer!", Toast.LENGTH_SHORT).show();
                        correctAnswersCount++;
                    } else {
                        Toast.makeText(MainActivity.this, "Wrong Answer!", Toast.LENGTH_SHORT).show();
                    }
                    generateNewQuestion();
                } else {
                    Toast.makeText(MainActivity.this, "Enter Your Answer First", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("correctAnswersCount", correctAnswersCount);
                intent.putExtra("questionsAndAnswers", (Serializable) questionsAndAnswers);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                int score = data.getIntExtra("score", 0);
                correctAnswersCount = score;
                TextView playerScoreTextView = findViewById(R.id.playerScoreTextView);
                playerScoreTextView.setText(String.format("Score: %d", correctAnswersCount));
            }
        }
    }

    private void generateNewQuestion() {
        Random random = new Random();
        int num1 = random.nextInt(10);
        int num2 = random.nextInt(10);
        int operation = random.nextInt(4); // 0: addition, 1: subtraction, 2: multiplication, 3: division

        switch (operation) {
            case 0: // Addition
                correctAnswer = num1 + num2;
                questionTextView.setText(String.format("%d + %d = ?", num1, num2));
                break;
            case 1: // Subtraction
                correctAnswer = num1 - num2;
                questionTextView.setText(String.format("%d - %d = ?", num1, num2));
                break;
            case 2: // Multiplication
                correctAnswer = num1 * num2;
                questionTextView.setText(String.format("%d * %d = ?", num1, num2));
                break;
            case 3: // Division
                do {
                    num2 = random.nextInt(10);
                } while (num2 == 0);

                // Generate a random multiplier, then set num1 as num2 multiplied by this multiplier
                int multiplier = random.nextInt(10) + 1;
                num1 = num2 * multiplier;

                correctAnswer = (double) num1 / num2;
                questionTextView.setText(String.format("%d / %d = ?", num1, num2));
                break;
        }

        correctAnswer = Double.parseDouble(decimalFormat.format(correctAnswer));
    }
}
