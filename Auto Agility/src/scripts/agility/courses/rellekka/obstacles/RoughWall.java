package scripts.agility.courses.rellekka.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.*;

public class RoughWall extends Obstacle {

    public static final RSTile ROUGH_WALL_TILE = new RSTile(2625, 3677, 0);
    private static final RSArea ROUGH_WALL_AREA = new RSArea(
        new RSTile(2622, 3677, 0),
        new RSTile(2627, 3680, 0)
    );
    private static final RSTile SUCCESS_TILE = new RSTile(2625, 3676, 3);

    public RoughWall() {
        super(14946, new RSTile(2625, 3677, 0), "Rough wall", "Climb", ROUGH_WALL_AREA);
    }

    @Override
    public boolean validate() {
        return Player.getPlane() == 0;
    }

    @Override
    public void traverse() {
        if (Player.isInCombat()) {
            Timing.waitCondition(() -> !Player.isAnimating(), 2400);
        }
        if (!getPosition().isOnScreen() && Player.distanceTo(getPosition()) > 5) {
            if (Walking.walkStraightPath(getPosition())) {
                Mouse.move(Player.getPosition().getHumanHoverPoint());
            }
        }
        RSObject obj = getRSObject();
        if (Interact.with(obj).walkTo(true).cameraTurn().click(getAction())) {
            int distance = Player.distanceTo(obj);
            if (Timing.waitCondition(Player::isAnimating, 2500)) {
                Timing.waitCondition(() -> performHumanActions() && adjustCameraForNextObstacle()
                    && Player.getPosition().equals(SUCCESS_TILE), 4000 + distance * 400);
            }
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        int rotation = Camera.getCameraRotation();
        if (rotation < 205 || rotation > 230) {
            Camera.setCameraRotation(General.random(205, 230));
        }
        return true;
    }
}
