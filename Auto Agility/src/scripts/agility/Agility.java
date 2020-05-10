package scripts.agility;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Camera.ROTATION_METHOD;
import org.tribot.api2007.Login;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Arguments;
import org.tribot.script.interfaces.Breaking;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.EventBlockingOverride;
import org.tribot.script.interfaces.Painting;
import scripts.agility.courses.Course;
import scripts.agility.courses.canifis.obstacles.TallTree;
import scripts.agility.courses.draynor.obstacles.RoughWall;
import scripts.agility.courses.gnome.obstacles.Log;
import scripts.agility.courses.pollnivneach.obstacles.Basket;
import scripts.agility.courses.seers.obstacles.Wall;
import scripts.agility.data.Vars;
import scripts.agility.session.OSBotsSession;
import scripts.agility.session.Session;
import scripts.api.Game;
import scripts.api.Player;
import scripts.api.concurrency.ExperienceListener;
import scripts.api.concurrency.InventoryListener;
import scripts.api.script.JavaFXGUI;
import scripts.api.script.TaskScript;
import scripts.api.script.frameworks.task.TaskSet;
import scripts.api.util.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import static scripts.agility.data.Constants.MARK_OF_GRACE;

@ScriptManifest(authors = "Encoded", name = "Auto Agility", category = "Agility", version = 1.0, description = "Local")
public class Agility extends TaskScript implements Painting, Breaking, EventBlockingOverride, Ending,
  ExperienceListener, InventoryListener, Arguments {

    private Statistics stats;
    private int stopLevel;

    @Override
    public ScriptSession getSession() {
        return new Session(this);
    }

    @Override
    public Painter getPainter(Statistics statistics) {
        return new scripts.agility.util.Painter(this, statistics, SKILLS.AGILITY);
    }

    @Override
    public JavaFXGUI getGUI() {
        return null;
    }

    @Override
    public Statistics getStatistics() {
        stats = new Statistics();
        Vars.get().stats = stats;
        return stats;
    }

    @Override
    public void onStart() {
        setRandomSolverState(false);
        setAIAntibanState(false);
        Camera.setRotationMethod(ROTATION_METHOD.ONLY_KEYS);
        Vars.get().script = this;
    }

    @Override
    public void onInitialLogin() {
        if (Player.distanceTo(Log.LOG_TILE) <= 100) {
            Vars.get().course = Course.GNOME;
        } else if (Player.distanceTo(RoughWall.ROUGH_WALL_TILE) <= 50) {
            Vars.get().course = Course.DRAYNOR;
        } else if (Player.distanceTo(scripts.agility.courses.varrock.obstacles.RoughWall.ROUGH_WALL_TILE) <= 100) {
            Vars.get().course = Course.VARROCK;
        } else if (Player.distanceTo(TallTree.TALL_TREE_TILE) <= 100) {
            Vars.get().course = Course.CANIFIS;
        } else if (Player.distanceTo(Wall.WALL_TILE) <= 75) {
            Vars.get().course = Course.SEERS;
        } else if (Player.distanceTo(scripts.agility.courses.falador.obstacles.RoughWall.ROUGH_WALL_TILE) <= 50) {
            Vars.get().course = Course.FALADOR;
        } else if (Player.distanceTo(Basket.BASKET_TILE) <= 100) {
            Vars.get().course = Course.POLLNIVNEACH;
        } else if (Player.distanceTo(scripts.agility.courses.rellekka.obstacles.RoughWall.ROUGH_WALL_TILE) <= 50) {
            Vars.get().course = Course.RELLEKKA;
        } else {
            Logging.critical("Unsupported course. Please start the script at one of the supported courses.");
            shutdown();
            return;
        }
        TaskSet taskSet = Vars.get().course.getTaskSet();
        if (stopLevel > 0) {
            taskSet.setStopCondition(() -> SKILLS.AGILITY.getActualLevel() >= stopLevel);
        }
        setTaskSet(taskSet);
        startExperienceListener(SKILLS.AGILITY);
        startInventoryListener();
        OSBotsSession osBotsSession = new OSBotsSession(this, stats);
        osBotsSession.start();
    }

    @Override
    public void onLoop() {
        if (Camera.getCameraAngle() < 87) {
            Camera.setCameraAngle(General.random(85, 97));
        }
    }

    @Override
    public void onEnd() {
        if (!isRunning() && stats.getTimeRan() >= 180_000) {
            Login.logout();
            General.sleep(600);
        }
        super.onEnd();
    }

    @Override
    public void onPaint(Graphics g) {
        super.onPaint(g);
        if (isLocal()) {
            GraphicsUtil.drawString(g, "Plane: " + Player.getPlane(), 10, 120, Color.WHITE);
            GraphicsUtil.drawString(g, "Moving: " + Player.isMoving(), 10, 150, Color.WHITE);
            GraphicsUtil.drawString(g, "Animating: " + Player.isAnimating(), 10, 180, Color.WHITE);
            GraphicsUtil.drawString(g, "Destination: " + Game.getDestination(), 10, 210, Color.WHITE);
            GraphicsUtil.drawString(g, "Position: " + Player.getPosition(), 10, 240, Color.WHITE);
        }
    }

    @Override
    public EventBlockingOverride.OVERRIDE_RETURN overrideKeyEvent(KeyEvent arg0) {
        return EventBlockingOverride.OVERRIDE_RETURN.PROCESS;
    }

    @Override
    public EventBlockingOverride.OVERRIDE_RETURN overrideMouseEvent(MouseEvent e) {
        if (painter.displayToggleContains(e.getPoint())) {
            if (e.getID() == 500) {
                e.consume();
                painter.show(!painter.getShowPaint());
                return EventBlockingOverride.OVERRIDE_RETURN.DISMISS;
            }
            if (e.getID() == 501) {
                return EventBlockingOverride.OVERRIDE_RETURN.DISMISS;
            }
        }
        return EventBlockingOverride.OVERRIDE_RETURN.PROCESS;
    }

    @Override
    public void passArguments(HashMap<String, String> arguments) {
        if (arguments.containsKey("custom_input") || arguments.containsKey("autostart")) {
            String args = arguments.containsKey("custom_input") ? arguments.get("custom_input") : arguments.get("autostart");
            try {
                stopLevel = Integer.parseInt(args.trim());
                Logging.info("Stopping at level: " + stopLevel);
            } catch (NumberFormatException ignored) {

            }
        }
    }

    @Override
    public void experienceGained(SKILLS skill, int xp) {
        if (skill == Skills.SKILLS.AGILITY) {
            switch (Vars.get().course) {
                case GNOME:
                    if (xp >= 46) {
                        stats.addCount("laps", 1);
                    }
                    break;
                case DRAYNOR:
                    if (xp == 79) {
                        stats.addCount("laps", 1);
                    }
                    break;
                case VARROCK:
                    if (xp == 125) {
                        stats.addCount("laps", 1);
                    }
                    break;
                case CANIFIS:
                    if (xp == 175) {
                        stats.addCount("laps", 1);
                    }
                    break;
                case FALADOR:
                    if (xp == 180) {
                        stats.addCount("laps", 1);
                    }
                    break;
                case SEERS:
                    if (xp == 435) {
                        stats.addCount("laps", 1);
                    }
                    break;
                case POLLNIVNEACH:
                    if (xp == 540) {
                        stats.addCount("laps", 1);
                    }
                    break;
                case RELLEKKA:
                    if (xp == 475) {
                        stats.addCount("laps", 1);
                    }
                    break;
            }
        }
    }

    @Override
    public void levelGained(SKILLS skill, int amount) {

    }

    @Override
    public void inventoryItemAdded(int id, int count) {
        if (id == MARK_OF_GRACE && count == 1) {
            stats.addCount("marks", count);
        }
    }

    @Override
    public void inventoryItemRemoved(int id, int count) {

    }

    @Override
    public void onBreakStart(long l) {
        super.onBreakStart(l);
    }

    @Override
    public void onBreakEnd() {
        super.onBreakEnd();
    }
}
