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
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author <a href="mailto:emericv@mbedsys.org">Emeric Verschuur</a>
 * Copyright 2014 MbedSYS
 */
public class VariantMap extends Variant implements Map<String, Variant> {
	
	private Map<String, Variant> data;
	
	public VariantMap(Dictionary<String, String> dic) {
		data = new HashMap<>();
		Enumeration<String> elts = dic.keys();
		while (elts.hasMoreElements()) {
			 String key = elts.nextElement();
			 data.put(key, new VariantString(dic.get(key)));
		}
	}
	
	public VariantMap(Map<String, Variant> value) {
		if (value == null)
			throw new IllegalArgumentException("value argument cannot be null");
		this.data = new HashMap<>(value);
	}

	public VariantMap() {
    	this.data = new HashMap<>();
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
			data.put(key, Variant.NULL);
		} else if (value instanceof Variant) {
			data.put(key, (Variant) value);
		} else if (value instanceof String) {
			data.put(key, new VariantString((String) value));
		} else {
			throw new IllegalArgumentException("Unsupported value type");
		}
		return this;
	}

    public static Variant resolvePath(VariantMap data, String path) {
        return resolvePath(data, path, '.');
    }

    public static VariantMap resolvePath(VariantMap data, String path, char sep) {
        for (String key: VariantString.split(path, sep)) {
            if (!data.containsKey(key)) {
                return null;
            }
            data = data.get(key).toMap();
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
        return updatePath(data, VariantString.split(path, '.'), value);
    }

	@Override
	public int compareTo(Object o) {
		throw new UnsupportedOperationException("VariantMap cannot be compared to an other Variant");
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public boolean containsKey(Object key) {
		return data.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return data.containsValue(value);
	}

	@Override
	public Variant get(Object key) {
		return data.get(key);
	}

	@Override
	public Variant put(String key, Variant value) {
		return data.put(key, value);
	}

	@Override
	public Variant remove(Object key) {
		return data.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Variant> m) {
		data.putAll(m);
	}

	@Override
	public void clear() {
		data.clear();
	}

	@Override
	public Set<String> keySet() {
		return data.keySet();
	}

	@Override
	public Collection<Variant> values() {
		return data.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, Variant>> entrySet() {
		return data.entrySet();
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
	public String toString() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(output);
		try {
			serializeJSONElt(writer, this, 4);
			writer.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return new String(output.toByteArray());
	}
	
	@Override
	public VariantMap toMap() {
		return this;
	}
	
	@Override
	public VariantList toList() {
		return new VariantList(data.values());
	}

	@Override
	public Variant clone(int flags) {
		VariantMap list;
		if ((flags & DEEP_COPY) != 0) {
			list = new VariantMap();
			for (Entry<String, Variant> elt: data.entrySet()) {
				list.put(elt.getKey(), elt.getValue().clone(flags));
			}
		} else {
			list = new VariantMap(data);
		}
		if ((flags & UNMODIFIABLE) != 0) {
			list.data = Collections.unmodifiableMap(list.data);
		}
		return list;
	}
}
