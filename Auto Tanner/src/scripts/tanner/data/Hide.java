package scripts.tanner.data;

public enum Hide {

    SOFT(1739, 1741, 100, 1, "Soft leather"),
    HARD(1739, 1743, 101, 3, "Hard leather"),
    SWAMP_SNAKESKIN(7801, 6289, 102, 20, "Snakeskin (Swamp)"),
    SNAKESKIN(6287, 6289, 103, 15, "Snakeskin"),
    GREEN(1753, 1745, 104, 20, "Green dragon leather"),
    BLUE(1751, 2505, 105, 20, "Blue dragon leather"),
    RED(1749, 2507, 106, 20, "Red dragon leather"),
    BLACK(1747, 2509, 107, 20, "Black dragon leather");

    private int untannedId;
    private int tannedId;
    private int childId;
    private int cost;
    private String name;

    Hide(int untannedId, int tannedId, int childId, int cost, String name) {
        this.untannedId = untannedId;
        this.tannedId = tannedId;
        this.childId = childId;
        this.cost = cost;
        this.name = name;
    }

    public int getUntannedId() {
        return untannedId;
    }

    public int getTannedId() {
        return tannedId;
    }

    public int getChildId() {
        return childId;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return name;
    }

}
