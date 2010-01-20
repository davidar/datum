/*
 * Copyright (C) 2010  David Roberts <d@vidr.cc>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.vidr.datum;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * A string of characters.
 * 
 * @author  David Roberts
 */
public final class StringTerm
extends AbstractConstant<String> implements Constant {
    private static final long serialVersionUID = -2214188722526140589L;
    
    /**
     * Create a new StringTerm for the given string.
     * 
     * @param string  the string
     */
    public StringTerm(String string) {
        super(string);
    }
    
    /**
     * Create a new StringTerm for the given string literal. Surrounding quotes
     * will be removed and escaped characters unescaped.
     * 
     * @param string  the string literal
     * @return        the StringTerm
     */
    public static StringTerm parse(String string) {
        if(string.charAt(0) == '"') // remove quotes
            string = string.substring(1, string.length()-1);
        return new StringTerm(StringEscapeUtils.unescapeJava(string));
    }
    
    public String toString() {
        return '"' + StringEscapeUtils.escapeJava(data) + '"';
    }
    
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o instanceof StringTerm) {
            StringTerm s = (StringTerm) o;
            return data.equals(s.data);
        }
        return false;
    }
    
    public int hashCode() {
        return data.hashCode();
    }
}
