/*
 * Copyright 2011-2013 Kevin Seim
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
package org.beanio.internal.parser.format.fixedlength;

import org.beanio.internal.parser.UnmarshallingContext;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * The {@link UnmarshallingContext} implementation for a fixed length formatted stream.
 * 
 * @author Kevin Seim
 * @since 2.0
 */
public class FixedLengthUnmarshallingContext extends UnmarshallingContext {

    private String record;
    private int recordLength;

    @Override
    public void setRecordValue(Object value) {
        this.record = (String) value;
        int reccordBytesLen = 0;
        try {
            reccordBytesLen = record.getBytes(getEncoding()).length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.recordLength = value == null ? 0 : reccordBytesLen ;

    }
    
    /**
     * Returns the length of the record being unmarshalled.
     * @return the record length
     */
    public int getRecordLength() {
        return recordLength;
    }

    /**
     * Returns the field text at the given position in the record.
     * @param name the field name
     * @param position the position of the field in the record
     * @param length the field length, or -1 if the field is at the end of the
     *   record and unbounded
     * @param until the maximum position of the field as an offset
     *   of the field count, for example -2 to indicate the any position
     *   except the last two fields in the record
     * @return the field text, or null if the record length is less than
     *   the position of the field
     */
    public String getFieldText(String name, int position, int length, int until) {
        int max = recordLength + until;
        
        if (position < 0) {
            position = recordLength + position;
            
            position = getAdjustedFieldPosition(position);
            if (position < 0) {
                return null;
            }
        }
        else {
            position = getAdjustedFieldPosition(position);
            if (position >= max) {
                return null;
            }
        }
        
        String text = null;
        try {
            byte[] src = record.getBytes(getEncoding());
            byte[] tmp = new byte[length];
            System.arraycopy(src, position, tmp, 0, length);
            text = new String(tmp, 0, length, getEncoding());
            setFieldText(name, text);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return text;
    }
}
