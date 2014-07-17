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

import java.util.Random;

/**
 * The Variant interface describes an object which acts like a union for the most
 * common data types.
 * 
 * @author <a href="mailto:emericv@mbedsys.org">Emeric Verschuur</a>
 *         Copyright 2014 MbedSYS
 */
public abstract class Variant implements Comparable<Object> {
	
	public enum Type {
		NULL,
        BOOL,
		MAP, 
		LIST,
        BYTEARRAY,
        STRING,
        BYTE,
        INT,
        LONG,
        UINT,
        ULONG,
        DOUBLE,
        DATETIME
	};

	public static int FORMAT_JSON_COMPACT = 0x00000020;

	public static int JSON_INDENT_MASK = 0x0000001F;

	public static Variant IUD_GENERATOR = new VariantString() {
		private volatile Random numberGenerator = new Random();

		private final long MSB = 0x8000000000000000L;

		@Override
		public Type type() {
			return Type.STRING;
		}

		@Override
		public String toString() {
			return VariantNumber.toHexString(MSB | numberGenerator.nextLong())
					+ VariantNumber.toHexString(MSB | numberGenerator.nextLong());
		}
	};

	/**
	 * Returns the variant as a boolean.
	 * 
	 * Applicable to a number or string variant
	 * 
	 * @return true if the variant has {@link #type()} Boolean, Double, Integer,
	 *         Long, Byte, Short, Float and the value is non-zero, or if the
	 *         variant has type String and its lower-case content is not empty,
	 *         "0" or "false"; otherwise returns false or throws an instance of
	 *         a runtime exception in case of List or Map value
	 * @throws UnsupportedOperationException
	 *             - if the convention is not possible.
	 */
	public boolean booleanValue() {
        throw new UnsupportedOperationException("A variant with type " + type() + " cannot be converted to a boolean");
    }

	/**
	 * Returns the variant as a byte.
	 * 
	 * Applicable to a number or string variant
	 * 
	 * @return the variant as a byte if the variant has {@link #type()} Double,
	 *         Float, Boolean, Integer, Long and String if the last one contains
	 *         a textual representation (decimal, octal or binary format) of a
	 *         byte.
	 * @throws NumberFormatException
	 *             - if the string does not contain a parsable byte.
	 * @throws UnsupportedOperationException
	 *             - if the convention is not possible.
	 */
	public byte byteValue() {
        throw new UnsupportedOperationException("A variant with type " + type() + " cannot be converted to a byte");
    }

	/**
	 * Returns the variant as a short.
	 * 
	 * Applicable to a number or string variant
	 * 
	 * @return the variant as a short if the variant has {@link #type()} Double,
	 *         Float, Boolean, Integer, Long and String if the last one contains
	 *         a textual representation (decimal, octal or binary format) of a
	 *         short. Otherwise throws an instance of VariantFormatException
	 *         runtime exception.
	 * @throws NumberFormatException
	 *             - if the string does not contain a parsable byte.
	 * @throws UnsupportedOperationException
	 *             - if the convention is not possible.
	 */
	public short shortValue() {
        throw new UnsupportedOperationException("A variant with type " + type() + " cannot be converted to a short");
    }

	/**
	 * Returns the variant as an integer.
	 * 
	 * Applicable to a number or string variant
	 * 
	 * @return the variant as an integer if the variant has {@link #type()}
	 *         Double, Float, Boolean, Integer, Long and String if the last one
	 *         contains a textual representation (decimal, octal or binary
	 *         format) of an integer.
	 * @throws NumberFormatException
	 *             - if the string does not contain a parsable integer.
	 * @throws UnsupportedOperationException
	 *             - if the convention is not possible.
	 */
	public int intValue() {
        throw new UnsupportedOperationException("A variant with type " + type() + " cannot be converted to an integer");
    }

	/**
	 * Returns the variant as a long.
	 * 
	 * Applicable to a number or string variant
	 * 
	 * @return the variant as a long if the variant has {@link #type()} Double,
	 *         Float, Boolean, Integer, Long and String if the last one contains
	 *         a textual representation (decimal, octal or binary format) of a
	 *         long.
	 * @throws NumberFormatException
	 *             - if the string does not contain a parsable long.
	 * @throws UnsupportedOperationException
	 *             - if the convention is not possible.
	 */
	public long longValue() {
        throw new UnsupportedOperationException("A variant with type " + type() + " cannot be converted to a long");
    }

	/**
	 * Returns the variant as a float.
	 * 
	 * Applicable to a number or string variant
	 * 
	 * @return the variant as a float if the variant has {@link #type()} Double,
	 *         Float, Boolean, Integer, Long and String if the last one contains
	 *         a textual representation (decimal, octal or binary format) of a
	 *         float.
	 * @throws NumberFormatException
	 *             - if the string does not contain a parsable float.
	 * @throws UnsupportedOperationException
	 *             - if the convention is not possible.
	 */
	public float floatValue() {
        throw new UnsupportedOperationException("A variant with type " + type() + " cannot be converted to a float");
    }

	/**
	 * Returns the variant as a double.
	 * 
	 * Applicable to a number or string variant
	 * 
	 * @return the variant as a double if the variant has {@link #type()}
	 *         Double, Float, Boolean, Integer, Long and String if the last one
	 *         contains a textual representation (decimal, octal or binary
	 *         format) of a double.
	 * @throws NumberFormatException
	 *             - if the string does not contain a parsable double.
	 * @throws UnsupportedOperationException
	 *             - if the convention is not possible.
	 */
	public double doubleValue() {
        throw new UnsupportedOperationException("A variant with type " + type() + " cannot be converted to a double");
    }

	/**
	 * Returns the variant as a String.
	 * 
	 * Applicable to all the variant types
	 * 
	 * @return the variant as a String
	 */
	@Override
	public abstract String toString();

	/**
	 * Returns the variant as a byte array.
	 * 
	 * Applicable to all the variant types
	 * 
	 * @return the variant as a byte array
	 */
	public byte[] toByteArray() {
        throw new UnsupportedOperationException("A variant with type " + type() + " cannot be converted to a Variant");
    }

	/**
	 * Get the original object reference.
	 * 
	 * Applicable to all the variant types
	 * 
	 * @return an Object
	 */
	public VariantMap toMap() {
        throw new UnsupportedOperationException("A variant with type " + type() + " cannot be converted to a Variant");
    }

	/**
	 * Get the original object reference.
	 *
	 * Applicable to type List and Map
	 *
	 * @return an Object
	 */
	public VariantList toList() {
        throw new UnsupportedOperationException("A variant with type " + type() + " cannot be converted to a Variant");
    }

	/**
	 * Get variant as a VariantNumber
	 *
	 * Applicable to types: (U)Int, (U)Long, Double, Byte and Null
	 *
	 * @return an Object
	 */
	public VariantNumber toNumber() {
        throw new UnsupportedOperationException("A variant with type " + type() + " cannot be converted to a VariantNumber");
    }

	/**
	 * Get if the variant is empty
	 * 
	 * Applicable to all the variant types
	 * 
	 * @return true if the variant is empty, otherwise false
	 */
	public abstract boolean isEmpty();

	/**
	 * Get if the variant is null/undefined
	 * 
	 * Applicable to all the variant types
	 * 
	 * @return true if the variant is null/undefined, otherwise false
	 */
	public abstract boolean isNull();

	/**
	 * Return the variant type
	 * 
	 * Applicable to all the variant types
	 * 
	 * @return the type as Type
	 */
	public abstract Type type();
}
