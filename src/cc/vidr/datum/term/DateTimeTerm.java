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

package cc.vidr.datum.term;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * An instant in time.
 * 
 * @author  David Roberts
 */
public class DateTimeTerm
extends AbstractConstant<DateTime> implements Constant {
    private static final long serialVersionUID = 2952370499870617173L;
    
    /**
     * Create a new DateTimeTerm from the given DateTime object.
     * 
     * @param datetime  the DateTime object
     */
    public DateTimeTerm(DateTime datetime) {
        super(datetime);
    }
    
    /**
     * Create a new term from the given string using the given parser.
     * 
     * @param text  the string
     * @param fmt   the parser
     */
    public DateTimeTerm(String text, DateTimeFormatter fmt) {
        this(fmt.parseDateTime(text));
    }
    
    /**
     * Creat a new term from the given ISO8601-formatted string.
     * 
     * @param text  the string
     */
    public DateTimeTerm(String text) {
        this(text, ISODateTimeFormat.dateTimeParser());
    }
    
    /**
     * Format this datetime using the given formatter.
     * 
     * @param fmt  the formatter
     * @return     the string representation
     */
    public String toString(DateTimeFormatter fmt) {
        return fmt.print(data);
    }
    
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o instanceof DateTimeTerm) {
            DateTimeTerm d = (DateTimeTerm) o;
            return data.equals(d.data);
        }
        return false;
    }
    
    public String toString() {
        return toString(ISODateTimeFormat.dateTime());
    }
}
