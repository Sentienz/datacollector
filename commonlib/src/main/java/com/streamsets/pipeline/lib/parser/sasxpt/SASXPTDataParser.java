/*
 * Copyright 2017 StreamSets Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsets.pipeline.lib.parser.sasxpt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import com.sentienz.sas.xpt.SASXportFileIterator;
import com.sentienz.sas.xpt.XPTTypes.ReadStatVariable;
import com.streamsets.pipeline.api.Field;
import com.streamsets.pipeline.api.ProtoConfigurableEntity;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.lib.parser.AbstractDataParser;

public class SASXPTDataParser extends AbstractDataParser {

  private final ProtoConfigurableEntity.Context context;
  private SASXportFileIterator sasXportFileIterator;
  private List<Field> headers;
  private boolean isClosed;
  private String id;
  private int offset;
  private long recordCount;
  private boolean eof;
  long currentOffset;

  public SASXPTDataParser(SASXportFileIterator sasXportFileIterator, ProtoConfigurableEntity.Context context, String id,String offset) {
    this.sasXportFileIterator = sasXportFileIterator;
    this.context = context;
    this.id = id;
    this.offset = Integer.parseInt(offset);
    this.recordCount = sasXportFileIterator.getRowCount();
    seekOffset();
  }

  @Override
  public Record parse() throws IOException {
    Record record = null;
    if (eof == true) {
      return null;
    }

    if (isClosed) {
      throw new IOException("The parser is closed");
    }

    if (sasXportFileIterator.hasNext()) {
      record = updateRecordsWithHeader(record);
        return record;
    } else {
      eof = true;
        return null;
    }
  }

  @Override
  public String getOffset() {
    return eof ? String.valueOf(-1) : Long.toString(sasXportFileIterator.getOffset());
  }

  @Override
  public void close() throws IOException {
    isClosed = true;
  }

  private Record updateRecordsWithHeader(Record record) throws IOException {
    currentOffset = sasXportFileIterator.getOffset();
    record = context.createRecord(id + "::" + currentOffset);
    List<String> rows = sasXportFileIterator.next();
    if (rows == null) {
      eof = true;
      return null;
    }

    headers = new ArrayList<Field>();
    ReadStatVariable[] columnList = sasXportFileIterator.getMetaData().variables;

    for (ReadStatVariable col : columnList) {
      headers.add(Field.create(col.name));
    }

    LinkedHashMap<String, Field> listMap = new LinkedHashMap<>();
    for (int i = 0; i < columnList.length; i++) {
      Field header = (headers != null) ? headers.get(i) : null;
      String key = header.getValueAsString();
      listMap.put(key, Field.create(Field.Type.STRING, rows.get(i)));
    }
    record.set(Field.createListMap(listMap));
      return record;
  }

  private void seekOffset() {
    int count = 1;
    while (count < offset) {
      List<String> rows = sasXportFileIterator.next();
      count++;
    }
  }
}