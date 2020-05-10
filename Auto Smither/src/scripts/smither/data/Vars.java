package scripts.smither.data;

import scripts.api.script.TaskScript;
import scripts.api.util.Statistics;

public class Vars {
    private static final Vars instance = new Vars();

    public TaskScript script;
    public Statistics stats;
    public Bar bar;
    public Item item;
    public boolean isSmelting;


    private Vars() {
    }

    public static Vars get() {
        return instance;
    }
}
