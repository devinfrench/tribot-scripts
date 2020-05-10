package scripts.woodcutter.data;

import org.tribot.api2007.types.RSTile;
import scripts.api.Banking;
import scripts.api.types.RSArea;

import static scripts.woodcutter.data.Constants.*;

public enum Location {

    // Tree
    LUMBRIDGE_TREE(LUMBRIDGE_TREE_AREA, null, Banking.Bank.LUMBRIDGE),
    VARROCK_WEST_TREE(VARROCK_WEST_TREE_AREA, VARROCK_WEST_TREE_PATH, Banking.Bank.VARROCK_WEST),
    SEERS_VILLAGE_TREE(SEERS_TREE_AREA, SEERS_TREE_PATH, Banking.Bank.SEERS_VILLAGE),
    // Oak
    VARROCK_WEST_OAK(VARROCK_WEST_OAK_AREA, VARROCK_WEST_OAK_PATH, Banking.Bank.VARROCK_WEST),
    SEERS_VILLAGE_OAK(SEERS_OAK_AREA, SEERS_OAK_PATH, Banking.Bank.SEERS_VILLAGE),
//    WOODCUTTING_GUILD_OAK(WOODCUTTING_GUILD_OAKS_AREA, WOODCUTTING_GUILD_OAKS_PATH, Banking.Bank.WOODCUTTING_GUILD),
    // Willow
    DRAYNOR_WILLOW(DRAYNOR_WILLOW_AREA, DRAYNOR_WILLOW_PATH, Banking.Bank.DRAYNOR),
    PORT_SARIM_WILLOW(PORT_SARIM_WILLOW_AREA, PORT_SARIM_WILLOW_PATH, Banking.Bank.PORT_SARIM),
    CATHERBY_WILLOW(CATHERBY_WILLOW_AREA, CATHERBY_WILLOW_PATH, Banking.Bank.CATHERBY),
    SEERS_VILLAGE_WILLOW(SEERS_WILLOW_AREA, SEERS_WILLOW_PATH, Banking.Bank.SEERS_VILLAGE),
    BARBARIAN_OUTPOST_WILLOW(BARBARIAN_OUTPOST_WILLOW_AREA, BARBARIAN_OUTPOST_WILLOW_PATH, Banking.Bank.BARBARIAN_OUTPOST),
    // Maple
    SEERS_VILLAGE_MAPLE(SEERS_MAPLE_AREA, SEERS_MAPLE_PATH, Banking.Bank.SEERS_VILLAGE),
    // Teak
    CASTLE_WARS_TEAK(CASTLE_WARS_TEAK_AREA, null, Banking.Bank.CASTLE_WARS),
    // Yew
    VARROCK_CASTLE_YEW(VARROCK_YEW_AREA, VARROCK_YEW_PATH, Banking.Bank.GRAND_EXCHANGE),
    EDGEVILLE_YEW(EDGEVILLE_YEW_AREA, EDGEVILLE_YEW_PATH, Banking.Bank.EDGEVILLE),
    RIMMINGTON_YEW(RIMMINGTON_YEW_AREA, RIMMINGTON_YEW_PATH, Banking.Bank.FALADOR_EAST),
    CATHERBY_YEW(CATHERBY_YEW_AREA, CATHERBY_YEW_PATH, Banking.Bank.CATHERBY),
    SEERS_VILLAGE_YEW(SEERS_YEW_AREA, SEERS_YEW_PATH, Banking.Bank.SEERS_VILLAGE),
    WOODCUTTING_GUILD_YEW(WOODCUTTING_GUILD_YEW_AREA, WOODCUTTING_GUILD_YEW_PATH, Banking.Bank.WOODCUTTING_GUILD),
    // Magic
    RANGING_GUILD_MAGIC(RANGING_GUILD_MAGIC_AREA, RANGING_GUILD_MAGIC_PATH, Banking.Bank.SEERS_VILLAGE),
    SORCERERS_TOWER_MAGIC(SORCERERS_TOWER_MAGIC_AREA, SORCERERS_TOWER_MAGIC_PATH, Banking.Bank.SEERS_VILLAGE),
    WOODCUTTING_GUILD_MAGIC(WOODCUTTING_GUILD_MAGIC_AREA, WOODCUTTING_GUILD_MAGIC_PATH, Banking.Bank.WOODCUTTING_GUILD);

    private RSArea treeArea;
    private Banking.Bank bank;
    private RSTile[] path;

    Location(RSArea treeArea, RSTile[] path, Banking.Bank bank) {
        this.treeArea = treeArea;
        this.path = path;
        this.bank = bank;
    }

    @Override
    public String toString() {
        String[] option = super.toString().split("_");
        StringBuilder output = new StringBuilder();
        for (String str : option) {
            output.append(str.charAt(0)).append(str.substring(1).toLowerCase()).append(" ");
        }
        return output.toString().trim() + "s";
    }

    public RSArea getTreeArea() {
        return treeArea;
    }

    public RSTile[] getPath() {
        return path;
    }

    public Banking.Bank getBank() {
        return bank;
    }

}
