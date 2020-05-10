package scripts.smither.tasks.smelting;

import org.tribot.api.General;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSTile;
import scripts.api.Inventory;
import scripts.api.Keyboard;
import scripts.api.Mouse;
import scripts.api.Objects;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.script.frameworks.task.Priority;
import scripts.api.script.frameworks.task.Task;
import scripts.smither.Smither;
import scripts.smither.data.Bar;
import scripts.smither.data.Vars;

public class Smelt implements Task {

    private final RSArea FURNACE_AREA = new RSArea(new RSTile[]{
      new RSTile(3110, 3496, 0),
      new RSTile(3110, 3502, 0),
      new RSTile(3105, 3502, 0),
      new RSTile(3105, 3496, 0)
    });
    private final RSTile FURNACE_TILE = new RSTile(3110, 3499, 0);

    @Override
    public String toString() {
        return "Attempting to Smelt";
    }

    @Override
    public Priority priority() {
        return Priority.LOW;
    }

    @Override
    public boolean validate() {
        return Vars.get().bar.hasOreRequirements() && Player.isInArea(FURNACE_AREA);
    }

    @Override
    public void execute() {
        if (Vars.get().script.getLastTask() instanceof Smelting) {
            Smither.waitReactionTime();
        }
        smelt();
    }

    private void smelt() {
        RSInterface smelt = Vars.get().bar.getInterface();
        if (smelt == null) {
            if (Mouse.click("Smelt", Objects.find().atPosition(FURNACE_TILE).nameEquals("Furnace").getFirst())) {
                smelt = Timing.waitValue(() -> Vars.get().bar.getInterface(), Player.distanceTo(FURNACE_TILE) * 600 + 1800);
            }
        }
        if (smelt != null) {
            clickMake(smelt);
        }
    }

    private void clickMake(RSInterface smelt) {
        if (Vars.get().bar == Bar.CANNONBALL) {
            General.sleep(200, 600);
            Keyboard.pressSpace();
            Timing.waitCondition(Player::isAnimating, 1800);
        } else {
            if (smelt.click("Smelt")) {
                Timing.waitCondition(Player::isAnimating, 1800);
            }
        }
    }

}
