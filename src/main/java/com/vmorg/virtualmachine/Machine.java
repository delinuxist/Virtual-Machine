package com.vmorg.virtualmachine;

public abstract class Machine {

    private final String hostname;
    private final String requestorName;
    private final int numberOfCPUs;
    private final int ramSize;
    private final int hardDiskSize;

    public Machine(String hostname, String requestorName, int numberOfCPUs, int ramSize, int hardDiskSize) {
        this.hostname = hostname;
        this.requestorName = requestorName;
        this.numberOfCPUs = numberOfCPUs;
        this.ramSize = ramSize;
        this.hardDiskSize = hardDiskSize;
    }

    public String getRequestorName() {
        return requestorName;
    }
}
