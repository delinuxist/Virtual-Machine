package com.vmorg.requestengine;

import com.vmorg.auth.AuthorisingService;
import com.vmorg.build.SystemBuildService;

import com.vmorg.custom_exception.MachineNotCreatedException;
import com.vmorg.custom_exception.UserNotEntitledException;
import com.vmorg.virtualmachine.Desktop;
import com.vmorg.virtualmachine.Machine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RequestEngineTest {
    AuthorisingService authorisingServiceMock;

    SystemBuildService systemBuildServiceMock;
    Machine windows;

    RequestEngine requestEngine;

    @BeforeEach
    void setUp() {
        authorisingServiceMock = mock(AuthorisingService.class);
        systemBuildServiceMock = mock(SystemBuildService.class);
        windows = new Desktop("host2020", "Jake", 1, 4, 200, "Windows 10", "hr498");

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
}