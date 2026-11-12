package com.aliyuswoodpecker;

import android.os.SystemClock;

public class TrainingTimer {

    private long startTime;
    private long savedTime;
    private boolean running;

    public void start() {
        if (!running) {
            startTime = SystemClock.elapsedRealtime();
            running = true;
        }
    }

    public void pause() {
        if (running) {
            savedTime += SystemClock.elapsedRealtime()
                    - startTime;
            running = false;
        }
    }

    public void reset() {
        startTime = 0;
        savedTime = 0;
        running = false;
    }

    public long getElapsedSeconds() {
        long total = savedTime;

        if (running) {
            total += SystemClock.elapsedRealtime()
                    - startTime;
        }

        return total / 1000;
    }

    public boolean isRunning() {
        return running;
    }

    public String getFormattedTime() {
        long seconds = getElapsedSeconds();

        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        return String.format(
                "%02d:%02d:%02d",
                hours, minutes, remainingSeconds
        );
    }
}
