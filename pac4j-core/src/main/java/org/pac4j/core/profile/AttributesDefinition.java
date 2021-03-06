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
package org.pac4j.core.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pac4j.core.profile.converter.AttributeConverter;

/**
 * This class is the definition of the attributes of a profile.
 * 
 * @author Jerome Leleu
 * @since 1.1.0
 */
public class AttributesDefinition {
    
    protected List<String> primaries = new ArrayList<>();
    
    protected List<String> secondaries = new ArrayList<>();
    
    protected Map<String, AttributeConverter<? extends Object>> converters = new HashMap<>();
    
    /**
     * Return the primary attributes names.
     * 
     * @return the primary attributes names
     */
    public List<String> getPrimaryAttributes() {
        return this.primaries;
    }
    
    /**
     * Return the secondary attributes names.
     * 
     * @return the secondary attributes names
     */
    public List<String> getSecondaryAttributes() {
        return this.secondaries;
    }

    /**
     * Add an attribute as a primary one and its converter.
     * 
     * @param name name of the attribute
     * @param converter converter
     */
    protected void primary(final String name, final AttributeConverter<? extends Object> converter) {
        primaries.add(name);
        converters.put(name, converter);
    }

    /**
     * Add an attribute as a secondary one and its converter.
     *
     * @param name name of the attribute
     * @param converter converter
     */
    protected void secondary(final String name, final AttributeConverter<? extends Object> converter) {
        secondaries.add(name);
        converters.put(name, converter);
    }

    /**
     * Convert an attribute into the right type. If no converter exists for this attribute name, the attribute is returned.
     * 
     * @param name name of the attribute
     * @param value value of the attribute
     * @return the converted attribute or the attribute if no converter exists for this attribute name
     */
    public Object convert(final String name, final Object value) {
        AttributeConverter<? extends Object> converter = this.converters.get(name);
        if (converter != null && value != null) {
            return converter.convert(value);
        } else {
            return value;
        }
    }
}
