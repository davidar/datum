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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A literal is composed of a predicate and a list of terms.
 * 
 * @author  David Roberts
 */
public final class Literal implements Serializable {
    private static final long serialVersionUID = -94014254982764756L;
    /** The predicate name/arity */
    private String predicate;
    /** The arguments to the literal */
    private Term[] arguments;
    
    /**
     * Create a new literal with the given predicate and arguments.
     * 
     * @param predicate  the predicate (name/arity)
     * @param arguments  the list of arguments
     */
    public Literal(String predicate, Term... arguments) {
        this.predicate = predicate.intern();
        this.arguments = arguments;
        if(!predicate.endsWith("/" + arguments.length))
            throw new IllegalArgumentException(
                    "Predicate arity does not match number of arguments");
    }
    
    /**
     * Return the predicate of the literal.
     * 
     * @return  the predicate
     */
    public String getPredicate() {
        return predicate;
    }
    
    /**
     * Return the predicate name.
     * 
     * @return  the predicate name
     */
    public String getPredicateName() {
        return predicate.substring(0, predicate.indexOf('/'));
    }
    
    /**
     * Return the arity of the literal i.e. the number of arguments.
     * 
     * @return  the arity
     */
    public int getArity() {
        return arguments.length;
    }
    
    /**
     * Return the i'th argument of the literal.
     * 
     * @param i  the zero-based index of the argument
     * @return   the argument
     */
    public Term getArgument(int i) {
        return arguments[i];
    }
    
    /**
     * Returns true iff the literal is ground i.e. it contains no variables.
     * 
     * @return  true iff the literal is ground
     */
    public boolean isGround() {
        for(Term argument : arguments)
            if(argument.isVariable())
                return false;
        return true;
    }
    
    /**
     * Returns the set of variables in this literal.
     * 
     * @return  the set of variables
     */
    public Set<Variable> variables() {
        Set<Variable> variables = new HashSet<Variable>();
        for(Term argument : arguments)
            if(argument instanceof Variable)
                variables.add((Variable) argument);
        return variables;
    }
    
    /**
     * Return the variant tag for this literal.
     * 
     * Two literals have the same variant tag iff there exists a mapping of
     * variables to variables such that the result of applying the mapping to
     * one literal is structurally equal to the other literal (i.e. they are
     * alphabetic variants of one another).
     * 
     * @return  the variant tag
     */
    public String getVariantTag() {
        return getVariantTag(new HashMap<Variable, String>());
    }
    
    /**
     * Return the variant tag for this literal, using variable tags from the
     * given map.
     * 
     * @param variableTags  the variable tags
     * @return              the variant tag
     */
    public String getVariantTag(Map<Variable, String> variableTags) {
        StringBuilder builder = new StringBuilder();
        builder.append(predicate.length()).append(':').append(predicate);
        for(Term argument : arguments) {
            String tag = argument.toString();
            if(argument instanceof Variable) {
                Variable variable = (Variable) argument;
                if(!variableTags.containsKey(variable))
                    variableTags.put(variable, "_" + variableTags.size());
                tag = variableTags.get(variable);
            }
            builder.append(tag.length()).append(':').append(tag);
        }
        return builder.toString();
    }
    
    /**
     * Unify two literals.
     * 
     * @param other  the other literal
     * @return       the Most General Unifier (MGU)
     * @throws       UnificationException if the literals cannot be unified
     */
    public Substitution unify(Literal other) throws UnificationException {
        Substitution substitution = new Substitution();
        unify(other, substitution);
        return substitution;
    }
    
    /**
     * Unify two literals.
     * 
     * @param other         the other literal 
     * @param substitution  the substitution built up so far
     * @throws              UnificationException if the literals cannot be
     *                      unified
     */
    public void unify(Literal other, Substitution substitution)
    throws UnificationException {
        if(this.predicate != other.predicate)
            throw new UnificationException();
        for(int i = 0; i < arguments.length; i++)
            arguments[i].subst(substitution).unify(
                    other.arguments[i].subst(substitution), substitution);
    }
    
    /**
     * Apply the given substitution to this literal.
     * 
     * @param substitution  the substitution
     * @return              the substituted literal
     */
    public Literal subst(Substitution substitution) {
        Term[] newArguments = new Term[arguments.length];
        for(int i = 0; i < arguments.length; i++)
            newArguments[i] = arguments[i].subst(substitution);
        return new Literal(predicate, newArguments);
    }
    
    /**
     * Substitute all variables with new variables.
     * 
     * @param substitution  the mapping of variables so far
     * @return              the substituted literal
     */
    public Literal rename(Substitution substitution) {
        for(Term argument : arguments)
            if(argument instanceof Variable)
                // has no effect if this variable has already been added to
                // the substitution
                substitution.put((Variable) argument, new Variable());
        return subst(substitution);
    }
    
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getPredicateName());
        if(arguments.length > 0) {
            builder.append('(').append(arguments[0]);
            for(int i = 1; i < arguments.length; i++)
                builder.append(", ").append(arguments[i]);
            builder.append(')');
        }
        return builder.toString();
    }
    
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o instanceof Literal) {
            Literal l = (Literal) o;
            return this.predicate == l.predicate
                && Arrays.equals(this.arguments, l.arguments);
        }
        return false;
    }
    
    public int hashCode() {
        return predicate.hashCode() + 13 * Arrays.hashCode(arguments);
    }
    
    private void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        predicate = predicate.intern();
    }
}
