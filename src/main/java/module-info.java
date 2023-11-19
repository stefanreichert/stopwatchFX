module net.wickedshell.lukas.stopwatch {
    requires javafx.controls;
    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.io;

    exports net.wickedshell.stopwatchFX;
    exports net.wickedshell.stopwatchFX.model;
}