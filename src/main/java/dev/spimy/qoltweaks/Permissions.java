package dev.spimy.qoltweaks;

public enum Permissions {
    LADDERWARP("ladderwarp"),
    BOOK_EXTRACT("bookextract"),
    NAMETAG_SHEAR("nametagshear"),
    PET_DOG("petdog"),
    PET_CAT("petcat"),
    PREVENT_PET_DAMAGE("preventpetdamage");

    private final String permissionNode;

    Permissions(String permissionNode) {
        this.permissionNode = String.format("qoltweaks.%s", permissionNode);
    }

    public String getPermissionNode() {
        return this.permissionNode;
    }

    public String toString() {
        return this.permissionNode;
    }
}
