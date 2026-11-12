package com.aliyuswoodpecker;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MistakeRepository {

    private final SharedPreferences preferences;

    public MistakeRepository(Context context) {
        preferences = context.getSharedPreferences(
                "woodpecker_mistakes",
                Context.MODE_PRIVATE
        );
    }

    public void addMistake(String puzzleId) {
        Set<String> mistakes = getMistakes();
        mistakes.add(puzzleId);

        preferences.edit()
                .putStringSet("mistakes", mistakes)
                .apply();
    }

    public void removeMistake(String puzzleId) {
        Set<String> mistakes = getMistakes();
        mistakes.remove(puzzleId);

        preferences.edit()
                .putStringSet("mistakes", mistakes)
                .apply();
    }

    public Set<String> getMistakes() {
        return new HashSet<>(
                preferences.getStringSet(
                        "mistakes",
                        new HashSet<>()
                )
        );
    }

    public boolean hasMistakes() {
        return !getMistakes().isEmpty();
    }

    public void clearMistakes() {
        preferences.edit()
                .remove("mistakes")
                .apply();
    }
  }
