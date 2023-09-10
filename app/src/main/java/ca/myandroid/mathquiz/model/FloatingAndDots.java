package ca.myandroid.mathquiz.model;

import android.text.InputFilter;
import android.text.Spanned;

public class FloatingAndDots implements InputFilter {
    private final int decimalDigits;

    public FloatingAndDots(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int dotPos = -1;
        int len = dest.length();
        for (int i = 0; i < len; i++) {
            char c = dest.charAt(i);
            if (c == '.') {
                dotPos = i;
                break;
            }
        }
        if (dotPos >= 0) {
            if (source.equals(".") || (dend > dotPos && len - dotPos > decimalDigits)) {
                return "";
            }
        }
        return null;
    }
}