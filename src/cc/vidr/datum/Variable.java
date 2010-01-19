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

/**
 * An unknown.
 * 
 * @author  David Roberts
 */
public class Variable implements Term {
    private static final long serialVersionUID = -8998096211913532047L;
    
    public void unify(Term term, Substitution substitution) {
        substitution.put(this, term);
    }
    
    public Term subst(Substitution substitution) {
        return substitution.subst(this);
    }
    
    public boolean isVariable() {
        return true;
    }
    
    public String toString() {
        return "_" + System.identityHashCode(this);
    }
}
