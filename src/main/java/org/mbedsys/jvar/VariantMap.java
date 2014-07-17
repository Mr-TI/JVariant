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
