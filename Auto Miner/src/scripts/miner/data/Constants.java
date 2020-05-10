package scripts.miner.data;

import org.tribot.api2007.types.RSTile;
import scripts.api.types.RSArea;

public final class Constants {

    public static final RSArea MINING_GUILD_MINING_AREA = new RSArea(
      new RSTile(3013, 9713, 0),
      new RSTile(3057, 9729, 0)
    );

    public static final RSArea RIMMINGTON_MINING_AREA = new RSArea(
      new RSTile(2988, 3249, 0),
      new RSTile(2967, 3232, 0)
    );


    public static final RSArea VARROCK_WEST_MINING_AREA = new RSArea(
      new RSTile(3184, 3380, 0),
      new RSTile(3171, 3364, 0)
    );
    public static final RSTile[] VARROCK_WEST_PATH = {
      new RSTile(3180, 3380, 0),
      new RSTile(3175, 3386, 0),
      new RSTile(3171, 3396, 0),
      new RSTile(3171, 3405, 0),
      new RSTile(3171, 3415, 0),
      new RSTile(3171, 3424, 0),
      new RSTile(3180, 3430, 0),
      new RSTile(3185, 3436, 0)
    };


    public static final RSArea VARROCK_EAST_MINING_AREA = new RSArea(
      new RSTile(3280, 3360, 0),
      new RSTile(3291, 3371, 0)
    );
    public static final RSTile[] VARROCK_EAST_PATH = {
      new RSTile(3287, 3370, 0),
      new RSTile(3292, 3378, 0),
      new RSTile(3291, 3389, 0),
      new RSTile(3292, 3401, 0),
      new RSTile(3290, 3410, 0),
      new RSTile(3286, 3417, 0),
      new RSTile(3280, 3422, 0),
      new RSTile(3274, 3427, 0),
      new RSTile(3264, 3427, 0),
      new RSTile(3254, 3420, 0)
    };


    public static final RSArea AL_KHARID_MINING_AREA = new RSArea(
      new RSTile(3306, 3280, 0),
      new RSTile(3293, 3318, 0)
    );

    public static final RSArea CRAFTING_GUILD_MINING_AREA = new RSArea(
      new RSTile(2943, 3276, 0),
      new RSTile(2937, 3291, 0)
    );

    public static final RSArea SHILO_VILLAGE_MINING_AREA = new RSArea(
      new RSTile(2819, 2996, 0),
      new RSTile(2827, 3004, 0)
    );

    public static final RSTile[] SHILO_VILLAGE_PATH = {
      new RSTile(2823, 2998, 0),
      new RSTile(2828, 2986, 0),
      new RSTile(2832, 2973, 0),
      new RSTile(2842, 2965, 0),
      new RSTile(2851, 2955, 0)
    };

    private Constants() {

    }
}
