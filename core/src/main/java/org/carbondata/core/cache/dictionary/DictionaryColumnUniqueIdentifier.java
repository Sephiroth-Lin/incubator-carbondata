/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.carbondata.core.cache.dictionary;

import org.carbondata.core.carbon.CarbonTableIdentifier;

/**
 * dictionary column identifier which includes table identifier and column identifier
 */
public class DictionaryColumnUniqueIdentifier {

  /**
   * table fully qualified name
   */
  private CarbonTableIdentifier carbonTableIdentifier;

  /**
   * unique column id
   */
  private String columnIdentifier;

  /**
   * @param carbonTableIdentifier
   * @param columnIdentifier
   */
  public DictionaryColumnUniqueIdentifier(CarbonTableIdentifier carbonTableIdentifier,
      String columnIdentifier) {
    this.carbonTableIdentifier = carbonTableIdentifier;
    this.columnIdentifier = columnIdentifier;
  }

  /**
   * @return table identifier
   */
  public CarbonTableIdentifier getCarbonTableIdentifier() {
    return carbonTableIdentifier;
  }

  /**
   * @return columnIdentifier
   */
  public String getColumnIdentifier() {
    return columnIdentifier;
  }

  /**
   * overridden equals method
   *
   * @param other
   * @return
   */
  @Override public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    DictionaryColumnUniqueIdentifier that = (DictionaryColumnUniqueIdentifier) other;
    if (!carbonTableIdentifier.equals(that.carbonTableIdentifier)) return false;
    return columnIdentifier.equals(that.columnIdentifier);

  }

  /**
   * overridden hashcode method
   *
   * @return
   */
  @Override public int hashCode() {
    int result = carbonTableIdentifier.hashCode();
    result = 31 * result + columnIdentifier.hashCode();
    return result;
  }
}
