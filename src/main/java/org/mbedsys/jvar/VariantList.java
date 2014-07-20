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
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 
 * @author <a href="mailto:emericv@mbedsys.org">Emeric Verschuur</a>
 * Copyright 2014 MbedSYS
 */
public class VariantList extends Variant implements List<Variant> {
	
	List<Variant> data;

    public VariantList(List<Variant> value) {
    	if (value == null)
			throw new IllegalArgumentException("value argument cannot be null");
		data = value;
	}

	public VariantList add(boolean value) {
		data.add(new VariantBool(value));
		return this;
	}

	public VariantList add(double value) {
		data.add(new VariantDouble(value));
		return this;
	}

	public VariantList add(int value) {
		data.add(new VariantInt(value));
		return this;
	}

	@Override
	public void add(int index, Variant element) {
		data.add(index, element);
	}

	public VariantList add(long value) {
		data.add(new VariantLong(value));
		return this;
	}

	public VariantList add(String value) {
		data.add(new VariantString(value));
		return this;
	}

	public VariantList add(Date value) {
		data.add(new VariantDateTime(value));
		return this;
	}
	
	@Override
	public boolean add(Variant e) {
		return data.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends Variant> c) {
		return data.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Variant> c) {
		return data.addAll(index, c);
	}

	@Override
	public void clear() {
		data.clear();
	}

	@Override
	public int compareTo(Object o) {
		throw new UnsupportedOperationException("VariantList cannot be compared to an other Variant");
	}

	@Override
	public boolean contains(Object o) {
		return data.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return data.containsAll(c);
	}

	@Override
	public Variant get(int index) {
		return data.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return data.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public boolean isNull() {
		return false;
	}

	@Override
	public Iterator<Variant> iterator() {
		return data.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return data.lastIndexOf(o);
	}

	@Override
	public ListIterator<Variant> listIterator() {
		return data.listIterator();
	}

	@Override
	public ListIterator<Variant> listIterator(int index) {
		return data.listIterator(index);
	}

	@Override
	public Variant remove(int index) {
		return data.remove(index);
	}

	@Override
	public boolean remove(Object o) {
		return data.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return data.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return data.retainAll(c);
	}

	@Override
	public Variant set(int index, Variant element) {
		return data.set(index, element);
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public List<Variant> subList(int fromIndex, int toIndex) {
		return data.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return data.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return data.toArray(a);
	}

	@Override
	public String toString() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(output);
		try {
			serializeJSON(writer, this, 4);
			writer.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return new String(output.toByteArray());
	}

	@Override
	public Type type() {
		return Type.LIST;
	}
}
