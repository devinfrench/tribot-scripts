package scripts.cooker;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSItemDefinition;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Breaking;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.EventBlockingOverride;
import org.tribot.script.interfaces.Painting;
import scripts.api.Banking;
import scripts.api.Inventory;
import scripts.api.Login;
import scripts.api.Player;
import scripts.api.concurrency.ExperienceListener;
import scripts.api.script.DecisionTreeScript;
import scripts.api.script.JavaFXGUI;
import scripts.api.script.frameworks.tree.DecisionTree;
import scripts.api.util.*;
import scripts.cooker.data.Location;
import scripts.cooker.data.Vars;
import scripts.cooker.decisions.BankDecisionNode;
import scripts.cooker.decisions.CookingDecisionNode;
import scripts.cooker.decisions.RangeDecisionNode;
import scripts.cooker.decisions.SuppliesDecisionNode;
import scripts.cooker.decisions.wine.MixingDecisionNode;
import scripts.cooker.runnables.BankRunnable;
import scripts.cooker.runnables.CookRunnable;
import scripts.cooker.runnables.CookingRunnable;
import scripts.cooker.runnables.alkharid.WalkBankRunnable;
import scripts.cooker.runnables.alkharid.WalkRangeRunnable;
import scripts.cooker.runnables.wine.MixRunnable;
import scripts.cooker.runnables.wine.MixingRunnable;
import scripts.cooker.session.OSBotsSession;
import scripts.cooker.session.Session;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static scripts.cooker.data.Constants.GRAPES;

@ScriptManifest(category = "Cooking", name = "Auto Cooker", authors = "Encoded", version = 1.0, description = "Local")
public class Cooker extends DecisionTreeScript implements Ending, Painting, EventBlockingOverride, ExperienceListener {

    private Statistics stats;
    private ScriptSettings settings;

    @Override
    public ScriptSession getSession() {
        return new Session(this);
    }

    @Override
    public Painter getPainter(Statistics statistics) {
        return new scripts.cooker.util.Painter(this, statistics, Skills.SKILLS.COOKING);
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
        Vars.get().script = this;
        loadRawSettings();
    }

    private void loadRawSettings() {
        settings = new ScriptSettings(this);
        if (settings.get("rawId") != null) {
            Vars.get().rawId = Integer.parseInt(settings.get("rawId"));
        }
        if (settings.get("rawName") != null) {
            Vars.get().rawName = settings.get("rawName");
        }
    }

    private void saveRawSettings() {
        settings.set("rawId", String.valueOf(Vars.get().rawId));
        settings.set("rawName", Vars.get().rawName);
        settings.save();
    }

    @Override
    public void onInitialLogin() {
        startExperienceListener(SKILLS.COOKING);
        setRaw();
        setLocation();
        if (Vars.get().rawId != GRAPES) {
            SuppliesDecisionNode suppliesDecisionNode = new SuppliesDecisionNode();
            RangeDecisionNode rangeDecisionNode = new RangeDecisionNode();
            CookingDecisionNode cookingDecisionNode = new CookingDecisionNode();
            BankDecisionNode bankDecisionNode = new BankDecisionNode();
            suppliesDecisionNode.setNodes(rangeDecisionNode, bankDecisionNode);
            rangeDecisionNode.setNodes(cookingDecisionNode, new WalkRangeRunnable());
            cookingDecisionNode.setNodes(new CookingRunnable(), new CookRunnable());
            bankDecisionNode.setNodes(new BankRunnable(), new WalkBankRunnable());
            DecisionTree decisionTree = new DecisionTree(suppliesDecisionNode);
            setDecisionTree(decisionTree);
        } else {
            scripts.cooker.decisions.wine.SuppliesDecisionNode suppliesDecisionNode = new scripts.cooker.decisions.wine.SuppliesDecisionNode();
            MixingDecisionNode mixingDecisionNode = new MixingDecisionNode();
            suppliesDecisionNode.setNodes(mixingDecisionNode, new scripts.cooker.runnables.wine.BankRunnable());
            mixingDecisionNode.setNodes(new MixingRunnable(), new MixRunnable());
            DecisionTree decisionTree = new DecisionTree(suppliesDecisionNode);
            setDecisionTree(decisionTree);
        }
        OSBotsSession osBotsSession = new OSBotsSession(this, stats);
        osBotsSession.start();
    }

    private void setRaw() {
        RSItem raw = Inventory.find().nameContains("Raw", "Grapes").getFirst();
        if (raw != null) {
            Vars.get().rawId = raw.getID();
            RSItemDefinition def = raw.getDefinition();
            if (def != null) {
                String name = def.getName();
                if (name != null) {
                    Vars.get().rawName = name;
                }
            }
            Logging.debug("rawId = " + Vars.get().rawId + " rawName = " + Vars.get().rawName);
        } else if (Vars.get().rawId == 0) {
            Logging.critical("Could not determine raw food to cook.");
            shutdown();
        }
    }

    private void setLocation() {
        RSTile playerPos = Player.getPosition();
        if (Banking.Bank.CATHERBY.getArea().getRandomTile().distanceTo(playerPos) < 30) {
            Vars.get().location = Location.CATHERBY;
        } else if (Banking.Bank.NARDAH.getArea().getRandomTile().distanceTo(playerPos) < 30) {
            Vars.get().location = Location.NARDAH;
        } else if (Banking.Bank.AL_KHARID.getArea().getRandomTile().distanceTo(playerPos) < 30) {
            Vars.get().location = Location.AL_KHARID;
        } else if (Banking.Bank.HOSIDIUS_KITCHEN.getArea().getRandomTile().distanceTo(playerPos) < 30) {
            Vars.get().location = Location.HOSIDIUS;
        } else if (playerPos.getPlane() == 1) {
            Vars.get().location = Location.ROGUES_DEN;
        } else if (Vars.get().rawId != GRAPES) {
            Logging.critical("Could not determine location.");
            shutdown();
        }
    }

    @Override
    public void onLoop() {
        if (Camera.getCameraAngle() < 90) {
            Camera.setCameraAngle(General.random(90, 100));
        }
        int c = Camera.getCameraRotation();
        int r = c;
        if (Vars.get().location == Location.CATHERBY && (c < 160 && c > 59 || c > 235 && c < 320)) {
            int i = Player.getName().length() * General.random(1, 2);
            r = i >= 12 ? General.random(325, 360) : General.random(1, 45);
        } else if (Vars.get().location == Location.NARDAH && (c > 217 && c < 300 || c < 105)) {
            r = General.random(105, 215);
        } else if (Vars.get().location == Location.HOSIDIUS && (c > 65 || c < 40)) {
            r = General.random(40, 65);
        } else if (Vars.get().location == Location.ROGUES_DEN && (c < 90 || c > 180)) {
            r = General.random(90, 180);
        }
        if (r != c) {
            Camera.setCameraRotation(r);
        }
    }

    @Override
    public void onEnd() {
        saveRawSettings();
        if (!isRunning() && stats.getTimeRan() >= 180_000) {
            Login.logout(true);
            General.sleep(600);
        }
        super.onEnd();
    }

    @Override
    public void onPaint(Graphics g) {
        super.onPaint(g);
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
    public void experienceGained(SKILLS skill, int xp) {
        if (skill == Skills.SKILLS.COOKING) {
            if (Vars.get().rawId == GRAPES) {
                stats.addCount("cooked", xp / 200);
            } else {
                stats.addCount("cooked", 1);
            }
        }
    }

}
