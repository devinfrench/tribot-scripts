package scripts.agility.util;

import org.tribot.api2007.Skills;
import scripts.agility.courses.Course;
import scripts.agility.data.Vars;
import scripts.api.script.Script;
import scripts.api.util.ImageUtil;
import scripts.api.util.Internet;
import scripts.api.util.Statistics;

import java.awt.*;

public class Painter extends scripts.api.util.Painter {

    private Statistics stats;
    private int marks;
    private int marksHr;
    private int laps;
    private int lapsHr;

    public Painter(Script script, Statistics stats, Skills.SKILLS skill) {
        super(script, stats, skill);
        this.stats = stats;
    }

    @Override
    public Image getImage() {
        Image image = ImageUtil.getImage("agility_paint");
        if (image == null) {
            image = Internet.getImage("http://encodedscripting.com/scripts/images/agility_paint.png");
            ImageUtil.saveImage(image, "agility_paint");
        }
        return image;
    }

    @Override
    public void update() {
        super.update();
        marks = stats.getCount("marks");
        marksHr = stats.getPerHour(marks);
        laps = stats.getCount("laps");
        lapsHr = stats.getPerHour(laps);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (stats != null && getShowPaint()) {
            g.drawString(Vars.get().course.toString(), 25, p.y - 7);
            g.drawString("Laps: " + laps, p.x + 210, p.y - 56);
            g.drawString("Laps/Hour: " + lapsHr, p.x + 210, p.y - 39);
            if (Vars.get().course != Course.GNOME) {
                g.drawString("Marks: " + marks, p.x + 210, p.y - 22);
                g.drawString("Marks/Hour: " + marksHr, p.x + 210, p.y - 5);
            }
        }
    }
}
