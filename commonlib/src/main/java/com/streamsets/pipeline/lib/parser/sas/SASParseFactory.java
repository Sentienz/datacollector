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
package com.streamsets.pipeline.lib.parser.sas;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.parso.SasFileReader;
import com.epam.parso.SasFileReader;
import com.epam.parso.impl.SasFileReaderImpl;
import com.streamsets.pipeline.lib.data.DataFactoryBuilder;
import com.streamsets.pipeline.lib.parser.DataParser;
import com.streamsets.pipeline.lib.parser.DataParserException;
import com.streamsets.pipeline.lib.parser.DataParserFactory;
import com.streamsets.pipeline.lib.parser.Errors;

public class SASParseFactory extends DataParserFactory {
  public static final Set<Class<? extends Enum>> MODES = Collections.emptySet();
  public static final Map<String, Object> CONFIGS = new HashMap<>();
  private SasFileReader sasFileReader;

  public SASParseFactory(Settings settings) {
    super(settings);
  }

  @Override
  public DataParser getParser(String id, InputStream is, String offset) throws DataParserException {
    try {
      String charset = getSettings().getCharset().toString();
      sasFileReader = new SasFileReaderImpl(is, charset);
        return new SASDataParser(sasFileReader, getSettings().getContext(), id, offset);
    } catch (IOException e) {
        throw new DataParserException(Errors.DATA_PARSER_01, e.toString(), e);
    }
  }

  @Override
  public DataParser getParser(String id, Reader reader, long offset) throws DataParserException {
    throw new UnsupportedOperationException();
  }

}