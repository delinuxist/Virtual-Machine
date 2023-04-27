package com.vmorg.virtualmachine;

public class Desktop extends Machine{
    private final String winVersion;
    private final String buildNumber;

    public Desktop(String hostname, String requestorName, int numberOfCPUs, int ramSize, int hardDiskSize, String winVersion, String buildNumber) {
        super(hostname, requestorName, numberOfCPUs, ramSize, hardDiskSize);
        this.winVersion = winVersion;
        this.buildNumber = buildNumber;
    }

    @Override
    public String toString() {
        return "Windows";
    }
}
