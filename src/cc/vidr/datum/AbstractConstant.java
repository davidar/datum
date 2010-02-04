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
 * Provides the basic implementation required for a Constant.
 * 
 * @author     David Roberts
 * @param <T>  the type of data which identifies the constant
 */
public abstract class AbstractConstant<T> implements Constant {
    private static final long serialVersionUID = -6448707812888360999L;
    /** The data which uniquely identifies the constant */
    protected T data;
    
    /**
     * Create a new Constant with the given data.
     * 
     * @param data  the data of the Constant
     */
    protected AbstractConstant(T data) {
        this.data = data;
    }
    
    /**
     * Return the data associated with this constant.
     * 
     * @return  the data associated with this constant
     */
    public final T getValue() {
        return data;
    }
    
    public final void unify(Term term, Substitution substitution)
    throws UnificationException {
        if(term.isVariable())
            term.unify(this, substitution);
        else if(!equals(term))
            throw new UnificationException();
    }
    
    public final Term subst(Substitution substitution) {
        return this;
    }
    
    public final boolean isVariable() {
        return false;
    }
    
    public final int hashCode() {
        return data.hashCode();
    }
}
