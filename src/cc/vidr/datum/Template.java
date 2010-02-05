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
import java.util.List;
import java.util.Map;

import cc.vidr.datum.term.Atom;
import cc.vidr.datum.term.StringTerm;
import cc.vidr.datum.term.Term;
import cc.vidr.datum.term.Variable;

/**
 * Provides template-based extraction of information from, and generation of
 * sentences.
 * 
 * @author  David Roberts
 */
public class Template {
    /** Cached question templates */
    private static Template[] questionTemplates;
    /** Cached statement templates by predicate */
    private static Map<String, Template[]> statementTemplates =
        new HashMap<String, Template[]>();
    
    /** The phrases that occur around and between slots */
    private String[] phrases;
    /** Slots into which strings can be placed */
    private Variable[] slots;
    /** The literal corresponding to this template */
    private Literal literal;
    
    /**
     * Create a new template from the given template definition, and the
     * corresponding predicate.
     * 
     * For example, Template("#1 is the father of #0", "father/2") will
     * produce a template with this corresponding literal:
     *     Y is the father of X : father(X, Y)
     * 
     * A template can contain 10 slots (#0..#9). A slot cannot be adjacent to
     * another slot.
     * 
     * @param template   the template definition
     * @param predicate  the corresponding predicate
     */
    public Template(String template, String predicate) {
        literal = new Literal(predicate);
        parseTemplate(template);
        if(!checkPhrases())
            throw new IllegalArgumentException("Invalid template definition");
    }
    
    /**
     * Parse the template definition.
     * 
     * @param template  the template definition
     */
    private void parseTemplate(String template) {
        List<String> phrasesList = new ArrayList<String>();
        List<Variable> slotsList = new ArrayList<Variable>();
        StringBuilder builder = new StringBuilder();
        
        char[] chars = template.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if(c == '#') { // variable slot
                phrasesList.add(builder.toString());
                builder.setLength(0);
                int slotNumber = chars[++i] - '0';
                slotsList.add((Variable) literal.getArgument(slotNumber));
            } else {
                builder.append(c);
            }
        }
        phrasesList.add(builder.toString());
        
        phrases = phrasesList.toArray(new String[0]);
        slots = slotsList.toArray(new Variable[0]);
    }
    
    /**
     * Ensure that the phrase array is valid.
     * 
     * @return  true iff the phrase array is valid
     */
    private boolean checkPhrases() {
        for(int i = 1; i < phrases.length-1; i++)
            if(phrases[i].isEmpty())
                return false;
        return true;
    }
    
    /**
     * Parse the given sentence according to this template. A lowered literal
     * of the extracted information is returned.
     * 
     * A lowered literal is one in which all arguments are strings, rather than
     * the entities that they refer to.
     * 
     * @param sentence  the sentence to parse
     * @return          the lowered literal, or null if the sentence does not
     *                  match the template
     */
    public Literal parse(String sentence) {
        Substitution substitution = new Substitution();
        if(sentence.indexOf(phrases[0]) != 0)
            // sentence does not begin with same phrase as template
            return null;
        int nameStart = phrases[0].length();
        for(int i = 0; i < slots.length; i++) {
            String phrase = phrases[i+1];
            int nameEnd = sentence.indexOf(phrase, nameStart);
            if(nameEnd == -1)
                // unable to find phrase in sentence
                return null;
            if(phrase.isEmpty())
                // apart from the first phrase, only the last phrase can be
                // empty, so match the phrase to the end of the sentence
                nameEnd = sentence.length();
            substitution.put(slots[i], new StringTerm(
                    sentence.substring(nameStart, nameEnd).trim()));
            nameStart = nameEnd + phrase.length();
        }
        if(nameStart != sentence.length())
            // junk at the end of the sentence
            return null;
        return literal.subst(substitution);
    }
    
    /**
     * Convenience method to to parse a sentence against a set of templates.
     * Any non-matching templates will be ignored.
     * 
     * @param sentence   the sentence
     * @param templates  the templates
     * @return           the array of lowered literals
     */
    public static Literal[] parse(String sentence, Template[] templates) {
        List<Literal> literals = new ArrayList<Literal>();
        for(Template template : templates) {
            Literal literal = template.parse(sentence);
            if(literal != null)
                literals.add(literal);
        }
        return literals.toArray(new Literal[0]);
    }
    
    /**
     * Generate the sentence for the given lowered literal.
     * 
     * @param literal  the lowered literal
     * @return         the generated sentence
     * @throws         UnificationException if the given literal does not match
     *                 the literal associated with this template
     */
    public String generate(Literal literal) throws UnificationException {
        return generate(this.literal.unify(literal));
    }
    
    /**
     * Generate a sentence for the given substitution.
     * 
     * @param substitution  the substitution
     * @return              the sentence
     */
    private String generate(Substitution substitution) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < slots.length; i++) {
            builder.append(phrases[i]);
            Term term = slots[i].subst(substitution);
            if(term instanceof StringTerm) {
                StringTerm st = (StringTerm) term;
                builder.append(st.getValue());
            } else {
                builder.append(term);
            }
        }
        builder.append(phrases[phrases.length-1]);
        return builder.toString();
    }
    
    /**
     * Return the array of all known question templates.
     * 
     * @return  the question templates
     */
    public static Template[] getQuestionTemplates() {
        if(questionTemplates == null)
            questionTemplates = getTemplates(new Literal(
                "question_template/2", new Variable(), new Variable()));
        return questionTemplates;
    }
    
    /**
     * Return the array of all known statement templates for the given
     * predicate.
     * 
     * @param predicate  the predicate
     * @return           the statement templates
     */
    public static Template[] getStatementTemplates(String predicate) {
        if(!statementTemplates.containsKey(predicate))
            statementTemplates.put(predicate, getTemplates(new Literal(
                "statement_template/2", new Variable(), new Atom(predicate))));
        return statementTemplates.get(predicate);
    }
    
    /**
     * Fetch template definitions matching the given goal.
     * 
     * @param goal  the goal
     * @return      the templates
     */
    private static Template[] getTemplates(Literal goal) {
        Literal[] facts = Server.query(goal);
        Template[] templates = new Template[facts.length];
        for(int i = 0; i < facts.length; i++) {
            Literal fact = facts[i];
            StringTerm st = (StringTerm) fact.getArgument(0);
            templates[i] = new Template(
                    st.getValue(), fact.getArgument(1).toString());
        }
        return templates;
    }
    
    /**
     * Replace the string arguments of the given literal with the entities that
     * they refer to.
     * 
     * @param literal  the literal to raise
     * @return         all possible raised literals
     */
    public static Literal[] raise(Literal literal) {
        Literal goal = new Literal(literal.getPredicate());
        return raiseLower(goal, "name/2", goal, literal);
    }
    
    /**
     * Replace the arguments of the given literal with their preferred names.
     * 
     * @param literal  the literal to lower
     * @return         all possible lowered literals (should usually only
     *                 contain one literal)
     */
    public static Literal[] lower(Literal literal) {
        Literal goal = new Literal(literal.getPredicate());
        return raiseLower(goal, "preferred_name/2", literal, goal);
    }
    
    /**
     * Perform the raising/lowering.
     */
    private static Literal[] raiseLower(Literal goal,
                                        String predicate,
                                        Literal first,
                                        Literal second) {
        ClauseBuilder builder = new ClauseBuilder();
        builder.setHead(goal);
        int arity = goal.getArity();
        for(int i = 0; i < arity; i++) {
            Term firstArgument = first.getArgument(i);
            Term secondArgument = second.getArgument(i);
            if(!firstArgument.isVariable() || !secondArgument.isVariable())
                builder.addCondition(new Literal(
                        predicate, firstArgument, secondArgument));
        }
        return Server.query(builder.toClause());
    }
    
    public String toString() {
        return generate((Substitution) null) + " : " + literal;
    }
}
