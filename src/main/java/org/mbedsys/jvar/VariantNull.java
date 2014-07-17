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

import java.util.Collections;

/**
 * 
 * @author <a href="mailto:emericv@mbedsys.org">Emeric Verschuur</a> Copyright
 *         2014 MbedSYS
 */
public class VariantNull extends VariantNumber {
	private static final VariantMap EMPTY_MAP = new VariantMap(
			Collections.<String, Variant> emptyMap());
	private static final VariantList EMPTY_LIST = new VariantList(
			Collections.<Variant> emptyList());

	/**
	 * Default constructor.
	 */
	public VariantNull() {
	}

	@Override
	public boolean booleanValue() {
		return false;
	}

	@Override
	public byte byteValue() {
		return 0;
	}

	@Override
	public short shortValue() {
		return 0;
	}

	@Override
	public int intValue() {
		return 0;
	}

	@Override
	public long longValue() {
		return 0;
	}

	@Override
	public float floatValue() {
		return 0;
	}

	@Override
	public double doubleValue() {
		return 0;
	}

	@Override
	public String toString() {
		return "null";
	}

	@Override
	public int compareTo(Object other) {
		return other instanceof VariantNull ? 0 : -1;
	}

	@Override
	public Type type() {
		return Type.NULL;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public boolean isNull() {
		return true;
	}

	@Override
	public byte[] toByteArray() {
		return new byte[0];
	}

	@Override
	public VariantMap toMap() {
		return EMPTY_MAP;
	}

	@Override
	public VariantList toList() {
		return EMPTY_LIST;
	}

	@Override
	public boolean isBitActive(int index) {
		return false;
	}

	@Override
	public String toHexString() {
		return "0";
	}
}
