/*
 *   Copyright 2014 Emeric Verschuur <emericv@mbedsys.org>
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *		   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.mbedsys.jvar;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * @author <a href="mailto:emericv@mbedsys.org">Emeric Verschuur</a>
 * Copyright 2014 MbedSYS
 */
public class VariantNull extends VariantNumber implements Comparable<Object> {

    public static final Variant NULL = new VariantNull();
    private static final Set<String> EMPTY_KEY_SET = new HashSet<>();
    private static final Set<Entry<String, Variant>> EMPTY_ENTRY_SET = new HashSet<>();

    private static final Iterator<Variant> EMPTY_VARIANT_ITERATOR = new Iterator<Variant>() {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Variant next() {
            return null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    };

    /**
     * Default constructor.
     */
    public VariantNull() {
    }

    /**
     * Get boolean value
     *
     * @return a boolean
     */
    @Override
    public boolean booleanValue() {
        return false;
    }

    /**
     * Get byte value
     *
     * @return a byte
     */
    @Override
    public byte byteValue() {
        return 0;
    }

    /**
     * Get the short integer value
     *
     * @return an integer
     */
    @Override
    public short shortValue() {
        return 0;
    }

    /**
     * Get the integer value
     *
     * @return an integer
     */
    @Override
    public int intValue() {
        return 0;
    }

    /**
     * Get the long integer value
     *
     * @return a long integer
     */
    @Override
    public long longValue() {
        return 0;
    }

    /**
     * Get the a float value
     *
     * @return a float
     */
    @Override
    public float floatValue() {
        return 0;
    }

    /**
     * Get the double value
     *
     * @return a double
     */
    @Override
    public double doubleValue() {
        return 0;
    }

    /**
     * Return the human readable representation of the frame field
     *
     * @return a string representation of the frame field
     */
    @Override
    public String toString() {
        return "null";
    }

    /**
     * Write the value in JSON format
     *
     * @param writer output stream writer
     * @throws IOException
     */
    @Override
    public void writeJSONTo(OutputStreamWriter writer, int flags) throws IOException {
        writer.write("null");
    }

    /**
     * Convert the value in BSON format
     * <p/>
     * Applicable to all the variant types
     *
     * @return the value as a JSON String
     */
    @Override
    public byte[] toBSON() {
        return new byte[0];
    }

    /**
     * Convert the value in BCON format
     * <p/>
     * Applicable to all the variant types
     *
     * @param writer TODO
     * @return the value as a JSON String
     * @throws java.io.IOException
     */
    @Override
    public void writeBCONTo(OutputStreamWriter writer) throws IOException {

    }

    /**
     * Compare this variant to an other variant
     *
     * @param other other field
     * @return the difference as an integer value
     */
    @Override
    public int compareTo(Object other) {
        return other instanceof VariantNull ? 0 : -1;
    }

    /**
     * Get type
     *
     * @return a Class
     */
    @Override
    public Type type() {
        return Type.NULL;
    }

    @Override
    public boolean containsValue(Variant value) {
        return false;
    }

    @Override
    public Set<String> keySet() {
        return EMPTY_KEY_SET;
    }

    @Override
    public Set<Entry<String, Variant>> entrySet() {
        return EMPTY_ENTRY_SET;
    }

    @Override
    public Variant get(String key) {
        throw new KeyNotFoundException(key);
    }

    @Override
    public Variant get(String key, Variant defaultValue) {
        return defaultValue == null ? NULL : defaultValue;
    }

    @Override
    public Variant get(int index) {
        return NULL;
    }

    @Override
    public Iterator<Variant> iterator() {
        return EMPTY_VARIANT_ITERATOR;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean isNull() {
        return true;
    }


    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }

    /**
     * Get the original object reference.
     * <p/>
     * Applicable to all the variant types
     *
     * @return an Object
     */
    @Override
    public VariantMap toMap() {
        return null;
    }

    /**
     * Get the original object reference.
     * <p/>
     * Applicable to all the variant types
     *
     * @return an Object
     */
    @Override
    public VariantList toList() {
        return null;
    }

    /**
     * Get the state of a single value bit
     *
     * @param index index
     * @return true: 1, false: 0
     */
    @Override
    public boolean isBitActive(int index) {
        return false;
    }

    /**
     * Returns the variant as a Hexadecimal String format for number variants.
     * <p/>
     * Applicable to all the variant types
     *
     * @return the variant as a String
     */
    @Override
    public String toHexString() {
        return "0";
    }
}
