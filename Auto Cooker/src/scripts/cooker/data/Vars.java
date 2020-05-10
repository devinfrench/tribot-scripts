package scripts.cooker.data;

import scripts.api.script.DecisionTreeScript;
import scripts.api.util.Statistics;

public class Vars {

    private static final Vars instance = new Vars();
    public DecisionTreeScript script;
    public Statistics stats;
    public Location location;
    public int rawId;
    public String rawName;

    private Vars() {
    }

    public static Vars get() {
        return instance;
    }

}
