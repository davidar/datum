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

grammar Datalog;

@header        { package cc.vidr.datum; }
@lexer::header { package cc.vidr.datum; }

@members {
    private VariableFactory variableFactory;
}

program returns [List<Clause> result]
    @init {
        $result = new ArrayList<Clause>();
    }
    : ( e=clause { $result.add($e.result); } )+ EOF
    ;

clause returns [Clause result]
    @init {
        variableFactory = new VariableFactory();
        ClauseBuilder builder = new ClauseBuilder();
    }
    : e=literal { builder.setHead($e.result); }
      ( ':-'
          e=literal { builder.addCondition($e.result); }
          ( ',' e=literal { builder.addCondition($e.result); } )*
      )? '.'
      { variableFactory = null;
        $result = builder.toClause(); }
    ;

literal returns [Literal result]
    @init {
        LiteralBuilder builder = new LiteralBuilder();
    }
    : ATOM { builder.setPredicateName($ATOM.text); }
      ( '('
          e=term { builder.addArgument($e.result); }
          ( ',' e=term { builder.addArgument($e.result); } )*
      ')' )?
      { $result = builder.toLiteral(); }
    ;

term returns [Term result]
    : ATOM     { $result = new Atom($ATOM.text); }
    | VARIABLE { $result = variableFactory.get($VARIABLE.text); }
    ;

ATOM: ('a'..'z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;

VARIABLE: ('A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;

COMMENT: '%' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;};

WS: (' '|'\t'|'\r'|'\n') {$channel=HIDDEN;};
