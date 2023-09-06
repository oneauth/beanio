/*
 * Copyright 2012-2014 Kevin Seim
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
package org.beanio;

import java.util.List;

import org.beanio.internal.util.Debuggable;
import org.w3c.dom.*;

/**
 * Interface for marshalling bean objects.
 * 
 * <p>A <code>Marshaller</code> can be used to marshal a bean object bound to
 * a <code>record</code> in a mapping file.  Marshalling bean objects that span multiple
 * records is not supported and will cause a {@link BeanWriterException}.</p>
 * 
 * <p>Depending on the stream format, a bean object can be marshalled to one or more
 * formats.  All stream formats support marshalling to a <code>String</code> value,
 * as shown in the following example:</p>
 * 
 * <pre>   marshaller.marshal(object).toString();</pre>
 * 
 * <p>A <code>Marshaller</code> instance is stateful.  If a BeanIO mapping file declares
 * record ordering and expected occurrences, a {@link BeanWriterException} may be thrown for
 * bean objects written out of sequence or that have exceeded a record's maximum occurrences.</p>
 * 
 * <p>There is some performance benefit for reusing the same <code>Marshaller</code> instance,
 * but a <code>Marshaller</code> is not thread safe and should not be used to concurrently
 * marshal multiple bean objects.</p>
 * 
 * @author Kevin Seim
 * @since 2.0
 */
public interface Marshaller extends Debuggable {

    /**
     * Marshals a single bean object.
     * @param bean the bean object to marshal
     * @return this <code>Marshaller</code>
     * @throws BeanWriterException if a record is not matched for the given bean object,
     *   or in some other rare (but fatal) conditions
     * @throws InvalidBeanException if BeanIO is configured to validate fields during marshalling,
     *   and a field does not meet the configured validation rules
     */
    public Marshaller marshal(Object bean, String encoding) throws BeanWriterException;
    
    /**
     * Marshals a single bean object.
     * @param recordName the name of the record to marshal
     * @param bean the bean object to marshal
     * @return this <code>Marshaller</code>
     * @throws BeanWriterException if a record is not matched for the given record name
     *   and bean object, or in some other rare (but fatal) conditions
     * @throws InvalidBeanException if BeanIO is configured to validate fields during marshalling,
     *   and a field does not meet the configured validation rules
     */
    public Marshaller marshal(String recordName, Object bean, String encoding) throws BeanWriterException;

    /**
     * Returns the most recent marshalled bean object as a <code>String</code>.  This method
     * is supported by all stream formats.
     * @return the record text
     * @throws BeanWriterException if a fatal error occurs
     */
    public String toString() throws BeanWriterException;
    
    /**
     * Returns the most recent marshalled bean object as a <code>String[]</code> for <code>csv</code>
     * and <code>delimited</code> formatted streams.
     * @return the <code>String</code> array of fields
     * @throws BeanWriterException if an array is not supported by the stream format
     */
    public String[] toArray() throws BeanWriterException;

    /**
     * Returns the most recent marshalled bean object as a {@link List} for <code>csv</code>
     * and <code>delimited</code> formatted streams.
     * @return the {@link List} of fields
     * @throws BeanWriterException if an array is not supported by the stream format
     */
    public List<String> toList() throws BeanWriterException;
    
    /**
     * Returns the most recent marshalled bean object as a {@link Document} for <code>xml</code>
     * formatted streams.
     * @return the {@link Document}
     * @throws BeanWriterException if {@link Document} is not supported by the stream format
     */
    public Document toDocument() throws BeanWriterException;

}
