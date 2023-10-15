package com.fyxar.injector.mapper.enums;

public enum MappingVersion {
    BountifulUpdate("1.8.9");

    private final String version;

    MappingVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}
