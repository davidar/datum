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

import java.util.HashMap;
import java.util.Map;

/**
 * A substitution is a mapping of variables to terms.
 * 
 * @author  David Roberts
 */
public class Substitution {
    /** The map backing this substitution */
    private Map<Variable, Term> map = new HashMap<Variable, Term>();
    
    /**
     * Apply the given mapping to this substitution.
     * 
     * Any terms currently in the substitution will have the mapping applied,
     * and the mapping will be added to the substitution if the given variable
     * is not currently mapped to anything.
     * 
     * @param newVariable  the variable being mapped
     * @param newTerm      the term being mapped to
     */
    public void put(Variable newVariable, Term newTerm) {
        /*
         * This method is equivalent to composing this substitution with
         * another substitution consisting only of the given mapping.
         * A composition obeys the following rule:
         *     x.subst(s.compose(o)) = x.subst(s).subst(o)
         * From <http://mathworld.wolfram.com/Unification.html>:
         * If s = {v1 <- x1, ..., vn <- xn} and
         *    o = {u1 <- y1, ..., un <- yn}
         * Then the composition is obtained from
         *     {v1 <- x1.subst(o), ..., vn <- xn.subst(o),
         *      u1 <- y1,          ..., un <- yn}
         * But removing all elements of the form
         *     vi <- xi.subst(o) where vi = xi.subst(o), and
         *     ui <- yi          where ui is one of v1,...,vn
         */
        for(Map.Entry<Variable, Term> entry : map.entrySet()) {
            Variable variable = entry.getKey();
            Term term = entry.getValue();
            if(term == newVariable) {
                if(variable == newTerm)
                    map.remove(variable);
                else
                    map.put(variable, newTerm);
            }
        }
        if(!map.containsKey(newVariable))
            map.put(newVariable, newTerm);
    }
    
    /**
     * Apply this substitution to the given variable.
     * 
     * @param variable  the variable to substitute
     * @return          the substituted term
     */
    public Term subst(Variable variable) {
        Term term = map.get(variable);
        if(term == null)
            term = variable;
        return term;
    }
}
