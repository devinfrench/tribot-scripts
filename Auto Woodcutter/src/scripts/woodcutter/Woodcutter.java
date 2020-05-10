package scripts.woodcutter;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSObjectDefinition;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.EventBlockingOverride;
import org.tribot.script.interfaces.Painting;
import scripts.api.*;
import scripts.api.concurrency.ExperienceListener;
import scripts.api.script.DecisionTreeScript;
import scripts.api.script.JavaFXGUI;
import scripts.api.util.Logging;
import scripts.api.util.Painter;
import scripts.api.util.ScriptSession;
import scripts.api.util.Statistics;
import scripts.woodcutter.data.Vars;
import scripts.woodcutter.gui.GUI;
import scripts.woodcutter.session.OSBotsSession;
import scripts.woodcutter.session.Session;
import scripts.woodcutter.util.AnticipatedLocations;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@ScriptManifest(category = "Woodcutting", name = "Auto Woodcutter", authors = "Encoded", version = 1.0, description = "Local")
public class Woodcutter extends DecisionTreeScript implements Ending, EventBlockingOverride, Painting, ExperienceListener {

    private final Color FILL_COLOR = new Color(0, 255, 0, 50);
    private final Color DRAW_COLOR = Color.GREEN;

    private Statistics stats;
    private BlockingQueue<Point> clickedPointsQueue = new ArrayBlockingQueue<>(64);
    private java.util.List<Polygon> treePolygonList = Collections.synchronizedList(new ArrayList<>());

    @Override
    public ScriptSession getSession() {
        return new Session(this);
    }

    @Override
    public Painter getPainter(Statistics statistics) {
        return new scripts.woodcutter.util.Painter(this, statistics, Skills.SKILLS.WOODCUTTING);
    }

    @Override
    public JavaFXGUI getGUI() {
        GUI gui = new GUI(Vars.get());
        return gui;
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
        Thread treesUpdater = new Thread(() -> {
            if (!Timing.waitCondition(() -> gui != null && gui.isOpen(), 5000)) {
                return;
            }
            while (gui.isOpen()) {
                General.sleep(300);
                Logging.debug(clickedPointsQueue.size());
                if (!Vars.get().isSelectingTrees) {
                    continue;
                }
                while (!clickedPointsQueue.isEmpty()) {
                    Point p = clickedPointsQueue.poll();
                    if (p != null) {
                        RSObject tree = getTreeAtPoint(p);
                        if (tree != null) {
                            RSTile tile = tree.getPosition();
                            if (!Vars.get().treeTileList.contains(tile)) {
                                Vars.get().treeTileList.add(tile);
                            } else {
                                Vars.get().treeTileList.remove(tile);
                            }
                        }
                    }
                }
                java.util.List<Polygon> polygons = Collections.synchronizedList(new ArrayList<>());
                for (RSTile tile : Vars.get().treeTileList) {
                    RSObject tree = Objects.find().getAt(tile);
                    if (tree != null) {
                        RSModel model = tree.getModel();
                        if (model != null) {
                            Polygon polygon = model.getEnclosedArea();
                            if (polygon != null) {
                                polygons.add(polygon);
                            }
                        }
                    }
                }
                treePolygonList = polygons;
            }
        });
        treesUpdater.setName("Auto Woodcutter Trees Updater Thread");
        treesUpdater.start();
    }

    @Override
    public void onInitialLogin() {
        if (Vars.get().location == null && Vars.get().treeTileList.isEmpty()) {
            Logging.critical("No trees selected.");
            shutdown();
            return;
        }
        Vars.get().anticipatedLocations = new AnticipatedLocations(Vars.get());
        Inventory.setDroppingPattern(Vars.get().droppingPattern);
        startExperienceListener(Skills.SKILLS.WOODCUTTING);
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
        drawTrees(g);
    }

    private void drawTrees(Graphics g) {
        if (Vars.get().isSelectingTrees) {
            for (Polygon poly : treePolygonList) {
                if (poly != null) {
                    g.setColor(DRAW_COLOR);
                    g.drawPolygon(poly);
                    g.setColor(FILL_COLOR);
                    g.fillPolygon(poly);
                }
            }
        }
    }

    @Override
    public void experienceGained(Skills.SKILLS skill, int amount) {
        if (skill == Skills.SKILLS.WOODCUTTING) {
            stats.addCount("cut", 1);
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
    public void onBreakStart(long l) {
        super.onBreakStart(l);
    }

    @Override
    public void onBreakEnd() {
        super.onBreakEnd();
    }

    @Override
    public OVERRIDE_RETURN overrideMouseEvent(MouseEvent mouseEvent) {
        if (Vars.get().isSelectingTrees && mouseEvent.getButton() > 1 && Game.isInGame()) {
            if (mouseEvent.getID() == 500) {
                clickedPointsQueue.offer(mouseEvent.getPoint());
            }
            return OVERRIDE_RETURN.DISMISS;
        }
        if (painter != null && painter.displayToggleContains(mouseEvent.getPoint())) {
            if (mouseEvent.getID() == 500) {
                mouseEvent.consume();
                painter.show(!painter.getShowPaint());
                return OVERRIDE_RETURN.DISMISS;
            }
            if (mouseEvent.getID() == 501) {
                return OVERRIDE_RETURN.DISMISS;
            }
        }
        return OVERRIDE_RETURN.PROCESS;
    }

    @Override
    public OVERRIDE_RETURN overrideKeyEvent(KeyEvent keyEvent) {
        return OVERRIDE_RETURN.PROCESS;
    }

    private RSObject getTreeAtPoint(Point p) {
        RSTile tile = Projection.screenToTile(p);
        if (tile == null) {
            return null;
        }
        java.util.List<RSObject> trees = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                RSObject tree = Objects.find().filter(obj -> {
                    RSObjectDefinition definition = obj.getDefinition();
                    RSModel model = obj.getModel();
                    if (definition != null && model != null) {
                        String name = definition.getName();
                        return obj.isOnScreen()
                            && name != null
                            && (name.equals(Vars.get().tree.getObjectName())
                            || name.toLowerCase().equals("tree stump")
                            && model.getIndexCount() == Vars.get().tree.getStumpIndexCount());
                    }
                    return false;
                }).getAt(tile.translate(x, y));
                if (tree != null) {
                    trees.add(tree);
                }
            }
        }
        if (trees.size() > 1) {
            RSObject obj = null;
            double min = Double.MAX_VALUE;
            for (RSObject tree : trees) {
                RSModel model = tree.getModel();
                if (model != null) {
                    if (obj == null) {
                        obj = tree;
                        min = p.distance(model.getCentrePoint());
                    } else {
                        double d = p.distance(model.getCentrePoint());
                        if (d < min) {
                            obj = tree;
                            min = d;
                        }
                    }
                }
            }
            return obj;
        } else if (trees.size() == 1) {
            return trees.get(0);
        }
        return null;
    }
}