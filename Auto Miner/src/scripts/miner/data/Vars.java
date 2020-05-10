package scripts.miner.data;

import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.api.Inventory;
import scripts.api.script.DecisionTreeScript;
import scripts.api.types.RSArea;
import scripts.api.util.Statistics;
import scripts.miner.util.AnticipatedLocations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Vars {
    private static final Vars instance = new Vars();

    public DecisionTreeScript script;
    public Statistics stats;
    public Option option;
    public Inventory.DROPPING_PATTERN droppingPattern;
    public boolean isStationary;
    public boolean isTimedActions;
    public boolean isWaitingReactionTime;
    public boolean isSelectingRocks;
    public List<RSTile> rockTileList = Collections.synchronizedList(new ArrayList<>());
    public Location location;
    public RSTile stationaryTile;
    public AnticipatedLocations anticipatedLocations;
    public RSArea miningArea;
    public RSObject nextRock;

    private Vars() {
    }

    public static Vars get() {
        return instance;
    }
}
