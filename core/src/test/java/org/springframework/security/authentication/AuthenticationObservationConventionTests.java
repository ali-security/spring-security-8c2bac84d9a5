/*
 * Copyright 2002-2024 the original author or authors.
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

package org.springframework.security.authentication;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import org.springframework.security.core.authority.AuthorityUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AuthenticationObservationConvention}
 */
public class AuthenticationObservationConventionTests {

	private final AuthenticationObservationConvention convention = new AuthenticationObservationConvention();

	@Test
	public void getContextualNameWhenNoAuthenticationRequestThenAuthenticate() {
		AuthenticationObservationContext context = new AuthenticationObservationContext();
		assertThat(this.convention.getContextualName(context)).isEqualTo("authenticate");
	}

	@Test
	public void getContextualNameWhenAuthenticationRequestThenTypeIsLowerCased() {
		AuthenticationObservationContext context = new AuthenticationObservationContext();
		context.setAuthenticationRequest(UsernamePasswordAuthenticationToken.unauthenticated("user", "password"));
		assertThat(this.convention.getContextualName(context)).isEqualTo("authenticate usernamepassword");
	}

	@Test
	public void getContextualNameWhenDefaultLocaleIsTurkishThenTypeIsLowerCasedIndependentOfLocale() {
		Locale defaultLocale = Locale.getDefault();
		Locale.setDefault(Locale.forLanguageTag("tr"));
		try {
			AuthenticationObservationContext context = new AuthenticationObservationContext();
			context.setAuthenticationRequest(new InternalAuthenticationToken());
			assertThat(this.convention.getContextualName(context)).isEqualTo("authenticate internal");
		}
		finally {
			Locale.setDefault(defaultLocale);
		}
	}

	static final class InternalAuthenticationToken extends AbstractAuthenticationToken {

		InternalAuthenticationToken() {
			super(AuthorityUtils.NO_AUTHORITIES);
		}

		@Override
		public Object getCredentials() {
			return "password";
		}

		@Override
		public Object getPrincipal() {
			return "user";
		}

	}

}
