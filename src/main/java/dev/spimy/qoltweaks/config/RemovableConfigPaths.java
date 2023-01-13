package dev.spimy.qoltweaks.config;

public enum RemovableConfigPaths {
    ENABLED("enabled"),
    REQUIRE_PERMISSION("require-permission");

    private final String path;

    RemovableConfigPaths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
