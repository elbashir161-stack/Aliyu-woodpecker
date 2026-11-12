package com.aliyuswoodpecker;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;

public class ChessGame {

    private Board board;

    public ChessGame() {
        board = new Board();
    }

    public void loadPosition(String fen) {
        board = new Board();
        board.loadFromFen(fen);
    }

    public boolean playMove(String uci) {
        try {
            Move move = board.fromUci(uci);

            if (!board.legalMoves().contains(move)) {
                return false;
            }

            return board.doMove(move);
        } catch (Exception e) {
            return false;
        }
    }

    public String getPosition() {
        return board.getFen();
    }

    public boolean isCheckmate() {
        return board.isMated();
    }

    public boolean isStalemate() {
        return board.isStaleMate();
    }

    public Board getBoard() {
        return board;
    }
}
