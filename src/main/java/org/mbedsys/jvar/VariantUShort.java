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


/**
 * Unsigned short integer variant
 * 
 * @author <a href="mailto:emericv@mbedsys.org">Emeric Verschuur</a>
 * Copyright 2014 MbedSYS
 */
public class VariantUShort extends VariantNumber {
	
	protected short data;

	/**
	 * Unsigned short integer variant constructor from a value
	 * 
	 * @param value
	 */
	public VariantUShort(short value) {
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
		return data;
	}

	@Override
	public int intValue() {
		return data & 0xFFFF;
	}

	/**
	 * Get the short value using a binary mask
	 * 
	 * @param mask mask to apply
	 * @return a short value
	 */
	public int intValue(int mask) {
		return data & mask;
	}

	/**
	 * Get the sub value
	 * 
	 * @param offset sub value bit offset
	 * @param length sub value bit length
	 * @return a short value
	 */
	public short shortValue(int offset, int length) {
		int mask = 0;
		for(int i=0; i < length; i++) {
			mask |= 1 << (offset + i);
		}
		return (short) ((data & mask) >> offset);
	}

	@Override
	public long longValue() {
		return data & 0xFFFFFFFFL;
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
	public boolean isBitActive(int index) {
		return (data & (1 << index)) != 0;
	}

	/**
	 * Test if the value match with a given mask
	 * 
	 * @param mask mask to test
	 * @return true: match succeed, false: match fails
	 */
	public boolean match(int mask) {
		return (data & mask) != 0;
	}

	@Override
	public String toString() {
		return String.valueOf(data);
	}

	@Override
	public String toHexString() {
		return toHexString(data);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + data;
		return result;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Variant)) {
			return false;
		}
		return data == ((Variant)other).intValue();
	}

	@Override
	public int compareTo(Object other) {
		if (!(other instanceof Variant)) {
			return -1;
		}
		return data - ((Variant)other).intValue();
	}

	@Override
	public boolean isNull() {
		return data == 0;
	}

	@Override
	public Type type() {
		return Type.UINT;
	}
	
	@Override
	public Variant clone(int flags) {
		return new VariantUShort(data);
	}
}
