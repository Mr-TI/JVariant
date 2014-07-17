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
