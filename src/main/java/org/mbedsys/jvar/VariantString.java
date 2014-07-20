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
 * @author <a href="mailto:emericv@mbedsys.org">Emeric Verschuur</a> Copyright
 *         2014 MbedSYS
 */
public class VariantString extends Variant {

	protected String data;

	/**
	 * Constructor from string value
	 * 
	 * @param value
	 *            String
	 */
	public VariantString(String value) {
		if (value == null)
			throw new IllegalArgumentException("value argument cannot be null");
		data = value;
	}

	@Override
	public boolean booleanValue() {
		return !(data == null || data.trim().equalsIgnoreCase("false")
				|| data.trim().equals("0") || data.length() == 0);
	}

	@Override
	public byte byteValue() {
		return Byte.parseByte(data);
	}

	@Override
	public short shortValue() {
		return Short.parseShort(data);
	}

	@Override
	public int intValue() {
		return Integer.parseInt(data);
	}

	@Override
	public long longValue() {
		return Long.parseLong(data);
	}

	@Override
	public float floatValue() {
		return Float.parseFloat(data);
	}

	@Override
	public double doubleValue() {
		return Double.parseDouble(data);
	}

	@Override
	public byte[] toByteArray() {
		return data.getBytes();
	}

	@Override
	public String toString() {
		return data;
	}

	/**
	 * Write the value in JSON format
	 * 
	 * @param writer
	 *            output stream writer
	 * @throws IOException
	 */
	public static void writeJSONTo(OutputStreamWriter writer, String value)
			throws IOException {
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

	@Override
	public Type type() {
		return Type.STRING;
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public boolean isNull() {
		return false;
	}
}
