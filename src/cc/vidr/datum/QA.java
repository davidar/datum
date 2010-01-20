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
import java.util.List;

/**
 * Provides methods for performing template-based question answering.
 * 
 * @author  David Roberts
 */
public final class QA {
    /**
     * Prevent instantiation.
     */
    private QA() {}
    
    /**
     * Return the list of facts which answer the given question, posed in
     * natural language.
     * 
     * @param question  the natural language question
     * @return          the facts answering the question
     */
    public static Literal[] query(String question) {
        question = sanitize(question);
        Template[] questionTemplates = Template.getQuestionTemplates();
        Literal[] literals = Template.parse(question, questionTemplates);
        List<Literal> goals = new ArrayList<Literal>();
        for(Literal literal : literals)
            for(Literal goal : Template.raise(literal))
                goals.add(goal);
        return Server.query(goals.toArray(new Literal[0]));
    }
    
    /**
     * Sanitize the given input string.
     * 
     * @param s  the string to sanitize
     * @return   the sanitized string
     */
    private static String sanitize(String s) {
        return s.toLowerCase()
                .replaceAll("[?.,-]", "")
                .replaceAll("\\s+", " ");
    }
    
    /**
     * Generate a natural language response for the given fact.
     * 
     * @param fact  the fact
     * @return      the natural language response
     */
    public static String respond(Literal fact) {
        Template statementTemplate = Random.element(
                Template.getStatementTemplates(fact.getPredicate()));
        Literal loweredFact = Random.element(Template.lower(fact));
        try {
            return humanize(statementTemplate.generate(loweredFact));
        } catch(UnificationException e) {
            throw new RuntimeException("Error generating response");
        }
    }
    
    /**
     * Convert the given string to a more human-friendly format.
     * 
     * @param s  the string to format
     * @return   the formatted string
     */
    private static String humanize(String s) {
        char[] chars = s.toCharArray();
        chars[0] = Character.toTitleCase(chars[0]);
        return new String(chars) + '.';
    }
}