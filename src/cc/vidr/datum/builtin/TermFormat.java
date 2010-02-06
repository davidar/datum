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
import cc.vidr.datum.term.FloatTerm;
import cc.vidr.datum.term.IntegerTerm;
import cc.vidr.datum.term.Measurement;
import cc.vidr.datum.term.StringTerm;
import cc.vidr.datum.term.Term;

/**
 * Builtin for formatting a term to a human-readable string.
 * 
 * @author  David Roberts
 */
public class TermFormat extends Builtin {
    protected String predicate() {
        return "term_format/2";
    }
    
    protected Literal[] handle(Literal goal) throws Exception {
        Term term = goal.getArgument(0);
        if(!goal.getArgument(1).isVariable())
            return new Literal[0];
        return new Literal[] {
                new Literal(predicate(), term, new StringTerm(format(term))) };
    }
    
    private String format(Term term) {
        if(term instanceof DateTimeTerm)
            return format((DateTimeTerm) term);
        if(term instanceof Measurement)
            return format((Measurement) term);
        if(term instanceof IntegerTerm)
            return format((IntegerTerm) term);
        if(term instanceof FloatTerm)
            return format((FloatTerm) term);
        throw new IllegalArgumentException("Unrecognised type: " + term);
    }
    
    private String format(DateTimeTerm datetime) {
        return datetime.toString(DateTimeFormat.longDate());
    }
    
    private String format(Measurement measurement) {
        return measurement.getValue() + " " +
               measurement.getUnit().toString().replace('_', ' ') + "(s)";
    }
    
    private String format(IntegerTerm integer) {
        return integer.toString();
    }
    
    private String format(FloatTerm f) {
        return f.toString();
    }
}
