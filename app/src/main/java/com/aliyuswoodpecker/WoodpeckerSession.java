package com.aliyuswoodpecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WoodpeckerSession {

    private final List<Puzzle> allPuzzles;
    private final List<Puzzle> mistakes = new ArrayList<>();
    private List<Puzzle> roundPuzzles;

    private int currentIndex = 0;
    private int correct = 0;
    private int incorrect = 0;
    private int round = 1;

    public WoodpeckerSession(List<Puzzle> puzzles) {
        allPuzzles = new ArrayList<>(puzzles);
        roundPuzzles = new ArrayList<>(allPuzzles);
        Collections.shuffle(roundPuzzles);
    }

    public Puzzle getCurrentPuzzle() {
        if (currentIndex >= roundPuzzles.size()) {
            return null;
        }

        return roundPuzzles.get(currentIndex);
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
        return currentIndex < roundPuzzles.size();
    }

    public void startNextRound() {
        round++;
        currentIndex = 0;

        roundPuzzles = new ArrayList<>(allPuzzles);
        Collections.shuffle(roundPuzzles);

        mistakes.clear();
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
        return roundPuzzles.size();
    }

    public List<Puzzle> getMistakes() {
        return new ArrayList<>(mistakes);
    }
    }
