package com.gonespy.service.auth;

import com.codahale.metrics.health.HealthCheck;

public class DummyHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return  Result.builder()
                // TODO Add proper health check
                .withMessage("Add proper health check here")
                .healthy()
                .build();
    }
}
