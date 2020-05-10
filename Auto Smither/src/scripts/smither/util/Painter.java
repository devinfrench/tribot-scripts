package scripts.smither.util;

import org.tribot.api2007.Skills;
import scripts.api.script.Script;
import scripts.api.util.ImageUtil;
import scripts.api.util.Internet;
import scripts.api.util.Statistics;
import scripts.smither.data.Vars;

import java.awt.*;

public class Painter extends scripts.api.util.Painter {

    private Statistics stats;
    private int crafted;
    private int craftedHr;

    public Painter(Script script, Statistics stats, Skills.SKILLS skill) {
        super(script, stats, skill);
        this.stats = stats;
    }

    @Override
    public Image getImage() {
        Image image = ImageUtil.getImage("smither_paint");
        if (image == null) {
            image = Internet.getImage("http://encodedscripting.com/resources/smither/smither_paint.png");
            ImageUtil.saveImage(image, "smither_paint");
        }
        return image;
    }

    @Override
    public void update() {
        super.update();
        crafted = stats.getCount("crafted");
        craftedHr = stats.getPerHour(crafted);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (getShowPaint()) {
            if (Vars.get().isSmelting) {
                g.drawString("Smelt: " + crafted, p.x + 210, p.y - 56);
                g.drawString("Smelt/Hour: " + craftedHr, p.x + 210, p.y - 39);
                g.drawString(Vars.get().bar.toString(), p.x + 18, p.y - 8);
            } else {
                g.drawString("Forged: " + crafted, p.x + 210, p.y - 56);
                g.drawString("Forged/Hour: " + craftedHr, p.x + 210, p.y - 39);
                g.drawString(Vars.get().item.toString(), p.x + 18, p.y - 5);
            }
        }
    }
}
