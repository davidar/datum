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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import cc.vidr.datum.db.FactDatabase;
import cc.vidr.datum.db.RuleDatabase;

/**
 * A program consists of a set of facts and rules, in Datalog-style notation.
 * 
 * @author  David Roberts
 */
public class Program {
    /** The stream for reading the program */
    private CharStream stream;
    /** The list of clauses contained in the program */
    private Clause[] clauses;
    
    /**
     * Create a new Program for the given stream.
     * 
     * @param stream  the stream
     */
    private Program(CharStream stream) {
        this.stream = stream;
    }
    
    /**
     * Create a new Program for the given string.
     * 
     * @param s  the string
     */
    public Program(String s) {
        this(new ANTLRStringStream(s));
    }
    
    /**
     * Create a new Program for the given reader.
     * 
     * @param r  the reader
     * @throws   IOException if an I/O error occurs
     */
    public Program(Reader r) throws IOException {
        this(new ANTLRReaderStream(r));
    }
    
    /**
     * Create a new Program for the given input stream.
     * 
     * @param input  the stream
     * @throws       IOException if an I/O error occurs
     */
    public Program(InputStream input) throws IOException {
        this(new ANTLRInputStream(input));
    }
    
    /**
     * Create a new Program for the given file.
     * 
     * @param f  the file
     * @throws   IOException if an I/O error occurs
     */
    public Program(File f) throws IOException {
        this(new ANTLRFileStream(f.getPath()));
    }
    
    /**
     * Parse the program.
     * 
     * @return  the array of clauses in the program
     * @throws  RecognitionException if a parsing error occurs
     */
    public Clause[] parse() throws RecognitionException {
        clauses = new DatalogParser(new CommonTokenStream(
                new DatalogLexer(stream))).program().toArray(new Clause[0]);
        return clauses;
    }
    
    /**
     * Assert the facts in the program to the given FactDatabase.
     * 
     * @param db  the database
     * @throws    UnsafeException if a non-gound fact is encountered in the
     *            program
     */
    public void assertFacts(FactDatabase db) throws UnsafeException {
        for(Clause clause : clauses)
            if(clause.isFact())
                db.assertFact(clause.getHead());
    }
    
    /**
     * Assert the rules in the program to the given RuleDatabase.
     * 
     * @param db  the database
     * @throws    UnsafeException if an unsafe rule is encountered in the
     *            program
     */
    public void assertRules(RuleDatabase db) throws UnsafeException {
        for(Clause clause : clauses)
            if(!clause.isFact())
                db.assertRule(clause);
    }
}
