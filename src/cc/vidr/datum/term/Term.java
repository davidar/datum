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

import java.io.Serializable;

import cc.vidr.datum.Substitution;
import cc.vidr.datum.UnificationException;

/**
 * A data structure used as the argument of a Literal.
 * 
 * @author  David Roberts
 */
public interface Term extends Serializable {
    /**
     * Unify two terms.
     * 
     * @param term          the other term
     * @param substitution  the substitution built up so far
     * @throws              UnificationException if the terms cannot be unified
     */
    void unify(Term term, Substitution substitution)
    throws UnificationException;
    
    /**
     * Apply the given substitution to this term.
     * 
     * @param substitution  the substitution
     * @return              the substituted term
     */
    Term subst(Substitution substitution);
    
    /**
     * Returns true iff this term is an instance of Variable.
     * 
     * @return  true iff this term is a variable
     */
    boolean isVariable();
}
