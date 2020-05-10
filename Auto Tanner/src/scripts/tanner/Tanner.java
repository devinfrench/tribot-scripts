package scripts.tanner;

import java.awt.Graphics;
import java.util.HashMap;
import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Arguments;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.Painting;
import scripts.api.Inventory;
import scripts.api.Login;
import scripts.api.Timing;
import scripts.api.script.DecisionTreeScript;
import scripts.api.script.JavaFXGUI;
import scripts.api.script.frameworks.tree.DecisionTree;
import scripts.api.util.Logging;
import scripts.api.util.Painter;
import scripts.api.util.ScriptSession;
import scripts.api.util.Statistics;
import scripts.tanner.data.Hide;
import scripts.tanner.data.Vars;
import scripts.tanner.decisions.BankDecisionNode;
import scripts.tanner.decisions.SuppliesDecisionNode;
import scripts.tanner.decisions.TannerDecisionNode;
import scripts.tanner.runnables.BankRunnable;
import scripts.tanner.runnables.TanRunnable;
import scripts.tanner.runnables.WalkBankRunnable;
import scripts.tanner.runnables.WalkTannerRunnable;
import scripts.tanner.session.OSBotsSession;
import scripts.tanner.session.Session;

@ScriptManifest(category = "Tanner", name = "Auto Tanner", authors = "Encoded", version = 1.0, description = "Local")
public class Tanner extends DecisionTreeScript implements Ending, Painting, Arguments {

    private Statistics stats;

    @Override
    public ScriptSession getSession() {
        return new Session(this);
    }

    @Override
    public Painter getPainter(Statistics statistics) {
        return new scripts.tanner.util.Painter(this, statistics, SKILLS.CRAFTING);
    }

    @Override
    public JavaFXGUI getGUI() {
        return null;
    }

    @Override
    public Statistics getStatistics() {
        stats = new Statistics();
        return stats;
    }

    @Override
    public void onStart() {
        Vars.get().script = this;
        Vars.get().stats = stats;
        SuppliesDecisionNode suppliesDecisionNode = new SuppliesDecisionNode();
        TannerDecisionNode tannerDecisionNode = new TannerDecisionNode();
        BankDecisionNode bankDecisionNode = new BankDecisionNode();
        suppliesDecisionNode.setNodes(tannerDecisionNode, bankDecisionNode);
        bankDecisionNode.setNodes(new BankRunnable(), new WalkBankRunnable());
        tannerDecisionNode.setNodes(new TanRunnable(), new WalkTannerRunnable());
        DecisionTree decisionTree = new DecisionTree(suppliesDecisionNode);
        setDecisionTree(decisionTree);
    }

    @Override
    public void onInitialLogin() {
        if (Vars.get().hide == null && Timing.waitCondition(() -> !Inventory.isEmpty(), 1200)) {
            for (Hide hide : Hide.values()) {
                if (Inventory.contains(hide.getUntannedId(), hide.getTannedId())) {
                    Vars.get().hide = hide;
                    break;
                }
            }
        }
        if (Vars.get().hide == null) {
            Logging.critical("Could not determine hide to tan.");
            Logging.info("Start the script with the tanned or untanned hide you wish to tan in the inventory.");
            shutdown();
            return;
        } else {
            Logging.info("Tanning " + Vars.get().hide.toString());
        }
        OSBotsSession osBotsSession = new OSBotsSession(this, stats);
        osBotsSession.start();
    }

    @Override
    public void onLoop() {
        if (Camera.getCameraAngle() < 85) {
            Camera.setCameraAngle(General.random(85, 100));
        }
    }

    @Override
    public void onPaint(Graphics g) {
        super.onPaint(g);
    }

    @Override
    public void onEnd() {
        super.onEnd();
        if (!isRunning() && stats.getTimeRan() >= 180_000) {
            Login.logout(true);
            General.sleep(600);
        }
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
    public void passArguments(HashMap<String, String> args) {
        if (args.containsKey("custom_input") || args.containsKey("autostart")) {
            if (args.containsKey("custom_input")) {
                Vars.get().hide = Hide.valueOf(args.get("custom_input").trim().toUpperCase());
            } else {
                Vars.get().hide = Hide.valueOf(args.get("autostart").trim().toUpperCase());
            }
        }
    }

}