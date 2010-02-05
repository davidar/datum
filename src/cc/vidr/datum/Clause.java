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

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cc.vidr.datum.term.Variable;

/**
 * A (Horn) clause is composed of a head literal, and a body of literals.
 * 
 * @author  David Roberts
 */
public final class Clause implements Serializable {
    private static final long serialVersionUID = -3597470628216220082L;
    /** The head of the clause */
    private Literal head;
    /** The literals comprising the body of the clause */
    private Literal[] body;
    
    /**
     * Create a new clause with the given head and body.
     * 
     * @param head  the head literal
     * @param body  the literals comprising the body
     */
    public Clause(Literal head, Literal... body) {
        this.head = head;
        this.body = body;
    }
    
    /**
     * Returns the head literal of this clause.
     * 
     * @return  the head literal.
     */
    public Literal getHead() {
        return head;
    }
    
    /**
     * Returns the i'th condition in the body of this clause.
     * 
     * @param i  the zero-based index of the condition
     * @return   the condition
     */
    public Literal getCondition(int i) {
        return body[i];
    }
    
    /**
     * Return the variant tag for this clause.
     * 
     * Two clauses have the same variant tag iff they are alphabetic variants
     * of each other.
     * 
     * @return  the variant tag
     */
    public String getVariantTag() {
        Map<Variable, String> variableTags = new HashMap<Variable, String>();
        StringBuilder builder = new StringBuilder();
        String tag = head.getVariantTag(variableTags);
        builder.append(tag.length()).append(':').append(tag);
        for(Literal literal : body) {
            tag = literal.getVariantTag(variableTags);
            builder.append(tag.length()).append(':').append(tag);
        }
        return builder.toString();
    }
    
    /**
     * Return a clause identical to this one, but missing the first condition
     * in the body.
     * 
     * @return   the shortened clause
     */
    public Clause pop() {
        return new Clause(head, Arrays.copyOfRange(body, 1, body.length));
    }
    
    /**
     * Returns true iff this clause is a fact (i.e. it has no body).
     * 
     * @return  true iff this clause is a fact
     */
    public boolean isFact() {
        return body.length == 0;
    }
    
    /**
     * Returns true iff this clause is safe i.e. every variable which occurs in
     * the head also occurs in the body.
     * 
     * @return  true iff this clause is safe
     */
    public boolean isSafe() {
        Set<Variable> headVariables = head.variables();
        for(Literal literal : body)
            headVariables.removeAll(literal.variables());
        return headVariables.isEmpty();
    }
    
    /**
     * Apply the given substitution to this clause.
     * 
     * @param substitution  the substitution
     * @return              the substituted clause
     */
    public Clause subst(Substitution substitution) {
        Literal[] newBody = new Literal[body.length];
        for(int i = 0; i < body.length; i++)
            newBody[i] = body[i].subst(substitution);
        return new Clause(head.subst(substitution), newBody);
    }
    
    /**
     * Substitute all variables with new variables.
     * 
     * @return  the substituted clause
     */
    public Clause rename() {
        Substitution substitution = new Substitution();
        Literal[] newBody = new Literal[body.length];
        for(int i = 0; i < body.length; i++)
            newBody[i] = body[i].rename(substitution);
        return new Clause(head.subst(substitution), newBody);
    }
    
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(head);
        if(body.length > 0) {
            builder.append(" :- ").append(body[0]);
            for(int i = 1; i < body.length; i++)
                builder.append(", ").append(body[i]);
        }
        builder.append('.');
        return builder.toString();
    }
}
