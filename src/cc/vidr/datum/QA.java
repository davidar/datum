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

import cc.vidr.datum.util.Random;

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
                .replaceAll("\\s+", " ")
                .trim();
    }
    
    /**
     * Generate a natural language response for the given fact.
     * 
     * @param fact  the fact
     * @return      the natural language response, or null if unable to
     *              generate one
     */
    public static String respond(Literal fact) {
        Template statementTemplate = Random.element(
                Template.getStatementTemplates(fact.getPredicate()));
        Literal loweredFact = Random.element(Template.lower(fact));
        if(statementTemplate == null || loweredFact == null)
            return null;
        try {
            return statementTemplate.generate(loweredFact);
        } catch(UnificationException e) {
            throw new RuntimeException("Error generating response");
        }
    }
}
