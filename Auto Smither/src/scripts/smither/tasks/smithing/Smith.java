package scripts.smither.tasks.smithing;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSTile;
import scripts.api.Mouse;
import scripts.api.Objects;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.script.frameworks.task.Priority;
import scripts.api.script.frameworks.task.Task;
import scripts.smither.Smither;
import scripts.smither.data.Vars;

public class Smith implements Task {

    private final RSArea ANVIL_AREA = new RSArea(
      new RSTile(3190, 3427, 0),
      new RSTile(3185, 3423, 0)
    );
    private final RSTile ANVIL_TILE = new RSTile(3188, 3426, 0);

    @Override
    public String toString() {
        return "Attempting to Smith";
    }

    @Override
    public Priority priority() {
        return Priority.LOW;
    }

    @Override
    public boolean validate() {
        return Player.isInArea(ANVIL_AREA) && Inventory.getCount(Vars.get().bar.getId()) >= Vars.get().item.getBarsRequired();
    }

    @Override
    public void execute() {
        if (Vars.get().script.getLastTask() instanceof Smithing) {
            Smither.waitReactionTime();
        }
        RSInterface smithing = Vars.get().item.getInterface();
        if (smithing == null) {
            if (Mouse.click("Smith", Objects.find().atPosition(ANVIL_TILE).nameEquals("Anvil").getFirst())) {
                smithing = Timing.waitValue(() -> Vars.get().item.getInterface(), Player.distanceTo(ANVIL_TILE) * 600 + 1200);
            }
        }
        if (smithing != null) {
            if (smithing.click("Smith")) {
                Timing.waitCondition(Player::isAnimating, 1800);
            }
        }
    }
}
