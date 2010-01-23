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

import java.io.File;
import java.io.IOException;

import org.antlr.runtime.RecognitionException;

import cc.vidr.datum.Program;
import cc.vidr.datum.Server;
import cc.vidr.datum.UnsafeException;

/**
 * Datalog program importer.
 * 
 * @author  David Roberts
 */
public class Import {
    public static void main(String[] args) {
        for(String arg : args) {
            try {
                System.out.print("Loading '" + arg + "'... ");
                System.out.flush();
                Program program = new Program(new File(arg));
                program.parse();
                program.assertFacts(Server.factDatabase);
                program.assertRules(Server.ruleDatabase);
                System.out.println("OK");
            } catch(RecognitionException e) {
                System.err.println("Malformed input: " + e.getMessage());
            } catch(UnsafeException e) {
                System.err.println("Unsafe rule or non-ground fact " +
                		   "encountered: " + e.getMessage());
            } catch(IOException e) {
                System.err.println("Error opening file: " + e.getMessage());
            }
        }
    }
}
