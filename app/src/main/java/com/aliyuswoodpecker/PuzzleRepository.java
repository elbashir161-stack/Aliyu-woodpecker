package com.aliyuswoodpecker;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PuzzleRepository {

    public static List<Puzzle> load(Context context) throws Exception {

        List<Puzzle> puzzles = new ArrayList<>();

        InputStream input = context.getAssets().open("puzzles.json");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(input, "UTF-8")
        );

        StringBuilder content = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            content.append(line);
        }

        reader.close();

        JSONArray array = new JSONArray(content.toString());

        for (int i = 0; i < array.length(); i++) {

            JSONObject object = array.getJSONObject(i);

            JSONArray moveArray = object.getJSONArray("moves");
            JSONArray themeArray = object.getJSONArray("themes");

            List<String> moves = new ArrayList<>();
            List<String> themes = new ArrayList<>();

            for (int j = 0; j < moveArray.length(); j++) {
                moves.add(moveArray.getString(j));
            }

            for (int j = 0; j < themeArray.length(); j++) {
                themes.add(themeArray.getString(j));
            }

            puzzles.add(new Puzzle(
                    object.getString("id"),
                    object.getString("fen"),
                    moves,
                    object.getInt("rating"),
                    themes
            ));
        }

        return puzzles;
    }
  }
