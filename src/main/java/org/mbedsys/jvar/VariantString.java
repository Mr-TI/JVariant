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

/**
 * 
 * @author <a href="mailto:emericv@mbedsys.org">Emeric Verschuur</a>
 * Copyright 2014 MbedSYS
 */
public class VariantString extends Variant {

    protected String data;
    
    /**
     * Constructor from string value
     *
     * @param value String
     */
    public VariantString(String value) {
        data = value;
    }

    /**
     * Get boolean value
     *
     * @return a boolean
     */
    @Override
    public boolean booleanValue() {
        return !(data == null || data.equalsIgnoreCase("false") ||
                data.equals("0") || data.length() == 0);
    }

    /**
     * Get byte value
     *
     * @return a byte
     */
    @Override
    public byte byteValue() {
        return Byte.parseByte(data);
    }

    /**
     * Get the short integer value
     *
     * @return an integer
     */
    @Override
    public short shortValue() {
        return Short.parseShort(data);
    }

    /**
     * Get the integer value
     *
     * @return an integer
     */
    @Override
    public int intValue() {
        return Integer.parseInt(data);
    }

    /**
     * Get the long integer value
     *
     * @return a long integer
     */
    @Override
    public long longValue() {
        return Long.parseLong(data);
    }

    /**
     * Get the a float value
     *
     * @return a float
     */
    @Override
    public float floatValue() {
        return Float.parseFloat(data);
    }

    /**
     * Get the double value
     *
     * @return a double
     */
    @Override
    public double doubleValue() {
        return Double.parseDouble(data);
    }

    /**
     * Convert to a byte array
     *
     * @return a byte[]
     */
    @Override
    public byte[] toByteArray() {
        return data.getBytes();
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
     * Return the human readable representation of the frame field
     *
     * @return a string representation of the frame field
     */
    @Override
    public String toString() {
        return data;
    }

    /**
     * Write the value in JSON format
     *
     * @param writer output stream writer
     * @throws IOException
     */
    @Override
    public void writeJSONTo(OutputStreamWriter writer, int flags) throws IOException {
        writeJSONTo(writer, data);
    }

    /**
     * Write the value in JSON format
     *
     * @param writer output stream writer
     * @throws IOException
     */
    public static void writeJSONTo(OutputStreamWriter writer, String value) throws IOException {
        char c;
        writer.write('"');
        for (int i = 0; i < value.length(); i++) {
            c = value.charAt(i);
            switch (c) {
                case '\\':
                case '"':
                case '\b':
                case '\f':
                case '\n':
                case '\r':
                case '\t':
                    writer.write('\\');
                default:
                    writer.write(c);
                    break;
            }
        }
        writer.write('"');
    }

    /**
     * Compare this field to an other field
     *
     * @param other other field
     * @return the difference as an integer value
     */
    @Override
    public int compareTo(Object other) {
        if (other instanceof VariantString) {
            return data.compareTo(((VariantString) other).data);
        } else {
            return data.compareTo((String) other);
        }
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VariantString) {
            return data.equals(((VariantString) obj).data);
        } else {
            return data.equals((String) obj);
        }
    }

    /**
     * Get type
     *
     * @return a Class
     */
    @Override
    public Type type() {
        return Type.STRING;
    }
}
