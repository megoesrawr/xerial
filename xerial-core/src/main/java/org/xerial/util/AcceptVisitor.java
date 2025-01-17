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
// Visitable.java
// Since: Apr 15, 2009 2:05:55 PM
//
// $URL$
// $Author$
//--------------------------------------
package org.xerial.util;

/**
 * Generic interface for objects that can accept {@link Visitor}
 * 
 * @author leo
 * 
 */
public interface AcceptVisitor<VisitorType extends Visitor<VisitorType, DataType>, DataType extends AcceptVisitor<VisitorType, DataType>>
{
    /**
     * Accept the visitor
     * 
     * @param visitor
     *            visitor
     */
    void accept(VisitorType visitor);
}
