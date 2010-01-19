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

package cc.vidr.datum.db.jdo;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import cc.vidr.datum.Literal;

/**
 * A JDO model wrapping a fact.
 * 
 * @author  David Roberts
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class JDOFact {
    /** The number of literal arguments indexed */
    public static final int NUM_INDEXED_ARGUMENTS = 4;
    
    /** The unique ID of the fact */
    @SuppressWarnings("unused")
    @PrimaryKey
    private String id;
    
    /** The fact */
    @Persistent(serialized = "true")
    private Literal fact;
    
    /** The predicate of the fact */
    @SuppressWarnings("unused")
    @Persistent
    private String predicate;
    
    /** The first argument of the fact */
    @SuppressWarnings("unused")
    @Persistent
    private String argument0;

    /** The second argument of the fact */
    @SuppressWarnings("unused")
    @Persistent
    private String argument1;

    /** The third argument of the fact */
    @SuppressWarnings("unused")
    @Persistent
    private String argument2;

    /** The fourth argument of the fact */
    @SuppressWarnings("unused")
    @Persistent
    private String argument3;
    
    /**
     * Wrap the given fact.
     * 
     * @param fact  the fact to be wrapped
     */
    public JDOFact(Literal fact) {
        this.id = fact.getVariantTag();
        this.fact = fact;
        this.predicate = fact.getPredicate();
        int arity = fact.getArity();
        if(arity > 0) argument0 = fact.getArgument(0).toString();
        if(arity > 1) argument1 = fact.getArgument(1).toString();
        if(arity > 2) argument2 = fact.getArgument(2).toString();
        if(arity > 3) argument3 = fact.getArgument(3).toString();
    }
    
    /**
     * Return the wrapped fact.
     * 
     * @return  the fact.
     */
    public Literal fact() {
        return fact;
    }
}
