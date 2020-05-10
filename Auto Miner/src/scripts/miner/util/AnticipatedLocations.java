package scripts.miner.util;

import org.tribot.api.General;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSObjectDefinition;
import org.tribot.api2007.types.RSTile;
import scripts.api.Objects;
import scripts.api.Player;
import scripts.miner.data.Vars;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class AnticipatedLocations extends Thread {

    private ConcurrentHashMap<RSObject, Long> depletedRocksMap = new ConcurrentHashMap<>();
    private Vars vars;

    public AnticipatedLocations(Vars vars) {
        this.vars = vars;
        Thread thread = new Thread(this);
        thread.setName("Auto Miner Anticipated Locations Thread");
        thread.start();
    }

    @Override
    public void run() {
        while (vars.script.isRunning()) {
            General.sleep(600);
            RSObject[] rocks = getDepletedRocks();
            removeInvalidRocks(rocks);
            addRocks(rocks);
        }
    }

    private RSObject[] getDepletedRocks() {
        return Objects.find().nameEquals("Rocks").filter(rock -> {
            RSObjectDefinition definition = rock.getDefinition();
            return definition != null
              && vars.rockTileList.contains(rock.getPosition())
              && definition.getModifiedColors().length == 0;
        }).getAll();
    }

    private void removeInvalidRocks(RSObject[] rocks) {
        depletedRocksMap.keySet()
          .stream()
          .filter(rock -> !Arrays.asList(rocks).contains(rock))
          .forEach(depletedRocksMap::remove);
    }

    private void addRocks(RSObject[] rocks) {
        long t = System.currentTimeMillis();
        for (RSObject rock : rocks) {
            if (!depletedRocksMap.containsKey(rock)) {
                depletedRocksMap.put(rock, t);
            }
        }
    }

    public RSObject getAnticipatedRock() {
        ConcurrentHashMap.Entry<RSObject, Long> max = null;
        for (ConcurrentHashMap.Entry<RSObject, Long> entry : depletedRocksMap.entrySet()) {
            if (max == null) {
                max = entry;
            }
            int compare = entry.getValue().compareTo(max.getValue());
            RSTile pos = Player.getPosition();
            if (compare < 0 ||
              compare == 0 && pos.distanceTo(entry.getKey()) < pos.distanceTo(max.getKey())) {
                max = entry;
            }
        }
        return max != null ? max.getKey() : null;
    }

}
