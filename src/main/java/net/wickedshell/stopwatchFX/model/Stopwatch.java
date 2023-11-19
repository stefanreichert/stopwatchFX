package net.wickedshell.stopwatchFX.model;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;

public class Stopwatch {
    private final Set<StopwatchListener> listeners = new HashSet<>();
    private int milliseconds = 0;
    private Timeline timeline;

    public boolean isRunning() {
        return timeline != null;
    }

    public int getMilliseconds() {
        return milliseconds;
    }

    public void start() {
        if (!isRunning()) {
            timeline = new Timeline(new KeyFrame(Duration.millis(1), event -> {
                milliseconds++;
                listeners.forEach(stopwatchListener -> stopwatchListener.onMillisecondsChange(milliseconds));
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();

            listeners.forEach(StopwatchListener::onStart);
        }
    }

    public void stop() {
        if (isRunning()) {
            timeline.stop();
            timeline = null;
            listeners.forEach(StopwatchListener::onStop);
        }
    }

    public void reset() {
        stop();
        milliseconds = 0;
        listeners.forEach(stopwatchListener -> stopwatchListener.onMillisecondsChange(milliseconds));
    }

    public void addListener(StopwatchListener listener) {
        listeners.add(listener);
    }

    public interface StopwatchListener {

        void onStart();

        void onStop();

        void onMillisecondsChange(int seconds);
    }
}
