package com.vmorg.virtualmachine;

public class Server extends Machine{
    private final String distroName;
    private final int majorNumberOfDistribution;
    private final String kernelVersion;
    private final String administrativeTeam;

    public Server(String hostname, String requestorName, int numberOfCPUs, int ramSize, int hardDiskSize, String distroName, int majorNumberOfDistribution, String kernelVersion, String administrativeTeam) {
        super(hostname, requestorName, numberOfCPUs, ramSize, hardDiskSize);
        this.distroName = distroName;
        this.majorNumberOfDistribution = majorNumberOfDistribution;
        this.kernelVersion = kernelVersion;
        this.administrativeTeam = administrativeTeam;
    }

    @Override
    public String toString() {
        return "Linux";
    }
}
