package scripts.agility.courses;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.api.ABCUtil;
import scripts.api.GroundItems;
import scripts.api.Interact;
import scripts.api.Objects;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.script.frameworks.task.Priority;
import scripts.api.script.frameworks.task.Task;

import static scripts.agility.data.Constants.MARK_OF_GRACE;

public abstract class Obstacle implements Task {

    private int id;
    private RSTile position;
    private String name;
    private String action;
    private RSArea area;

    public Obstacle(int id, RSTile position, String name, String action, RSArea area) {
        this.id = id;
        this.position = position;
        this.name = name;
        this.action = action;
        this.area = area;
    }

    @Override
    public Priority priority() {
        return Priority.NONE;
    }

    @Override
    public boolean validate() {
        return Player.isInArea(getArea()) && Player.getPlane() > 0;
    }

    @Override
    public void execute() {
        ABCUtil.generateTrackers(6000, true);
        ABCUtil.performRunActivation();
        if (isMarkOfGrace()) {
            lootMarkOfGrace();
        } else {
            traverse();
        }
    }

    public int getId() {
        return id;
    }

    public RSTile getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public String getAction() {
        return action;
    }

    public RSArea getArea() {
        return area;
    }

    public RSObject getRSObject() {
        return Objects.find()
//          .idEquals(getId())
          .atPosition(getPosition())
          .nameEquals(getName())
          .actionEquals(getAction())
          .getFirst();
    }

    public abstract void traverse();

    public abstract boolean adjustCameraForNextObstacle();

    protected boolean performHumanActions() {
        ABCUtil.performMoveMouse();
        ABCUtil.performExamineEntity();
        ABCUtil.performRightClick();
        return true;
    }

    private boolean isMarkOfGrace() {
        return getArea() != null && (!Inventory.isFull() || Inventory.getCount(MARK_OF_GRACE) > 0)
          && GroundItems.find().idEquals(MARK_OF_GRACE).inArea(getArea()).getAll().length > 0;
    }

    private void lootMarkOfGrace() {
        RSGroundItem mark = GroundItems.find()
          .idEquals(MARK_OF_GRACE)
          .inArea(getArea())
          .getFirst();
        int c = Inventory.getCount(MARK_OF_GRACE);
        if (Interact.with(mark).walkTo().cameraTurn().click("Take")) {
            Timing.waitCondition(() -> Inventory.getCount(MARK_OF_GRACE) > c, 4000);
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}
