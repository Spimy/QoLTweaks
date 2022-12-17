package dev.spimy.qoltweaks;

public enum Permissions {
    LADDERWARP("ladderwarp");

    private final String permissionNode;

    private Permissions(String permissionNode) {
        this.permissionNode = String.format("qoltweaks.%s", permissionNode);
    }

    public boolean equalsName(String permissionNode) {
        return this.permissionNode.equals(permissionNode);
    }

    public String getPermissionNode() {
        return this.permissionNode;
    }

    public String toString() {
        return this.permissionNode;
    }
}
