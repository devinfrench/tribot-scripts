package scripts.agility.courses;

import scripts.agility.courses.canifis.obstacles.*;
import scripts.agility.courses.draynor.obstacles.Crate;
import scripts.agility.courses.draynor.obstacles.Gap;
import scripts.agility.courses.draynor.obstacles.NarrowWall;
import scripts.agility.courses.draynor.obstacles.RoughWall;
import scripts.agility.courses.draynor.obstacles.TightRopeOne;
import scripts.agility.courses.draynor.obstacles.TightRopeTwo;
import scripts.agility.courses.draynor.obstacles.Wall;
import scripts.agility.courses.gnome.obstacles.BranchOne;
import scripts.agility.courses.gnome.obstacles.BranchTwo;
import scripts.agility.courses.gnome.obstacles.Log;
import scripts.agility.courses.gnome.obstacles.NetOne;
import scripts.agility.courses.gnome.obstacles.NetTwo;
import scripts.agility.courses.gnome.obstacles.Rope;
import scripts.agility.courses.gnome.obstacles.Tunnel;
import scripts.agility.courses.varrock.obstacles.ClothesLine;
import scripts.agility.courses.varrock.obstacles.Edge;
import scripts.agility.courses.varrock.obstacles.GapFour;
import scripts.agility.courses.varrock.obstacles.GapOne;
import scripts.agility.courses.varrock.obstacles.GapThree;
import scripts.agility.courses.varrock.obstacles.GapTwo;
import scripts.agility.courses.varrock.obstacles.Ledge;
import scripts.agility.tasks.Heal;
import scripts.api.script.frameworks.task.Task;
import scripts.api.script.frameworks.task.TaskSet;

public enum Course {

    GNOME(
      new Log(),
      new NetOne(),
      new BranchOne(),
      new Rope(),
      new BranchTwo(),
      new NetTwo(),
      new Tunnel()
    ),
    DRAYNOR(
      new RoughWall(),
      new TightRopeOne(),
      new TightRopeTwo(),
      new NarrowWall(),
      new Wall(),
      new Gap(),
      new Crate(),
      new Heal()
    ),
    VARROCK(
      new scripts.agility.courses.varrock.obstacles.RoughWall(),
      new ClothesLine(),
      new GapOne(),
      new scripts.agility.courses.varrock.obstacles.Wall(),
      new GapTwo(),
      new GapThree(),
      new GapFour(),
      new Ledge(),
      new Edge(),
      new Heal()
    ),
    CANIFIS(
      new TallTree(),
      new MeatGap(),
      new ClothesGap(),
      new TannerGap(),
      new StoreGap(),
      new PoleVault(),
      new InnGap(),
      new BankGap(),
      new Heal()
    ),
    FALADOR(
      new scripts.agility.courses.falador.obstacles.RoughWall(),
      new scripts.agility.courses.falador.obstacles.TightRopeOne(),
      new scripts.agility.courses.falador.obstacles.HandHolds(),
      new scripts.agility.courses.falador.obstacles.GapOne(),
      new scripts.agility.courses.falador.obstacles.GapTwo(),
      new scripts.agility.courses.falador.obstacles.TightRopeTwo(),
      new scripts.agility.courses.falador.obstacles.TightRopeThree(),
      new scripts.agility.courses.falador.obstacles.GapThree(),
      new scripts.agility.courses.falador.obstacles.LedgeOne(),
      new scripts.agility.courses.falador.obstacles.LedgeTwo(),
      new scripts.agility.courses.falador.obstacles.LedgeThree(),
      new scripts.agility.courses.falador.obstacles.LedgeFour(),
      new scripts.agility.courses.falador.obstacles.Edge(),
      new Heal()
    ),
    SEERS(
      new scripts.agility.courses.seers.obstacles.Wall(),
      new scripts.agility.courses.seers.obstacles.GapOne(),
      new scripts.agility.courses.seers.obstacles.TightRope(),
      new scripts.agility.courses.seers.obstacles.GapTwo(),
      new scripts.agility.courses.seers.obstacles.GapThree(),
      new scripts.agility.courses.seers.obstacles.Edge(),
      new Heal()
    ),
    POLLNIVNEACH(
            new scripts.agility.courses.pollnivneach.obstacles.Basket(),
            new scripts.agility.courses.pollnivneach.obstacles.MarketStall(),
            new scripts.agility.courses.pollnivneach.obstacles.Banner(),
            new scripts.agility.courses.pollnivneach.obstacles.Gap(),
            new scripts.agility.courses.pollnivneach.obstacles.TreeOne(),
            new scripts.agility.courses.pollnivneach.obstacles.Wall(),
            new scripts.agility.courses.pollnivneach.obstacles.Monkeybars(),
            new scripts.agility.courses.pollnivneach.obstacles.TreeTwo(),
            new scripts.agility.courses.pollnivneach.obstacles.DryingLine(),
            new Heal()
    ),
    RELLEKKA(
        new scripts.agility.courses.rellekka.obstacles.RoughWall(),
        new scripts.agility.courses.rellekka.obstacles.GapOne(),
        new scripts.agility.courses.rellekka.obstacles.TightRopeOne(),
        new scripts.agility.courses.rellekka.obstacles.GapTwo(),
        new scripts.agility.courses.rellekka.obstacles.GapThree(),
        new scripts.agility.courses.rellekka.obstacles.TightRopeTwo(),
        new scripts.agility.courses.rellekka.obstacles.PileOfFish(),
        new Heal()
    );

    private TaskSet taskSet;

    Course(Task... tasks) {
        taskSet = new TaskSet(tasks);
    }

    public TaskSet getTaskSet() {
        return taskSet;
    }

    @Override
    public String toString() {
        switch (this) {
            case GNOME:
                return "Gnome Stronghold";
            case DRAYNOR:
                return "Draynor Rooftop";
            case VARROCK:
                return "Varrock Rooftop";
            case CANIFIS:
                return "Canifis Rooftop";
            case FALADOR:
                return "Falador Rooftop";
            case SEERS:
                return "Seers' Village Rooftop";
            case POLLNIVNEACH:
                return "Pollnivneach Rooftop";
            case RELLEKKA:
                return "Rellekka Rooftop";
        }
        return super.toString();
    }
}
