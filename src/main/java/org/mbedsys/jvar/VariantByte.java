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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.Format;

/**
 * 
 * @author <a href="mailto:emericv@mbedsys.org">Emeric Verschuur</a>
 * Copyright 2014 MbedSYS
 */
public class VariantByte extends VariantNumber implements Comparable<Object> {
	
	protected byte data;

	/**
	 * Default constructor.
	 */
	public VariantByte() {
	}

	/**
	 * Constructor from byte array input stream
	 * 
	 * @param input ByteArrayInputStream
	 */
	public VariantByte(ByteArrayInputStream input) {
		data = (byte) input.read();
	}

	/**
	 * Constructor from value
	 * 
	 * @param value
	 */
	public VariantByte(byte value) {
		data = value;
	}

	/**
	 * Get boolean value
	 * 
	 * @return a boolean
	 */
	@Override
	public boolean booleanValue() {
		return data != 0;
	}

	/**
	 * Get byte value
	 * 
	 * @return a byte
	 */
	@Override
	public byte byteValue() {
		return data;
	}

	/**
	 * Get the byte value using a binary mask
	 * 
	 * @param mask
	 * @return
	 */
	public byte byteValue(byte mask) {
		return (byte)(data & mask);
	}

	/**
	 * Get the sub value
	 * 
	 * @param offset sub value bit offset
	 * @param length sub value bit length
	 * @return
	 */
	public byte byteValue(int offset, int length) {
		byte mask = 0;
		for(int i=0; i < length; i++) {
			mask |= 1 << (offset + i);
		}
		return (byte)((data & mask) >> offset);
	}

	/**
	 * Get the short integer value
	 * 
	 * @return an integer
	 */
	@Override
	public short shortValue() {
		return data;
	}

	/**
	 * Get the integer value
	 * 
	 * @return an integer
	 */
	@Override
	public int intValue() {
		return shortValue();
	}

	/**
	 * Get the long integer value
	 * 
	 * @return a long integer
	 */
	@Override
	public long longValue() {
		return shortValue();
	}

	/**
	 * Get the a float value
	 * 
	 * @return a float
	 */
	@Override
	public float floatValue() {
		return shortValue();
	}

	/**
	 * Get the double value
	 * 
	 * @return a double
	 */
	@Override
	public double doubleValue() {
		return shortValue();
	}

	/**
	 * Convert to a byte array
	 * 
	 * @return a byte[]
	 */
	@Override
	public byte[] toByteArray() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		writeTo(output);
		return output.toByteArray();
	}

	/**
	 * Get the value as a Object
	 * 
	 * @return the value as a Object
	 */
	@Override
	public Object toObject() {
		return new Byte(data);
	}

	/**
	 * Get the state of a single value bit
	 * 
	 * @param indexbit index
	 * @return true: 1, false: 0
	 */
	@Override
	public boolean isBitActive(int index) {
		return (data & (1 << index)) != 0;
	}

	/**
	 * Test if the value match with a given mask
	 * 
	 * @param mask mask to test
	 * @return true: match succeed, false: match fails
	 */
	public boolean match(byte mask) {
		return ((data & mask) == mask);
	}

	/**
	 * Write the value to an output stream
	 * 
	 * @param output output stream
	 * @return number of written bytes
	 */
	@Override
	public final void writeTo(ByteArrayOutputStream output) {
		output.write(data);
	}

	/**
	 * Return the human readable representation of the frame variant
	 * 
	 * @return a string representation of the frame variant
	 */
	@Override
	public String toString() {
		return String.valueOf(data);
	}

	/**
	 * Return the variant as a string hexadecimal value
	 * 
	 * @return a string hexadecimal value
	 */
	@Override
	public String toHexString() {
		return Format.toHexString(data);
	}
	
	/**
	 * Write the value in JSON format
	 * 
	 * @param writer output stream writer
	 * @throws IOException 
	 */
	@Override
	public void writeJSONTo(OutputStreamWriter writer, int flags) throws IOException {
		writer.write(String.valueOf(data));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + data;
		return result;
	}

	/**
	 * Test if this object to an other variant
	 * 
	 * @param other other variant
	 * @return true if the two objects are equals, otherwise false
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Variant)) {
			return false;
		}
		return data == ((Variant)other).byteValue();
	}

	/**
	 * Compare this variant to an other variant
	 * 
	 * @param other other variant
	 * @return the difference as an integer value
	 */
	@Override
	public int compareTo(Object other) {
		if (!(other instanceof Variant)) {
			return -1;
		}
		return data - ((Variant)other).byteValue();
	}
}
