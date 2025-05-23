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

/**
 * Simple server-independent abstraction for SSL configuration.
 *
 * @author Andy Wilkinson
 * @author Vladimir Tsanev
 * @author Stephane Nicoll
 * @author Scott Frederick
 * @since 2.0.0
 */
public class Ssl {

	private boolean enabled = true;

	private String bundle;

	private ClientAuth clientAuth;

	private String[] ciphers;

	private String[] enabledProtocols;

	private String keyAlias;

	private String keyPassword;

	private String keyStore;

	private String keyStorePassword;

	private String keyStoreType;

	private String keyStoreProvider;

	private String trustStore;

	private String trustStorePassword;

	private String trustStoreType;

	private String trustStoreProvider;

	private String certificate;

	private String certificatePrivateKey;

	private String trustCertificate;

	private String trustCertificatePrivateKey;

	private String protocol = "TLS";

	/**
	 * Return whether to enable SSL support.
	 * @return whether to enable SSL support
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Return the name of the SSL bundle to use.
	 * @return the SSL bundle name
	 * @since 3.1.0
	 */
	public String getBundle() {
		return this.bundle;
	}

	/**
	 * Set the name of the SSL bundle to use.
	 * @param bundle the SSL bundle name
	 * @since 3.1.0
	 */
	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	/**
	 * Return Whether client authentication is not wanted ("none"), wanted ("want") or
	 * needed ("need"). Requires a trust store.
	 * @return the {@link ClientAuth} to use
	 */
	public ClientAuth getClientAuth() {
		return this.clientAuth;
	}

	public void setClientAuth(ClientAuth clientAuth) {
		this.clientAuth = clientAuth;
	}

	/**
	 * Return the supported SSL ciphers.
	 * @return the supported SSL ciphers
	 */
	public String[] getCiphers() {
		return this.ciphers;
	}

	public void setCiphers(String[] ciphers) {
		this.ciphers = ciphers;
	}

	/**
	 * Return the enabled SSL protocols.
	 * @return the enabled SSL protocols.
	 */
	public String[] getEnabledProtocols() {
		return this.enabledProtocols;
	}

	public void setEnabledProtocols(String[] enabledProtocols) {
		this.enabledProtocols = enabledProtocols;
	}

	/**
	 * Return the alias that identifies the key in the key store.
	 * @return the key alias
	 */
	public String getKeyAlias() {
		return this.keyAlias;
	}

	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}

	/**
	 * Return the password used to access the key in the key store.
	 * @return the key password
	 */
	public String getKeyPassword() {
		return this.keyPassword;
	}

	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}

	/**
	 * Return the path to the key store that holds the SSL certificate (typically a jks
	 * file).
	 * @return the path to the key store
	 */
	public String getKeyStore() {
		return this.keyStore;
	}

	public void setKeyStore(String keyStore) {
		this.keyStore = keyStore;
	}

	/**
	 * Return the password used to access the key store.
	 * @return the key store password
	 */
	public String getKeyStorePassword() {
		return this.keyStorePassword;
	}

	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}

	/**
	 * Return the type of the key store.
	 * @return the key store type
	 */
	public String getKeyStoreType() {
		return this.keyStoreType;
	}

	public void setKeyStoreType(String keyStoreType) {
		this.keyStoreType = keyStoreType;
	}

	/**
	 * Return the provider for the key store.
	 * @return the key store provider
	 */
	public String getKeyStoreProvider() {
		return this.keyStoreProvider;
	}

	public void setKeyStoreProvider(String keyStoreProvider) {
		this.keyStoreProvider = keyStoreProvider;
	}

	/**
	 * Return the trust store that holds SSL certificates.
	 * @return the trust store
	 */
	public String getTrustStore() {
		return this.trustStore;
	}

	public void setTrustStore(String trustStore) {
		this.trustStore = trustStore;
	}

	/**
	 * Return the password used to access the trust store.
	 * @return the trust store password
	 */
	public String getTrustStorePassword() {
		return this.trustStorePassword;
	}

	public void setTrustStorePassword(String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}

	/**
	 * Return the type of the trust store.
	 * @return the trust store type
	 */
	public String getTrustStoreType() {
		return this.trustStoreType;
	}

	public void setTrustStoreType(String trustStoreType) {
		this.trustStoreType = trustStoreType;
	}

	/**
	 * Return the provider for the trust store.
	 * @return the trust store provider
	 */
	public String getTrustStoreProvider() {
		return this.trustStoreProvider;
	}

	public void setTrustStoreProvider(String trustStoreProvider) {
		this.trustStoreProvider = trustStoreProvider;
	}

	/**
	 * Return the location of the certificate in PEM format.
	 * @return the certificate location
	 */
	public String getCertificate() {
		return this.certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	/**
	 * Return the location of the private key for the certificate in PEM format.
	 * @return the location of the certificate private key
	 */
	public String getCertificatePrivateKey() {
		return this.certificatePrivateKey;
	}

	public void setCertificatePrivateKey(String certificatePrivateKey) {
		this.certificatePrivateKey = certificatePrivateKey;
	}

	/**
	 * Return the location of the trust certificate authority chain in PEM format.
	 * @return the location of the trust certificate
	 */
	public String getTrustCertificate() {
		return this.trustCertificate;
	}

	public void setTrustCertificate(String trustCertificate) {
		this.trustCertificate = trustCertificate;
	}

	/**
	 * Return the location of the private key for the trust certificate in PEM format.
	 * @return the location of the trust certificate private key
	 */
	public String getTrustCertificatePrivateKey() {
		return this.trustCertificatePrivateKey;
	}

	public void setTrustCertificatePrivateKey(String trustCertificatePrivateKey) {
		this.trustCertificatePrivateKey = trustCertificatePrivateKey;
	}

	/**
	 * Return the SSL protocol to use.
	 * @return the SSL protocol
	 */
	public String getProtocol() {
		return this.protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * Returns if SSL is enabled for the given instance.
	 * @param ssl the {@link Ssl SSL} instance or {@code null}
	 * @return {@code true} if SSL is enabled
	 * @since 3.1.0
	 */
	public static boolean isEnabled(Ssl ssl) {
		return (ssl != null) && ssl.isEnabled();
	}

	/**
	 * Factory method to create an {@link Ssl} instance for a specific bundle name.
	 * @param bundle the name of the bundle
	 * @return a new {@link Ssl} instance with the bundle set
	 * @since 3.1.0
	 */
	public static Ssl forBundle(String bundle) {
		Ssl ssl = new Ssl();
		ssl.setBundle(bundle);
		return ssl;
	}

	/**
	 * Client authentication types.
	 */
	public enum ClientAuth {

		/**
		 * Client authentication is not wanted.
		 */
		NONE,

		/**
		 * Client authentication is wanted but not mandatory.
		 */
		WANT,

		/**
		 * Client authentication is needed and mandatory.
		 */
		NEED;

		/**
		 * Map an optional {@link ClientAuth} value to a different type.
		 * @param <R> the result type
		 * @param clientAuth the client auth to map (may be {@code null})
		 * @param none the value for {@link ClientAuth#NONE} or {@code null}
		 * @param want the value for {@link ClientAuth#WANT}
		 * @param need the value for {@link ClientAuth#NEED}
		 * @return the mapped value
		 * @since 3.1.0
		 */
		public static <R> R map(ClientAuth clientAuth, R none, R want, R need) {
			return switch ((clientAuth != null) ? clientAuth : NONE) {
				case NONE -> none;
				case WANT -> want;
				case NEED -> need;
			};
		}

	}

}
