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
package org.pac4j.core.util;

import org.junit.Assert;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.MockWebContext;
import org.pac4j.core.exception.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class is an helper for tests: get a basic web client, parameters from an url, a formatted date, etc.
 *
 * @author Jerome Leleu
 * @since 1.0.0
 */
public final class TestsHelper {

    private static final Logger logger = LoggerFactory.getLogger(TestsHelper.class);

    public static Map<String, String> getParametersFromUrl(String url) {
        int pos = url.indexOf("?");
        if (pos >= 0) {
            url = url.substring(pos + 1);
        }
        pos = url.indexOf("#");
        if (pos >= 0) {
            url = url.substring(0, pos);
        }
        final Map<String, String> parameters = new HashMap<String, String>();
        final StringTokenizer st = new StringTokenizer(url, "&");
        while (st.hasMoreTokens()) {
            final String keyValue = st.nextToken();
            final String[] parts = keyValue.split("=");
            if (parts != null && parts.length >= 2) {
                try {
                    parameters.put(parts[0], URLDecoder.decode(parts[1], HttpConstants.UTF8_ENCODING));
                } catch (final UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return parameters;
    }

    public static String getFormattedDate(final long l, final String format, final Locale locale) {
        final Date d = new Date(l);
        SimpleDateFormat simpleDateFormat;
        if (locale == null) {
            simpleDateFormat = new SimpleDateFormat(format);
        } else {
            simpleDateFormat = new SimpleDateFormat(format, locale);
        }
        return simpleDateFormat.format(d);
    }

    public static void initShouldFail(final InitializableObject obj, final String message) {
        try {
            obj.init();
            Assert.fail("init should fail");
        } catch (final TechnicalException e) {
            Assert.assertEquals(message, e.getMessage());
        }
    }

    public static void initShouldFail(final InitializableWebObject obj, final String message) {
        try {
            obj.init(MockWebContext.create());
            Assert.fail("init should fail");
        } catch (final TechnicalException e) {
            Assert.assertEquals(message, e.getMessage());
        }
    }
}
