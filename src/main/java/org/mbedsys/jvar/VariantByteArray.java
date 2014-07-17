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
public class VariantByteArray extends Variant {
	
	protected byte[] data;

	private byte lengthType = LENGTH_UNDEFINED;
	
	public static final byte LENGTH_NONE		= (byte)0x00;
	public static final byte LENGTH_UNDEFINED	= (byte)0x01;
	public static final byte LENGTH_DEF_8BIT	= (byte)0x02;

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
	 * Variable sized frame field constructor from a byte array
	 * 
	 * @param data source byte array
	 */
	public VariantByteArray(byte[] data) {
		this.data = data;
	}
	
	/**
	 * Variable sized frame field constructor from an input stream
	 * 
	 * @param input			source input stream
	 * @param length		length
	 * @param lengthType	length type
	 */
	public VariantByteArray(ByteArrayInputStream input, int length, byte lengthType) {
		this.lengthType = lengthType;
		data = new byte[length];
		try {
			input.read(data);
		} catch (IOException e) {} // cannot fail
	}
	
	/**
	 * Variable sized frame field constructor from an input stream
	 * 
	 * @param input			source input stream
	 * @param lengthType	length type
	 */
	public VariantByteArray(ByteArrayInputStream input, byte lengthType) {
		this.lengthType = lengthType;
		switch (lengthType) {
		case LENGTH_NONE:
			data = new byte[input.available()];
			try {
				input.read(data);
			} catch (IOException e) {} // cannot fail
			break;
		case LENGTH_DEF_8BIT:
			data = new byte[input.read()];
			try {
				input.read(data);
			} catch (IOException e) {} // cannot fail
			break;
		default:
		}
	}

	/**
	 * Convert to a byte array
	 * 
	 * @return a byte[]
	 */
	@Override
	public byte[] toByteArray() {
		return data;
	}

	/**
	 * Get the value as a Object
	 * 
	 * @return the value as a Object
	 */
	@Override
	public Object toObject() {
		return data;
	}
	
	/**
	 * Write data to an output stream
	 * 
	 * @param output output stream
	 * @return number of written bytes
	 */
	@Override
	public final void writeTo(ByteArrayOutputStream output) {
		try {
			if (lengthType == LENGTH_DEF_8BIT) {
				output.write((byte)(data.length));
			}
			output.write(data);
		} catch (IOException e) {} // cannot fail
	}

	/**
	 * Return the human readable representation of the frame
	 * 
	 * @return a string
	 */
	@Override
	public String toString() {
		String dispFrame = "";
		for (int i = 0; i < data.length; i++) {
			dispFrame += " " + Integer.toHexString(0x100 | (int)(data[i] & 0xff)).substring(1);
		}
		return dispFrame;
	}
	
	/**
	 * Write the value in JSON format
	 * 
	 * @param writer output stream writer
	 * @throws IOException 
	 */
	@Override
	public void writeJSONTo(OutputStreamWriter writer, int flags) throws IOException {
		throw new UnsupportedOperationException("a byte array variant cannot be converted to JSON");
	}

	/**
	 * Compare this field to an other field
	 * 
	 * @param other other field
	 * @return the difference as an integer value
	 */
	@Override
	public int compareTo(Object other) {
		return data == ((VariantByteArray)other).data? 0 : -1;
	}

	/**
	 * Get type
	 * 
	 * @return a Class
	 */
	@Override
	public Type type() {
		return Type.BYTE_ARRAY;
	}
}
