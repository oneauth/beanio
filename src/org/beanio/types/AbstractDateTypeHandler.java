/*
 * Copyright 2013 Kevin Seim
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.beanio.types;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This abstract type handler uses a <tt>SimpleDateFormat</tt> class to parse and format 
 * <tt>java.util.Date</tt> objects.  If no pattern is set, <tt>DateFormat.getInstance()</tt> 
 * is used to create a default date format.  By default, <tt>lenient</tt> is false.
 * 
 * @author Kevin Seim
 * @since 2.1.0
 * @see Date
 * @see DateFormat
 * @see SimpleDateFormat
 */
public abstract class AbstractDateTypeHandler implements ConfigurableTypeHandler {

    private String pattern = null;
    private boolean lenient = false;

    /**
     * Constructs a new AbstractDateTypeHandler.
     */
    public AbstractDateTypeHandler() { }

    /**
     * Constructs a new AbstractDateTypeHandler.
     * @param pattern the {@link SimpleDateFormat} pattern
     */
    public AbstractDateTypeHandler(String pattern) {
        this.pattern = pattern;
    }
    
    /**
     * Parses text into a {@link Date}.
     * @param text the text to parse
     * @return the parsed {@link Date}
     * @throws TypeConversionException
     */
    protected Date parseDate(String text) throws TypeConversionException {
        if ("".equals(text))
            return null;

        DateFormat dateFormat = createDateFormat();
        dateFormat.setLenient(lenient);
        
        ParsePosition pp = new ParsePosition(0);
        Date date = dateFormat.parse(text, pp);
        if (pp.getErrorIndex() >= 0 || pp.getIndex() != text.length()) {
            throw new TypeConversionException("Invalid date");
        }
        return date;
    }

    /**
     * Converts a {@link Date} to text.
     * @param date the {@link Date} to convert
     * @return the formatted text
     */
    protected String formatDate(Date date) {
        return date == null ? null : createDateFormat().format(date);
    }
    
    /**
     * Creates the <tt>DateFormat</tt> to use to parse and format the field value.
     * @return the <tt>DateFormat</tt> for type conversion
     */
    protected DateFormat createDateFormat() {
        if (pattern == null) {
            return createDefaultDateFormat();
        }
        else {
            return new SimpleDateFormat(pattern);
        }
    }
    
    /**
     * Creates a default date format when no pattern is set.
     * @return the default date format
     */
    protected DateFormat createDefaultDateFormat() {
        return DateFormat.getDateTimeInstance();
    }

    /**
     * Returns the date pattern used by the <tt>SimpleDateFormat</tt>.
     * @return the date pattern
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Sets the date pattern used by the <tt>SimpleDateFormat</tt>.
     * @param pattern the date pattern
     * @throws IllegalArgumentException if the date pattern is invalid
     */
    public void setPattern(String pattern) throws IllegalArgumentException {
        // validate the pattern
        try {
            if (pattern != null) {
                new SimpleDateFormat(pattern);
            }
        }
        catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid date format pattern '" + pattern + "': " + ex.getMessage());
        }
        
        this.pattern = pattern;
    }

    /**
     * Returns whether the <tt>SimpleDateFormat</tt> is lenient.
     * @return <tt>true</tt> if lenient, <tt>false</tt> otherwise
     */
    public boolean isLenient() {
        return lenient;
    }

    /**
     * Sets whether the <tt>SimpleDateFormat</tt> is lenient.
     * @param lenient <tt>true</tt> if lenient, <tt>false</tt> otherwise
     */
    public void setLenient(boolean lenient) {
        this.lenient = lenient;
    }
}
