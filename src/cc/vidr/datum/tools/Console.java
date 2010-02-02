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

package cc.vidr.datum.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.antlr.runtime.RecognitionException;
import org.apache.commons.lang.StringUtils;

import cc.vidr.datum.Clause;
import cc.vidr.datum.Literal;
import cc.vidr.datum.Program;
import cc.vidr.datum.QA;
import cc.vidr.datum.Server;

/**
 * Interactive question answering console.
 * 
 * @author  David Roberts
 */
public class Console {
    private static final boolean DEBUG = true;
    
    private static void printFact(Literal fact, int depth) {
        Literal[] proof = Server.getProof(fact);
        String response = StringUtils.capitalize(QA.respond(fact));
        System.out.print(StringUtils.repeat("  ", depth));
        System.out.print("- ");
        System.out.print(response != null ? response : fact);
        System.out.println(proof.length > 0 ? ", because:" : ".");
        for(Literal literal : proof)
            printFact(literal, depth + 1);
    }
    
    public static void main(String[] args) throws IOException {
        System.out.print("Warming up... ");
        System.out.flush();
        QA.query("");
        System.out.println("Ready.");
        System.out.println("Ask me a question. Leave a blank line to exit.");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in));
        while(true) {
            int numServers = Server.getNumServers();
            int numFacts = Server.getNumFacts();
            System.out.print("> ");
            String q = in.readLine();
            if(q == null || q.isEmpty()) {
                System.out.println("Bye.");
                break;
            }
            Literal[] facts = QA.query(q);
            if(facts.length == 0) {
                try {
                    // perhaps the input is a datalog statement
                    Program program = new Program(q);
                    Clause[] query = program.parse();
                    facts = Server.query(query[0].getHead());
                } catch (RecognitionException e) {
                    System.out.println("I don't know.");
                }
            }
            for(Literal fact : facts)
                printFact(fact, 0);
            if(DEBUG) {
                System.err.println((Server.getNumServers() - numServers)
                        + " servers spawned");
                System.err.println((Server.getNumFacts() - numFacts)
                        + " facts retrieved/generated");
            }
        }
    }
}
