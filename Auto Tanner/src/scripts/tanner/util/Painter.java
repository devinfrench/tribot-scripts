package scripts.tanner.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import org.tribot.api.Timing;
import org.tribot.api2007.Skills.SKILLS;
import scripts.api.script.Script;
import scripts.api.util.GraphicsUtil;
import scripts.api.util.Statistics;

public class Painter extends scripts.api.util.Painter {

    private Statistics stats;
    private int tanned = 0;

    public Painter(Script script, Statistics stats, SKILLS skill) {
        super(script, stats, skill);
        this.stats = stats;
    }

    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public void update() {
        stats.updateTimeRan();
        tanned = stats.getCount("tanned");
    }

    @Override
    public void paint(Graphics g) {
        if (stats != null) {
            GraphicsUtil.drawString(g, "Auto Tanner", 10, 40, Color.WHITE);
            GraphicsUtil.drawString(g, "Runtime: " + Timing.msToString(stats.getTimeRan()),10, 65, Color.WHITE);
            GraphicsUtil.drawString(g, "Status: " + getStatus(),10, 90, Color.WHITE);
            GraphicsUtil.drawString(g, "Tanned: " + tanned + " (" + stats.getPerHour(tanned) + " /hr)",10, 115, Color.WHITE);
        }
    }

}
