package org.mbedsys.jvar;

/**
 *   Autor: Emeric Verschuur <emericv@mbedsys.org>
 */

import java.io.EOFException;
import java.io.IOException;
%%

%class JSONScanner
%type JSONTocken
%unicode
%line
%column

%{
	private StringBuffer string = new StringBuffer();
	
	public boolean ready() throws IOException {
		while (true) {
			if ((zzCurrentPos + 1) < zzEndRead) {
				while ((zzCurrentPos + 1) < zzEndRead) {
					if (Character.isWhitespace(zzBuffer[zzCurrentPos])) {
						zzCurrentPos++;
					} else {
						return true;
					}
				}
			} else if (this.zzReader != null && this.zzReader.ready()) {
				if (zzRefill()) {
					throw new EOFException();
				}
			} else {
				return false;
			}
		}
	}
  
	public int getLine() {
		return yyline;
	}
	
	public int getColumn() {
		return yycolumn;
	}
  
	public int getNbChar() {
		return yychar;
	}
	
	public boolean isAtBOL() {
		return zzAtBOL;
	}
	
	public boolean isEOFDone() {
		return zzEOFDone;
	}
%}

space                   = [\t\n\r ]

kw_null                 = null
kw_undefined            = undefined
kw_true                 = true
kw_false                = false
kw_nan                  = nan
kw_infinity             = [\-\+]?infinity

number                  = \-?[[:digit:]]+
number_ext              = \-?[[:digit:]]+(\.[[:digit:]]+)?(e[\-\+]?[[:digit:]]+)?

coma                    = ,
semicolon               = :
dblcote                 = \"
square_bracket_open     = \[
square_bracket_close    = \]
curly_bracket_open      = \{
curly_bracket_close     = \}

escape_unicode          = \\u[0-9a-f]{4}
escape_backslash        = \\
escape_dblcote          = \\\"
escape_backspace        = \\b
escape_formfeed         = \\f
escape_newline          = \\n
escape_car_ret          = \\r
escape_horiz_tab        = \\t

%state STRING

%%

<<EOF>>                  { yybegin(YYINITIAL); return new JSONTocken(JSONTocken.TEOF);}

<YYINITIAL>{
  {kw_null}                { return new JSONTocken(JSONTocken.TVARIANT, Variant.NULL); }
  {kw_undefined}           { return new JSONTocken(JSONTocken.TVARIANT, Variant.NULL); }
  {kw_true}                { return new JSONTocken(JSONTocken.TVARIANT, Variant.TRUE); }
  {kw_false}               { return new JSONTocken(JSONTocken.TVARIANT, Variant.FALSE); }
  {kw_nan}                 { return new JSONTocken(JSONTocken.TVARIANT, Variant.NULL); }
  {kw_infinity}            { return new JSONTocken(JSONTocken.TVARIANT, Variant.NULL); }
  
  {number}                 { return new JSONTocken(JSONTocken.TVARIANT, VariantNumber.optimize(Long.parseLong(yytext())); }
  {number_ext}             { return new JSONTocken(JSONTocken.TVARIANT, new VariantDouble(Double.parseDouble(yytext()))); }
  
  {square_bracket_open}    { return new JSONTocken(JSONTocken.TARRBEGIN); }
  {square_bracket_close}   { return new JSONTocken(JSONTocken.TARREND); }
  {curly_bracket_open}     { return new JSONTocken(JSONTocken.TOBJBEGIN); }
  {curly_bracket_close}    { return new JSONTocken(JSONTocken.TOBJEND); }
  {coma}                   { return new JSONTocken(JSONTocken.TELEMENTSEP); }
  {semicolon}              { return new JSONTocken(JSONTocken.TMEMBERSEP); }
  {dblcote}                { string.setLength(0); yybegin(STRING); }
  {space}                  {  }
  .                        { return new JSONTocken(JSONTocken.TSYNERR); }
}

<STRING>{
  {dblcote}                { yybegin(YYINITIAL); return new JSONTocken(JSONTocken.TSTRING, string.toString()); }
  {escape_backspace}       { string.append('\b'); }
  {escape_formfeed}        { string.append('\f'); }
  {escape_horiz_tab}       { string.append('\t'); }
  {escape_newline}         { string.append('\n'); }
  {escape_car_ret}         { string.append('\r'); }
  {escape_dblcote}         { string.append('\"'); }
  {escape_backslash}       { string.append('\\'); }
  {escape_unicode}         { string.append((char)Short.parseShort(yytext().substring(2, 6), 16)); }
  .|{space}                { string.append(yytext()); }
}
