package scripts.smither.data;

import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;

public enum Item {

    DART_TIPS(1, 29), KNIVES(1, 31), PLATEBODY(5, 22);

    private int barsRequired;
    private int child;

    Item(int barsRequired, int child) {
        this.barsRequired = barsRequired;
        this.child = child;
    }

    public int getBarsRequired() {
        return barsRequired;
    }

    public RSInterface getInterface() {
        return Interfaces.get(312, child);
    }

    @Override
    public String toString() {
        String str = super.toString().replaceAll("_", " ");
        return str.charAt(0) + str.substring(1).toLowerCase();
    }

}
