package com.aliyuswoodpecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WoodpeckerSession {

    private final List<Puzzle> puzzles;
    private final List<Puzzle> mistakes = new ArrayList<>();

    private int currentIndex = 0;
    private int correct = 0;
    private int incorrect = 0;
    private int round = 1;

    public WoodpeckerSession(List<Puzzle> puzzles) {
        this.puzzles = new ArrayList<>(puzzles);
        Collections.shuffle(this.puzzles);
    }

    public Puzzle getCurrentPuzzle() {
        if (currentIndex >= puzzles.size()) {
            return null;
        }

        return puzzles.get(currentIndex);
    }

    public void recordCorrect() {
        correct++;
        currentIndex++;
    }

    public void recordIncorrect() {
        incorrect++;

        Puzzle puzzle = getCurrentPuzzle();

        if (puzzle != null && !mistakes.contains(puzzle)) {
            mistakes.add(puzzle);
        }
    }

    public boolean hasNext() {
        return currentIndex < puzzles.size();
    }

    public void startNextRound() {
        if (!mistakes.isEmpty()) {
            puzzles.clear();
            puzzles.addAll(mistakes);
            mistakes.clear();
            Collections.shuffle(puzzles);

            currentIndex = 0;
            round++;
        }
    }

    public int getCorrect() {
        return correct;
    }

    public int getIncorrect() {
        return incorrect;
    }

    public int getRound() {
        return round;
    }

    public int getCompleted() {
        return currentIndex;
    }

    public int getTotal() {
        return puzzles.size();
    }
  }
