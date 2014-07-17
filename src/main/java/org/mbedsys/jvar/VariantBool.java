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


/**
 * 
 * @author <a href="mailto:emericv@mbedsys.org">Emeric Verschuur</a>
 * Copyright 2014 MbedSYS
 */
public class VariantBool extends VariantNumber {
	
	protected boolean data;

    /**
     * Returns the variant as a boolean.
     * <p/>
     * Applicable to a number or string variant
     *
     * @return true if the variant has {@link #type()} Boolean, Double, Integer,
     * Long, Byte, Short, Float and the value is non-zero, or if the
     * variant has type String and its lower-case content is not empty,
     * "0" or "false"; otherwise returns false or throws an instance of
     * a runtime exception in case of List or Map value
     * @throws UnsupportedOperationException - if the convention is not possible.
     */
    @Override
    public boolean booleanValue() {
        return super.booleanValue();
    }

    /**
     * Returns the variant as a byte.
     * <p/>
     * Applicable to a number or string variant
     *
     * @return the variant as a byte if the variant has {@link #type()} Double,
     * Float, Boolean, Integer, Long and String if the last one contains
     * a textual representation (decimal, octal or binary format) of a
     * byte.
     * @throws NumberFormatException         - if the string does not contain a parsable byte.
     * @throws UnsupportedOperationException - if the convention is not possible.
     */
    @Override
    public byte byteValue() {
        return super.byteValue();
    }

    /**
     * Returns the variant as a short.
     * <p/>
     * Applicable to a number or string variant
     *
     * @return the variant as a short if the variant has {@link #type()} Double,
     * Float, Boolean, Integer, Long and String if the last one contains
     * a textual representation (decimal, octal or binary format) of a
     * short. Otherwise throws an instance of VariantFormatException
     * runtime exception.
     * @throws NumberFormatException         - if the string does not contain a parsable byte.
     * @throws UnsupportedOperationException - if the convention is not possible.
     */
    @Override
    public short shortValue() {
        return super.shortValue();
    }

    /**
     * Returns the variant as an integer.
     * <p/>
     * Applicable to a number or string variant
     *
     * @return the variant as an integer if the variant has {@link #type()}
     * Double, Float, Boolean, Integer, Long and String if the last one
     * contains a textual representation (decimal, octal or binary
     * format) of an integer.
     * @throws NumberFormatException         - if the string does not contain a parsable integer.
     * @throws UnsupportedOperationException - if the convention is not possible.
     */
    @Override
    public int intValue() {
        return super.intValue();
    }

    /**
     * Returns the variant as a long.
     * <p/>
     * Applicable to a number or string variant
     *
     * @return the variant as a long if the variant has {@link #type()} Double,
     * Float, Boolean, Integer, Long and String if the last one contains
     * a textual representation (decimal, octal or binary format) of a
     * long.
     * @throws NumberFormatException         - if the string does not contain a parsable long.
     * @throws UnsupportedOperationException - if the convention is not possible.
     */
    @Override
    public long longValue() {
        return super.longValue();
    }

    /**
     * Returns the variant as a float.
     * <p/>
     * Applicable to a number or string variant
     *
     * @return the variant as a float if the variant has {@link #type()} Double,
     * Float, Boolean, Integer, Long and String if the last one contains
     * a textual representation (decimal, octal or binary format) of a
     * float.
     * @throws NumberFormatException         - if the string does not contain a parsable float.
     * @throws UnsupportedOperationException - if the convention is not possible.
     */
    @Override
    public float floatValue() {
        return super.floatValue();
    }

    /**
     * Returns the variant as a double.
     * <p/>
     * Applicable to a number or string variant
     *
     * @return the variant as a double if the variant has {@link #type()}
     * Double, Float, Boolean, Integer, Long and String if the last one
     * contains a textual representation (decimal, octal or binary
     * format) of a double.
     * @throws NumberFormatException         - if the string does not contain a parsable double.
     * @throws UnsupportedOperationException - if the convention is not possible.
     */
    @Override
    public double doubleValue() {
        return super.doubleValue();
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables such as those provided by
     * {@link java.util.HashMap}.
     * <p/>
     * The general contract of {@code hashCode} is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during
     * an execution of a Java application, the {@code hashCode} method
     * must consistently return the same integer, provided no information
     * used in {@code equals} comparisons on the object is modified.
     * This integer need not remain consistent from one execution of an
     * application to another execution of the same application.
     * <li>If two objects are equal according to the {@code equals(Object)}
     * method, then calling the {@code hashCode} method on each of
     * the two objects must produce the same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal
     * according to the {@link Object#equals(Object)}
     * method, then calling the {@code hashCode} method on each of the
     * two objects must produce distinct integer results.  However, the
     * programmer should be aware that producing distinct integer results
     * for unequal objects may improve the performance of hash tables.
     * </ul>
     * <p/>
     * As much as is reasonably practical, the hashCode method defined by
     * class {@code Object} does return distinct integers for distinct
     * objects. (This is typically implemented by converting the internal
     * address of the object into an integer, but this implementation
     * technique is not required by the
     * Java<font size="-2"><sup>TM</sup></font> programming language.)
     *
     * @return a hash code value for this object.
     * @see Object#equals(Object)
     * @see System#identityHashCode
     */
    @Override
    public int hashCode() {
        final int prime = 31;
		int result = 1;
		result = prime * result + (data ? 1231 : 1237);
		return result;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p/>
     * The {@code equals} method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     * {@code x}, {@code x.equals(x)} should return
     * {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     * {@code x} and {@code y}, {@code x.equals(y)}
     * should return {@code true} if and only if
     * {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     * {@code x}, {@code y}, and {@code z}, if
     * {@code x.equals(y)} returns {@code true} and
     * {@code y.equals(z)} returns {@code true}, then
     * {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     * {@code x} and {@code y}, multiple invocations of
     * {@code x.equals(y)} consistently return {@code true}
     * or consistently return {@code false}, provided no
     * information used in {@code equals} comparisons on the
     * objects is modified.
     * <li>For any non-null reference value {@code x},
     * {@code x.equals(null)} should return {@code false}.
     * </ul>
     * <p/>
     * The {@code equals} method for class {@code Object} implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values {@code x} and
     * {@code y}, this method returns {@code true} if and only
     * if {@code x} and {@code y} refer to the same object
     * ({@code x == y} has the value {@code true}).
     * <p/>
     * Note that it is generally necessary to override the {@code hashCode}
     * method whenever this method is overridden, so as to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @see #hashCode()
     * @see java.util.HashMap
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof VariantBool && ((VariantBool)obj).data) ||
                (obj instanceof Boolean && ((Boolean)obj).booleanValue());
    }

    /**
     * Returns the variant as a String.
     * <p/>
     * Applicable to all the variant types
     *
     * @return the variant as a String
     */
    @Override
    public String toString() {
        return data ? "true": "false";
    }

    /**
     * Get variant as a VariantNumber
     * <p/>
     * Applicable to types: (U)Int, (U)Long, Double, Byte and Null
     *
     * @return an Object
     */
    @Override
    public VariantNumber toNumber() {
        return this;
    }

    /**
     * Get if the variant is empty
     * <p/>
     * Applicable to all the variant types
     *
     * @return true if the variant is empty, otherwise false
     */
    @Override
    public boolean isEmpty() {
        return false;
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
     * Return the variant type
     * <p/>
     * Applicable to all the variant types
     *
     * @return the type as Type
     */
    @Override
    public Type type() {
        return Type.BOOL;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p/>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p/>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p/>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p/>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p/>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Object o) {
        return ((o instanceof VariantBool && ((VariantBool)o).data) ||
                (o instanceof Boolean && ((Boolean)o).booleanValue()))? 0: -1;
    }

    /**
     * Get the state of a single value bit
     *
     * @param index index
     * @return true: 1, false: 0
     */
    @Override
    public boolean isBitActive(int index) {
        return index == 0 && data;
    }

    /**
     * Returns the variant as a Hexadecimal String format for number variants.
     * <p/>
     * Applicable to all the variant types
     *
     * @return the variant as a String
     */
    @Override
    public String toHexString() {
        return data? "1": "0";
    }
}
