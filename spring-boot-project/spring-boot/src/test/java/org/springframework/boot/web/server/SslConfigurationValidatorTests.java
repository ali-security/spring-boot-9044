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

package org.springframework.boot.web.server;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * Tests for {@link SslConfigurationValidator}.
 *
 * @author Chris Bono
 * @deprecated since 3.1.0 for removal in 3.3.0
 */
@SuppressWarnings("removal")
@Deprecated(since = "3.1.0", forRemoval = true)
class SslConfigurationValidatorTests {

	private static final String VALID_ALIAS = "test-alias";

	private static final String INVALID_ALIAS = "test-alias-5150";

	private KeyStore keyStore;

	@BeforeEach
	void loadKeystore() throws Exception {
		this.keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		try (InputStream stream = new FileInputStream("src/test/resources/test.jks")) {
			this.keyStore.load(stream, "secret".toCharArray());
		}
	}

	@Test
	void validateKeyAliasWhenAliasFoundShouldNotFail() {
		SslConfigurationValidator.validateKeyAlias(this.keyStore, VALID_ALIAS);
	}

	@Test
	void validateKeyAliasWhenNullAliasShouldNotFail() {
		SslConfigurationValidator.validateKeyAlias(this.keyStore, null);
	}

	@Test
	void validateKeyAliasWhenEmptyAliasShouldNotFail() {
		SslConfigurationValidator.validateKeyAlias(this.keyStore, "");
	}

	@Test
	void validateKeyAliasWhenAliasNotFoundShouldThrowException() {
		assertThatIllegalStateException()
			.isThrownBy(() -> SslConfigurationValidator.validateKeyAlias(this.keyStore, INVALID_ALIAS))
			.withMessage("Keystore does not contain alias '" + INVALID_ALIAS + "'");
	}

	@Test
	void validateKeyAliasWhenKeyStoreThrowsExceptionOnContains() throws KeyStoreException {
		KeyStore uninitializedKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		assertThatIllegalStateException()
			.isThrownBy(() -> SslConfigurationValidator.validateKeyAlias(uninitializedKeyStore, "alias"))
			.withMessage("Could not determine if keystore contains alias 'alias'");
	}

}
