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

/**
 * 
 * @author <a href="mailto:emericv@mbedsys.org">Emeric Verschuur</a>
 * Copyright 2014 MbedSYS
 */
public class VariantDouble extends VariantNumber {
	
	protected double data = 0;
	
	/**
	 * Default constructor.
	 */
	public VariantDouble() {
	}


	/**
	 * Constructor from byte array input stream
	 * 
	 * @param input ByteArrayInputStream
	 */
	public VariantDouble(ByteArrayInputStream input, int byteLength) {
		throw new NotImplementedException();
		//TODO: Implementation
//		data = 0;
//		for (int i = 0; i < byteLength && input.available() > 0; i++) {
//			data |= ((long)input.read() << (i * 8));
//		}
	}


	/**
	 * Constructor from byte array input stream
	 * 
	 * @param input ByteArrayInputStream
	 */
	public VariantDouble(ByteArrayInputStream input) {
		this(input, 8);
	}
	
	/**
	 * Constructor from value
	 * 
	 * @param value
	 */
	public VariantDouble(double value) {
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
		return (byte) data;
	}

	/**
	 * Get the short integer value
	 * 
	 * @return an integer
	 */
	@Override
	public short shortValue() {
		return (short) data;
	}

	/**
	 * Get the integer value
	 * 
	 * @return an integer
	 */
	@Override
	public int intValue() {
		return (int) data;
	}

	/**
	 * Get the long integer value
	 * 
	 * @return a long integer
	 */
	@Override
	public long longValue() {
		return (long) data;
	}

	/**
	 * Get the a float value
	 * 
	 * @return a float
	 */
	@Override
	public float floatValue() {
		return (float) data;
	}

	/**
	 * Get the double value
	 * 
	 * @return a double
	 */
	@Override
	public double doubleValue() {
		return data;
	}
	
	/**
	 * Convert to a byte array
	 * 
	 * @return a byte[]
	 */
	@Override
	public byte[] toByteArray() {
		throw new NotImplementedException();
		//TODO: Implementation
//		ByteArrayOutputStream output = new ByteArrayOutputStream();
//		writeTo(output);
//		return output.toByteArray();
	}

	/**
	 * Get the value as a Object
	 * 
	 * @return the value as a Object
	 */
	@Override
	public Object toObject() {
		return new Double(data);
	}

	/**
	 * Write the value to an output stream
	 * 
	 * @param output output stream
	 * @return number of written bytes
	 */
	public final void writeTo(ByteArrayOutputStream output, int limit) {
		throw new NotImplementedException();
		//TODO: Implementation
//		for (int i = 0; i < limit; i++) {
//			output.write((byte) ((data >> (i * 8)) & 0xff));
//		}
	}

	/**
	 * Write the value to an output stream
	 * 
	 * @param output output stream
	 * @return number of written bytes
	 */
	@Override
	public final void writeTo(ByteArrayOutputStream output) {
		throw new NotImplementedException();
		//TODO: Implementation
//		writeTo(output, 8);
	}

	/**
	 * Return the human readable representation of the frame field
	 * 
	 * @return a string representation of the frame field
	 */
	@Override
	public String toString() {
		return String.valueOf(data);
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
		result = prime * result + (int) ((long)data ^ ((long)data >>> 32));
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
		return data == ((Variant)other).doubleValue();
	}

	/**
	 * Compare this field to an other field
	 * 
	 * @param other other field
	 * @return the difference as an integer value
	 */
	@Override
	public int compareTo(Object other) {
		if (!(other instanceof Variant)) {
			return -1;
		}
		return (int) (data - ((Variant)other).doubleValue());
	}

	@Override
	public boolean isBitActive(int index) {
		throw new UnsupportedOperationException();
	}
}
