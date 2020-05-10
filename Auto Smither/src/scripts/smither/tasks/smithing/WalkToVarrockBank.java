package scripts.smither.tasks.smithing;

import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.api.*;
import scripts.api.script.frameworks.task.Priority;
import scripts.api.script.frameworks.task.Task;
import scripts.smither.Smither;
import scripts.smither.data.Vars;

public class WalkToVarrockBank implements Task {

    private final RSTile BANK_TILE = new RSTile(3185, 3436, 0);

    @Override
    public String toString() {
        return "Walking to Bank";
    }

    @Override
    public Priority priority() {
        return Priority.LOW;
    }

    @Override
    public boolean validate() {
        return !Banking.Bank.VARROCK_WEST.containsPlayer()
          && Inventory.getCount(Vars.get().bar.getId()) < Vars.get().item.getBarsRequired();
    }

    @Override
    public void execute() {
        if (Vars.get().script.getLastTask() instanceof Smithing) {
            Smither.waitReactionTime();
        }
        walk();
    }

    private void walk() {
        RSObject booth = Objects.find().nameEquals("Bank booth").getFirst();
        if (booth != null) {
            if (Interact.with(booth).walkTo().cameraTurn().click("Bank")) {
                Timing.waitCondition(Banking::isBankScreenOpen, Player.distanceTo(booth) * 600 + 1500);
            }
        } else {
            if (Projection.isInMinimap(BANK_TILE)) {
                if (Walking.walkTo(BANK_TILE)) {
                    Timing.waitCondition(Banking.Bank.VARROCK_WEST::containsPlayer, Player.distanceTo(BANK_TILE) * 600 + 1500);
                }
            } else {
                Walking.walkStraightPath(BANK_TILE);
            }
        }
    }

}
