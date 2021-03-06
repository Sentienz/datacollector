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
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.sentienz.sas.xpt.SASXportFileIterator;
import com.streamsets.pipeline.lib.parser.DataParser;
import com.streamsets.pipeline.lib.parser.DataParserException;
import com.streamsets.pipeline.lib.parser.DataParserFactory;
import com.streamsets.pipeline.lib.parser.Errors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SASXPTParseFactory extends DataParserFactory {

  private static final Logger LOG = LoggerFactory.getLogger(SASXPTParseFactory.class);
  public static final Set<Class<? extends Enum>> MODES = Collections.emptySet();
  public static final Map<String, Object> CONFIGS = new HashMap<>();

  public SASXPTParseFactory(Settings settings) {
    super(settings);
  }

  @Override
  public DataParser getParser(String id, InputStream is, String offset) throws DataParserException {
    SASXportFileIterator sasXportFileIterator = null;
    String charset = getSettings().getCharset().toString();
    try {
      sasXportFileIterator = new SASXportFileIterator(is, charset);
    } catch (Exception e) {
      LOG.error("Error has occured while initiating file reader", e);
    }
      return new SASXPTDataParser(sasXportFileIterator, getSettings().getContext(), id, offset);
  }

  @Override
  public DataParser getParser(String id, Reader reader, long offset) {
    throw new UnsupportedOperationException();
  }
}