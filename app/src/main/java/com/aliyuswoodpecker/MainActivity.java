package com.aliyuswoodpecker;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity
        implements ChessBoardView.SquareListener {

    private ChessBoardView chessBoard;
    private ChessGame chessGame;
    private WoodpeckerSession session;

    private TextView title;
    private TextView status;
    private TextView progress;

    private Button nextButton;

    private String selectedSquare = null;
    private int solutionIndex = 0;
    private boolean puzzleSolved = false;
    private boolean mistakeRecorded = false;

    private int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildInterface();

        try {
            List<Puzzle> puzzles =
                    PuzzleRepository.load(this);

            if (puzzles.isEmpty()) {
                status.setText("No puzzles found.");
                return;
            }

            session = new WoodpeckerSession(puzzles);
            chessGame = new ChessGame();

            loadCurrentPuzzle();

        } catch (Exception e) {
            status.setText("Could not load puzzles: "
                    + e.getMessage());
        }
    }

    private void buildInterface() {
        ScrollView scroll = new ScrollView(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setPadding(dp(16), dp(20), dp(16), dp(20));
        layout.setBackgroundColor(Color.rgb(25, 25, 25));

        title = new TextView(this);
        title.setText("ALIYU'S WOODPECKER");
        title.setTextColor(Color.WHITE);
        title.setTextSize(25);
        title.setGravity(Gravity.CENTER);

        progress = new TextView(this);
        progress.setTextColor(Color.LTGRAY);
        progress.setTextSize(15);
        progress.setGravity(Gravity.CENTER);
        progress.setPadding(0, dp(18), 0, dp(12));

        chessBoard = new ChessBoardView(this);
        chessBoard.setSquareListener(this);

        status = new TextView(this);
        status.setTextColor(Color.WHITE);
        status.setTextSize(16);
        status.setGravity(Gravity.CENTER);
        status.setPadding(dp(8), dp(18), dp(8), dp(18));

        nextButton = new Button(this);
        nextButton.setText("Next puzzle");
        nextButton.setVisibility(android.view.View.GONE);
        nextButton.setOnClickListener(v -> nextPuzzle());

        layout.addView(title);
        layout.addView(progress);

        layout.addView(chessBoard,
                new LinearLayout.LayoutParams(
                        -1, dp(340)));

        layout.addView(status);
        layout.addView(nextButton);

        scroll.addView(layout);
        setContentView(scroll);
    }

    private void loadCurrentPuzzle() {
        Puzzle puzzle = session.getCurrentPuzzle();

        if (puzzle == null) {
            status.setText("Round completed!");
            nextButton.setVisibility(
                    android.view.View.VISIBLE);
            return;
        }

        chessGame.loadPosition(puzzle.fen);
        chessBoard.setPosition(chessGame.getPosition());

        selectedSquare = null;
        solutionIndex = 0;
        puzzleSolved = false;
        mistakeRecorded = false;

        nextButton.setVisibility(android.view.View.GONE);

        progress.setText(
                "Round " + session.getRound()
                + "  |  Puzzle "
                + (session.getCompleted() + 1)
                + " / " + session.getTotal()
        );

        status.setText("Find the best move.");
    }

    @Override
    public void onSquareTapped(String square) {
        if (puzzleSolved || chessGame == null) {
            return;
        }

        if (selectedSquare == null) {
            selectedSquare = square;
            status.setText("Select the destination square.");
            return;
        }

        String attemptedMove =
                selectedSquare + square;

        selectedSquare = null;

        Puzzle puzzle = session.getCurrentPuzzle();

        if (puzzle == null ||
                solutionIndex >= puzzle.moves.size()) {
            return;
        }

        String expectedMove =
                puzzle.moves.get(solutionIndex);

        if (!attemptedMove.equalsIgnoreCase(expectedMove)) {

            if (!mistakeRecorded) {
                session.recordIncorrect();
                mistakeRecorded = true;
            }

            status.setText("Not the solution. Try again.");
            return;
        }

        if (!chessGame.playMove(expectedMove)) {
            status.setText("Move validation error.");
            return;
        }

        solutionIndex++;

        // Play the opponent's automatic reply.
        if (solutionIndex < puzzle.moves.size()) {
            String reply = puzzle.moves.get(solutionIndex);

            if (!chessGame.playMove(reply)) {
                status.setText("Puzzle data error.");
                return;
            }

            solutionIndex++;
        }

        chessBoard.setPosition(chessGame.getPosition());

        if (solutionIndex >= puzzle.moves.size()) {
            puzzleSolved = true;
            session.recordCorrect();

            status.setText("Puzzle solved!");
            nextButton.setVisibility(
                    android.view.View.VISIBLE);
        } else {
            status.setText("Correct. Continue solving.");
        }
    }

    private void nextPuzzle() {
        if (session == null) return;

        if (session.hasNext()) {
            loadCurrentPuzzle();
            return;
        }

        session.startNextRound();

        if (session.hasNext()) {
            loadCurrentPuzzle();
        } else {
            status.setText(
                    "Training complete! Correct: "
                    + session.getCorrect()
                    + " | Incorrect: "
                    + session.getIncorrect()
            );

            nextButton.setVisibility(
                    android.view.View.GONE);
        }
    }
    }
