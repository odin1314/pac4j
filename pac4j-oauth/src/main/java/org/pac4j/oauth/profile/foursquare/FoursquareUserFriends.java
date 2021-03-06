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
package org.pac4j.oauth.profile.foursquare;

import org.pac4j.oauth.profile.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Foursquare user friend group container, with count and list of different groups.
 *
 * @author Alexey Ogarkov
 * @since 1.5.0
 */
public class FoursquareUserFriends extends JsonObject {

    private static final long serialVersionUID = -6264070010780654226L;

    private int count;
    private List<FoursquareUserFriendGroup> groups = new ArrayList<FoursquareUserFriendGroup>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<FoursquareUserFriendGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<FoursquareUserFriendGroup> groups) {
        this.groups = groups;
    }
}
