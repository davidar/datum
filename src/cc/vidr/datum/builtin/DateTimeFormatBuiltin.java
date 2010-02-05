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

package cc.vidr.datum.builtin;

import org.joda.time.format.DateTimeFormat;

import cc.vidr.datum.Literal;
import cc.vidr.datum.term.DateTimeTerm;
import cc.vidr.datum.term.StringTerm;

/**
 * Builtin for formatting a datetime term to a string.
 * 
 * @author  David Roberts
 */
public class DateTimeFormatBuiltin extends Builtin {
    protected String predicate() {
        return "datetime_format/2";
    }
    
    protected Literal[] handle(Literal goal) throws Exception {
        DateTimeTerm datetime = (DateTimeTerm) goal.getArgument(0);
        String s = datetime.toString(DateTimeFormat.longDate());
        if(goal.getArgument(1).isVariable())
            return new Literal[] {
                new Literal(predicate(), datetime, new StringTerm(s)) };
        return new Literal[0];
    }
}
