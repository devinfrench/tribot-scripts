package scripts.woodcutter.util;

import org.tribot.api.General;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.api.Objects;
import scripts.api.Player;
import scripts.api.entities.RSObjectEntity;
import scripts.woodcutter.data.Vars;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class AnticipatedLocations extends Thread {

    private ConcurrentHashMap<RSObject, Long> stumpsMap = new ConcurrentHashMap<>();
    private Vars vars;
    private int stumpId;

    public AnticipatedLocations(Vars vars) {
        this.vars = vars;
        Thread thread = new Thread(this);
        thread.setName("Auto Woodcutter Anticipated Locations Thread");
        thread.start();
    }

    @Override
    public void run() {
        while (vars.script.isRunning()) {
            General.sleep(1800);
            RSObject[] stumps = getStumps();
            removeInvalidStumps(stumps);
            addStumps(stumps);
        }
    }

    private RSObject[] getStumps() {
        RSObjectEntity supplier = Objects.find();
        if (stumpId > 0) {
            supplier = supplier.idEquals(stumpId);
        } else {
            supplier = supplier.indexCountEquals(vars.tree.getStumpIndexCount());
        }
        if (vars.location != null) {
            supplier = supplier.inArea(vars.location.getTreeArea());
        } else {
            supplier = supplier.filter(stump -> vars.treeTileList.contains(stump.getPosition()));
        }
        RSObject[] stumps = supplier.getAll();
        if (stumps.length > 0 && stumpId <= 0) {
            stumpId = stumps[0].getID();
        }
        return stumps;
    }

    private void removeInvalidStumps(RSObject[] stumps) {
        stumpsMap.keySet()
            .stream()
            .filter(stump -> !Arrays.asList(stumps).contains(stump))
            .forEach(stumpsMap::remove);
    }

    private void addStumps(RSObject[] stumps) {
        long t = System.currentTimeMillis();
        for (RSObject stump : stumps) {
            if (!stumpsMap.containsKey(stump)) {
                stumpsMap.put(stump, t);
            }
        }
    }

    public RSObject getAnticipatedTree() {
        ConcurrentHashMap.Entry<RSObject, Long> max = null;
        for (ConcurrentHashMap.Entry<RSObject, Long> entry : stumpsMap.entrySet()) {
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
