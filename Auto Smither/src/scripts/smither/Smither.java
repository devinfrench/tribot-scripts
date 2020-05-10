package scripts.smither;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Skills;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Breaking;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.EventBlockingOverride;
import org.tribot.script.interfaces.Painting;
import scripts.api.Banking;
import scripts.api.Inventory;
import scripts.api.Login;
import scripts.api.Timing;
import scripts.api.concurrency.ExperienceListener;
import scripts.api.ext.Failsafe;
import scripts.api.script.JavaFXGUI;
import scripts.api.script.TaskScript;
import scripts.api.script.frameworks.task.TaskSet;
import scripts.api.util.Logging;
import scripts.api.util.Painter;
import scripts.api.util.ScriptSession;
import scripts.api.util.Statistics;
import scripts.smither.data.Vars;
import scripts.smither.gui.GUI;
import scripts.smither.session.OSBotsSession;
import scripts.smither.session.Session;
import scripts.smither.tasks.smelting.Smelt;
import scripts.smither.tasks.smelting.Smelting;
import scripts.smither.tasks.smelting.WalkToEdgevilleBank;
import scripts.smither.tasks.smelting.WalkToEdgevilleFurnace;
import scripts.smither.tasks.smithing.Smith;
import scripts.smither.tasks.smithing.Smithing;
import scripts.smither.tasks.smithing.WalkToVarrockAnvil;
import scripts.smither.tasks.smithing.WalkToVarrockBank;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

@ScriptManifest(category = "Smithing", name = "Auto Smither", authors = "Encoded", version = 2018, description = "Local")
public class Smither extends TaskScript implements Breaking, Ending, Painting, ExperienceListener, EventBlockingOverride {

    private Statistics stats;

    public static void waitReactionTime() {
        General.sleep(General.randomSD(900, 6000, 4500, 2000));
    }

    @Override
    public ScriptSession getSession() {
        return new Session(this);
    }

    @Override
    public Painter getPainter(Statistics statistics) {
        return new scripts.smither.util.Painter(this, statistics, Skills.SKILLS.SMITHING);
    }

    @Override
    public JavaFXGUI getGUI() {
        return new GUI(Vars.get());
    }

    @Override
    public Statistics getStatistics() {
        stats = new Statistics();
        Vars.get().stats = stats;
        return stats;
    }

    @Override
    public void onStart() {
        Vars.get().script = this;
    }

    @Override
    public void onInitialLogin() {
        startExperienceListener(Skills.SKILLS.SMITHING);
        if (Vars.get().isSmelting) {
            setTaskSet(new TaskSet(
              new scripts.smither.tasks.smelting.Bank(),
              new Smelt(),
              new Smelting(),
              new WalkToEdgevilleBank(),
              new WalkToEdgevilleFurnace()
            ));
        } else {
            setTaskSet(new TaskSet(
              new scripts.smither.tasks.smithing.Bank(),
              new Smith(),
              new Smithing(),
              new WalkToVarrockBank(),
              new WalkToVarrockAnvil()
            ));
            if (!Timing.waitCondition(() -> Inventory.contains(2347), 3000)) {
                Logging.critical("Please start the script with a hammer.");
                shutdown();
                return;
            }
        }
        OSBotsSession osBotsSession = new OSBotsSession(this, stats);
        osBotsSession.start();
    }

    @Override
    public void onLoop() {
        Mouse.setSpeed(General.randomSD(130, 25));
        int rotation = Camera.getCameraRotation();
        if (Vars.get().isSmelting && (rotation < 175 || rotation > 195)) {
            waitReactionTime();
            Camera.setCameraRotation(General.random(175, 195));
        }
        if (!Vars.get().isSmelting && (rotation < 250 || rotation > 300)) {
            waitReactionTime();
            Camera.setCameraRotation(General.random(250, 300));
        }
        if (Camera.getCameraAngle() < 90) {
            Camera.setCameraAngle(General.random(90, 100));
        }
        Failsafe.closeWorldMap();
        if (Banking.Bank.EDGEVILLE.containsPlayer()
          || Banking.Bank.VARROCK_WEST.containsPlayer()) {
            Failsafe.closeCollectionInterface();
            Failsafe.closePollInterface();
        }
    }

    @Override
    public void experienceGained(Skills.SKILLS skill, int amount) {
        if (skill == Skills.SKILLS.SMITHING) {
            stats.addCount("crafted", 1);
        }
    }

    @Override
    public void onEnd() {
        if (!isRunning && stats.getTimeRan() >= 120_000) {
            if (Login.logout(true)) {
                General.sleep(600);
            }
        }
        super.onEnd();
    }

    @Override
    public void onPaint(Graphics g) {
        super.onPaint(g);
    }

    @Override
    public void onBreakStart(long l) {
        super.onBreakStart(l);
    }

    @Override
    public void onBreakEnd() {
        super.onBreakEnd();
    }

    @Override
    public EventBlockingOverride.OVERRIDE_RETURN overrideMouseEvent(MouseEvent e) {
        if (painter != null && painter.displayToggleContains(e.getPoint())) {
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
    public EventBlockingOverride.OVERRIDE_RETURN overrideKeyEvent(KeyEvent keyEvent) {
        return EventBlockingOverride.OVERRIDE_RETURN.PROCESS;
    }
}