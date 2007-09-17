// $ANTLR 3.0.1 D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g 2007-09-17 19:26:58

//--------------------------------------
// Xerial Project
//
// JSONWalker.java
// Since: Apr 26, 2007
//
//--------------------------------------
package org.xerial.json;
import org.xerial.util.StringUtil;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

/**
 * JSONWalker generates JSONObject and JSONArray instances
 * from a given input JSON parse tree.
 */
public class JSONWalker extends TreeParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "OBJECT", "ELEMENT", "ARRAY", "STRING", "INTEGER", "DOUBLE", "Colon", "Comma", "LBrace", "RBrace", "LBracket", "RBracket", "Dot", "TRUE", "FALSE", "NULL", "Digit", "HexDigit", "UnicodeChar", "EscapeSequence", "StringChar", "Int", "Frac", "Exp", "WhiteSpace", "String", "Integer", "Double"
    };
    public static final int INTEGER=8;
    public static final int Double=31;
    public static final int LBrace=12;
    public static final int Frac=26;
    public static final int NULL=19;
    public static final int Exp=27;
    public static final int RBracket=15;
    public static final int Colon=10;
    public static final int UnicodeChar=22;
    public static final int Digit=20;
    public static final int EOF=-1;
    public static final int HexDigit=21;
    public static final int TRUE=17;
    public static final int Int=25;
    public static final int OBJECT=4;
    public static final int ELEMENT=5;
    public static final int Dot=16;
    public static final int DOUBLE=9;
    public static final int StringChar=24;
    public static final int WhiteSpace=28;
    public static final int String=29;
    public static final int LBracket=14;
    public static final int FALSE=18;
    public static final int Comma=11;
    public static final int EscapeSequence=23;
    public static final int ARRAY=6;
    public static final int Integer=30;
    public static final int RBrace=13;
    public static final int STRING=7;
    protected static class ObjectLayer_scope {
        ArrayList<JSONElement> elems;
    }
    protected Stack ObjectLayer_stack = new Stack();
    protected static class ArrayLayer_scope {
        ArrayList<JSONValue> elems;
    }
    protected Stack ArrayLayer_stack = new Stack();


        public JSONWalker(TreeNodeStream input) {
            super(input);
        }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g"; }

    
    	public String unquote(String s) { return StringUtil.unquote(s); }



    // $ANTLR start jsonObject
    // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:46:1: jsonObject returns [JSONObject r] : o= object ;
    public final JSONObject jsonObject() throws RecognitionException {
        JSONObject r = null;

        JSONObject o = null;


        try {
            // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:47:2: (o= object )
            // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:47:4: o= object
            {
            pushFollow(FOLLOW_object_in_jsonObject80);
            o=object();
            _fsp--;

             r = o; 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return r;
    }
    // $ANTLR end jsonObject


    // $ANTLR start jsonArray
    // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:49:1: jsonArray returns [JSONArray r] : a= array ;
    public final JSONArray jsonArray() throws RecognitionException {
        JSONArray r = null;

        JSONArray a = null;


        try {
            // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:50:2: (a= array )
            // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:50:4: a= array
            {
            pushFollow(FOLLOW_array_in_jsonArray99);
            a=array();
            _fsp--;

             r = a; 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return r;
    }
    // $ANTLR end jsonArray


    // $ANTLR start object
    // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:54:1: object returns [JSONObject v] : ^( OBJECT ( objectElement )* ) ;
    public final JSONObject object() throws RecognitionException {
        ObjectLayer_stack.push(new ObjectLayer_scope());

        JSONObject v = null;

        
        	((ObjectLayer_scope)ObjectLayer_stack.peek()).elems = new ArrayList<JSONElement>();

        try {
            // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:59:2: ( ^( OBJECT ( objectElement )* ) )
            // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:59:4: ^( OBJECT ( objectElement )* )
            {
            match(input,OBJECT,FOLLOW_OBJECT_in_object130); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:59:13: ( objectElement )*
                loop1:
                do {
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0==ELEMENT) ) {
                        alt1=1;
                    }


                    switch (alt1) {
                	case 1 :
                	    // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:59:13: objectElement
                	    {
                	    pushFollow(FOLLOW_objectElement_in_object132);
                	    objectElement();
                	    _fsp--;


                	    }
                	    break;

                	default :
                	    break loop1;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }
             v = new JSONObject(((ObjectLayer_scope)ObjectLayer_stack.peek()).elems); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            ObjectLayer_stack.pop();

        }
        return v;
    }
    // $ANTLR end object


    // $ANTLR start objectElement
    // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:63:1: objectElement : ^( ELEMENT s= String v= value ) ;
    public final void objectElement() throws RecognitionException {
        CommonTree s=null;
        JSONValue v = null;


        try {
            // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:64:2: ( ^( ELEMENT s= String v= value ) )
            // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:64:4: ^( ELEMENT s= String v= value )
            {
            match(input,ELEMENT,FOLLOW_ELEMENT_in_objectElement152); 

            match(input, Token.DOWN, null); 
            s=(CommonTree)input.LT(1);
            match(input,String,FOLLOW_String_in_objectElement156); 
            pushFollow(FOLLOW_value_in_objectElement160);
            v=value();
            _fsp--;


            match(input, Token.UP, null); 
             ((ObjectLayer_scope)ObjectLayer_stack.peek()).elems.add(new JSONElement(unquote(s.getText()), v)); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end objectElement


    // $ANTLR start array
    // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:68:1: array returns [JSONArray v] : ^( ARRAY ( arrayElement )* ) ;
    public final JSONArray array() throws RecognitionException {
        ArrayLayer_stack.push(new ArrayLayer_scope());

        JSONArray v = null;

        
        	((ArrayLayer_scope)ArrayLayer_stack.peek()).elems = new ArrayList<JSONValue>();

        try {
            // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:73:2: ( ^( ARRAY ( arrayElement )* ) )
            // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:73:4: ^( ARRAY ( arrayElement )* )
            {
            match(input,ARRAY,FOLLOW_ARRAY_in_array195); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:73:12: ( arrayElement )*
                loop2:
                do {
                    int alt2=2;
                    int LA2_0 = input.LA(1);

                    if ( (LA2_0==OBJECT||(LA2_0>=ARRAY && LA2_0<=DOUBLE)||(LA2_0>=TRUE && LA2_0<=NULL)) ) {
                        alt2=1;
                    }


                    switch (alt2) {
                	case 1 :
                	    // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:73:12: arrayElement
                	    {
                	    pushFollow(FOLLOW_arrayElement_in_array197);
                	    arrayElement();
                	    _fsp--;


                	    }
                	    break;

                	default :
                	    break loop2;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }
             v = new JSONArray(((ArrayLayer_scope)ArrayLayer_stack.peek()).elems); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            ArrayLayer_stack.pop();

        }
        return v;
    }
    // $ANTLR end array


    // $ANTLR start arrayElement
    // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:77:1: arrayElement : v= value ;
    public final void arrayElement() throws RecognitionException {
        JSONValue v = null;


        try {
            // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:78:2: (v= value )
            // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:78:4: v= value
            {
            pushFollow(FOLLOW_value_in_arrayElement220);
            v=value();
            _fsp--;

             ((ArrayLayer_scope)ArrayLayer_stack.peek()).elems.add(v); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end arrayElement


    // $ANTLR start value
    // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:82:1: value returns [JSONValue v] : ( ^( STRING s= String ) | ^( INTEGER n= Integer ) | ^( DOUBLE n= Double ) | o= object | a= array | TRUE | FALSE | NULL );
    public final JSONValue value() throws RecognitionException {
        JSONValue v = null;

        CommonTree s=null;
        CommonTree n=null;
        JSONObject o = null;

        JSONArray a = null;


        try {
            // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:83:2: ( ^( STRING s= String ) | ^( INTEGER n= Integer ) | ^( DOUBLE n= Double ) | o= object | a= array | TRUE | FALSE | NULL )
            int alt3=8;
            switch ( input.LA(1) ) {
            case STRING:
                {
                alt3=1;
                }
                break;
            case INTEGER:
                {
                alt3=2;
                }
                break;
            case DOUBLE:
                {
                alt3=3;
                }
                break;
            case OBJECT:
                {
                alt3=4;
                }
                break;
            case ARRAY:
                {
                alt3=5;
                }
                break;
            case TRUE:
                {
                alt3=6;
                }
                break;
            case FALSE:
                {
                alt3=7;
                }
                break;
            case NULL:
                {
                alt3=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("82:1: value returns [JSONValue v] : ( ^( STRING s= String ) | ^( INTEGER n= Integer ) | ^( DOUBLE n= Double ) | o= object | a= array | TRUE | FALSE | NULL );", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:83:4: ^( STRING s= String )
                    {
                    match(input,STRING,FOLLOW_STRING_in_value240); 

                    match(input, Token.DOWN, null); 
                    s=(CommonTree)input.LT(1);
                    match(input,String,FOLLOW_String_in_value244); 

                    match(input, Token.UP, null); 
                     v = new JSONString(unquote(s.getText())); 

                    }
                    break;
                case 2 :
                    // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:84:4: ^( INTEGER n= Integer )
                    {
                    match(input,INTEGER,FOLLOW_INTEGER_in_value253); 

                    match(input, Token.DOWN, null); 
                    n=(CommonTree)input.LT(1);
                    match(input,Integer,FOLLOW_Integer_in_value257); 

                    match(input, Token.UP, null); 
                     v = new JSONInteger(n.getText()); 

                    }
                    break;
                case 3 :
                    // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:85:4: ^( DOUBLE n= Double )
                    {
                    match(input,DOUBLE,FOLLOW_DOUBLE_in_value266); 

                    match(input, Token.DOWN, null); 
                    n=(CommonTree)input.LT(1);
                    match(input,Double,FOLLOW_Double_in_value270); 

                    match(input, Token.UP, null); 
                     v = new JSONDouble(n.getText()); 

                    }
                    break;
                case 4 :
                    // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:86:4: o= object
                    {
                    pushFollow(FOLLOW_object_in_value280);
                    o=object();
                    _fsp--;

                     v = o; 

                    }
                    break;
                case 5 :
                    // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:87:4: a= array
                    {
                    pushFollow(FOLLOW_array_in_value289);
                    a=array();
                    _fsp--;

                     v = a; 

                    }
                    break;
                case 6 :
                    // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:88:4: TRUE
                    {
                    match(input,TRUE,FOLLOW_TRUE_in_value298); 
                     v = new JSONBoolean(true); 

                    }
                    break;
                case 7 :
                    // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:89:4: FALSE
                    {
                    match(input,FALSE,FOLLOW_FALSE_in_value305); 
                     v = new JSONBoolean(false); 

                    }
                    break;
                case 8 :
                    // D:\\cygwin\\home\\leo\\work\\eclipse\\workspace\\XerialJ\\xerial-core\\src\\main\\java\\org\\xerial\\json\\JSONWalker.g:90:4: NULL
                    {
                    match(input,NULL,FOLLOW_NULL_in_value312); 
                     v = new JSONNull(); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return v;
    }
    // $ANTLR end value


 

    public static final BitSet FOLLOW_object_in_jsonObject80 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_array_in_jsonArray99 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OBJECT_in_object130 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_objectElement_in_object132 = new BitSet(new long[]{0x0000000000000028L});
    public static final BitSet FOLLOW_ELEMENT_in_objectElement152 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_String_in_objectElement156 = new BitSet(new long[]{0x00000000000E03D0L});
    public static final BitSet FOLLOW_value_in_objectElement160 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ARRAY_in_array195 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_arrayElement_in_array197 = new BitSet(new long[]{0x00000000000E03D8L});
    public static final BitSet FOLLOW_value_in_arrayElement220 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_value240 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_String_in_value244 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_INTEGER_in_value253 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_Integer_in_value257 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DOUBLE_in_value266 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_Double_in_value270 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_object_in_value280 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_array_in_value289 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRUE_in_value298 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FALSE_in_value305 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_value312 = new BitSet(new long[]{0x0000000000000002L});

}