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
public abstract class VariantNumber extends Variant {

    /**
     * Convert byte value to an hexadecimal string
     *
     * @return a string
     */
    public static String toHexString(byte value) {
        return Integer.toHexString(0x100 | (int)(value & 0xff)).substring(1).toUpperCase();
    }

    /**
     * Convert a short integer value to an hexadecimal string
     *
     * @return a string
     */
    public static String toHexString(short value) {
        return Integer.toHexString(0x10000 | (int)(value & 0xffff)).substring(1).toUpperCase();
    }

    /**
     * Convert an integer to an hexadecimal string
     *
     * @return a string
     */
    public static String toHexString(int value) {
        return Integer.toHexString(0x10000 | (int)((value >> 16) & 0xffff)).substring(1).toUpperCase()
                + Integer.toHexString(0x10000 | (int)(value & 0xffff)).substring(1).toUpperCase();
    }

    /**
     * Convert a long integer to an hexadecimal string
     *
     * @return a string
     */
    public static String toHexString(long value) {
        return Integer.toHexString(0x10000 | (int)((value >> 48) & 0xffff)).substring(1).toUpperCase()
                + Integer.toHexString(0x10000 | (int)((value >> 32) & 0xffff)).substring(1).toUpperCase()
                + Integer.toHexString(0x10000 | (int)((value >> 16) & 0xffff)).substring(1).toUpperCase()
                + Integer.toHexString(0x10000 | (int)(value & 0xffff)).substring(1).toUpperCase();
    }
	
	public static VariantNumber optimize(int number) {
		if ((number & 0x7FFFFF80) == 0) {
			return new VariantByte((byte)number);
		} else if ((number & 0xFFFF8000) == 0) {
			return new VariantUShort((short)number);
		} else if ((number & 0x7FFF8000) == 0) {
			return new VariantShort((short)number);
		} else if ((number & 0x80000000) == 0) {
			return new VariantUInt(number);
		} else {
			return new VariantInt(number);
		}
	}
	
	public static VariantNumber optimize(long number) {
		if ((number & 0x7FFFFFFFFFFFFF80L) == 0) {
			return new VariantByte((byte)number);
		} else if ((number & 0xFFFFFFFFFFFF8000L) == 0) {
			return new VariantUShort((short)number);
		} else if ((number & 0x7FFFFFFFFFFF8000L) == 0) {
			return new VariantShort((short)number);
		} else if ((number & 0xFFFFFFFF80000000L) == 0) {
			return new VariantUInt((int)number);
		} else if ((number & 0x7FFFFFFF80000000L) == 0) {
			return new VariantInt((int)number);
		} else if ((number & 0x8000000000000000L) == 0) {
			return new VariantULong(number);
		} else {
			return new VariantLong(number);
		}
	}

	/**
	 * Get the state of a single value bit
	 * 
	 * @param index index
	 * @return true: 1, false: 0
	 */
	public abstract boolean isBitActive(int index);

	/**
	 * Returns the variant as a Hexadecimal String format for number variants.
	 *
	 * Applicable to all the variant types
	 *
	 * @return the variant as a String
	 */
	public abstract String toHexString();

	@Override
	public boolean isEmpty() {
		return false;
	}
}
