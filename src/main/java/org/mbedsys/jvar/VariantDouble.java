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
 * 
 * @author <a href="mailto:emericv@mbedsys.org">Emeric Verschuur</a>
 * Copyright 2014 MbedSYS
 */
public class VariantDouble extends VariantNumber {
	
	protected double data;

	/**
	 * Double variant constructor from a value
	 * 
	 * @param value double
	 */
	public VariantDouble(double value) {
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
		return (long) data;
	}

	@Override
	public float floatValue() {
		return (float) data;
	}

	@Override
	public double doubleValue() {
		return data;
	}

	@Override
	public String toString() {
		return String.valueOf(data);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) ((long)data ^ ((long)data >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Variant)) {
			return false;
		}
		return data == ((Variant)other).doubleValue();
	}

	@Override
	public int compareTo(Object other) {
		if (!(other instanceof Variant)) {
			return -1;
		}
		return (int) (data - ((Variant)other).doubleValue());
	}

	@Override
	public boolean isBitActive(int index) {
		throw new UnsupportedOperationException("boolean isBitActive(int index) unsupported for Double variant");
	}

	@Override
	public String toHexString() {
		return toHexString((long) data);
	}

	@Override
	public boolean isNull() {
		return false;
	}

	@Override
	public Type type() {
		return Type.DOUBLE;
	}

	@Override
	public Variant clone(int flags) {
		return new VariantDouble(data);
	}
}
