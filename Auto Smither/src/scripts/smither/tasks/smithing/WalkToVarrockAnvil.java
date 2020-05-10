package scripts.smither.tasks.smithing;

import org.tribot.api.Clicking;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.api.*;
import scripts.api.script.frameworks.task.Priority;
import scripts.api.script.frameworks.task.Task;
import scripts.smither.data.Vars;

import java.awt.*;

public class WalkToVarrockAnvil implements Task {

    private final RSTile ANVIL_TILE = new RSTile(3187, 3426, 0);
    private final RSArea ANVIL_AREA = new RSArea(
      new RSTile(3190, 3427, 0),
      new RSTile(3185, 3423, 0)
    );

    @Override
    public String toString() {
        return "Walking to Anvil";
    }

    @Override
    public Priority priority() {
        return Priority.LOW;
    }

    @Override
    public boolean validate() {
        return !Player.isInArea(ANVIL_AREA)
          && Inventory.getCount(Vars.get().bar.getId()) >= Vars.get().item.getBarsRequired();
    }

    @Override
    public void execute() {
        walk();
    }

    private void walk() {
        RSObject anvil = Objects.find().nameEquals("Anvil").getFirst();
        if (isOnScreen(anvil)) {
            if (Interact.with(anvil).walkTo().cameraTurn().click("Smith")) {
                Timing.waitCondition(() -> Vars.get().item.getInterface() != null, Player.distanceTo(ANVIL_TILE) * 600 + 1500);
            }
        } else {
            if (Projection.isInMinimap(ANVIL_TILE)) {
                if (Walking.walkTo(ANVIL_TILE)) {
                    RSTile dest = Timing.waitValue(Game::getDestination, 1200);
                    if (dest != null && !ANVIL_AREA.contains(dest)) {
                        walk();
                    } else {
                        Timing.waitCondition(() -> Player.isInArea(ANVIL_AREA), Player.distanceTo(ANVIL_TILE) * 600 + 1500);
                    }
                }
            } else {
                Walking.walkStraightPath(ANVIL_TILE);
            }
        }
    }

    private boolean isOnScreen(Positionable positionable) {
        if (positionable == null) {
            return false;
        }
        RSTile tile = positionable.getPosition();
        RSInterface bank = Interfaces.get(12);
        if (bank != null) {
            Point p = Projection.tileToScreen(tile, 0);
            Rectangle r = bank.getAbsoluteBounds();
            if (r != null && r.contains(p)) {
                return false;
            }
        }
        return tile.isOnScreen();
    }
}
