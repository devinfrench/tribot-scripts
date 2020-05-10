package scripts.smither.tasks.smelting;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.api.ABCUtil;
import scripts.api.Banking;
import scripts.api.Game;
import scripts.api.Objects;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.Walking;
import scripts.api.script.frameworks.task.Priority;
import scripts.api.script.frameworks.task.Task;
import scripts.api.util.Logging;
import scripts.smither.data.Vars;

public class WalkToEdgevilleFurnace implements Task {

    private final RSTile FURNACE_WALKABLE_TILE = new RSTile(3109, 3499, 0);
    private final RSArea FURNACE_AREA = new RSArea(
      new RSTile(3105, 3501, 0),
      new RSTile(3110, 3496, 0)
    );
    private final RSTile FURNACE_TILE = new RSTile(3110, 3499, 0);

    @Override
    public String toString() {
        return "Walking to Furnace";
    }

    @Override
    public Priority priority() {
        return Priority.LOW;
    }

    @Override
    public boolean validate() {
        return Vars.get().bar.hasOreRequirements() && !Player.isInArea(FURNACE_AREA);
    }

    @Override
    public void execute() {
        RSObject furnace = Objects.find(Player.distanceTo(FURNACE_TILE) + 3).atPosition(FURNACE_TILE).nameEquals("Furnace").getFirst();
        if (furnace != null && furnace.isClickable()) {
            if (Banking.isBankScreenOpen()) {
                Banking.close(true);
            }
            if (!Banking.isBankScreenOpen()) {
                clickFurnace(furnace);
            }
        } else {
            walk();
        }
    }

    private void clickFurnace(RSObject furnace) {
        if (Clicking.click("Smelt", furnace)) {
            if (Player.distanceTo(furnace) > 2) {
                Timing.waitCondition(Player::isMoving, 1500);
            }
            Timing.waitCondition(() -> Vars.get().bar.getInterface() != null, Player.distanceTo(furnace) * 600 + 1200);
        }
    }

    private void walk() {
        ABCUtil.performRunActivation();
        if (Walking.walkTo(FURNACE_WALKABLE_TILE)) {
            RSTile dest = Timing.waitValue(Game::getDestination, 1200);
            if (dest != null && !FURNACE_AREA.contains(dest)) {
                Logging.debug(dest.toString());
                walk();
            } else {
                Timing.waitCondition(() -> Player.isInArea(FURNACE_AREA), Player.distanceTo(FURNACE_WALKABLE_TILE) * 600 + 1200);
            }
        } else {
            Camera.setCameraRotation(General.random(160, 180));
        }
    }

}

