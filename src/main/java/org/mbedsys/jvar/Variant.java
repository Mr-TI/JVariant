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
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * The Variant abstract class describes an object which acts like a union for
 * the most common data types.
 * 
 * @author <a href="mailto:emericv@mbedsys.org">Emeric Verschuur</a> Copyright
 *         2014 MbedSYS
 */
public abstract class Variant implements Comparable<Object> {

	public enum Type {
		BOOL, BYTE, BYTEARRAY, DATETIME, DOUBLE, INT, LIST, LONG, MAP, NULL, STRING, UINT, ULONG
	}

	public enum Format {
		JSON, BSON, BCON
	}

	public static final Variant NULL = new VariantNull();

	public static int FORMAT_JSON_COMPACT = 0x00000020;

	public static Variant IUD_GENERATOR = new VariantString("") {
		private final long MSB = 0x8000000000000000L;

		private volatile Random numberGenerator = new Random();

		@Override
		public String toString() {
			return VariantNumber.toHexString(MSB | numberGenerator.nextLong())
					+ VariantNumber.toHexString(MSB
							| numberGenerator.nextLong());
		}

		@Override
		public Type type() {
			return Type.STRING;
		}
	};

	public static int JSON_INDENT_MASK = 0x0000001F;

	private static char[] spaces = null;

	public static void appendSpaces(StringBuffer buffer, int count) {
		if (spaces == null || spaces.length < count) {
			spaces = new char[count + 32];
			Arrays.fill(spaces, ' ');
		}
		buffer.append(spaces, 0, count);
	}

	public static void appendSpaces(OutputStreamWriter writer, int count)
			throws IOException {
		if (spaces == null || spaces.length < count) {
			spaces = new char[count + 32];
			Arrays.fill(spaces, ' ');
		}
		writer.write(spaces, 0, count);
	}

	private static final byte BCON_TOKEN_END = (byte) 0x00;
	private static final byte BCON_TOKEN_NULL = (byte) 0x01;
	private static final byte BCON_TOKEN_TRUE = (byte) 0x02;
	private static final byte BCON_TOKEN_FALSE = (byte) 0x03;
	private static final byte BCON_TOKEN_BYTE = (byte) 0x04;
	private static final byte BCON_TOKEN_INT32 = (byte) 0x05;
	private static final byte BCON_TOKEN_INT64 = (byte) 0x06;
	private static final byte BCON_TOKEN_UINT32 = (byte) 0x07;
	private static final byte BCON_TOKEN_UINT64 = (byte) 0x08;
	private static final byte BCON_TOKEN_DOUBLE = (byte) 0x0A;
	private static final byte BCON_TOKEN_DATETIME = (byte) 0x0B;
	private static final byte BCON_TOKEN_MAP = (byte) 0x0C;
	private static final byte BCON_TOKEN_LIST = (byte) 0x0D;
	private static final byte BCON_TOKEN_DATA6 = (byte) 0xA0;
	private static final byte BCON_TOKEN_STRING6 = (byte) 0xC0;
	private static final byte BCON_TOKEN_DATA12 = (byte) 0x10;
	private static final byte BCON_TOKEN_DATA20 = (byte) 0x20;
	private static final byte BCON_TOKEN_DATA36 = (byte) 0x30;
	private static final byte BCON_TOKEN_STRING12 = (byte) 0x50;
	private static final byte BCON_TOKEN_STRING20 = (byte) 0x60;
	private static final byte BCON_TOKEN_STRING36 = (byte) 0x70;

	private static final int LENGTH2P6 = 64;
	private static final int LENGTH2P12 = 4096;
	private static final int LENGTH2P20 = 1048576;
	private static final long LENGTH2P36 = 68719476736L;

	private static final byte BSON_TOKEN_END = (byte) 0x00;
	private static final byte BSON_TOKEN_NULL = (byte) 0x0A;
	private static final byte BSON_TOKEN_UNDEF = (byte) 0x06;
	private static final byte BSON_TOKEN_TRUE = (byte) 0x00;
	private static final byte BSON_TOKEN_FALSE = (byte) 0x01;
	private static final byte BSON_TOKEN_INT32 = (byte) 0x10;
	private static final byte BSON_TOKEN_INT64 = (byte) 0x12;
	private static final byte BSON_TOKEN_DOUBLE = (byte) 0x01;
	private static final byte BSON_TOKEN_DATETIME = (byte) 0x09;
	private static final byte BSON_TOKEN_STRING = (byte) 0x02;
	private static final byte BSON_TOKEN_DATA = (byte) 0x05;
	private static final byte BSON_TOKEN_JSCODE = (byte) 0x0D;
	private static final byte BSON_TOKEN_OID = (byte) 0x07;
	private static final byte BSON_TOKEN_BOOL = (byte) 0x08;
	private static final byte BSON_TOKEN_MAP = (byte) 0x03;
	private static final byte BSON_TOKEN_LIST = (byte) 0x04;
	private static final byte BSON_TOKEN_GENERIC = (byte) 0x00;
	private static final byte BSON_TOKEN_OLDUUID = (byte) 0x03;
	private static final byte BSON_TOKEN_UUID = (byte) 0x04;

	public static final Variant TRUE = new VariantBool(true);

	public static final Variant FALSE = new VariantBool(false);

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
		throw new UnsupportedOperationException("A variant with type " + type()
				+ " cannot be converted to a boolean");
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
		throw new UnsupportedOperationException("A variant with type " + type()
				+ " cannot be converted to a byte");
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
		throw new UnsupportedOperationException("A variant with type " + type()
				+ " cannot be converted to a double");
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
		throw new UnsupportedOperationException("A variant with type " + type()
				+ " cannot be converted to a float");
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
		throw new UnsupportedOperationException("A variant with type " + type()
				+ " cannot be converted to an integer");
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
		throw new UnsupportedOperationException("A variant with type " + type()
				+ " cannot be converted to a long");
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
		throw new UnsupportedOperationException("A variant with type " + type()
				+ " cannot be converted to a short");
	}

	/**
	 * Returns the variant as a byte array.
	 * 
	 * Applicable to all the variant types
	 * 
	 * @return the variant as a byte array
	 */
	public byte[] toByteArray() {
		throw new UnsupportedOperationException("A variant with type " + type()
				+ " cannot be converted to a Variant");
	}

	/**
	 * Get the original object reference.
	 * 
	 * Applicable to type List and Map
	 * 
	 * @return an Object
	 */
	public VariantList toList() {
		throw new UnsupportedOperationException("A variant with type " + type()
				+ " cannot be converted to a Variant");
	}

	/**
	 * Get the original object reference.
	 * 
	 * Applicable to all the variant types
	 * 
	 * @return an Object
	 */
	public VariantMap toMap() {
		throw new UnsupportedOperationException("A variant with type " + type()
				+ " cannot be converted to a Variant");
	}

	/**
	 * Get variant as a VariantNumber
	 * 
	 * Applicable to types: (U)Int, (U)Long, Double, Byte and Null
	 * 
	 * @return an Object
	 */
	public VariantNumber toNumber() {
		throw new UnsupportedOperationException("A variant with type " + type()
				+ " cannot be converted to a VariantNumber");
	}

	/**
	 * Get variant as a VariantDate
	 * 
	 * Applicable to types: (U)Int, (U)Long, Double, Byte and Null
	 * 
	 * @return an Object
	 */
	public VariantDateTime toDate() {
		throw new UnsupportedOperationException("A variant with type " + type()
				+ " cannot be converted to a VariantNumber");
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
	 * Return the variant type
	 * 
	 * Applicable to all the variant types
	 * 
	 * @return the type as Type
	 */
	public abstract Type type();

	/**
	 * Abstract parser
	 */
	public static abstract class Parser {
		/**
		 * Parse next node on the stream
		 * 
		 * @param wait wait for available data on stream
		 * @return a Variant object or null if wait=<code>false</code> and no data is available 
		 * @throws IOException on IO/parsing error
		 */
		public abstract Variant next(boolean wait) throws IOException;
		
		/**
		 * Parse next node on the stream
		 * 
		 * @return a Variant object
		 * @throws IOException on IO/parsing error
		 */
		public Variant next() throws IOException {
			return next(true);
		}
	}

	/**
	 * Return the suitable parser corresponding to the given format
	 * 
	 * @param input Data stream
	 * @param format Data format type
	 * @return the suitable parser
	 */
	public static Parser newParser(final InputStream input, Format format) {
		switch (format) {
		case BCON:
			return new Parser() {
				@Override
				public Variant next(boolean wait) throws IOException {
					if (!wait && input.available() == 0) {
						return null;
					}
					return parseBCON(input, null);
				}
			};
		case BSON:
			return new Parser() {
				@Override
				public Variant next(boolean wait) throws IOException {
					if (!wait && input.available() == 0) {
						return null;
					}
					return parseBSONDocument(input);
				}
			};
		case JSON:
			return new Parser() {
				JSONScanner scanner = new JSONScanner(new InputStreamReader(
						input));

				@Override
				public Variant next(boolean wait) throws IOException {
					if (!wait && !scanner.ready()) {
						return null;
					}
					JSONTocken t = scanner.yylex();
					switch (t.getId()) {
					case JSONTocken.TEOF:
						throw new EOFException();
					case JSONTocken.TOBJBEGIN:
						return parseJSONObject(scanner);
					case JSONTocken.TARRBEGIN:
						return parseJSONArray(scanner);
					default:
						throwJSONError(t, new int[] { JSONTocken.TOBJBEGIN,
								JSONTocken.TARRBEGIN });
					}
					return null;
				}
			};
		default:
			throw new IllegalArgumentException("Unsupported format");
		}
	}

	/**
	 * Write the value in JSON format
	 * 
	 * @param writer
	 *            output stream writer
	 * @throws IOException
	 */
	private static void serializeJSONList(OutputStreamWriter writer,
			List<Variant> list, int flags) throws IOException {
		boolean conpact = (flags & FORMAT_JSON_COMPACT) != 0;
		int indentStep = flags & JSON_INDENT_MASK;
		int indentOff = (flags >> 16) + indentStep;
		flags = (flags & 0xFFFF) | (indentOff << 16);
		Iterator<Variant> it = list.iterator();
		writer.write('[');
		if (it.hasNext()) {
			if (indentOff != 0) {
				writer.write('\n');
				appendSpaces(writer, indentOff);
			}
			serializeJSONElt(writer, it.next(), flags);
			while (it.hasNext()) {
				writer.write(',');
				if (indentOff != 0) {
					writer.write('\n');
					appendSpaces(writer, indentOff);
				} else if (!conpact) {
					writer.write(' ');
				}
				serializeJSONElt(writer, it.next(), flags);
			}
			if (indentOff != 0) {
				writer.write('\n');
				appendSpaces(writer, indentOff - indentStep);
			}
		}
		writer.write(']');
	}

	/**
	 * Write the value in JSON format
	 * 
	 * @param writer
	 *            output stream writer
	 * @throws IOException
	 */
	private static void serializeJSONMap(OutputStreamWriter writer,
			Map<String, Variant> map, int flags) throws IOException {
		Iterator<String> keys = map.keySet().iterator();
		boolean conpact = (flags & FORMAT_JSON_COMPACT) != 0;
		int indentStep = flags & JSON_INDENT_MASK;
		int indentOff = (flags >> 16) + indentStep;
		flags = (flags & 0xFFFF) | (indentOff << 16);
		String key;
		writer.write('{');
		if (keys.hasNext()) {
			if (indentOff != 0) {
				writer.write('\n');
				appendSpaces(writer, indentOff);
			}
			key = keys.next();
			VariantString.writeJSONTo(writer, key);
			writer.write(':');
			if (!conpact) {
				writer.write(' ');
			}
			serializeJSONElt(writer, map.get(key), flags);
			while (keys.hasNext()) {
				key = keys.next();
				writer.write(',');
				if (indentOff != 0) {
					writer.write('\n');
					appendSpaces(writer, indentOff);
				} else if (!conpact) {
					writer.write(' ');
				}
				VariantString.writeJSONTo(writer, key);
				writer.write(':');
				if (!conpact) {
					writer.write(' ');
				}
				serializeJSONElt(writer, map.get(key), flags);
			}
			if (indentOff != 0) {
				writer.write('\n');
				appendSpaces(writer, indentOff - indentStep);
			}
		}
		writer.write('}');
	}

	protected static void serializeJSONElt(OutputStreamWriter writer,
			Variant variant, int flags) throws IOException {
		switch (variant.type()) {
		case BOOL:
			writer.append(variant.booleanValue() ? "true" : "false");
			break;
		case BYTE:
			writer.append(Byte.toString(variant.byteValue()));
			break;
		case BYTEARRAY:
		case STRING:
		case DATETIME:
			VariantString.writeJSONTo(writer, variant.toString());
			break;
		case LIST:
			serializeJSONList(writer, variant.toList(), flags);
			break;
		case MAP:
			serializeJSONMap(writer, variant.toMap(), flags);
			break;
		case NULL:
			writer.append("null");
			break;
		case INT:
		case UINT:
		case LONG:
		case ULONG:
			writer.append(Long.toString(variant.longValue()));
			break;
		case DOUBLE:
			writer.append(Double.toString(variant.doubleValue()));
			break;
		default:
			break;

		}
	}

	public static void serializeJSON(OutputStreamWriter writer,
			Variant variant, int flags) throws IOException {
		switch (variant.type()) {
		case LIST:
			serializeJSONList(writer, variant.toList(), flags);
			break;
		case MAP:
			serializeJSONMap(writer, variant.toMap(), flags);
			break;
		default:
			throw new IllegalArgumentException(
					"The root node to serialize must be an map or an array");
		}
	}

	private static void write32(OutputStream output, byte type, int value)
			throws IOException {
		output.write(type);
		output.write(((byte) ((value & 0xFF))));
		output.write(((byte) (((value >> 8) & 0xFF))));
		output.write(((byte) (((value >> 16) & 0xFF))));
		output.write(((byte) (((value >> 24) & 0xFF))));
	}

	private static void write64(OutputStream output, byte type, long value)
			throws IOException {
		output.write(type);
		output.write(((byte) ((value & 0xFF))));
		output.write(((byte) (((value >> 8) & 0xFF))));
		output.write(((byte) (((value >> 16) & 0xFF))));
		output.write(((byte) (((value >> 24) & 0xFF))));
		output.write(((byte) (((value >> 32) & 0xFF))));
		output.write(((byte) (((value >> 40) & 0xFF))));
		output.write(((byte) (((value >> 48) & 0xFF))));
		output.write(((byte) (((value >> 56) & 0xFF))));
	}

	public static void serializeBCON(OutputStream output, Variant variant)
			throws IOException {
		serializeBCON(output, variant, null);
	}

	private static void serializeBCON(OutputStream output, Variant variant,
			String key) throws IOException {
		switch (variant.type()) {
		case NULL:
			output.write(BCON_TOKEN_NULL);
			break;
		case BOOL: // Case of BCON boolean
			output.write((variant.booleanValue() ? BCON_TOKEN_TRUE
					: BCON_TOKEN_FALSE));
			break;
		case BYTE: // Case of BCON boolean
			output.write(BCON_TOKEN_BYTE);
			output.write(variant.byteValue());
			break;
		case UINT: {
			write32(output, BCON_TOKEN_UINT32, variant.intValue());
			break;
		}
		case ULONG: {
			write64(output, BCON_TOKEN_UINT64, variant.intValue());
			break;
		}
		case INT: {
			write32(output, BCON_TOKEN_INT32, variant.intValue());
			break;
		}
		case LONG: {
			write64(output, BCON_TOKEN_INT64, variant.longValue());
			break;
		}
		case DOUBLE: {
			write64(output, BCON_TOKEN_DOUBLE, variant.longValue());
			break;
		}
		case DATETIME: {
			write64(output, BCON_TOKEN_DATETIME, variant.longValue());
			break;
		}
		case MAP: // Case of BCON object
		{
			output.write(BCON_TOKEN_MAP);
			for (Entry<String, Variant> entry : variant.toMap().entrySet()) {
				serializeBCON(output, entry.getValue(), entry.getKey());
			}
			output.write(BCON_TOKEN_END);
			break;
		}
		case LIST: // Case of BCON array
		{
			output.write(BCON_TOKEN_LIST);
			for (Variant elt : variant.toList()) {
				serializeBCON(output, elt, null);
			}
			output.write(BCON_TOKEN_END);
			break;
		}
		case STRING: {
			byte[] data = variant.toString().getBytes();
			int len = data.length;
			if (len < (LENGTH2P6)) {
				output.write((byte) ((BCON_TOKEN_STRING6 & 0xFF) | (len & 0x3F)));
				output.write(data);
			} else if (len < (LENGTH2P12)) {
				output.write((byte) ((BCON_TOKEN_STRING12 & 0xFF) | (len & 0x0F)));
				output.write((byte) ((len & 0xFF0) >> 4));
				output.write(data);
			} else if (len < (LENGTH2P20)) {
				output.write((byte) ((BCON_TOKEN_STRING20 & 0xFF) | (len & 0x0F)));
				output.write((byte) ((len & 0xFF0) >> 4));
				output.write((byte) ((len & 0xFF000) >> 12));
				output.write(data);
			} else if (len < (LENGTH2P36)) {
				output.write((byte) ((BCON_TOKEN_STRING36 & 0xFF) | (len & 0x0F)));
				output.write((byte) ((len & 0xFF0) >> 4));
				output.write((byte) ((len & 0xFF000) >> 12));
				output.write((byte) (len >> 20));
				output.write(data);
			} else {
				throw new SerializerException("Fatal: too big String (length="
						+ len + ")");
			}
			break;
		}
		case BYTEARRAY: // Case of BCON string
		{
			byte[] data = variant.toByteArray();
			int len = data.length;
			if (len < (LENGTH2P6)) {
				output.write((byte) ((BCON_TOKEN_DATA6 & 0xFF) | (len & 0x3F)));
				output.write(data);
			} else if (len < (LENGTH2P12)) {
				output.write((byte) ((BCON_TOKEN_DATA12 & 0xFF) | (len & 0x0F)));
				output.write((byte) ((len & 0xFF0) >> 4));
				output.write(data);
			} else if (len < (LENGTH2P20)) {
				output.write((byte) ((BCON_TOKEN_DATA20 & 0xFF) | (len & 0x0F)));
				output.write((byte) ((len & 0xFF0) >> 4));
				output.write((byte) ((len & 0xFF000) >> 12));
				output.write(data);
			} else if (len < (LENGTH2P36)) {
				output.write((byte) ((BCON_TOKEN_DATA36 & 0xFF) | (len & 0x0F)));
				output.write((byte) ((len & 0xFF0) >> 4));
				output.write((byte) ((len & 0xFF000) >> 12));
				output.write((byte) (len >> 20));
				output.write(data);
			} else {
				throw new SerializerException(
						"Fatal: too big byte array (length=" + len + ")");
			}
			break;
		}
		default:
			throw new SerializerException("Fatal: QVariant type not managed.");

		}

		if (key != null) {
			output.write(key.getBytes());
			output.write('\0');
		}
	}

	private static void write32(ByteArrayOutputStream output, int value) {
		output.write((byte) ((value & 0xFF)));
		output.write((byte) (((value >> 8) & 0xFF)));
		output.write((byte) (((value >> 16) & 0xFF)));
		output.write((byte) (((value >> 24) & 0xFF)));
	}

	private static void write64(ByteArrayOutputStream output, long value) {
		output.write((byte) ((value & 0xFF)));
		output.write((byte) (((value >> 8) & 0xFF)));
		output.write((byte) (((value >> 16) & 0xFF)));
		output.write((byte) (((value >> 24) & 0xFF)));
		output.write((byte) (((value >> 32) & 0xFF)));
		output.write((byte) (((value >> 40) & 0xFF)));
		output.write((byte) (((value >> 48) & 0xFF)));
		output.write((byte) (((value >> 56) & 0xFF)));
	}

	public static void serializeBSON(OutputStream output, Variant variant)
			throws IOException {
		output.write(serializeBSONDocument(variant));
	}

	private static byte[] serializeBSONDocument(Variant variant)
			throws IOException {
		ByteArrayOutputStream payload = new ByteArrayOutputStream();
		switch (variant.type()) {
		case MAP: // Case of BSON object
		{
			for (Entry<String, Variant> entry : variant.toMap().entrySet()) {
				payload.write(serializeBSONElt(entry.getValue(), entry.getKey()));
			}
			break;
		}
		case LIST: // Case of BSON array
		{
			int i = 0;
			for (Variant elt : variant.toList()) {
				payload.write(serializeBSONElt(elt, String.valueOf(i++)));
			}
			break;
		}
		default:
			throw new SerializerException("Fatal: Invalid document.");
		}
		byte[] payloadBytes = payload.toByteArray();
		ByteArrayOutputStream ret = new ByteArrayOutputStream();
		write32(ret, payloadBytes.length + 1);
		ret.write(payloadBytes);
		ret.write(BSON_TOKEN_END);
		return ret.toByteArray();
	}

	private static byte[] serializeBSONElt(Variant variant, String key)
			throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		switch (variant.type()) {
		case NULL:
			output.write(BSON_TOKEN_NULL);
			output.write(key.getBytes());
			output.write('\0');
			break;
		case BOOL: // Case of BSON boolean
			output.write(BSON_TOKEN_BOOL);
			output.write(key.getBytes());
			output.write('\0');
			output.write(variant.booleanValue() ? BSON_TOKEN_TRUE
					: BSON_TOKEN_FALSE);
			break;
		case UINT:
		case INT: {
			output.write(BSON_TOKEN_INT32);
			output.write(key.getBytes());
			output.write('\0');
			write32(output, variant.intValue());
			break;
		}
		case ULONG:
		case LONG: {
			output.write(BSON_TOKEN_INT64);
			output.write(key.getBytes());
			output.write('\0');
			write64(output, variant.intValue());
			break;
		}
		case DOUBLE: {
			output.write(BSON_TOKEN_DOUBLE);
			output.write(key.getBytes());
			output.write('\0');
			write64(output, variant.longValue());
			break;
		}
		case DATETIME: {
			output.write(BSON_TOKEN_DATETIME);
			output.write(key.getBytes());
			output.write('\0');
			write64(output, variant.longValue());
			break;
		}
		case MAP: // Case of BSON object
		{
			output.write(BSON_TOKEN_MAP);
			output.write(key.getBytes());
			output.write('\0');
			output.write(serializeBSONDocument(variant));
			break;
		}
		case LIST: // Case of BSON array
		{
			output.write(BSON_TOKEN_LIST);
			output.write(key.getBytes());
			output.write('\0');
			output.write(serializeBSONDocument(variant));
			break;
		}
		case STRING: {
			byte[] data = variant.toString().getBytes();
			output.write(BSON_TOKEN_STRING);
			output.write(key.getBytes());
			output.write('\0');
			write32(output, data.length + 1);
			output.write(data);
			output.write('\0');
			break;
		}
		case BYTEARRAY: // Case of BSON string
		{
			byte[] data = variant.toByteArray();
			output.write(BSON_TOKEN_DATA);
			output.write(key.getBytes());
			output.write('\0');
			write32(output, data.length);
			output.write(BSON_TOKEN_GENERIC);
			output.write(data);
			break;
		}
		default:
			throw new SerializerException("Fatal: Variant type not managed.");

		}
		return output.toByteArray();
	}

	private static int read32(InputStream input) throws IOException {
		return (input.read() & 0xFF) | ((input.read() & 0xFF) << 8)
				| ((input.read() & 0xFF) << 16) | ((input.read() & 0xFF) << 24);
	}

	private static long read64(InputStream input) throws IOException {
		return (long) (input.read() & 0xFF)
				| ((long) (input.read() & 0xFF) << 8)
				| ((long) (input.read() & 0xFF) << 16)
				| ((long) (input.read() & 0xFF) << 24)
				| ((long) (input.read() & 0xFF) << 32)
				| ((long) (input.read() & 0xFF) << 40)
				| ((long) (input.read() & 0xFF) << 48)
				| ((long) (input.read() & 0xFF) << 56);
	}

	private static Variant parseBCON(InputStream input, StringBuilder key)
			throws IOException {
		Variant ret;
		byte c = (byte) input.read();
		if ((c & 0x80) != 0) {
			int len;
			len = c & 0x3F;
			byte[] buf = new byte[len];
			for (int i = 0; i < len; i++) {
				buf[i] = (byte) input.read();
			}
			ret = ((c & 0x40) != 0) ? new VariantString(new String(buf))
					: new VariantByteArray(buf);
		} else if ((c & 0xF0) != 0) {
			int len;
			len = c & 0x0F;
			switch (c & 0x30) {
			case 0x10:
				len |= ((byte) input.read() & 0xFF) << 4;
				break;
			case 0x20:
				len |= (((byte) input.read() & 0xFF) << 4)
						| (((byte) input.read() & 0xFF) << 12);
				break;
			case 0x30:
				len |= (((byte) input.read() & 0xFF) << 4)
						| (((byte) input.read() & 0xFF) << 12)
						| (((byte) input.read() & 0xFF) << 20);
				break;
			default:
				throw new ParserException("Invalid "
						+ (((c & 0x40) != 0) ? "BCON_TOKEN_STRING"
								: "BCON_TOKEN_DATA") + " type "
						+ VariantNumber.toHexString((byte) (c & 0x30)));
			}
			byte[] buf = new byte[len];
			for (int i = 0; i < len; i++) {
				buf[i] = (byte) input.read();
			}
			ret = (c & 0x40) != 0 ? new VariantString(new String(buf))
					: new VariantByteArray(buf);
		} else {
			switch (c) {
			case BCON_TOKEN_END:
				return null;
			case BCON_TOKEN_NULL:
				ret = new VariantNull();
				break;
			case BCON_TOKEN_TRUE:
				ret = new VariantBool(true);
				break;
			case BCON_TOKEN_FALSE:
				ret = new VariantBool(false);
				break;
			case BCON_TOKEN_BYTE:
				ret = new VariantByte((byte) input.read());
				break;
			case BCON_TOKEN_INT32:
				ret = new VariantInt(read32(input));
				break;
			case BCON_TOKEN_INT64:
				ret = new VariantLong(read64(input));
				break;
			case BCON_TOKEN_UINT32:
				ret = new VariantUInt(read32(input));
				break;
			case BCON_TOKEN_UINT64:
				ret = new VariantULong(read64(input));
				break;
			case BCON_TOKEN_DOUBLE:
				ret = new VariantDouble((double) read64(input));
				break;
			case BCON_TOKEN_DATETIME:
				ret = new VariantDateTime(read64(input));
				break;
			case BCON_TOKEN_MAP: {
				HashMap<String, Variant> map = new HashMap<>();
				while (true) {
					StringBuilder subKey = new StringBuilder();
					Variant subValue = parseBCON(input, subKey);
					if (subValue == null)
						break;
					map.put(subKey.toString(), subValue);
				}
				ret = new VariantMap(map);
				break;
			}
			case BCON_TOKEN_LIST: {
				ArrayList<Variant> list = new ArrayList<>();
				while (true) {
					Variant subValue = parseBCON(input, null);
					if (subValue == null)
						break;
					list.add(subValue);
				}
				ret = new VariantList(list);
				break;
			}
			default:
				throw new ParserException("Invalid token " + c);
			}
		}
		if (key != null) {
			while ((c = (byte) input.read()) != '\0') {
				key.append((char)c);
			}
		}
		return ret;
	}

	private static Variant parseBSONDocument(InputStream input)
			throws IOException {
		read32(input);
		HashMap<String, Variant> map = new HashMap<>();
		while (true) {
			StringBuilder key = new StringBuilder();
			Variant value = parseBSONElt(input, key);
			if (value == null)
				break;
			map.put(key.toString(), value);
		}
		return new VariantMap(map);
	}

	private static Variant parseBSONElt(InputStream input, StringBuilder key)
			throws IOException {
		byte c, t = (byte) input.read();
		Variant res;
		if (t == BSON_TOKEN_END) {
			return null;
		}
		while ((c = (byte) input.read()) != '\0') {
			key.append((char)c);
		}
		switch (t) {
		case BSON_TOKEN_UNDEF:
		case BSON_TOKEN_NULL:
			res = new VariantNull();
			break;
		case BSON_TOKEN_BOOL:
			res = new VariantBool((byte) input.read() == BSON_TOKEN_TRUE);
			break;
		case BSON_TOKEN_INT32:
			res = new VariantInt(read32(input));
			break;
		case BSON_TOKEN_INT64:
			res = new VariantLong(read64(input));
			break;
		case BSON_TOKEN_DOUBLE:
			res = new VariantDouble((double) read64(input));
			break;
		case BSON_TOKEN_DATETIME:
			res = new VariantDateTime(read64(input));
			break;
		case BSON_TOKEN_STRING:
		case BSON_TOKEN_JSCODE: {
			int len = read32(input) - 1;
			byte[] buf = new byte[len];
			for (int i = 0; i < len; i++) {
				buf[i] = (byte) input.read();
			}
			input.read();
			res = new VariantString(new String(buf));
			break;
		}
		case BSON_TOKEN_OID: {
			int len = 12;
			byte[] buf = new byte[len];
			for (int i = 0; i < len; i++) {
				buf[i] = (byte) input.read();
			}
			res = new VariantByteArray(buf);
			break;
		}
		case BSON_TOKEN_DATA: {
			byte type = (byte) input.read();
			int len = read32(input);
			byte[] buf = new byte[len];
			for (int i = 0; i < len; i++) {
				buf[i] = (byte) input.read();
			}
			switch (type) {
			case BSON_TOKEN_OLDUUID:
			case BSON_TOKEN_UUID:
			case BSON_TOKEN_GENERIC:
			default:
				res = new VariantByteArray(buf);
			}
			break;
		}
		case BSON_TOKEN_MAP: {
			read32(input);
			HashMap<String, Variant> map = new HashMap<>();
			while (true) {
				StringBuilder subKey = new StringBuilder();
				Variant subValue = parseBSONElt(input, subKey);
				if (subValue == null)
					break;
				map.put(subKey.toString(), subValue);
			}
			res = new VariantMap(map);
			break;
		}
		case BSON_TOKEN_LIST: {
			read32(input);
			ArrayList<Variant> list = new ArrayList<>();
			while (true) {
				StringBuilder subKey = new StringBuilder();
				Variant subValue = parseBSONElt(input, subKey);
				if (subValue == null)
					break;
				list.add(Integer.parseInt(subKey.toString()), subValue);
			}
			res = new VariantList(list);
			break;
		}
		default:
			throw new ParserException("Unsupported token "
					+ VariantNumber.toHexString(t));
		}
		return res;
	}

	private static Variant parseJSONObject(JSONScanner scanner)
			throws IOException {
		HashMap<String, Variant> map = new HashMap<>();
		JSONTocken tocken;
		String key = null;
		Variant value = null;
		while (true) {
			tocken = scanner.yylex();
			switch (tocken.getId()) {
			case JSONTocken.TOBJEND:
				if (value != null) {
					map.put(key, value);
				}
				return new VariantMap(map);
			case JSONTocken.TELEMENTSEP:
				if (value == null) {
					throwJSONError(tocken, JSONTocken.TSTRING);
				}
				map.put(key, value);
				value = null;
				tocken = scanner.yylex();
				if (tocken.getId() != JSONTocken.TSTRING) {
					throwJSONError(tocken, JSONTocken.TSTRING);
				}
			case JSONTocken.TSTRING:
				key = tocken.getKey();
				break;
			default:
				if (map.isEmpty()) {
					throwJSONError(tocken, new int[] { JSONTocken.TOBJEND,
							JSONTocken.TSTRING });
				} else {
					throwJSONError(tocken, new int[] { JSONTocken.TELEMENTSEP,
							JSONTocken.TOBJEND, JSONTocken.TSTRING });
				}
			}
			tocken = scanner.yylex();
			if (tocken.getId() != JSONTocken.TMEMBERSEP) {
				throwJSONError(tocken, JSONTocken.TMEMBERSEP);
			}
			tocken = scanner.yylex();
			switch (tocken.getId()) {
			case JSONTocken.TOBJBEGIN:
				value = parseJSONObject(scanner);
				break;
			case JSONTocken.TARRBEGIN:
				value = parseJSONArray(scanner);
				break;
			case JSONTocken.TSTRING:
			case JSONTocken.TVARIANT:
				value = tocken.getVariant();
				break;
			default:
				throwJSONError(tocken, new int[] { JSONTocken.TVARIANT,
						JSONTocken.TSTRING, JSONTocken.TOBJBEGIN,
						JSONTocken.TARRBEGIN });
			}
		}
	}

	private static Variant parseJSONArray(JSONScanner scanner)
			throws IOException {
		ArrayList<Variant> list = new ArrayList<>();
		JSONTocken tocken;
		Variant value = null;
		while (true) {
			tocken = scanner.yylex();
			switch (tocken.getId()) {
			case JSONTocken.TARREND:
				if (value != null) {
					list.add(value);
				}
				return new VariantList(list);
			case JSONTocken.TELEMENTSEP:
				if (value != null) {
					list.add(value);
					value = null;
				} else {
					throwJSONError(tocken, JSONTocken.TVARIANT);
				}
				tocken = scanner.yylex();
				break;
			case JSONTocken.TOBJBEGIN:
			case JSONTocken.TARRBEGIN:
			case JSONTocken.TSTRING:
			case JSONTocken.TVARIANT:
				break;
			default:
				if (list.isEmpty()) {
					throwJSONError(tocken, new int[] { JSONTocken.TVARIANT,
							JSONTocken.TSTRING, JSONTocken.TOBJBEGIN,
							JSONTocken.TARREND, JSONTocken.TARRBEGIN });
				} else {
					throwJSONError(tocken, new int[] { JSONTocken.TVARIANT,
							JSONTocken.TSTRING, JSONTocken.TELEMENTSEP,
							JSONTocken.TOBJBEGIN, JSONTocken.TARREND,
							JSONTocken.TARRBEGIN });
				}
			}
			switch (tocken.getId()) {
			case JSONTocken.TOBJBEGIN:
				value = parseJSONObject(scanner);
				break;
			case JSONTocken.TARRBEGIN:
				value = parseJSONArray(scanner);
				break;
			case JSONTocken.TSTRING:
			case JSONTocken.TVARIANT:
				value = tocken.getVariant();
				break;
			default:
				throwJSONError(tocken, new int[] { JSONTocken.TVARIANT,
						JSONTocken.TSTRING, JSONTocken.TOBJBEGIN,
						JSONTocken.TARRBEGIN });
			}
		}
	}

	private static void throwJSONError(JSONTocken tocken, int expectedTockenId)
			throws IOException {
		throw new ParserException("Unexpected " + tocken + ". "
				+ JSONTocken.toString(expectedTockenId) + " expected instead.");
	}

	private static void throwJSONError(JSONTocken tocken, int[] tokens)
			throws IOException {
		StringBuffer buffer = new StringBuffer("Unexpected " + tocken + ". ");
		for (int i = 0; i < tokens.length - 1; i++) {
			buffer.append(JSONTocken.toString(tokens[i]));
			buffer.append(", ");
		}
		buffer.append("or ");
		buffer.append(JSONTocken.toString(tokens[tokens.length - 1]));
		buffer.append(" expected.");
		throw new ParserException(buffer.toString());
	}

}
