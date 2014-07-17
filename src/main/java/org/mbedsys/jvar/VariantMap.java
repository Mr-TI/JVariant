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
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:emericv@mbedsys.org">Emeric Verschuur</a>
 * Copyright 2014 MbedSYS
 */
public class VariantMap implements Variant, Map<String, Variant> {
	
	private HashMap<String, Variant> data;
	
	public VariantMap(Dictionary<String, String> dic) {
		data = new HashMap<>();
		Enumeration<String> elts = dic.keys();
		while (elts.hasMoreElements()) {
			 String key = elts.nextElement();
			 data.put(key, new VariantString(dic.get(key)));
		}
	}
	
	public VariantMap() {
		data = new HashMap<>();
	}
	
	public VariantMap(Map<String, Variant> properties) {
		data = new HashMap<>(properties);
	}

    /**
     * Returns a {@link java.util.Collection} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa.  If the map is
     * modified while an iteration over the collection is in progress
     * (except through the iterator's own <tt>remove</tt> operation),
     * the results of the iteration are undefined.  The collection
     * supports element removal, which removes the corresponding
     * mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Collection.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not
     * support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a collection view of the values contained in this map
     */
    @Override
    public Collection<Variant> values() {
        return null;
    }

    /**
     * Get if the variant is null/undefined
     * <p/>
     * Applicable to all the variant types
     *
     * @return true if the variant is null/undefined, otherwise false
     */
    @Override
    public boolean isNull() {
        return false;
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified
     * key.  More formally, returns <tt>true</tt> if and only if
     * this map contains a mapping for a key <tt>k</tt> such that
     * <tt>(key==null ? k==null : key.equals(k))</tt>.  (There can be
     * at most one such mapping.)
     *
     * @param key key whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     * key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the
     * specified value.  More formally, returns <tt>true</tt> if and only if
     * this map contains at least one mapping to a value <tt>v</tt> such that
     * <tt>(value==null ? v==null : value.equals(v))</tt>.  This operation
     * will probably require time linear in the map size for most
     * implementations of the <tt>Map</tt> interface.
     *
     * @param value value whose presence in this map is to be tested
     * @return <tt>true</tt> if this map maps one or more keys to the
     * specified value
     * @throws ClassCastException   if the value is of an inappropriate type for
     *                              this map
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified value is null and this
     *                              map does not permit null values
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     * <p/>
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
     * key.equals(k))}, then this method returns {@code v}; otherwise
     * it returns {@code null}.  (There can be at most one such mapping.)
     * <p/>
     * <p>If this map permits null values, then a return value of
     * {@code null} does not <i>necessarily</i> indicate that the map
     * contains no mapping for the key; it's also possible that the map
     * explicitly maps the key to {@code null}.  The {@link #containsKey
     * containsKey} operation may be used to distinguish these two cases.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     * {@code null} if this map contains no mapping for the key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public Variant get(Object key) {
        return null;
    }

    /**
     * Associates the specified value with the specified key in this map
     * (optional operation).  If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.  (A map
     * <tt>m</tt> is said to contain a mapping for a key <tt>k</tt> if and only
     * if {@link #containsKey(Object) m.containsKey(k)} would return
     * <tt>true</tt>.)
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * (A <tt>null</tt> return can also indicate that the map
     * previously associated <tt>null</tt> with <tt>key</tt>,
     * if the implementation supports <tt>null</tt> values.)
     * @throws UnsupportedOperationException if the <tt>put</tt> operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the class of the specified key or value
     *                                       prevents it from being stored in this map
     * @throws NullPointerException          if the specified key or value is null
     *                                       and this map does not permit null keys or values
     * @throws IllegalArgumentException      if some property of the specified key
     *                                       or value prevents it from being stored in this map
     */
    @Override
    public Variant put(String key, Variant value) {
        return null;
    }

    /**
     * Removes the mapping for a key from this map if it is present
     * (optional operation).   More formally, if this map contains a mapping
     * from key <tt>k</tt> to value <tt>v</tt> such that
     * <code>(key==null ?  k==null : key.equals(k))</code>, that mapping
     * is removed.  (The map can contain at most one such mapping.)
     * <p/>
     * <p>Returns the value to which this map previously associated the key,
     * or <tt>null</tt> if the map contained no mapping for the key.
     * <p/>
     * <p>If this map permits null values, then a return value of
     * <tt>null</tt> does not <i>necessarily</i> indicate that the map
     * contained no mapping for the key; it's also possible that the map
     * explicitly mapped the key to <tt>null</tt>.
     * <p/>
     * <p>The map will not contain a mapping for the specified key once the
     * call returns.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * @throws UnsupportedOperationException if the <tt>remove</tt> operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the key is of an inappropriate type for
     *                                       this map
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if the specified key is null and this
     *                                       map does not permit null keys
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public Variant remove(Object key) {
        return null;
    }

    /**
     * Copies all of the mappings from the specified map to this map
     * (optional operation).  The effect of this call is equivalent to that
     * of calling {@link #put(Object, Object) put(k, v)} on this map once
     * for each mapping from key <tt>k</tt> to value <tt>v</tt> in the
     * specified map.  The behavior of this operation is undefined if the
     * specified map is modified while the operation is in progress.
     *
     * @param m mappings to be stored in this map
     * @throws UnsupportedOperationException if the <tt>putAll</tt> operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the class of a key or value in the
     *                                       specified map prevents it from being stored in this map
     * @throws NullPointerException          if the specified map is null, or if
     *                                       this map does not permit null keys or values, and the
     *                                       specified map contains null keys or values
     * @throws IllegalArgumentException      if some property of a key or value in
     *                                       the specified map prevents it from being stored in this map
     */
    @Override
    public void putAll(Map<? extends String, ? extends Variant> m) {

    }

    /**
     * Convert the value in JSON format
     * <p/>
     * Applicable to all the variant types
     *
     * @return the value as a JSON String
     */
    public String toJSONString() {
        return null;
    }

    /**
     * Convert the value in BSON format
     * <p/>
     * Applicable to all the variant types
     *
     * @param writer TODO
     * @return the value as a JSON String
     * @throws IOException
     */
    public byte[] toBSON() {
        return new byte[0];
    }

    /**
	 * Write the value in JSON format
	 * 
	 * @param writer output stream writer
	 * @throws IOException 
	 */
	@Override
	public void writeJSONTo(OutputStreamWriter writer, int flags) throws IOException {
		Iterator<String> keys = data.keySet().iterator();
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
			data.get(key).writeJSONTo(writer, flags);
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
				data.get(key).writeJSONTo(writer, flags);
			}
			if (indentOff != 0) {
				writer.write('\n');
				appendSpaces(writer, indentOff - indentStep);
			}
		}
		writer.write('}');
	}



    @Override
	public Type type() {
		return Type.MAP;
	}

	public VariantMap put(String key, boolean value) {
		data.put(key, new VariantBool(value));
		return this;
	}

	public VariantMap put(String key, int value) {
		data.put(key, new VariantInt(value));
		return this;
	}

	public VariantMap put(String key, long value) {
		data.put(key, new VariantLong(value));
		return this;
	}

	public VariantMap put(String key, double value) {
		data.put(key, new VariantDouble(value));
		return this;
	}

	public VariantMap put(String key, Object value) {
		if (value == null) {
			data.put(key, NullVariant.NULL);
		} else if (value instanceof Variant) {
			data.put(key, (Variant) value);
		} else if (value instanceof String) {
			data.put(key, new VariantString((String) value));
		} else {
			data.put(key, new GenericVariant(value));
		}
		return this;
	}
	
	/**
	 * Remove a value from a list using it's key
	 * 
	 * Applicable to a map variant
	 * 
	 * @param key
	 *            the key
	 * @return the removed value as a variant or null if the value doesn't exist
	 */
	public Variant remove(String key) {
		return data.remove(key);
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

    public static Variant resolvePath(Variant data, String path) {
        return resolvePath(data, path, '.');
    }

    public static Variant resolvePath(Variant data, String path, char sep) {
        for (String key: Utils.split(path, sep)) {
            if (!data.containsKey(key)) {
                return null;
            }
            data = data.get(key);
        }
        return data;
    }

    private static void updatePath(VariantMap node, String[] pathParts, Variant value, int i) {
        String k = pathParts[i++];
        if (i == pathParts.length) {
            if (value == null) {
                node.remove(k);
            } else {
                node.put(k, value);
            }
            return;
        }
        Variant subNode;
        if (!node.containsKey(k) || (subNode = node.get(k)).type() != Variant.Type.MAP) {
            node.put(k, subNode = new VariantMap());
        }
        updatePath((VariantMap) subNode, pathParts, value, i);
    }

    public static Variant updatePath(Variant data, String[] pathParts, Variant value) {
        if (data == null || data.type() != Variant.Type.MAP) {
            data = new VariantMap();
        }
        updatePath((VariantMap) data, pathParts, value, 0);
        return data;
    }

    public static Variant updatePath(Variant data, String path, Variant value) {
        return updatePath(data, Utils.split(path, '.'), value);
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

}
