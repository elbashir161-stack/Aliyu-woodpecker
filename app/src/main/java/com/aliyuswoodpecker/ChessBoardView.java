package com.aliyuswoodpecker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

public class ChessBoardView extends View {

    public interface SquareListener {
        void onSquareTapped(String square);
    }

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private String fen = "8/8/8/8/8/8/8/8";
    private SquareListener listener;

    private final String[][] pieces = new String[8][8];

    public ChessBoardView(Context context) {
        super(context);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        parseFen();
    }

    public void setSquareListener(SquareListener listener) {
        this.listener = listener;
    }

    public void setPosition(String fen) {
        this.fen = fen;
        parseFen();
        invalidate();
    }

    private void parseFen() {
        String[] rows = fen.split(" ")[0].split("/");

        for (int r = 0; r < 8; r++) {
            int c = 0;

            for (int i = 0; i < 8; i++) {
                pieces[r][i] = "";
            }

            if (r >= rows.length) continue;

            for (char ch : rows[r].toCharArray()) {
                if (Character.isDigit(ch)) {
                    c += Character.getNumericValue(ch);
                } else if (c < 8) {
                    pieces[r][c++] = String.valueOf(ch);
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float size = Math.min(getWidth(), getHeight());
        float square = size / 8f;
        float left = (getWidth() - size) / 2f;
        float top = (getHeight() - size) / 2f;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                paint.setColor((r + c) % 2 == 0
                        ? 0xFFF0D9B5 : 0xFFB58863);

                canvas.drawRect(
                        left + c * square,
                        top + r * square,
                        left + (c + 1) * square,
                        top + (r + 1) * square,
                        paint
                );

                String piece = pieces[r][c];

                if (!piece.isEmpty()) {
                    paint.setTypeface(Typeface.create(
                            "serif", Typeface.NORMAL
                    ));

                    paint.setTextSize(square * 0.75f);
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setColor(Character.isUpperCase(
                            piece.charAt(0)
                    ) ? 0xFFFFFFFF : 0xFF202020);

                    canvas.drawText(
                            getSymbol(piece.charAt(0)),
                            left + (c + 0.5f) * square,
                            top + (r + 0.78f) * square,
                            paint
                    );
                }
            }
        }
    }

    private String getSymbol(char piece) {
        switch (piece) {
            case 'K': return "♔";
            case 'Q': return "♕";
            case 'R': return "♖";
            case 'B': return "♗";
            case 'N': return "♘";
            case 'P': return "♙";
            case 'k': return "♚";
            case 'q': return "♛";
            case 'r': return "♜";
            case 'b': return "♝";
            case 'n': return "♞";
            case 'p': return "♟";
            default: return "";
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float size = Math.min(getWidth(), getHeight());
            float square = size / 8f;
            float left = (getWidth() - size) / 2f;
            float top = (getHeight() - size) / 2f;

            int col = (int) ((event.getX() - left) / square);
            int row = (int) ((event.getY() - top) / square);

            if (row >= 0 && row < 8 && col >= 0 && col < 8) {
                char file = (char) ('a' + col);
                char rank = (char) ('8' - row);

                if (listener != null) {
                    listener.onSquareTapped(
                            String.valueOf(file) + rank
                    );
                }
            }

            performClick();
            return true;
        }

        return true;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }
}
