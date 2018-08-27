package com.gonespy.service.auth.resources;

import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import static com.google.common.base.Strings.isNullOrEmpty;

@Api
@Path("/version")
@Produces(MediaType.APPLICATION_JSON)
public class VersionResource {

	private static final Map<String, Object> VERSION_INFO;

	static {
		String version = "unknown";
		String scmRevision = "unknown";

		try (InputStream is = VersionResource.class.getResourceAsStream("/META-INF/MANIFEST.MF")) {
			Manifest manifest = new Manifest(is);
			Attributes attr = manifest.getMainAttributes();

			String implementationVersion = attr.getValue("Implementation-Version");
			if (!isNullOrEmpty(implementationVersion)) {
				version = implementationVersion;
			}

			String implementationScmRevision = attr.getValue("Implementation-SCM-Revision");
			if (!isNullOrEmpty(implementationScmRevision)) {
				scmRevision = implementationScmRevision;
			}

		} catch (IOException ioEx) {
			throw new RuntimeException(ioEx);
		}

		VERSION_INFO = ImmutableMap.of(
                "version", version,
                "scmRevision", scmRevision
		);
	}

	@GET
	public Map<String, Object> getVersionInfo() {
		return VERSION_INFO;
	}
}
