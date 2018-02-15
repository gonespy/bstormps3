package com.gonespy.bstormps3.service;

import com.gonespy.bstormps3.service.availability.AvailabilityService;
import com.gonespy.bstormps3.service.debug.DebugService;
import com.gonespy.bstormps3.service.gpcm.GPCMService;
import com.gonespy.bstormps3.service.gpsp.GPSPService;

public class RunAllServices {

    public static void main(String[] args) {

        Thread availabilityServiceThread = new Thread(() -> new AvailabilityService().run());

        Thread gpcmServiceThread = new Thread(() -> new GPCMService().run());

        Thread gpspServiceThread = new Thread(() -> new GPSPService().run());

        Thread debugServiceThread = new Thread(() -> DebugService.main(new String[]{"server", "src/main/config/dw-config.yml"}));

        gpcmServiceThread.start();
        availabilityServiceThread.start();
        gpspServiceThread.start();
        debugServiceThread.start();

    }



}
