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
// TupleTest.java
// Since: 2010/03/11 8:53:29
//
// $URL$
// $Author$
//--------------------------------------
package org.xerial.lens.relation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TupleTest {

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    public static class TString implements TupleElement<TString> {

        public void accept(TupleVisitor<TString> visitor) {
        // TODO Auto-generated method stub

        }

        public TString castToNode() {
            // TODO Auto-generated method stub
            return null;
        }

        public Tuple<TString> castToTuple() {
            // TODO Auto-generated method stub
            return null;
        }

        public TupleElement<TString> get(TupleIndex index) {
            // TODO Auto-generated method stub
            return null;
        }

        public TupleElement<TString> get(int index) {
            // TODO Auto-generated method stub
            return null;
        }

        public TString getNode(TupleIndex index) {
            // TODO Auto-generated method stub
            return null;
        }

        public boolean isAtom() {
            // TODO Auto-generated method stub
            return false;
        }

        public boolean isTuple() {
            // TODO Auto-generated method stub
            return false;
        }

        public int size() {
            // TODO Auto-generated method stub
            return 0;
        }

    }

    @Test
    public void construct() throws Exception {

    }
}
