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
public class VariantByteArray extends Variant {
	
	protected byte[] data;

    protected static int toDigit(final char ch, final int index) {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new IllegalArgumentException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

	public static byte[] decodeHex(final char[] data) {

        final int len = data.length;

        if ((len & 0x01) != 0) {
            throw new IllegalArgumentException("Odd number of characters.");
        }

        final byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }
	
	/**
	 * Byte array variant constructor from a value
	 * 
	 * @param data source byte array
	 */
	public VariantByteArray(byte[] data) {
		this.data = data;
	}

	@Override
	public byte[] toByteArray() {
		return data;
	}

	@Override
	public String toString() {
		String dispFrame = "";
		for (int i = 0; i < data.length; i++) {
			dispFrame += " " + Integer.toHexString(0x100 | (int)(data[i] & 0xff)).substring(1);
		}
		return dispFrame;
	}

	@Override
	public int compareTo(Object other) {
		return data == ((VariantByteArray)other).data? 0 : -1;
	}

	@Override
	public Type type() {
		return Type.BYTEARRAY;
	}

	@Override
	public boolean isEmpty() {
		return data.length == 0;
	}

	@Override
	public boolean isNull() {
		return data == null;
	}

	@Override
	public Variant clone(int flags) {
		return new VariantByteArray(data);
	}
}
