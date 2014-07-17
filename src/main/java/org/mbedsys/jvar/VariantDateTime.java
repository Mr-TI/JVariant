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

	private long data;

	/**
	 * DateTime variant constructor from a date
	 *
	 * @param value Date
	 */
	public VariantDateTime(Date value) {
		data = value.getTime();
	}

	/**
	 * Constructor from a time
	 *
	 * @param value time in milliseconds from UNIX epoch
	 */
	public VariantDateTime(long value) {
		data = value;
	}

	@Override
	public boolean booleanValue() {
		return data != 0;
	}

	@Override
	public byte byteValue() {
		return (byte) data;
	}

	@Override
	public short shortValue() {
		return (short) data;
	}

	@Override
	public int intValue() {
		return (int) data;
	}

	@Override
	public long longValue() {
		return data;
	}

	@Override
	public float floatValue() {
		return data;
	}

	@Override
	public double doubleValue() {
		return data;
	}

	@Override
	public String toString() {
		return toString(new Date(data));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (data ^ (data >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Variant)) {
			return false;
		}
		return data == ((Variant)other).longValue();
	}

	@Override
	public int compareTo(Object other) {
		if (!(other instanceof Variant)) {
			return -1;
		}
		return (int) (data - ((Variant)other).longValue());
	}
	
	@Override
	public VariantDateTime toDate() {
		return this;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean isNull() {
		return false;
	}

	@Override
	public Type type() {
		return Type.DATETIME;
	}
}
