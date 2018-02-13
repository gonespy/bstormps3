package com.gonespy.bstormps3.service.debug;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;

import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import com.gonespy.bstormps3.service.debug.resources.DebugResource;
import com.gonespy.bstormps3.service.debug.resources.VersionResource;

import java.security.Security;

public class DebugService extends Application<DebugServiceConfiguration> {

	static {
		Security.setProperty("jdk.tls.disabledAlgorithms", "");
		Security.setProperty("https.cipherSuites", "SSL_RSA_WITH_RC4_128_MD5");
		Security.setProperty("jdk.tls.legacyAlgorithms", "SSL_RSA_WITH_RC4_128_MD5");
	}

	public static void main(String[] args) throws Exception {
		new DebugService().run(args);
	}

	@Override
	public void initialize(Bootstrap<DebugServiceConfiguration> bootstrap) {
		bootstrap.setConfigurationSourceProvider(
				new SubstitutingSourceProvider(
						bootstrap.getConfigurationSourceProvider(),
                        // not strict as we want to be able to supply defaults
						new EnvironmentVariableSubstitutor(false)
				)
		);

		bootstrap.addBundle(
				new SwaggerBundle<DebugServiceConfiguration>() {
					@Override
					protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(DebugServiceConfiguration configuration) {
						return configuration.swaggerBundleConfiguration;
					}
				}
		);
	}

	@Override
	public void run(DebugServiceConfiguration configuration, Environment environment) throws Exception {

		DebugResource debugResource = new DebugResource();

		// resources
		environment.jersey().register(new VersionResource());
		environment.jersey().register(debugResource);
		environment.jersey().register(new JerseyObjectMapper());

		environment.jersey().register(new JsonProcessingExceptionMapper());

		environment.getObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
		environment.getObjectMapper().registerModule(new Jdk8Module());

		// health checks
		environment.healthChecks().register("dummy", new DummyHealthCheck());
	}

	@Provider
	public static class JerseyObjectMapper implements ContextResolver<ObjectMapper> {

		private ObjectMapper mapper;

		public JerseyObjectMapper() {
			mapper = new ObjectMapper();
			mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		}

		@Override
		public ObjectMapper getContext(Class<?> type) {
			return mapper;
		}

	}

	public class JsonProcessingExceptionMapper implements ExceptionMapper<JsonProcessingException> {
		@Override
		public Response toResponse(JsonProcessingException exception) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
}
