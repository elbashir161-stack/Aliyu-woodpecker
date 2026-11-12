package com.aliyuswoodpecker;

import android.content.Context;
import android.content.SharedPreferences;

public class ProgressStore {

    private final SharedPreferences preferences;

    public ProgressStore(Context context) {
        preferences = context.getSharedPreferences(
                "woodpecker_progress",
                Context.MODE_PRIVATE
        );
    }

    public void save(
            int round,
            int completed,
            int correct,
            int incorrect,
            long elapsedSeconds
    ) {
        preferences.edit()
                .putInt("round", round)
                .putInt("completed", completed)
                .putInt("correct", correct)
                .putInt("incorrect", incorrect)
                .putLong("elapsed", elapsedSeconds)
                .apply();
    }

    public int getRound() {
        return preferences.getInt("round", 1);
    }

    public int getCompleted() {
        return preferences.getInt("completed", 0);
    }

    public int getCorrect() {
        return preferences.getInt("correct", 0);
    }

    public int getIncorrect() {
        return preferences.getInt("incorrect", 0);
    }

    public long getElapsedSeconds() {
        return preferences.getLong("elapsed", 0);
    }

    public void reset() {
        preferences.edit().clear().apply();
    }
}
