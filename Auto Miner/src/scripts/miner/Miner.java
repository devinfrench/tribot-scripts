package scripts.miner;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.EventBlockingOverride;
import org.tribot.script.interfaces.Painting;
import scripts.api.Game;
import scripts.api.Inventory;
import scripts.api.Login;
import scripts.api.Objects;
import scripts.api.Player;
import scripts.api.Projection;
import scripts.api.Timing;
import scripts.api.concurrency.ExperienceListener;
import scripts.api.script.DecisionTreeScript;
import scripts.api.script.JavaFXGUI;
import scripts.api.util.ImageUtil;
import scripts.api.util.Internet;
import scripts.api.util.Logging;
import scripts.api.util.Painter;
import scripts.api.util.ScriptSession;
import scripts.api.util.Statistics;
import scripts.miner.data.Vars;
import scripts.miner.gui.GUI;
import scripts.miner.session.OSBotsSession;
import scripts.miner.session.Session;
import scripts.miner.util.AnticipatedLocations;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@ScriptManifest(category = "Mining", name = "Auto Miner", authors = "Encoded", version = 1.0, description = "Local")
public class Miner extends DecisionTreeScript implements Ending, EventBlockingOverride, Painting, ExperienceListener {

    private final Color FILL_COLOR = new Color(0, 255, 255, 50);
    private final Color DRAW_COLOR = Color.CYAN;

    private Statistics stats;
    private BlockingQueue<Point> clickedPointsQueue = new ArrayBlockingQueue<>(64);
    private List<Polygon> tilePolygonList = Collections.synchronizedList(new ArrayList<>());

    @Override
    public ScriptSession getSession() {
        return new Session(this);
    }

    @Override
    public Painter getPainter(Statistics statistics) {
        return new Painter(this, statistics, Skills.SKILLS.MINING) {
            @Override
            public Image getImage() {
                Image image = ImageUtil.getImage("mining_paint");
                if (image == null) {
                    image = Internet.getImage("http://encodedscripting.com/resources/miner/img/paint.png");
                    ImageUtil.saveImage(image, "mining_paint");
                }
                return image;
            }
        };
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
        Thread rocksUpdater = new Thread(() -> {
            if (!Timing.waitCondition(() -> gui != null && gui.isOpen() && Vars.get().isSelectingRocks, 5000)) {
                return;
            }
            while (gui.isOpen() && Vars.get().isSelectingRocks) {
                General.sleep(300);
                while (!clickedPointsQueue.isEmpty()) {
                    Point p = clickedPointsQueue.poll();
                    if (p != null) {
                        RSTile tile = Projection.screenToTile(p);
                        if (tile != null && Objects.isAt(tile, "Rocks")) {
                            if (!Vars.get().rockTileList.contains(tile)) {
                                Vars.get().rockTileList.add(tile);
                            } else {
                                Vars.get().rockTileList.remove(tile);
                            }
                        }
                    }
                }
                List<Polygon> polygons = Collections.synchronizedList(new ArrayList<>());
                for (RSTile tile : Vars.get().rockTileList) {
                    if (tile != null && tile.isOnScreen()) {
                        Polygon polygon = Projection.getTileBoundsPoly(tile);
                        if (polygon != null) {
                            polygons.add(polygon);
                        }
                    }
                }
                tilePolygonList = polygons;
            }
        });
        rocksUpdater.setName("Auto Miner Rocks Updater Thread");
        rocksUpdater.start();
    }

    @Override
    public void onInitialLogin() {
        if (Vars.get().rockTileList.isEmpty()) {
            Logging.critical("No rocks selected.");
            shutdown();
            return;
        }
        if (Vars.get().isStationary) {
            Vars.get().stationaryTile = Player.getPosition();
        }
        Vars.get().anticipatedLocations = new AnticipatedLocations(Vars.get());
        setAIAntibanState(false);
        Inventory.setDroppingPattern(Vars.get().droppingPattern);
        startExperienceListener(Skills.SKILLS.MINING);
        OSBotsSession osBotsSession = new OSBotsSession(this, stats);
        osBotsSession.start();
    }

    @Override
    public void onLoop() {
        if (Camera.getCameraAngle() < 85) {
            Camera.setCameraAngle(General.random(85, 95));
        }
    }

    @Override
    public void onPaint(Graphics g) {
        super.onPaint(g);
        if (stats != null && painter != null && painter.getShowPaint()) {
            int mined = stats.getCount("mined");
            painter.paint(g, "Mined: " + mined, "Mined/Hour: " + stats.getPerHour(mined));
        }
        drawRocks(g);
    }

    private void drawRocks(Graphics g) {
        if (Vars.get().isSelectingRocks) {
            for (Polygon poly : tilePolygonList) {
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
        if (skill == Skills.SKILLS.MINING) {
            stats.addCount("mined", 1);
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
    public OVERRIDE_RETURN overrideMouseEvent(MouseEvent mouseEvent) {
        if (Vars.get().isSelectingRocks && mouseEvent.getButton() > 1 && Game.isInGame()) {
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

    @Override
    public void onBreakStart(long l) {
        super.onBreakStart(l);
    }

    @Override
    public void onBreakEnd() {
        super.onBreakEnd();
    }
}