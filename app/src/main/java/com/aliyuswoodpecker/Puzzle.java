package com.aliyuswoodpecker;

import java.util.List;

public class Puzzle {

    public final String id;
    public final String fen;
    public final List<String> moves;
    public final int rating;
    public final List<String> themes;

    public Puzzle(
            String id,
            String fen,
            List<String> moves,
            int rating,
            List<String> themes
    ) {
        this.id = id;
        this.fen = fen;
        this.moves = moves;
        this.rating = rating;
        this.themes = themes;
    }
  }
