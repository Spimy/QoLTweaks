package dev.spimy.qoltweaks;

public enum Permissions {
    LADDERWARP("ladderwarp"),
    BOOKEXTRACT("bookextract");

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
