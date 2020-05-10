package scripts.tanner.data;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public final class Constants {

    public static final RSTile OPEN_DOOR_TILE = new RSTile(3278, 3191, 0);
    public static final RSTile CLOSED_DOOR_TILE = new RSTile(3277, 3191, 0);

    public static final RSArea TANNER_AREA = new RSArea(
        new RSTile(3277, 3194, 0),
        new RSTile(3270, 3189, 0)
    );

    public static final RSTile[] PATH_TO_TANNER = {
        new RSTile(3269, 3167, 0),
        new RSTile(3276, 3173, 0),
        new RSTile(3280, 3183, 0),
        new RSTile(3276, 3191, 0)
    };

    public static final RSTile[] PATH_TO_DOOR = {
        new RSTile(3269, 3167, 0),
        new RSTile(3276, 3173, 0),
        new RSTile(3280, 3183, 0),
        new RSTile(3277, 3191, 0)
    };

    public static final int COINS = 995;
    public static final int TANNING_INTERFACE = 324;

    private Constants() {

    }

}
