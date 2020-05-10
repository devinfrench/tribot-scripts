package scripts.woodcutter.data;

import org.tribot.api.General;

public enum Tree {

    TREE("Tree", "Logs", 1511, 350),
    OAK("Oak", "Oak logs", 1521, 114),
    WILLOW("Willow", "Willow logs", 1519, 114),
    MAPLE("Maple tree", "Maple logs", 1517, 114),
    TEAK("Teak", "Teak logs", 6333, 44),
    YEW("Yew", "Yew logs", 1515, 104),
    MAGIC("Magic tree", "Magic logs", 1513, 324);

    private String objectName;
    private String logName;
    private int logID;
    private int stumpIndexCount;

    Tree(String objectName, String logName, int logID, int stumpIndexCount) {
        this.objectName = objectName;
        this.logName = logName;
        this.logID = logID;
        this.stumpIndexCount = stumpIndexCount;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getLogName() {
        return logName;
    }

    public int getLogID() {
        return logID;
    }

    public int getStumpIndexCount() {
        return stumpIndexCount;
    }

    @Override
    public String toString() {
        String[] option = super.toString().split("_");
        StringBuilder output = new StringBuilder();
        for (String str : option) {
            output.append(str.charAt(0)).append(str.substring(1).toLowerCase()).append(" ");
        }
        return output.toString().trim();
    }

    public long getReactionTime() {
        long reaction = 0;
        switch (this) {
            case TREE:
                reaction = General.randomSD(500, 150);
                break;
            case OAK:
            case WILLOW:
            case MAPLE:
            case TEAK:
                reaction = General.randomSD(2, 11800, 3000, 2900);
                break;
            case YEW:
            case MAGIC:
                reaction = General.randomSD(2, 16200, 4500, 4000);
                break;
        }
        return reaction;
    }

}
