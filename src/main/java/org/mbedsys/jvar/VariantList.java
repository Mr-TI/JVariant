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
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 
 * @author <a href="mailto:emericv@mbedsys.org">Emeric Verschuur</a>
 * Copyright 2014 MbedSYS
 */
public class VariantList implements Variant, List<Variant> {

    public VariantList(List emptyList) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Write the value in JSON format
	 * 
	 * @param writer output stream writer
	 * @throws IOException 
	 */
	@Override
	public void writeJSONTo(OutputStreamWriter writer, int flags) throws IOException {
		boolean conpact = (flags & FORMAT_JSON_COMPACT) != 0;
		int indentStep = flags & JSON_INDENT_MASK;
		int indentOff = (flags >> 16) + indentStep;
		flags = (flags & 0xFFFF) | (indentOff << 16);
		Iterator<Variant> it = data.iterator();
		writer.write('[');
		if (it.hasNext()) {
			if (indentOff != 0) {
				writer.write('\n');
				Utils.appendSpaces(writer, indentOff);
			}
			it.next().writeJSONTo(writer, flags);
			while (it.hasNext()) {
				writer.write(',');
				if (indentOff != 0) {
					writer.write('\n');
					Utils.appendSpaces(writer, indentOff);
				} else if (!conpact) {
					writer.write(' ');
				}
				it.next().writeJSONTo(writer, flags);
			}
			if (indentOff != 0) {
				writer.write('\n');
				Utils.appendSpaces(writer, indentOff - indentStep);
			}
		}
		writer.write(']');
	}

	public VariantList add(boolean value) {
		data.add(new BooleanVariant(value));
		return this;
	}

	public VariantList add(int value) {
		data.add(new IntegerVariant(value));
		return this;
	}

	public VariantList add(long value) {
		data.add(new LongVariant(value));
		return this;
	}

	public VariantList add(double value) {
		data.add(new DoubleVariant(value));
		return this;
	}

	public VariantList add_(Object value) {
		if (value == null) {
			data.add(NullVariant.NULL);
		} else if (value instanceof Variant) {
			data.add((Variant) value);
		} else if (value instanceof String) {
			data.add(new StringVariant((String) value));
		} else {
			data.add(new GenericVariant(value));
		}
		return this;
	}
	
	@Override
	public String toString_() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(output);
		try {
			writeJSONTo(writer, 4);
			writer.flush();
		} catch (IOException e) {
			System.out.println("AbstractVariant.toJSONString()\n"
					+ PfException.getStackTraceToString(e));
		}
		return new String(output.toByteArray());
	}

    @Override
	public final String toJSONString_() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(output);
		try {
			writeJSONTo(writer, FORMAT_JSON_COMPACT);
			writer.flush();
		} catch (IOException e) {
			throw new Error(e);
		}
		return new String(output.toByteArray());
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<Variant> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(Variant e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends Variant> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends Variant> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Variant get(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Variant set(int index, Variant element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(int index, Variant element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Variant remove(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ListIterator<Variant> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<Variant> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Variant> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}
}
