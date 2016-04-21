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

import java.util.List;

import org.carbondata.core.util.ByteUtil;

/**
 * class that implements methods specific for dictionary data look up
 */
public class ColumnDictionaryInfo extends AbstractColumnDictionaryInfo {

  /**
   * minimum value of surrogate key, dictionary value key will start from count 1
   */
  private static final int MINIMUM_SURROGATE_KEY = 1;

  /**
   * index after members are sorted
   */
  private List<Integer> sortOrderIndex;

  /**
   * inverted index to retrieve the member
   */
  private List<Integer> sortReverseOrderIndex;

  /**
   * This method will find and return the surrogate key for a given dictionary value
   * Applicable scenario:
   * 1. Incremental data load : Dictionary will not be generated for existing values. For
   * that values have to be looked up in the existing dictionary cache.
   * 2. Filter scenarios where from value surrogate key has to be found.
   *
   * @param value dictionary value as byte array
   * @return if found returns key else 0
   */
  @Override public int getSurrogateKey(byte[] value) {
    return getSurrogateKeyFromDictionaryValue(value);
  }

  /**
   * This method will find and return the sort index for a given dictionary id.
   * Applicable scenarios:
   * 1. Used in case of order by queries when data sorting is required
   *
   * @param surrogateKey a unique ID for a dictionary value
   * @return if found returns key else 0
   */
  @Override public int getSortedIndex(int surrogateKey) {
    if (surrogateKey > sortReverseOrderIndex.size() || surrogateKey < MINIMUM_SURROGATE_KEY) {
      return -1;
    }
    // decrement surrogate key as surrogate key basically means the index in array list
    // because surrogate key starts from 1 and index of list from 0, so it needs to be
    // decremented by 1
    return sortReverseOrderIndex.get(surrogateKey - 1);
  }

  /**
   * This method will find and return the dictionary value from sorted index.
   * Applicable scenarios:
   * 1. Query final result preparation in case of order by queries:
   * While convert the final result which will
   * be surrogate key back to original dictionary values this method will be used
   *
   * @param sortedIndex sort index of dictionary value
   * @return value if found else null
   */
  @Override public String getDictionaryValueFromSortedIndex(int sortedIndex) {
    if (sortedIndex > sortReverseOrderIndex.size() || sortedIndex < MINIMUM_SURROGATE_KEY) {
      return null;
    }
    // decrement surrogate key as surrogate key basically means the index in array list
    // because surrogate key starts from 1, sort index will start form 1 and index
    // of list from 0, so it needs to be decremented by 1
    int surrogateKey = sortOrderIndex.get(sortedIndex - 1);
    return getDictionaryValueForKey(surrogateKey);
  }

  /**
   * This method will add a new dictionary chunk to existing list of dictionary chunks
   *
   * @param dictionaryChunk
   */
  @Override public void addDictionaryChunk(List<byte[]> dictionaryChunk) {
    dictionaryChunks.add(dictionaryChunk);
  }

  /**
   * This method will set the sort order index of a dictionary column.
   * Sort order index if the index of dictionary values after they are sorted.
   *
   * @param sortOrderIndex
   */
  @Override public void setSortOrderIndex(List<Integer> sortOrderIndex) {
    this.sortOrderIndex = convertSortIndexChunkArrayListToCopyOnWriteArrayList(sortOrderIndex);
  }

  /**
   * This method will set the sort reverse index of a dictionary column.
   * Sort reverse index is the index of dictionary values before they are sorted.
   *
   * @param sortReverseOrderIndex
   */
  @Override public void setSortReverseOrderIndex(List<Integer> sortReverseOrderIndex) {
    this.sortReverseOrderIndex =
        convertSortIndexChunkArrayListToCopyOnWriteArrayList(sortReverseOrderIndex);
  }

  /**
   * This method will apply binary search logic to find the surrogate key for the
   * given value
   *
   * @param key to be searched
   * @return
   */
  private int getSurrogateKeyFromDictionaryValue(byte[] key) {
    int low = 0;
    int high = sortOrderIndex.size() - 1;
    while (low <= high) {
      int mid = (low + high) >>> 1;
      int surrogateKey = sortOrderIndex.get(mid);
      byte[] dictionaryValue = getDictionaryBytesFromSurrogate(surrogateKey);
      int cmp = ByteUtil.UnsafeComparer.INSTANCE.compareTo(dictionaryValue, key);
      if (cmp < 0) {
        low = mid + 1;
      } else if (cmp > 0) {
        high = mid - 1;
      } else {
        return surrogateKey; // key found
      }
    }
    return 0;
  }
}
