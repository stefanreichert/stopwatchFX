package net.wickedshell.stopwatchFX.view.serialize;

public class TimeSerializer {

    public static String serialize(long milliseconds) {
        long minutes = (milliseconds % 3600000) / 60000;
        long seconds = (milliseconds % 60000) / 1000;
        long millis = milliseconds % 1000;
        return String.format("%02d:%02d.%03d", minutes, seconds, millis);
    }
}
