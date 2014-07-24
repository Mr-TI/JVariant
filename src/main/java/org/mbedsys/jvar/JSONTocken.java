package org.mbedsys.jvar;

/** CUP generated class containing symbol constants. */
class JSONTocken {
	public static final int TEOF = 0;
	public static final int TARRBEGIN = 5;
	public static final int TOBJBEGIN = 3;
	public static final int TOBJEND = 4;
	public static final int TVARIANT = 13;
	public static final int TMEMBERSEP = 7;
	public static final int TELEMENTSEP = 8;
	public static final int TSYNERR = 11;
	public static final int TARREND = 6;
	public static final int TSTRING = 12;

	private int id;
	private Object data = null;

	public JSONTocken(int id) {
		this.id = id;
	}

	public JSONTocken(int id, Variant value) {
		this(id);
		this.data = value;
	}

	public JSONTocken(int id, String value) {
		this(id);
		this.data = value;
	}

	public static String toString(int id) {
		return toString(id, null);
	}

	public int getId() {
		return id;
	}

	public Variant getVariant() {
		return data instanceof Variant ? (Variant) data : new VariantString(
				(String) data);
	}

	public String getKey() {
		return (String) data;
	}

	public static String toString(int id, Object data) {
		switch (id) {
		case TEOF:
			return "End of file";
		case TARRBEGIN:
			return "Array begin '['";
		case TOBJBEGIN:
			return "Object begin '{'";
		case TOBJEND:
			return "Object end '}'";
		case TVARIANT:
			return data == null ? "Variant" : "Variant (" + data + ")";
		case TMEMBERSEP:
			return "Object member separator";
		case TELEMENTSEP:
			return "Array element separator";
		case TSYNERR:
			return "Syntax error";
		case TARREND:
			return "Array end ']'";
		case TSTRING:
			return data == null ? "String" : "String (" + data + ")";
		default:
			return "invalid tocken";
		}
	}

	@Override
	public String toString() {
		return toString(id, data);
	}
}
