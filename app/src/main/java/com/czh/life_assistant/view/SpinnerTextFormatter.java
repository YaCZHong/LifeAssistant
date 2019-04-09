package com.czh.life_assistant.view;

import android.text.Spannable;

public interface SpinnerTextFormatter {
    Spannable format(String text);

    Spannable format(Object item);
}
