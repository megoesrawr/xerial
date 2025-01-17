/*--------------------------------------------------------------------------
 *  Copyright 2009 Taro L. Saito
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *--------------------------------------------------------------------------*/
//--------------------------------------
// XerialJ
//
// SilkLine.g
// Since: Jan 28, 2009 1:00:02 PM
//
//--------------------------------------
 
grammar SilkLine;
options 
{
	language=Java;
	output=AST;
}
tokens {
Silk;
SilkNode;
SilkLine;
Name;
Value;
Occurrence;
DataType;
Function;
Argument;
KeyValuePair;
Key;
}


@lexer::header
{
/*--------------------------------------------------------------------------
 *  Copyright 2009 Taro L. Saito
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *--------------------------------------------------------------------------*/
//--------------------------------------
// XerialJ
//
// SilkLexer.java
// Since: Jan 28, 2009 1:00:02 PM
//
//--------------------------------------

package org.xerial.silk.impl;
import org.xerial.core.XerialError;
import org.xerial.core.XerialErrorCode;
import org.xerial.util.log.Logger;
import org.xerial.silk.impl.SilkLexerState;
import org.xerial.silk.impl.SilkLexerState.State;
import org.xerial.silk.impl.SilkLexerState.Symbol;
}
 
 
@header
{
/*--------------------------------------------------------------------------
 *  Copyright 2009 Taro L. Saito
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *--------------------------------------------------------------------------*/
//--------------------------------------
// XerialJ
//
// SilkParser.java
// Since: Jan 28, 2009 1:00:02 PM
//
//--------------------------------------
package org.xerial.silk.impl;
}

@lexer::members {

private static Logger _logger = Logger.getLogger(SilkLineLexer.class);

private SilkLexerState lexerContext = new SilkLexerState();

private void transit(Symbol token) { lexerContext.transit(token); } 
private boolean isKey() { return lexerContext.isInKey(); }
private boolean isValue() { return lexerContext.isValue(); }
private boolean isInValue() { return lexerContext.isInValue(); }
private boolean isOutValue() { return lexerContext.isOutValue(); }
private boolean isHead() { return getCharPositionInLine() == 0; }

  public void resetContext() { lexerContext.reset(); }

  @Override
  public void reportError(RecognitionException e) {
        String message = "line=" + getLine() + "(" + getCharPositionInLine() + "): " + e.toString();;
        throw new XerialError(XerialErrorCode.INVALID_TOKEN, message);
  } 
  
}
 


// lexer rules for analyzing a single line of Silk format data
fragment
WhiteSpace
	:	(' ' | '\t') 
	;		

// comment 
LineComment: '#' ~(LineBreakChar)* ;

// r: <CR> n : <LF>
fragment LineBreakChar: '\n' | '\r';

BlockIndent: { isHead() }? => (' ')* '--' { transit(Symbol.NodeStart); };
NodeIndent: { isHead() }? => (' ')* '-' { transit(Symbol.NodeStart); } ;
PullUpNodeIndent: { isHead() }? => (' ')* '->' { transit(Symbol.NodeStart); };
FunctionIndent: { isHead() }? => (' ')* '@' { transit(Symbol.NodeStart); } ;
BlankLine: { isHead() }? => WhiteSpace*;

LParen: '(' { transit(Symbol.EnterParen); };  
RParen:	')' { transit(Symbol.LeaveParen); };
Comma: 	','; 
Colon:	':' { transit(Symbol.Colon); } ;

Seq: 	'>';
TabSeq:	'|';
Star: 	'*';
At:		'@' { transit(Symbol.At); };
Plus:	'+';
LBracket:	'[' { transit(Symbol.EnterParen); };
RBracket:	']';
Question:	'?';

fragment Digit: '0' .. '9';
fragment Letter: 'A' .. 'F' | 'a' .. 'f';
fragment HexDigit: Digit | Letter;
fragment UnicodeChar: ~('"'| '\\');
fragment EscapeSequence
	: '\\' ('\"' | '\\' | '/' | 'b' | 'f' | 'n' | 'r' | 't' | 'u' HexDigit HexDigit HexDigit HexDigit)
	; 
fragment StringChar :  UnicodeChar | EscapeSequence;
fragment StringChar_s: StringChar*;
String: '"' s=StringChar_s '"' { setText($s.text);  transit(Symbol.LeaveValue); };
 

fragment ScopeIndicator: '(' | ')';
fragment FlowIndicator:  '[' | ']' | '{' | '}';
fragment Indicator:  FlowIndicator | ScopeIndicator | ',' | ':' | '>' | '|' | '*' | '\'' | '"' | '@' | '%' | '\\' | '=';	


fragment PlainUnsafeChar: '"';

fragment PlainSafeKey: ~(PlainUnsafeChar | ScopeIndicator | FlowIndicator | ',' | ':' | '>' | '*' | '|' | '='); 
fragment PlainSafeIn: ~(PlainUnsafeChar | ScopeIndicator | ',');
fragment PlainSafeOut: ~(PlainUnsafeChar);

fragment NonSpaceChar: ~('"'| '\\' | WhiteSpace );
fragment PlainFirst
	:  ~('-' | '+' | '?' | PlainUnsafeChar | WhiteSpace | Indicator ) 
	| { isValue() }? => ('-' | '=' | '+' | '*' | (':' | '?') NonSpaceChar)
	;

PlainOneLine
	: { isKey() }? =>  PlainFirst PlainSafeKey* { transit(Symbol.LeaveValue); }  
  | { isInValue() }? => PlainFirst  PlainSafeIn* { transit(Symbol.LeaveValue); }  
  | { isOutValue() }? => (String | ~('"' | '@' | WhiteSpace) PlainSafeOut* | JSON){ transit(Symbol.LeaveValue); }  
	;
  

JSON
	: { isValue() }? => '{'
	{
		//_logger.info("enter JSON object");
		InLineJSONLexer l = new InLineJSONLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(l);
		InLineJSONParser p = new InLineJSONParser(tokens);
		p.jsonObjectFragment();
		
		//$channel = JSON_CHANNEL;
		emit(new CommonToken(JSON, getText())); 
	}
	| { isValue() }? => '['
	{
		//_logger.info("enter JSON array");
		InLineJSONLexer l = new InLineJSONLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(l);
		InLineJSONParser p = new InLineJSONParser(tokens);
		p.jsonArrayFragment();
		
		//$channel = JSON_CHANNEL;
		emit(new CommonToken(JSON, getText())); 
	}  
	;
	 
Spaces: { !isHead() }? => WhiteSpace+ { $channel=HIDDEN; };

 

// parser rules	


silkFile: silkLine* -> ^(Silk silkLine*)
	;

silkLine
	: silkNode 
	| LineComment
	| WhiteSpace -> BlankLine
	;

fragment
nodeName: PlainOneLine | String;

fragment
nodeValue
	: function_i -> ^(Function function_i)
	| (PlainOneLine | String) -> Value[$nodeValue.text]
	| JSON 
	; 


silkNode
	: indent nodeItem -> ^(SilkNode indent nodeItem)
	| indent (LParen attributeList RParen)? plural? (Colon nodeValue)?
	-> ^(SilkNode indent attributeList? plural? nodeValue?)
	| function  
	;

indent
  : NodeIndent
  | BlockIndent
  | PullUpNodeIndent 
  ;

fragment
nodeItem: nodeName dataType?  (LParen attributeList RParen)? plural? (Colon nodeValue)?
	-> Name[$nodeName.text.trim()] dataType? attributeList? plural? nodeValue?  
	;

fragment
dataType: LBracket! dataTypeName RBracket!
	;

fragment	
dataTypeName: PlainOneLine
	-> DataType[$dataTypeName.text]
	; 	
	
fragment
attributeList: attributeItem (Comma! attributeItem)* ;	

fragment
attributeItem: nodeItem -> ^(SilkNode nodeItem);


fragment
plural
	: Star -> Occurrence["ZERO_OR_MORE"]
	| Plus -> Occurrence["ONE_OR_MORE"]
	| Question -> Occurrence["ZERO_OR_ONE"]
	| Seq -> Occurrence["SEQUENCE"]
	| TabSeq -> Occurrence["TABBED_SEQUENCE"]
	;
   
function
	: NodeIndent function_i
	-> ^(Function NodeIndent function_i)
	| FunctionIndent PlainOneLine LParen (functionArg (Comma functionArg)*)? RParen
	-> ^(Function NodeIndent[$FunctionIndent.text] Name[$PlainOneLine.text.trim()] functionArg*)
	;

fragment
function_i: At PlainOneLine LParen (functionArg (Comma functionArg)*)? RParen
	-> Name[$PlainOneLine.text.trim()] functionArg*
	;

fragment
functionArg
	: nodeValue -> Argument[$functionArg.text]
	| nodeName Colon nodeValue -> ^(KeyValuePair Key[$nodeName.text.trim()] Value[$nodeValue.text.trim()])
	;





