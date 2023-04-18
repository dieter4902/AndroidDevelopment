package com.example.aufgabe4;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.itis.libs.parserng.android.expressParser.MathExpression;

public class MainActivity extends AppCompatActivity {

    TextView formula;
    TextView resultT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        formula = (TextView) findViewById(R.id.textView_formula);
        resultT = (TextView) findViewById(R.id.textView_result);

        findViewById(R.id.button_c).setOnClickListener(e -> clearSingle());
        findViewById(R.id.button_ce).setOnClickListener(e -> clear());
        findViewById(R.id.button22).setOnClickListener(e -> equals());

        final int buttonCount = 14;

        for (int i = 0; i < buttonCount; i++) {//https://stackoverflow.com/questions/22639218/how-to-get-all-buttons-ids-in-one-time-on-android
            @SuppressLint("DiscouragedApi") int id = getResources().getIdentifier("button" + i, "id", getPackageName());
            ((Button) findViewById(id)).setOnClickListener(v -> {
                String operation = ((TextView) v).getText().toString();
                CharSequence formulaChars = formula.getText();
                if (formulaChars.length() == 0 && !operation.matches("[-+0-9]")) {
                    return;
                }

                if (operation.matches("[-+/*]") && (formula.getText().toString().charAt(formula.getText().toString().length() - 1) + "").matches("[-+/]")) {
                    formula.setText(formula.getText().subSequence(0, formula.length() - 1));
                    if ((formula.getText().toString().charAt(formula.getText().toString().length() - 1) + "").matches("[-*+/]")) {
                        formula.setText(formula.getText().subSequence(0, formula.length() - 1));
                    }
                }

                formula.append(operation);
            });
        }

        formula.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                calculate();
            }
        });


    }

    private void calculate() {//https://stackoverflow.com/questions/40957885/android-with-math-parser
        String f = formula.getText().toString();
        MathExpression expression = new MathExpression(f);
        resultT.setText(expression.solve() + "");
    }

    private void clear() {
        formula.setText("");
    }

    private void clearSingle() {
        formula.setText(formula.getText().subSequence(0, formula.getText().length() - 1));
    }

    private void equals() {
        formula.setText(resultT.getText());
    }
}