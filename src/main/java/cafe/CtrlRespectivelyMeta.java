package cafe;

import java.awt.event.InputEvent;

public class CtrlRespectivelyMeta {

    private static final boolean macOS = System.getProperty("os.name").toLowerCase().contains("mac");

    private static final int DOWN_MASK = macOS ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_DOWN_MASK;

    public static boolean isDown(InputEvent event) {
        return (event.getModifiersEx() & DOWN_MASK) != 0;
    }
}
