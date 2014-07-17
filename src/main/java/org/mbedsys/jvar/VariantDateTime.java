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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 
 * @author <a href="mailto:emericv@mbedsys.org">Emeric Verschuur</a>
 * Copyright 2014 MbedSYS
 */
public class VariantDateTime extends Variant {
	private static Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));

	public static String toString(Date date) {
		StringBuffer buff = new StringBuffer();

		synchronized (calendar) {
			calendar.setTime(date);

			buff.insert(0, calendar.get(Calendar.SECOND));
			if (buff.length() < 2) {
				buff.insert(0, '0');
			}
			buff.insert(0, ':');
			buff.insert(0, calendar.get(Calendar.MINUTE));
			if (buff.length() < 5) {
				buff.insert(0, '0');
			}
			buff.insert(0, ':');
			buff.insert(0, calendar.get(Calendar.HOUR_OF_DAY));
			if (buff.length() < 8) {
				buff.insert(0, ' ');
			}
			buff.insert(0, ' ');
			buff.insert(0, calendar.get(Calendar.DAY_OF_MONTH));
			if (buff.length() < 11) {
				buff.insert(0, '0');
			}
			buff.insert(0, '-');
			buff.insert(0, calendar.get(Calendar.MONTH) + 1);
			if (buff.length() < 14) {
				buff.insert(0, '0');
			}
			buff.insert(0, '-');
			buff.insert(0, calendar.get(Calendar.YEAR));
		}

		return buff.toString();
	}

	public static Date getDate(int year, int month, int day, int hour, int minute, int second, int millisecond) {
		synchronized (calendar) {
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.DAY_OF_MONTH, day);
			calendar.set(Calendar.HOUR_OF_DAY, hour);
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, second);
			calendar.set(Calendar.MILLISECOND, millisecond);
			return calendar.getTime();
		}
	}

	private long data = 0;

	/**
	 * Default constructor.
	 */
	public VariantDateTime() {
	}

	/**
	 * Constructor from value
	 *
	 * @param value
	 */
	public VariantDateTime(long value) {
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
	 * Get the long value using a binary mask
	 *
	 * @param mask mask to apply
	 * @return a long value
	 */
	public long longValue(int mask) {
		return data & mask;
	}

	/**
	 * Get the sub value
	 *
	 * @param offset sub value bit offset
	 * @param length sub value bit length
	 * @return a long integer value
	 */
	public long longValue(int offset, int length) {
		long mask = 0;
		for(int i=0; i < length; i++) {
			mask |= 1 << (offset + i);
		}
		return (data & mask) >> offset;
	}

	/**
	 * Get the long integer value
	 *
	 * @return a long integer
	 */
	@Override
	public long longValue() {
		return data;
	}

	/**
	 * Get the a float value
	 *
	 * @return a float
	 */
	@Override
	public float floatValue() {
		return data;
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
		return new Long(data);
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
	 * @throws java.io.IOException
	 */
	@Override
	public void writeJSONTo(OutputStreamWriter writer, int flags) throws IOException {
		writer.write(String.valueOf(data));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (data ^ (data >>> 32));
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
		return data == ((Variant)other).longValue();
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
		return (int) (data - ((Variant)other).longValue());
	}
}
