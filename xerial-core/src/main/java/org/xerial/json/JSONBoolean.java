/*--------------------------------------------------------------------------
 *  Copyright 2007 Taro L. Saito
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
// XerialJ Project
//
// JSONBoolean.java
// Since: Apr 4, 2007
//
// $URL: http://dev.utgenome.org/svn/utgb/trunk/common/src/org/utgenome/json/JSONBoolean.java $ 
// $Author: leo $
//--------------------------------------
package org.xerial.json;

public class JSONBoolean extends JSONValueBase {
	
	private boolean _value;
	
	public JSONBoolean(boolean b)
	{
		this._value = b; 
	}
	
	public JSONBoolean(String text)
    {
	    if(text.equals("true"))
	        this._value = true;
	    else
	        this._value = false;
    }

    public boolean getValue()
	{
		return _value;
	}
	
	public String toString() 
	{
		return _value ? "true" : "false"; 
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof JSONBoolean))
			return false;
		
		return getValue() == ((JSONBoolean) o).getValue();
	}
	
	@Override
	public JSONBoolean getJSONBoolean() {
		return this;
	}

    public JSONValueType getValueType()
    {
        return JSONValueType.Boolean;
    }
}




