package scripts.cooker.data;

import org.tribot.api2007.types.RSObject;
import scripts.api.Entities;
import scripts.api.entities.RSObjectEntity;

import static scripts.cooker.data.Constants.ROGUES_DEN_FIRE_TILE;

public enum Location {

    ROGUES_DEN(new RSObjectEntity().nameEquals("Fire").atPosition(ROGUES_DEN_FIRE_TILE)),
    CATHERBY(new RSObjectEntity().nameEquals("Range")),
    NARDAH(new RSObjectEntity().nameEquals("Clay Oven")),
    AL_KHARID(new RSObjectEntity().nameEquals("Range")),
    HOSIDIUS(new RSObjectEntity().nameEquals("Clay oven"));

    private RSObjectEntity entity;

    Location(RSObjectEntity entity) {
        this.entity = entity;
    }

    public RSObject getHeatSource() {
        return Entities.find(entity).getNearest();
    }

}
