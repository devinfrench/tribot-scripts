package scripts.smither.tasks.smelting;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.api.ABCUtil;
import scripts.api.Banking;
import scripts.api.Objects;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.Walking;
import scripts.api.script.frameworks.task.Priority;
import scripts.api.script.frameworks.task.Task;
import scripts.smither.Smither;
import scripts.smither.data.Vars;

public class WalkToEdgevilleBank implements Task {

    private final RSTile EDGEVILLE_BANK_TILE = new RSTile(3096, 3494, 0);
    private final RSTile BANK_BOOTH_TILE = new RSTile(3096, 3493, 0);

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
        return !Vars.get().bar.hasOreRequirements() && !Banking.Bank.EDGEVILLE.containsPlayer();
    }

    @Override
    public void execute() {
        if (Vars.get().script.getLastTask() instanceof Smelting) {
            Smither.waitReactionTime();
        }
        RSObject booth = Objects.find(Player.distanceTo(BANK_BOOTH_TILE) + 3).atPosition(BANK_BOOTH_TILE).nameEquals("Bank booth").getFirst();
        if (booth != null && booth.isClickable()) {
            openBank(booth);
        } else {
            walk();
        }
    }

    private void openBank(RSObject booth) {
        if (Clicking.click("Bank", booth)) {
            if (Player.distanceTo(booth) > 2) {
                Timing.waitCondition(Player::isMoving, 1500);
            }
            Timing.waitCondition(Banking::isBankScreenOpen, Player.distanceTo(booth) * 600 + 1200);
        }
    }

    private void walk() {
        ABCUtil.performRunActivation();
        if (Walking.walkTo(EDGEVILLE_BANK_TILE)) {
            RSTile dest = Timing.waitValue(scripts.api.Game::getDestination, 1200);
            if (dest != null && EDGEVILLE_BANK_TILE.distanceTo(dest) > 2) {
                walk();
            } else {
                Timing.waitCondition(Banking.Bank.EDGEVILLE::containsPlayer, Player.distanceTo(EDGEVILLE_BANK_TILE) * 600 + 1500);
            }
        } else {
            Camera.setCameraRotation(General.random(160, 180));
        }
    }

}

