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

import java.util.ArrayList;
import java.util.List;

/**
 * Allows incremental construction of Literals.
 * 
 * @author  David Roberts
 */
public class LiteralBuilder {
    /** The predicate name (not including arity) */
    private String predicateName;
    /** The current list of arguments */
    private List<Term> arguments = new ArrayList<Term>();
    
    /**
     * Set the predicate name.
     * 
     * @param predicateName  the predicate name
     */
    public void setPredicateName(String predicateName) {
        this.predicateName = predicateName;
    }
    
    /**
     * Append the given argument to the current list of arguments.
     * 
     * @param argument  the argument
     */
    public void addArgument(Term argument) {
        arguments.add(argument);
    }
    
    /**
     * Build the Literal.
     * 
     * @return  the literal
     */
    public Literal toLiteral() {
        String predicate = predicateName + "/" + arguments.size();
        return new Literal(predicate, arguments.toArray(new Term[0]));
    }
}
