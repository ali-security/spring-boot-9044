/*
 * Copyright 2012-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure.flyway;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.internal.scanner.LocationScannerCache;
import org.flywaydb.core.internal.scanner.ResourceNameCache;
import org.flywaydb.core.internal.scanner.Scanner;

/**
 * Registers {@link NativeImageResourceProvider} as a Flyway
 * {@link org.flywaydb.core.api.ResourceProvider}.
 *
 * @author Moritz Halbritter
 * @author Maziz Esa
 */
class NativeImageResourceProviderCustomizer extends ResourceProviderCustomizer {

	@Override
	public void customize(FluentConfiguration configuration) {
		if (configuration.getResourceProvider() == null) {
			Scanner<?> scanner = createScanner(configuration);
			NativeImageResourceProvider resourceProvider = new NativeImageResourceProvider(scanner,
					configuration.getClassLoader(), Arrays.asList(configuration.getLocations()),
					configuration.getEncoding(), configuration.isFailOnMissingLocations());
			configuration.resourceProvider(resourceProvider);
		}
	}

	private static Scanner<?> createScanner(FluentConfiguration configuration) {
		try {
			return new Scanner<>(JavaMigration.class, Arrays.asList(configuration.getLocations()),
					configuration.getClassLoader(), configuration.getEncoding(), configuration.isDetectEncoding(),
					false, new ResourceNameCache(), new LocationScannerCache(),
					configuration.isFailOnMissingLocations());
		}
		catch (NoSuchMethodError ex) {
			return createFlyway10Scanner(configuration);
		}
	}

	private static Scanner<?> createFlyway10Scanner(FluentConfiguration configuration) throws LinkageError {
		try {
			Constructor<?> constructor = Scanner.class.getDeclaredConstructor(Class.class, boolean.class,
					ResourceNameCache.class, LocationScannerCache.class, Configuration.class);
			return (Scanner<?>) constructor.newInstance(JavaMigration.class, false, new ResourceNameCache(),
					new LocationScannerCache(), configuration);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
