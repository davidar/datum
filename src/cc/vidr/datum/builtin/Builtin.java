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

import java.util.HashMap;
import java.util.Map;

import cc.vidr.datum.Literal;

/**
 * Handles literals with built-in predicates.
 * 
 * @author  David Roberts
 */
public abstract class Builtin {
    /** Mapping of built-in predicates to their corresponding handler */
    private static Map<String, Builtin> builtins =
        new HashMap<String, Builtin>();
    
    static {
        // register default builtins
        register(new Unequal());
        register(new TermType());
        register(new FreebaseSearch());
        register(new FreebaseTriple());
        register(new DateTimeFormatBuiltin());
    }
    
    /**
     * Register the given built-in handler.
     * 
     * @param builtin  the handler
     */
    public static void register(Builtin builtin) {
        builtins.put(builtin.predicate(), builtin);
    }
    
    /**
     * Returns true iff there is a built-in handler registered for the given
     * predicate.
     * 
     * @param predicate  the predicate
     * @return           true iff it is a built-in predicate
     */
    public static boolean isBuiltinPredicate(String predicate) {
        return builtins.containsKey(predicate);
    }
    
    /**
     * Return the array of facts that satisfy the given goal.
     * 
     * @param goal  the goal literal
     * @return      the array of facts
     */
    public static Literal[] satisfy(Literal goal) {
        try {
            return builtins.get(goal.getPredicate()).handle(goal);
        } catch(Exception e) {
            return new Literal[0];
        }
    }
    
    /**
     * Return the built-in predicate corresponding to this handler.
     * 
     * @return  the predicate
     */
    protected abstract String predicate();
    
    /**
     * Handle the given goal.
     * 
     * @param goal  the goal literal
     * @return      the array of facts satisfying the goal
     */
    protected abstract Literal[] handle(Literal goal) throws Exception;
}
