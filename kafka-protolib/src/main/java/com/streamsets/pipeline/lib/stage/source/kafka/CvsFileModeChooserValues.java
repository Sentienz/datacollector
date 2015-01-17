/**
 * (c) 2014 StreamSets, Inc. All rights reserved. May not
 * be copied, modified, or distributed in whole or part without
 * written consent of StreamSets, Inc.
 */
package com.streamsets.pipeline.lib.stage.source.kafka;

import com.streamsets.pipeline.api.base.BaseEnumChooserValues;

public class CvsFileModeChooserValues extends BaseEnumChooserValues {

  public CvsFileModeChooserValues() {
    super(CsvFileMode.class);
  }

}
