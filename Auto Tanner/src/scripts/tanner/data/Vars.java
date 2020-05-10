package scripts.tanner.data;

import scripts.api.script.DecisionTreeScript;
import scripts.api.util.Statistics;

public class Vars {

    private static final Vars instance = new Vars();
    public DecisionTreeScript script;
    public Statistics stats;
    public Hide hide;

    public static Vars get() {
        return instance;
    }

    private Vars() {

    }

}
