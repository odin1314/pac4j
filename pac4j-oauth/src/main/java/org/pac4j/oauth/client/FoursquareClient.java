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
import com.github.scribejava.apis.Foursquare2Api;
import com.github.scribejava.core.builder.api.Api;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.Token;
import org.pac4j.core.context.WebContext;
import org.pac4j.oauth.profile.JsonHelper;
import org.pac4j.oauth.profile.foursquare.FoursquareProfile;
import org.pac4j.scribe.oauth.Foursquare20Service;

/**
 * <p>This class is the OAuth client to authenticate users in Foursquare.
 * It returns a {@link org.pac4j.oauth.profile.foursquare.FoursquareProfile}.</p>
 * <p>More information at https://developer.foursquare.com/overview/auth.html</p>
 *
 * @author Alexey Ogarkov
 * @since 1.5.0
 */
public class FoursquareClient extends BaseOAuth20Client<FoursquareProfile>{

    public FoursquareClient() {}

    public FoursquareClient(String key, String secret) {
        setKey(key);
        setSecret(secret);
    }

    @Override
    protected void internalInit(final WebContext context) {
        super.internalInit(context);
        this.service = new Foursquare20Service((DefaultApi20) getApi(), buildOAuthConfig(context));
    }

    @Override
    protected Api getApi() {
        return Foursquare2Api.instance();
    }

    @Override
    protected String getOAuthScope() {
        return "user";
    }

    @Override
    protected String getProfileUrl(final Token accessToken) {
        return "https://api.foursquare.com/v2/users/self?v=20131118";
    }

    @Override
    protected FoursquareProfile extractUserProfile(String body) {
        FoursquareProfile profile = new FoursquareProfile();
        JsonNode json = JsonHelper.getFirstNode(body);
        if (json == null) {
            return profile;
        }
        JsonNode response = (JsonNode) JsonHelper.getElement(json, "response");
        if (response == null) {
            return profile;
        }
        JsonNode user = (JsonNode) JsonHelper.getElement(response, "user");
        if (user != null) {
            profile.setId(JsonHelper.getElement(user, "id"));

            for (final String attribute : profile.getAttributesDefinition().getPrimaryAttributes()) {
                profile.addAttribute(attribute, JsonHelper.getElement(user, attribute));
            }
        }
        return profile;
    }
}
