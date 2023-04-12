package com.vmorg.requestengine;

import com.vmorg.auth.AuthorisingService;
import com.vmorg.build.SystemBuildService;

import com.vmorg.custom_exception.MachineNotCreatedException;
import com.vmorg.custom_exception.UserNotEntitledException;
import com.vmorg.virtualmachine.Desktop;
import com.vmorg.virtualmachine.Machine;
import com.vmorg.virtualmachine.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RequestEngineTest {
    AuthorisingService authorisingServiceMock;

    SystemBuildService systemBuildServiceMock;
    Machine windows;
    Machine linux;

    RequestEngine requestEngine;
    RequestEngine requestEngineNew;

    @BeforeEach
    void setUp() {
        authorisingServiceMock = mock(AuthorisingService.class);
        systemBuildServiceMock = mock(SystemBuildService.class);
        windows = new Desktop("host2020", "Jake", 1, 4, 200, "Windows 10", "hr498");
        linux = new Server("host2021","Mike",4,8,500,"Ubuntu",8,"s83424h","admin.gh");
    }

    @Test
    void shouldThrowUserNotEntitledException() {

        //when
        when(authorisingServiceMock.isAuthorised(windows.getRequestorName())).thenReturn(false);
        requestEngine = new RequestEngine(authorisingServiceMock, systemBuildServiceMock);

        //then
        assertThrows(UserNotEntitledException.class, () -> requestEngine.createNewRequest(windows));
    }

    @Test
    void shouldThrowMachineNotCreatedException() {

        //when
        when(authorisingServiceMock.isAuthorised(windows.getRequestorName())).thenReturn(true);
        when(systemBuildServiceMock.createNewMachine(windows)).thenReturn("");
        requestEngine = new RequestEngine(authorisingServiceMock, systemBuildServiceMock);

        //then
        assertThrows(MachineNotCreatedException.class, () -> requestEngine.createNewRequest(windows));
    }

    @Test
    void shouldIncreaseTotalFailedBuilds() {
        //when
        when(authorisingServiceMock.isAuthorised(windows.getRequestorName())).thenReturn(true);
        when(systemBuildServiceMock.createNewMachine(windows)).thenReturn("");
        requestEngine = new RequestEngine(authorisingServiceMock, systemBuildServiceMock);
        try {
            requestEngine.createNewRequest(windows);
        }catch (MachineNotCreatedException | UserNotEntitledException e){
            System.out.println(e.getMessage());
        }

        //then
        assertEquals(1,requestEngine.totalFailedBuildsForDay());
    }

    @Test
    void shouldIncreaseTotalBuildMachines() throws MachineNotCreatedException, UserNotEntitledException {

        //when
        when(authorisingServiceMock.isAuthorised(windows.getRequestorName())).thenReturn(true);
        when(systemBuildServiceMock.createNewMachine(windows)).thenReturn("Windows");
        requestEngine = new RequestEngine(authorisingServiceMock, systemBuildServiceMock);

        requestEngine.createNewRequest(windows);

        //then
        assertEquals(1,requestEngine.totalBuildsByUserForDay().size());
    }

    @Test
    void shouldReturnTwoTotalBuiltMachines() throws MachineNotCreatedException, UserNotEntitledException {

        //when
        when(authorisingServiceMock.isAuthorised(windows.getRequestorName())).thenReturn(true);
        when(systemBuildServiceMock.createNewMachine(windows)).thenReturn("Windows");
        requestEngine = new RequestEngine(authorisingServiceMock, systemBuildServiceMock);

        when(authorisingServiceMock.isAuthorised(linux.getRequestorName())).thenReturn(true);
        when(systemBuildServiceMock.createNewMachine(linux)).thenReturn("Linux");
        requestEngineNew = new RequestEngine(authorisingServiceMock, systemBuildServiceMock);

        requestEngine.createNewRequest(windows);
        requestEngineNew.createNewRequest(linux);

        //then
        assertEquals(2,requestEngine.totalBuildsByUserForDay().size());
    }

    @Test
    void shouldContainRequestorNameInTotalBuildMachines() throws MachineNotCreatedException, UserNotEntitledException {

        //when
        when(authorisingServiceMock.isAuthorised(windows.getRequestorName())).thenReturn(true);
        when(systemBuildServiceMock.createNewMachine(windows)).thenReturn("Windows");
        requestEngine = new RequestEngine(authorisingServiceMock, systemBuildServiceMock);

        requestEngine.createNewRequest(windows);

        //then
        assertTrue(requestEngine.totalBuildsByUserForDay().containsKey(windows.getRequestorName()));
    }

    @Test
    void shouldContainMachineTypeInTotalBuildMachines() throws MachineNotCreatedException, UserNotEntitledException {

        //when
        when(authorisingServiceMock.isAuthorised(windows.getRequestorName())).thenReturn(true);
        when(systemBuildServiceMock.createNewMachine(windows)).thenReturn("Windows");
        requestEngine = new RequestEngine(authorisingServiceMock, systemBuildServiceMock);

        requestEngine.createNewRequest(windows);
        Map<String,Integer> data = requestEngine.totalBuildsByUserForDay().get(windows.getRequestorName());

        //then
        assertTrue(data.containsKey(windows.toString()));
        assertEquals(1,data.get(windows.toString()));
    }

    @Test
    void shouldContainMachineTypeAndIncreaseQuantity() throws MachineNotCreatedException, UserNotEntitledException {

        //when
        when(authorisingServiceMock.isAuthorised(windows.getRequestorName())).thenReturn(true);
        when(systemBuildServiceMock.createNewMachine(windows)).thenReturn("Windows");
        requestEngine = new RequestEngine(authorisingServiceMock, systemBuildServiceMock);

        requestEngine.createNewRequest(windows);
        requestEngine.createNewRequest(windows);
        Map<String,Integer> data = requestEngine.totalBuildsByUserForDay().get(windows.getRequestorName());

        //then
        assertTrue(data.containsKey(windows.toString()));
        assertEquals(2,data.get(windows.toString()));
    }
}