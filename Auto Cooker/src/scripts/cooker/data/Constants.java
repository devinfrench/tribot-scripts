package scripts.cooker.data;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public class Constants {

    public static final int RAW_KARAMBWAN = 3142;
    public static final int GRAPES = 1987;
    public static final int JUG_OF_WATER = 1937;
    public static final int JUG_OF_WINE = 1993;
    public static final int UNFERMENTED_WINE = 1995;
    public static final RSTile ROGUES_DEN_FIRE_TILE = new RSTile(3043, 4973, 1);
    public static final RSTile CATHERBY_BANK_BOOTH_TILE = new RSTile(2811, 3442, 0);
    public static final RSTile NARDAH_BANK_BOOTH_TILE = new RSTile(3426, 2889, 0);
    public static final RSTile AL_KHARID_OPEN_DOOR_TILE = new RSTile(3276, 3180, 0);
    public static final RSTile AL_KHARID_CLOSED_DOOR_TILE = new RSTile(3275, 3180, 0);
    public static final RSTile AL_KHARID_RANGE_TILE = new RSTile(3272, 3180, 0);
    public static final RSTile AL_KHARID_BANK_TILE = new RSTile(3269, 3167, 0);
    public static final RSArea AL_KHARID_RANGE_AREA = new RSArea(
      new RSTile(3271, 3183, 0),
      new RSTile(3275, 3179, 0)
    );
    public static int COOKING_PARENT = 270;

    private Constants() {

    }

}
