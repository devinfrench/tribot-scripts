package scripts.agility.courses.gnome.obstacles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.agility.courses.Obstacle;
import scripts.api.Interact;
import scripts.api.Player;
import scripts.api.Timing;
import scripts.api.Walking;

public class Log extends Obstacle {

    public static final RSTile LOG_TILE = new RSTile(2474, 3436, 0);

    public Log() {
        super(23145, new RSTile(2474, 3435, 0), "Log balance", "Walk-across", null);
    }

    @Override
    public boolean validate() {
        return Player.getPlane() == 0 && Player.getPosition().getY() >= 3432;
    }

    @Override
    public void traverse() {
        RSObject obj = getRSObject();
        if (Interact.with(getRSObject()).walkTo(true).cameraTurn().click(getAction())) {
            int distance = Player.distanceTo(obj);
            if (Timing.waitCondition(Player::isMoving, 3000 + distance * 300)) {
                Timing.waitCondition(() -> performHumanActions()
                  && adjustCameraForNextObstacle()
                  && Player.getPosition().getY() <= 3429
                  && !Player.isMoving()
                  && !Player.isAnimating(), 12000);
            }
        } else if (Player.distanceTo(LOG_TILE) > 5) {
            Walking.walkStraightPath(LOG_TILE);
        }
    }

    @Override
    public boolean adjustCameraForNextObstacle() {
        int rotation = Camera.getCameraRotation();
        if (rotation < 145 || rotation > 225) {
            Camera.setCameraRotation(General.random(145, 225));
        }
        return true;
    }
}
