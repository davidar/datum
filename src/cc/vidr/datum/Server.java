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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.vidr.datum.db.FactDatabase;
import cc.vidr.datum.db.RuleDatabase;
import cc.vidr.datum.db.jdo.JDOFactDatabase;
import cc.vidr.datum.db.jdo.JDORuleDatabase;

/**
 * A server infers, caches, and provides facts matching a given goal.
 * See <http://www.cs.sunysb.edu/~warren/xsbbook/node15.html> for a more
 * complete description.
 * 
 * @author  David Roberts
 */
public final class Server {
    /** The FactDatabase to be used by servers */
    public static FactDatabase factDatabase = new JDOFactDatabase();
    /** The RuleDatabase to be used by servers */
    public static RuleDatabase ruleDatabase = new JDORuleDatabase();
    /** Mapping of goal variant tags to servers */
    private static Map<String, Server> servers =
        new HashMap<String, Server>();
    
    /** The set of facts satisfying the goal */
    private Set<Literal> facts = new HashSet<Literal>();
    /** The set of clients */
    private Set<Server> clients = new HashSet<Server>();
    /** Mapping of servers to dependent clauses */
    private Map<Server, List<Clause>> clauses =
        new HashMap<Server, List<Clause>>();
    
    /**
     * Construct a new server.
     */
    private Server() {}
    
    /**
     * Return an array of facts matching the given goal.
     * 
     * @param goal  the goal
     * @return      the array of facts
     */
    public static Literal[] query(Literal goal) {
        return getServer(goal).facts.toArray(new Literal[0]);
    }
    
    /**
     * Convenience method to aggregate the facts matching the given goals.
     * 
     * @param goals  the goals
     * @return       the array of facts
     */
    public static Literal[] query(Literal... goals) {
        Set<Literal> facts = new HashSet<Literal>();
        for(Literal goal : goals)
            facts.addAll(getServer(goal).facts);
        return facts.toArray(new Literal[0]);
    }
    
    /**
     * Return the facts matching the head of the given rule, using the
     * conditions in the body of the clause rather than those in the database.
     * 
     * @param clause  the query clause
     * @return        the array of facts
     */
    public static Literal[] query(Clause clause) {
        Server server = new Server();
        server.add(clause.rename());
        return server.facts.toArray(new Literal[0]);
    }
    
    /**
     * Return the server for the given goal.
     * 
     * @param goal  the goal
     * @return      the server for the goal
     */
    private static Server getServer(Literal goal) {
        Server server = servers.get(goal.getVariantTag());
        if(server == null) {
            server = new Server();
            servers.put(goal.getVariantTag(), server);
            server.run(goal);
        }
        return server;
    }
    
    /**
     * Run the server, searching the databases for facts and rules matching the
     * server's goal.
     * 
     * @param goal  this server's goal
     */
    private void run(Literal goal) {
        for(Literal fact : factDatabase.search(goal))
            add(fact);
        for(Clause rule : ruleDatabase.search(goal))
            add(rule);
    }
    
    /**
     * Request facts from this server.
     * 
     * @param client  the server requesting facts
     * @return        the list of facts currently held by the server
     */
    private Literal[] request(Server client) {
        clients.add(client);
        return facts.toArray(new Literal[0]);
    }
    
    /**
     * Inform this client of a new fact.
     * 
     * @param server  the server providing the fact
     * @param fact    the fact
     */
    private void respond(Server server, Literal fact) {
        for(Clause clause : clauses(server))
            add(clause, fact);
    }
    
    /**
     * Return the list of clauses dependent on the given server.
     * 
     * @param server  the server
     * @return        the clauses dependent on the server
     */
    private List<Clause> clauses(Server server) {
        if(clauses.containsKey(server))
            return clauses.get(server);
        List<Clause> list = new ArrayList<Clause>();
        clauses.put(server, list);
        return list;
    }
    
    /**
     * Resolve the given fact against the given clause and add the resulting
     * clause to this server.
     * 
     * @param clause  the clause
     * @param fact    the fact
     */
    private void add(Clause clause, Literal fact) {
        /*
         * For example, if the clause is
         *     p(X,Y,Z) :- q(X,Y), r(Y,Z).
         * and the fact is
         *     q(a,b).
         * Then the following clause will be added to the server
         *     p(a,b,Z) :- r(b,Z).
         */
        Literal condition = clause.getCondition(0);
        clause = clause.pop();
        try {
            add(clause.subst(condition.unify(fact)));
        } catch(UnificationException e) {
            throw new IllegalArgumentException(
                    "Server returned fact that does not unify with its goal");
        }
    }
    
    /**
     * Add a clause whose head matches this server's goal.
     * 
     * @param clause  the matching clause
     */
    private void add(Clause clause) {
        if(clause.isFact()) {
            add(clause.getHead());
            return;
        }
        Server server = getServer(clause.getCondition(0));
        clauses(server).add(clause);
        Literal[] facts = server.request(this);
        for(Literal fact : facts)
            add(clause, fact);
    }
    
    /**
     * Add a fact matching this server's goal.
     * 
     * @param fact  the matching fact
     */
    private void add(Literal fact) {
        if(facts.contains(fact))
            return;
        facts.add(fact);
        for(Server client : clients)
            client.respond(this, fact);
    }
}
