/*
  Copyright 2012 - 2015 pac4j organization

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.pac4j.oauth.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.api.Api;
import com.github.scribejava.core.model.Token;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.oauth.exception.OAuthCredentialsException;
import org.pac4j.oauth.profile.JsonHelper;
import org.pac4j.oauth.profile.google2.Google2Profile;

/**
 * <p>This class is the OAuth client to authenticate users in Google using OAuth protocol version 2.0.</p>
 * <p>The <i>scope</i> is by default : {@link Google2Scope#EMAIL_AND_PROFILE}, but it can also but set to : {@link Google2Scope#PROFILE}
 * or {@link Google2Scope#EMAIL}.</p>
 * <p>It returns a {@link org.pac4j.oauth.profile.google2.Google2Profile}.</p>
 * <p>More information at https://developers.google.com/accounts/docs/OAuth2Login</p>
 *
 * @author Jerome Leleu
 * @since 1.2.0
 */
public class Google2Client extends BaseOAuth20StateClient<Google2Profile> {

    public enum Google2Scope {
        EMAIL,
        PROFILE,
        EMAIL_AND_PROFILE
    }

    protected final String PROFILE_SCOPE = "profile";

    protected final String EMAIL_SCOPE = "email";

    protected Google2Scope scope = Google2Scope.EMAIL_AND_PROFILE;

    protected String scopeValue;

    private boolean requiresStateParameter = true;

    public Google2Client() {
    }

    public Google2Client(final String key, final String secret) {
        setKey(key);
        setSecret(secret);
    }

    @Override
    protected void internalInit(final WebContext context) {
        CommonHelper.assertNotNull("scope", this.scope);
        if (this.scope == Google2Scope.EMAIL) {
            this.scopeValue = this.EMAIL_SCOPE;
        } else if (this.scope == Google2Scope.PROFILE) {
            this.scopeValue = this.PROFILE_SCOPE;
        } else {
            this.scopeValue = this.PROFILE_SCOPE + " " + this.EMAIL_SCOPE;
        }
        super.internalInit(context);
    }

    @Override
    protected Api getApi() {
        return GoogleApi20.instance();
    }

    @Override
    protected String getOAuthScope() {
        return this.scopeValue;
    }

    @Override
    protected String getProfileUrl(final Token accessToken) {
        return "https://www.googleapis.com/plus/v1/people/me";
    }

    @Override
    protected Google2Profile extractUserProfile(final String body) {
        final Google2Profile profile = new Google2Profile();
        final JsonNode json = JsonHelper.getFirstNode(body);
        if (json != null) {
            profile.setId(JsonHelper.getElement(json, "id"));
            for (final String attribute : profile.getAttributesDefinition().getPrimaryAttributes()) {
                profile.addAttribute(attribute, JsonHelper.getElement(json, attribute));
            }
        }
        return profile;
    }

    public Google2Scope getScope() {
        return this.scope;
    }

    public void setScope(final Google2Scope scope) {
        this.scope = scope;
    }

    @Override
    protected boolean hasBeenCancelled(final WebContext context) {
        final String error = context.getRequestParameter(OAuthCredentialsException.ERROR);
        // user has denied permissions
        if ("access_denied".equals(error)) {
            return true;
        }
        return false;
    }
}
