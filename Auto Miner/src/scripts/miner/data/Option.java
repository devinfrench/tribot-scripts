package scripts.miner.data;

public enum Option {

    SHIFT_DROP, MOUSE_KEYS_DROP, ABC_DROP, BANK;

    @Override
    public String toString() {
        String[] option = super.toString().split("_");
        StringBuilder output = new StringBuilder();
        for (String str : option) {
            output.append(str.charAt(0)).append(str.substring(1).toLowerCase()).append(" ");
        }
        output = new StringBuilder(output.toString().trim());
        if (output.toString().equals("Abc Drop")) {
            output = new StringBuilder("Anti-Ban Compliance Drop");
        }
        return output.toString();
    }

}
