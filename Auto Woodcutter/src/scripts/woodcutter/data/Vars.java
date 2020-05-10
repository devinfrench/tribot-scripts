package scripts.woodcutter.data;

import org.tribot.api2007.types.RSTile;
import scripts.api.Inventory;
import scripts.api.script.DecisionTreeScript;
import scripts.api.types.RSArea;
import scripts.api.util.Statistics;
import scripts.woodcutter.util.AnticipatedLocations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Vars {
    private static final Vars instance = new Vars();

    public DecisionTreeScript script;
    public Statistics stats;
    public Location location;
    public Tree tree;
    public Option option;
    public boolean isSelectingTrees;
    public boolean isWaitingReactionTime;
    public boolean isTimedActions;
    public boolean isPickingUpNests;
    public Inventory.DROPPING_PATTERN droppingPattern;
    public List<RSTile> treeTileList = Collections.synchronizedList(new ArrayList<>());
    public AnticipatedLocations anticipatedLocations;
    public RSArea customTreeArea;

    private Vars() {
    }

    public static Vars get() {
        return instance;
    }
}
