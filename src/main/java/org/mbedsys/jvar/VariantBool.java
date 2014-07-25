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
 * Boolean variant
 * 
 * @author <a href="mailto:emericv@mbedsys.org">Emeric Verschuur</a>
 * Copyright 2014 MbedSYS
 */
public class VariantBool extends VariantNumber {
	
	protected boolean data;

	/**
	 * Bool variant constructor from value
	 * 
	 * @param value initial value
	 */
	public VariantBool(boolean value) {
		data = value;
	}

    @Override
    public boolean booleanValue() {
        return data;
    }

    @Override
    public byte byteValue() {
        return (byte) (data ? 1 : 0);
    }

    @Override
    public int compareTo(Object o) {
        return ((o instanceof Variant && ((Variant)o).booleanValue()) ||
                (o instanceof Boolean && ((Boolean)o).booleanValue()))? 0: -1;
    }

    @Override
    public double doubleValue() {
        return data ? 1 : 0;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Variant && ((Variant)obj).booleanValue()) ||
                (obj instanceof Boolean && ((Boolean)obj).booleanValue());
    }

    @Override
    public float floatValue() {
        return data ? 1 : 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
		int result = 1;
		result = prime * result + (data ? 1231 : 1237);
		return result;
    }

    @Override
    public int intValue() {
        return data ? 1 : 0;
    }

    @Override
    public boolean isBitActive(int index) {
        return index == 0 && data;
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
    public long longValue() {
        return data ? 1 : 0;
    }

    @Override
    public short shortValue() {
        return (short) (data ? 1 : 0);
    }

    @Override
    public String toHexString() {
        return data? "1": "0";
    }

    @Override
    public VariantNumber toNumber() {
        return this;
    }

    @Override
    public String toString() {
        return data ? "true": "false";
    }

    @Override
    public Type type() {
        return Type.BOOL;
    }
}
