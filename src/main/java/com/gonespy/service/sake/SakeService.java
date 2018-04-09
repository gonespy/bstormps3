package com.gonespy.service.sake;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.gonespy.service.sake.resources.SakeResource;
import com.gonespy.service.sake.resources.VersionResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

public class SakeService extends Application<SakeServiceConfiguration> {

	public static void main(String[] args) {
		try {
			new SakeService().run(args);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(Bootstrap<SakeServiceConfiguration> bootstrap) {
		bootstrap.setConfigurationSourceProvider(
				new SubstitutingSourceProvider(
						bootstrap.getConfigurationSourceProvider(),
                        // not strict as we want to be able to supply defaults
						new EnvironmentVariableSubstitutor(false)
				)
		);

		bootstrap.addBundle(
				new SwaggerBundle<SakeServiceConfiguration>() {
					@Override
					protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(SakeServiceConfiguration configuration) {
						return configuration.swaggerBundleConfiguration;
					}
				}
		);
	}

	@Override
	public void run(SakeServiceConfiguration configuration, Environment environment) {

		SakeResource sakeResource = new SakeResource();

		// resources
		environment.jersey().register(new VersionResource());
		environment.jersey().register(sakeResource);
		environment.jersey().register(new JerseyObjectMapper());

		environment.jersey().register(new JsonProcessingExceptionMapper());

		environment.getObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
		environment.getObjectMapper().registerModule(new Jdk8Module());

		// health checks
		environment.healthChecks().register("dummy1", new DummyHealthCheck());
		//MetricRegistry retrievedMetricRegistry = SharedMetricRegistries.getOrCreate("default");
		//environment.metrics().register("registry1", retrievedMetricRegistry);


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
