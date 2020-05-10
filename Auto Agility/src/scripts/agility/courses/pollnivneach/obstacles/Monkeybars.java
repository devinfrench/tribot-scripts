package scripts.agility.courses.pollnivneach.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Game;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.types.RSArea;

public class Monkeybars extends Obstacle {

    private static final RSArea MONKEYBARS_AREA = new RSArea(new RSTile[]{
        new RSTile(3366, 2983, 2),
        new RSTile(3363, 2982, 2),
        new RSTile(3361, 2980, 2),
        new RSTile(3355, 2980, 2),
        new RSTile(3355, 2986, 2),
        new RSTile(3360, 2986, 2),
        new RSTile(3362, 2985, 2),
        new RSTile(3363, 2984, 2)

    });
    private static final RSTile SUCCESS_TILE = new RSTile(3358, 2991, 2);

    public Monkeybars() {
        super(14941, new RSTile(3358, 2985, 2), "Monkeybars", "Cross", MONKEYBARS_AREA);
    }

    @Override
    public void traverse() {
        int distance = Player.distanceTo(getPosition());
        if (Interact.with(getRSObject()).walkTo().cameraTurn().click(getAction())
            && Timing.waitValue(Game::getDestination, 1500) != null) {
            if (Timing.waitCondition(() -> performHumanActions() && adjustCameraForNextObstacle()
                && Player.getPosition().equals(SUCCESS_TILE), 7000 + distance * 400)) {
                General.sleep(300, 500);
            }
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        int rotation = Camera.getCameraRotation();
        if (rotation < 345) {
            Camera.setCameraRotation(General.random(345, 359));
        }
        return true;
    }
}
