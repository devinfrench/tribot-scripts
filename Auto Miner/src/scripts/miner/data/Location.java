package scripts.miner.data;

import org.tribot.api2007.types.RSTile;
import scripts.api.Banking;
import scripts.api.types.RSArea;

import static scripts.miner.data.Constants.*;

public enum Location {

    VARROCK_WEST(VARROCK_WEST_MINING_AREA, VARROCK_WEST_PATH, Banking.Bank.VARROCK_WEST),
    VARROCK_EAST(VARROCK_EAST_MINING_AREA, VARROCK_EAST_PATH, Banking.Bank.VARROCK_EAST),
    RIMMINGTON(RIMMINGTON_MINING_AREA, null, Banking.Bank.PORT_SARIM),
    MINING_GUILD(MINING_GUILD_MINING_AREA, null, Banking.Bank.MINING_GUILD),
    AL_KHARID(AL_KHARID_MINING_AREA, null, Banking.Bank.AL_KHARID),
    CRAFTING_GUILD(CRAFTING_GUILD_MINING_AREA, null, Banking.Bank.CRAFTING_GUILD),
    SHILO_VILLAGE(SHILO_VILLAGE_MINING_AREA, SHILO_VILLAGE_PATH, Banking.Bank.SHILO_VILLAGE);

    private RSArea area;
    private RSTile[] path;
    private Banking.Bank bank;

    Location(RSArea area, RSTile[] path, Banking.Bank bank) {
        this.area = area;
        this.path = path;
        this.bank = bank;
    }

    public RSArea getArea() {
        return area;
    }

    public RSTile[] getPath() {
        return path;
    }

    public Banking.Bank getBank() {
        return bank;
    }
}
