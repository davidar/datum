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

import cc.vidr.datum.Literal;
import cc.vidr.datum.term.Atom;
import cc.vidr.datum.term.DateTimeTerm;
import cc.vidr.datum.term.StringTerm;
import cc.vidr.datum.term.Term;

/**
 * Builtin for determining if the type of a term.
 * 
 * @author  David Roberts
 */
public class TermType extends Builtin {
    protected String predicate() {
        return "term_type/2";
    }
    
    protected Literal[] handle(Literal goal) throws Exception {
        Term term = goal.getArgument(0);
        String type;
        if(term instanceof Atom)
            type = "atom";
        else if(term instanceof StringTerm)
            type = "string";
        else if(term instanceof DateTimeTerm)
            type = "datetime";
        else
            throw new IllegalArgumentException("Unrecognised type: " + term);
        if(goal.getArgument(1).isVariable()
        || goal.getArgument(1).toString().equals(type))
            return new Literal[] {
                new Literal(predicate(), term, new Atom(type)) };
        return new Literal[0];
    }
}
