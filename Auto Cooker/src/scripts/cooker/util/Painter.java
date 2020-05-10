package scripts.cooker.util;

import org.tribot.api2007.Skills;
import scripts.api.script.Script;
import scripts.api.util.ImageUtil;
import scripts.api.util.Internet;
import scripts.api.util.Statistics;
import scripts.cooker.data.Vars;

import java.awt.*;

import static scripts.cooker.data.Constants.GRAPES;

public class Painter extends scripts.api.util.Painter {

    private Statistics stats;
    private int cooked;
    private int cookedHr;

    public Painter(Script script, Statistics stats, Skills.SKILLS skill) {
        super(script, stats, skill);
        this.stats = stats;
    }

    @Override
    public Image getImage() {
        Image image = ImageUtil.getImage("cooker_paint");
        if (image == null) {
            image = Internet.getImage("http://encodedscripting.com/scripts/images/cooker_paint.png");
            ImageUtil.saveImage(image, "cooker_paint");
        }
        return image;
    }

    @Override
    public void update() {
        super.update();
        cooked = stats.getCount("cooked");
        cookedHr = stats.getPerHour(cooked);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (stats != null && getShowPaint()) {
            g.drawString("Cooked: " + cooked, p.x + 210, p.y - 56);
            g.drawString("Cooked/Hour: " + cookedHr, p.x + 210, p.y - 39);
            g.drawString(Vars.get().rawId == GRAPES ? "Wine" : Vars.get().rawName, 21, p.y - 7);
        }
    }

}
