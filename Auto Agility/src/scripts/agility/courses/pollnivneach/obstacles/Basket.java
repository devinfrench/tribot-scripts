package scripts.agility.courses.pollnivneach.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.*;

public class Basket extends Obstacle {

    public static final RSTile BASKET_TILE = new RSTile(3352, 2962, 0);
    private static final RSArea BASKET_AREA = new RSArea(new RSTile[]{
        new RSTile(3349, 2962, 0),
        new RSTile(3349, 2959, 0),
        new RSTile(3352, 2958, 0),
        new RSTile(3353, 2961, 0),
        new RSTile(3354, 2961, 0),
        new RSTile(3354, 2964, 0)
    });
    private static final RSTile SUCCESS_TILE = new RSTile(3351, 2964, 1);

    public Basket() {
        super(14935, new RSTile(3351, 2962, 0), "Basket", "Climb-on", BASKET_AREA);
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
        if (!getPosition().isOnScreen() && Player.distanceTo(BASKET_TILE) > 5) {
            if (Walking.walkStraightPath(BASKET_TILE)) {
                Mouse.move(Player.getPosition().getHumanHoverPoint());
            }
        }
        RSObject obj = getRSObject();
        if (Interact.with(obj).walkTo(true).cameraTurn().click(getAction())) {
            int distance = Player.distanceTo(obj);
            if (Timing.waitValue(Game::getDestination, 1500) != null) {
                Timing.waitCondition(() -> performHumanActions() && adjustCameraForNextObstacle()
                    && Player.getPosition().equals(SUCCESS_TILE), 4000 + distance * 400);
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
