package net.wickedshell.stopwatchFX.controller;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import net.wickedshell.stopwatchFX.model.Stopwatch;

public class SceneController implements EventHandler<KeyEvent> {

    private final Stopwatch stopwatch;

    public SceneController(Stopwatch stopwatch) {
        this.stopwatch = stopwatch;
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            onEnterPressed();
        } else if (keyEvent.getCode() == KeyCode.SPACE) {
            onSpacePressed();
        }
    }

    private void onEnterPressed() {
        System.out.println("gedrückt: Eingabe");
        stopwatch.reset();
    }

    private void onSpacePressed() {
        System.out.println("gedrückt: Leertaste");
        if (stopwatch.isRunning()) {
            stopwatch.stop();
        } else {
            stopwatch.start();
        }
    }
}
