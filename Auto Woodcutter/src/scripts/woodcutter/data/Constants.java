package scripts.woodcutter.data;

import org.tribot.api2007.types.RSTile;
import scripts.api.types.RSArea;

public final class Constants {

    /* Draynor */
    public static final RSArea DRAYNOR_WILLOW_AREA = new RSArea(
        new RSTile(3079, 3240, 0),
        new RSTile(3091, 3225, 0)
    );
    public static final RSTile[] DRAYNOR_WILLOW_PATH = {new RSTile(3087, 3236, 0), new RSTile(3092, 3245, 0)};
    /* Port Sarmin */
    public static final RSArea PORT_SARIM_WILLOW_AREA = new RSArea(
        new RSTile(3056, 3257, 0),
        new RSTile(3065, 3250, 0)
    );
    public static final RSTile[] PORT_SARIM_WILLOW_PATH = {new RSTile(3058, 3253, 0), new RSTile(3051, 3246, 0),
        new RSTile(3045, 3235, 0)};
    /* Varrock */
    public static final RSArea VARROCK_WEST_TREE_AREA = new RSArea(
        new RSTile(3173, 3422, 0),
        new RSTile(3155, 3375, 0)
    );
    public static final RSTile[] VARROCK_WEST_TREE_PATH = {new RSTile(3162, 3412, 0), new RSTile(3166, 3420, 0),
        new RSTile(3174, 3429, 0), new RSTile(3185, 3436, 0)};
    public static final RSArea VARROCK_WEST_OAK_AREA = new RSArea(new RSTile[] {
        new RSTile(3159, 3419, 0),
        new RSTile(3159, 3414, 0),
        new RSTile(3163, 3414, 0),
        new RSTile(3163, 3410, 0),
        new RSTile(3172, 3409, 0),
        new RSTile(3172, 3424, 0),
        new RSTile(3165, 3424, 0)
    });
    public static final RSTile[] VARROCK_WEST_OAK_PATH = {new RSTile(3166, 3420, 0), new RSTile(3174, 3429, 0),
        new RSTile(3185, 3436, 0)};
    public static final RSArea VARROCK_YEW_AREA = new RSArea(
        new RSTile(3202, 3506, 0),
        new RSTile(3225, 3498, 0)
    );
    public static final RSTile[] VARROCK_YEW_PATH = {new RSTile(3206, 3502, 0), new RSTile(3197, 3498, 0), new RSTile(
        3191,
        3490,
        0), new RSTile(3181, 3490, 0), new RSTile(3173, 3490, 0), new RSTile(3167, 3490, 0)};
    /* Edgeville */
    public static final RSTile EDGEVILLE_CLOSED_DOOR_TILE = new RSTile(3091, 3470, 0);
    public static final RSArea EDGEVILLE_YEW_AREA = new RSArea(
        new RSTile(3089, 3468, 0),
        new RSTile(3085, 3482, 0)
    );
    public static final RSTile[] EDGEVILLE_YEW_PATH = {new RSTile(3087, 3471, 0), new RSTile(3094, 3479, 0), new RSTile(
        3094,
        3489,
        0)};
    /* Rimmington */
    public static final RSArea RIMMINGTON_YEW_AREA = new RSArea(
        new RSTile(2943, 3236, 0),
        new RSTile(2931, 3223, 0)
    );
    public static final RSTile[] RIMMINGTON_YEW_PATH = {
        new RSTile(2939, 3232, 0),
        new RSTile(2946, 3240, 0),
        new RSTile(2956, 3244, 0),
        new RSTile(2963, 3254, 0),
        new RSTile(2970, 3261, 0),
        new RSTile(2976, 3268, 0),
        new RSTile(2982, 3276, 0),
        new RSTile(2990, 3281, 0),
        new RSTile(2995, 3289, 0),
        new RSTile(2998, 3298, 0),
        new RSTile(3004, 3306, 0),
        new RSTile(3007, 3315, 0),
        new RSTile(3007, 3326, 0),
        new RSTile(3007, 3334, 0),
        new RSTile(3007, 3344, 0),
        new RSTile(3011, 3355, 0)
    };
    /* Catherby */
    public static final RSArea CATHERBY_WILLOW_AREA = new RSArea(
        new RSTile(2779, 3425, 0),
        new RSTile(2789, 3432, 0)
    );
    public static final RSTile[] CATHERBY_WILLOW_PATH = {
        new RSTile(2783, 3428, 0),
        new RSTile(2793, 3434, 0),
        new RSTile(2802, 3435, 0),
        new RSTile(2809, 3441, 0)
    };
    public static final RSArea CATHERBY_YEW_AREA = new RSArea(new RSTile[]{
        new RSTile(2769, 3440, 0),
        new RSTile(2764, 3436, 0),
        new RSTile(2759, 3436, 0),
        new RSTile(2756, 3436, 0),
        new RSTile(2752, 3437, 0),
        new RSTile(2751, 3428, 0),
        new RSTile(2760, 3425, 0),
        new RSTile(2769, 3425, 0),
        new RSTile(2774, 3430, 0),
        new RSTile(2775, 3441, 0)
    });
    public static final RSTile[] CATHERBY_YEW_PATH = {
        new RSTile(2762, 3430, 0),
        new RSTile(2771, 3433, 0),
        new RSTile(2782, 3433, 0),
        new RSTile(2783, 3428, 0),
        new RSTile(2793, 3434, 0),
        new RSTile(2802, 3435, 0),
        new RSTile(2809, 3441, 0)
    };
    /* Seers Village */
    public static final RSArea SEERS_TREE_AREA = new RSArea(new RSTile[]{
        new RSTile(2727, 3452, 0),
        new RSTile(2727, 3446, 0),
        new RSTile(2723, 3446, 0),
        new RSTile(2725, 3449, 0),
        new RSTile(2724, 3453, 0),
        new RSTile(2717, 3455, 0),
        new RSTile(2708, 3450, 0),
        new RSTile(2710, 3436, 0),
        new RSTile(2714, 3428, 0),
        new RSTile(2728, 3434, 0),
        new RSTile(2730, 3441, 0),
        new RSTile(2731, 3446, 0)
    });
    public static final RSTile[] SEERS_TREE_PATH = {
        new RSTile(2718, 3451, 0),
        new RSTile(2719, 3461, 0),
        new RSTile(2718, 3471, 0),
        new RSTile(2718, 3479, 0),
        new RSTile(2725, 3487, 0),
        new RSTile(2724, 3493, 0)
    };
    public static final RSArea SEERS_OAK_AREA = new RSArea(
        new RSTile(2717, 3484, 0),
        new RSTile(2723, 3477, 0)
    );
    public static final RSTile[] SEERS_OAK_PATH = {
        new RSTile(2719, 3480, 0),
        new RSTile(2725, 3486, 0),
        new RSTile(2724, 3493, 0)
    };
    public static final RSArea SEERS_WILLOW_AREA = new RSArea(
        new RSTile(2715, 3506, 0),
        new RSTile(2706, 3516, 0)
    );
    public static final RSTile[] SEERS_WILLOW_PATH = {
        new RSTile(2711, 3510, 0),
        new RSTile(2718, 3502, 0),
        new RSTile(2724, 3493, 0)
    };
    public static final RSArea SEERS_MAPLE_AREA = new RSArea(
        new RSTile(2734, 3496, 0),
        new RSTile(2717, 3505, 0)
    );
    public static final RSTile[] SEERS_MAPLE_PATH = {
        new RSTile(2731, 3500, 0),
        new RSTile(2727, 3493, 0)
    };
    public static final RSArea SEERS_YEW_AREA = new RSArea(
        new RSTile(2704, 3467, 0),
        new RSTile(2717, 3457, 0)
    );
    public static final RSTile[] SEERS_YEW_PATH = {
        new RSTile(2714, 3462, 0),
        new RSTile(2718, 3471, 0),
        new RSTile(2718, 3479, 0),
        new RSTile(2725, 3487, 0),
        new RSTile(2724, 3493, 0)
    };
    public static final RSArea RANGING_GUILD_MAGIC_AREA = new RSArea(
        new RSTile(2699, 3421, 0),
        new RSTile(2688, 3430, 0)
    );
    public static final RSTile[] RANGING_GUILD_MAGIC_PATH = {
        new RSTile(2696, 3425, 0),
        new RSTile(2705, 3432, 0),
        new RSTile(2709, 3441, 0),
        new RSTile(2713, 3449, 0),
        new RSTile(2719, 3457, 0),
        new RSTile(2719, 3465, 0),
        new RSTile(2723, 3474, 0),
        new RSTile(2724, 3484, 0),
        new RSTile(2724, 3493, 0)
    };
    public static final RSArea SORCERERS_TOWER_MAGIC_AREA = new RSArea(
        new RSTile(2707, 3395, 0),
        new RSTile(2697, 3400, 0)
    );
    public static final RSTile[] SORCERERS_TOWER_MAGIC_PATH = {
        new RSTile(2700, 3397, 0),
        new RSTile(2708, 3393, 0),
        new RSTile(2716, 3398, 0),
        new RSTile(2719, 3407, 0),
        new RSTile(2718, 3418, 0),
        new RSTile(2719, 3427, 0),
        new RSTile(2725, 3434, 0),
        new RSTile(2729, 3441, 0),
        new RSTile(2730, 3450, 0),
        new RSTile(2727, 3460, 0),
        new RSTile(2728, 3470, 0),
        new RSTile(2727, 3479, 0),
        new RSTile(2726, 3486, 0),
        new RSTile(2727, 3493, 0)
    };
    /* Barbarian Outpost */
    public static final RSArea BARBARIAN_OUTPOST_WILLOW_AREA = new RSArea(
        new RSTile(2522, 3575, 0),
        new RSTile(2514, 3584, 0)
    );
    public static final RSTile[] BARBARIAN_OUTPOST_WILLOW_PATH = {
        new RSTile(2519, 3579, 0),
        new RSTile(2527, 3571, 0),
        new RSTile(2536, 3573, 0)
    };
    /* Castle Wars */
    public static final RSArea CASTLE_WARS_TEAK_AREA = new RSArea(
        new RSTile(2333, 3050, 0),
        new RSTile(2337, 3046, 0)
    );
    /* Lumbridge */
    public static final RSArea LUMBRIDGE_TREE_AREA = new RSArea(new RSTile[]{
        new RSTile(3204, 3237, 0),
        new RSTile(3203, 3257, 0),
        new RSTile(3192, 3257, 0),
        new RSTile(3191, 3264, 0),
        new RSTile(3180, 3264, 0),
        new RSTile(3169, 3257, 0),
        new RSTile(3166, 3237, 0),
        new RSTile(3174, 3232, 0),
        new RSTile(3177, 3228, 0),
        new RSTile(3199, 3234, 0)
    });
    /* Woodcutting Guild */
    public static final RSArea WOODCUTTING_GUILD_YEW_AREA = new RSArea(new RSTile[]{
        new RSTile(1592, 3482, 0),
        new RSTile(1599, 3483, 0),
        new RSTile(1600, 3497, 0),
        new RSTile(1589, 3498, 0),
        new RSTile(1589, 3485, 0)
    });
    public static final RSTile[] WOODCUTTING_GUILD_YEW_PATH = {
        new RSTile(1594, 3484, 0),
        new RSTile(1592, 3476, 0)
    };

    public static final RSArea WOODCUTTING_GUILD_MAGIC_AREA = new RSArea(
        new RSTile(1577, 3480, 0),
        new RSTile(1583, 3495, 0)
    );
    public static final RSTile[] WOODCUTTING_GUILD_MAGIC_PATH = {
        new RSTile(1581, 3484, 0),
        new RSTile(1592, 3476, 0)
    };

    public static final RSArea WOODCUTTING_GUILD_OAKS_AREA = new RSArea(
        new RSTile(1614, 3515, 0),
        new RSTile(1624, 3506, 0)
    );

    public static final RSTile[] WOODCUTTING_GUILD_OAKS_PATH = {
        new RSTile(1649, 3495, 0),
        new RSTile(1645, 3504, 0),
        new RSTile(1634, 3506, 0),
        new RSTile(1623, 3507, 0)
    };

    private Constants() {

    }

}
