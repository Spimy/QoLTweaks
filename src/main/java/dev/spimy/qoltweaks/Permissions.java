package dev.spimy.qoltweaks;

public enum Permissions {
    LADDERWARP("ladderwarp"),
    BOOK_EXTRACT("bookextract"),
    NAMETAG_SHEAR("nametagshear"),
    PET_DOG("petdog"),
    PET_CAT("petcat"),
    PREVENT_PET_DAMAGE("preventpetdamage"),
    HOE_HARVEST("hoeharvest"),
    RELOAD("reload", true);


    private final String permissionNode;

    Permissions(String permissionNode) {
        this.permissionNode = String.format("qoltweaks.%s", permissionNode);
    }
    Permissions(String permissionNode, boolean command) {
        if (command) {
            this.permissionNode = String.format("qoltweaks.%s", permissionNode);
        } else {
            this.permissionNode = String.format("qoltweaks.command.%s", permissionNode);
        }
    }

    public String getPermissionNode() {
        return this.permissionNode;
    }

    public String toString() {
        return this.permissionNode;
    }
}
