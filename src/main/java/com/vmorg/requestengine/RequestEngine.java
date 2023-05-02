package com.vmorg.requestengine;

import com.vmorg.auth.AuthorisingService;
import com.vmorg.build.SystemBuildService;
import com.vmorg.buildrequest.VirtualMachineRequestor;
import com.vmorg.custom_exception.MachineNotCreatedException;
import com.vmorg.custom_exception.UserNotEntitledException;
import com.vmorg.virtualmachine.Machine;

import java.util.HashMap;
import java.util.Map;

public class RequestEngine implements VirtualMachineRequestor {
    AuthorisingService authorisingService;
    SystemBuildService systemBuildService;
    private int totalFailedBuilds;

    private Map<String,Map<String,Integer>> totalBuildsByUser = new HashMap<>();



    public RequestEngine(AuthorisingService authorisingService, SystemBuildService systemBuildService) {
        this.authorisingService = authorisingService;
        this.systemBuildService = systemBuildService;
    }

    @Override
    public void createNewRequest(Machine machine) throws UserNotEntitledException, MachineNotCreatedException {
        if(authorisingService.isAuthorised(machine.getRequestorName())){
            if (systemBuildService.createNewMachine(machine).isEmpty()){
                totalFailedBuilds++;
                throw new MachineNotCreatedException("Machine creating denied!!!");
            } else {
                recordSuccessfulBuild(machine);
            }
        } else {
            throw new UserNotEntitledException("Access Denied!!!!");
        }
    }

    private void recordSuccessfulBuild(Machine machine) {
        if (totalBuildsByUser.containsKey(machine.getRequestorName())) {
            Map<String,Integer> data = totalBuildsByUser.get(machine.getRequestorName());
            if(data.containsKey(machine.toString())){
                data.put(machine.toString(),data.get(machine.toString())+1);
            } else {
                data.put(machine.toString(),1);
            }
        }else{
            Map<String,Integer> data = new HashMap<>();
            data.put(machine.toString(),1);
            totalBuildsByUser.put(machine.getRequestorName(),data);
        }
    }

    @Override
    public Map<String, Map<String, Integer>> totalBuildsByUserForDay() {
        return totalBuildsByUser;
    }

    @Override
    public int totalFailedBuildsForDay() {
        return totalFailedBuilds;
    }
}
