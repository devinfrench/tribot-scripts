package scripts.smither.data;

import org.tribot.api2007.types.RSInterface;
import scripts.api.Inventory;
import scripts.api.entities.RSInterfaceEntity;
import scripts.api.types.RSInterfaceCache;

public enum Bar {

    CANNONBALL("Cannonball", 2, getCannonballRequirements(),
               new RSInterfaceEntity().parentEquals(270).componentNameContains("Cannonball")
    ),
    BRONZE("Bronze bar", 2349, getBronzeBarOreRequirements(),
           new RSInterfaceEntity().parentEquals(270).actionEquals("Smelt").componentNameContains("Bronze bar")
    ),
    IRON("Iron bar", 2351, getIronBarOreRequirements(),
         new RSInterfaceEntity().parentEquals(270).actionEquals("Smelt").componentNameContains("Iron bar")
    ),
    SILVER("Silver bar", 2355, getSilverBarOreRequirements(),
           new RSInterfaceEntity().parentEquals(270).actionEquals("Smelt").componentNameContains("Silver bar")
    ),
    STEEL("Steel bar", 2353, getSteelBarOreRequirements(),
          new RSInterfaceEntity().parentEquals(270).actionEquals("Smelt").componentNameContains("Steel bar")
    ),
    GOLD("Gold bar", 2357, getGoldBarOreRequirements(),
         new RSInterfaceEntity().parentEquals(270).actionEquals("Smelt").componentNameContains("Gold bar")
    ),
    MITHRIL("Mithril bar", 2359, getMithrilBarOreRequirements(),
            new RSInterfaceEntity().parentEquals(270).actionEquals("Smelt").componentNameContains("Mithril bar")
    ),
    ADAMANTITE("Adamantite bar", 2361, getAdamantBarOreRequirements(),
               new RSInterfaceEntity().parentEquals(270).actionEquals("Smelt").componentNameContains("Adamantite bar")
    ),
    RUNITE("Runite bar", 2363, getRuniteBarOreRequirements(),
           new RSInterfaceEntity().parentEquals(270).actionEquals("Smelt").componentNameContains("Runite bar")
    );

    private String name;
    private int id;
    private int[][] oreRequirements;
    private RSInterfaceCache cache;

    Bar(String name, int id, int[][] oreRequirements, RSInterfaceEntity entity) {
        this.name = name;
        this.id = id;
        this.oreRequirements = oreRequirements;
        this.cache = new RSInterfaceCache(entity);
    }

    private static int[][] getBronzeBarOreRequirements() {
        int[][] requirements = new int[2][3];
        requirements[0][0] = 1; // amount
        requirements[0][1] = 436; // copper id
        requirements[0][2] = 14; // amount to withdraw
        requirements[1][0] = 1; //amount
        requirements[1][1] = 438; // tin id
        requirements[1][2] = 14; // amount to withdraw
        return requirements;
    }

    private static int[][] getIronBarOreRequirements() {
        int[][] requirements = new int[1][3];
        requirements[0][0] = 1; // amount
        requirements[0][1] = 440; // iron ore id
        requirements[0][2] = 0; // amount to withdraw
        return requirements;
    }

    private static int[][] getSilverBarOreRequirements() {
        int[][] requirements = new int[1][3];
        requirements[0][0] = 1; // amount
        requirements[0][1] = 442; // silver ore id
        requirements[0][2] = 0; // amount to withdraw
        return requirements;
    }

    private static int[][] getSteelBarOreRequirements() {
        int[][] requirements = new int[2][3];
        requirements[1][0] = 2; // amount
        requirements[1][1] = 453; // coal id
        requirements[1][2] = 0; // amount to withdraw
        requirements[0][0] = 1; //amount
        requirements[0][1] = 440; // iron ore id
        requirements[0][2] = 9; // amount to withdraw
        return requirements;
    }

    private static int[][] getGoldBarOreRequirements() {
        int[][] requirements = new int[1][3];
        requirements[0][0] = 1; // amount
        requirements[0][1] = 444; // gold ore id
        requirements[0][2] = 0; // amount to withdraw
        return requirements;
    }

    private static int[][] getMithrilBarOreRequirements() {
        int[][] requirements = new int[2][3];
        requirements[1][0] = 4; // amount
        requirements[1][1] = 453; // coal id
        requirements[1][2] = 20; // amount to withdraw
        requirements[0][0] = 1; //amount
        requirements[0][1] = 447; // mithril ore id
        requirements[0][2] = 5; // amount to withdraw
        return requirements;
    }

    private static int[][] getAdamantBarOreRequirements() {
        int[][] requirements = new int[2][3];
        requirements[1][0] = 6; // amount
        requirements[1][1] = 453; // coal id
        requirements[1][2] = 24; // amount to withdraw
        requirements[0][0] = 1; //amount
        requirements[0][1] = 449; // adamant ore id
        requirements[0][2] = 4; // amount to withdraw
        return requirements;
    }

    private static int[][] getRuniteBarOreRequirements() {
        int[][] requirements = new int[2][3];
        requirements[1][0] = 8; // amount
        requirements[1][1] = 453; // coal id
        requirements[1][2] = 24; // amount to withdraw
        requirements[0][0] = 1; //amount
        requirements[0][1] = 451; // rune ore id
        requirements[0][2] = 3; // amount to withdraw
        return requirements;
    }

    private static int[][] getCannonballRequirements() {
        int[][] requirements = new int[1][3];
        requirements[0][0] = 1; // amount
        requirements[0][1] = 2353; // steel bar id
        requirements[0][2] = 0; // amount to withdraw
        return requirements;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int[][] getOreRequirements() {
        return oreRequirements;
    }

    public boolean hasOreRequirements() {
        for (int[] oreRequirement : oreRequirements) {
            if (!Inventory.contains(oreRequirement[1])) {
                return false;
            }
        }
        return true;
    }

    public RSInterface getInterface() {
        return cache.get();
    }

    @Override
    public String toString() {
        return name;
    }

}
