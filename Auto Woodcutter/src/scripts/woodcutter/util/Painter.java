package scripts.woodcutter.util;

import org.tribot.api2007.Skills;
import scripts.api.GrandExchange;
import scripts.api.script.Script;
import scripts.api.util.ImageUtil;
import scripts.api.util.Internet;
import scripts.api.util.Statistics;
import scripts.api.util.Util;
import scripts.woodcutter.data.Option;
import scripts.woodcutter.data.Vars;

import java.awt.*;

public class Painter extends scripts.api.util.Painter {

    private Statistics stats;
    private int cut;
    private int cutHr;
    private int price;
    private String profit = "0";
    private String profitHr = "0";

    public Painter(Script script, Statistics stats, Skills.SKILLS skill) {
        super(script, stats, skill);
        this.stats = stats;
    }

    @Override
    public Image getImage() {
        Image image = ImageUtil.getImage("woodcutting_paint");
        if (image == null) {
            image = Internet.getImage("http://encodedscripting.com/resources/woodcutter/img/paint.png");
            ImageUtil.saveImage(image, "woodcutting_paint");
        }
        return image;
    }

    @Override
    public void update() {
        super.update();
        if (price == 0 && Vars.get().option == Option.BANK) {
            price = GrandExchange.getPrice(Vars.get().tree.getLogID());
        }
        cut = stats.getCount("cut");
        cutHr = stats.getPerHour(cut);
        profit = Util.numberFormat(cut * price);
        profitHr = Util.numberFormat(stats.getPerHour(cut * price));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (getShowPaint()) {
            g.drawString("Cut: " + cut, p.x + 210, p.y - 56);
            g.drawString("Cut/Hour: " + cutHr, p.x + 210, p.y - 39);
            g.drawString(Vars.get().tree.toString(), p.x + 21, p.y - 16);
            if (Vars.get().option == Option.BANK) {
                g.drawString("Profit: " + profit + " (" + profitHr + " /hr)", p.x + 210, p.y - 22);
            }
        }
    }
}
