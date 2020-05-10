package scripts.agility.tasks;

import scripts.agility.data.Vars;
import scripts.api.ABCUtil;
import scripts.api.Inventory;
import scripts.api.Mouse;
import scripts.api.Timing;
import scripts.api.script.frameworks.task.Priority;
import scripts.api.script.frameworks.task.Task;
import scripts.api.util.Logging;

public class Heal implements Task {

    @Override
    public Priority priority() {
        return Priority.HIGH;
    }

    @Override
    public boolean validate() {
        return ABCUtil.shouldEat();
    }

    @Override
    public void execute() {
        if (eat()) {
            ABCUtil.generateEatAtPercentage();
        } else if (Inventory.find().actionEquals("Eat").getAll().length == 0) {
            Logging.critical("Out of food. Ending script.");
            Vars.get().script.shutdown();
        }
    }

    private boolean eat() {
        return Mouse.click("Eat", Inventory.find().actionEquals("Eat").getAll())
          && Timing.waitCondition(() -> !ABCUtil.shouldEat(), 900);
    }

    @Override
    public String toString() {
        return "Healing";
    }
}
