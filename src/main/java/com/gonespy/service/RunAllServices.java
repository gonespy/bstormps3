package com.gonespy.service;

import com.gonespy.service.auth.AuthService;
import com.gonespy.service.availability.AvailabilityService;
import com.gonespy.service.gpcm.GPCMService;
import com.gonespy.service.gpsp.GPSPService;
import com.gonespy.service.sake.SakeService;

public class RunAllServices {

    public static void main(String[] args) throws Exception {

        Thread availabilityServiceThread = new Thread(() -> new AvailabilityService().run());

        Thread gpcmServiceThread = new Thread(() -> new GPCMService().run());

        Thread gpspServiceThread = new Thread(() -> new GPSPService().run());

        Thread authServiceThread = new Thread(() -> AuthService.main(new String[]{"server", "resources/dw-auth-config.yml"}));

        Thread sakeServiceThread = new Thread(() -> SakeService.main(new String[]{"server", "resources/dw-sake-config.yml"}));

        gpcmServiceThread.start();
        availabilityServiceThread.start();
        gpspServiceThread.start();
        authServiceThread.start();
        Thread.sleep(2000); // TODO: stupid dropwizard
        sakeServiceThread.start();

    }



}
