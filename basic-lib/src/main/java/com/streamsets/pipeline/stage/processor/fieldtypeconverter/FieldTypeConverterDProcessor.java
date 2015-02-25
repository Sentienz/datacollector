/**
 * (c) 2014 StreamSets, Inc. All rights reserved. May not
 * be copied, modified, or distributed in whole or part without
 * written consent of StreamSets, Inc.
 */
package com.streamsets.pipeline.stage.processor.fieldtypeconverter;

import com.streamsets.pipeline.api.ComplexField;
import com.streamsets.pipeline.api.ConfigDef;
import com.streamsets.pipeline.api.ConfigDef.Type;
import com.streamsets.pipeline.api.ConfigGroups;
import com.streamsets.pipeline.api.GenerateResourceBundle;
import com.streamsets.pipeline.api.HideConfig;
import com.streamsets.pipeline.api.Processor;
import com.streamsets.pipeline.api.StageDef;
import com.streamsets.pipeline.configurablestage.DProcessor;
import java.util.List;

@GenerateResourceBundle
@StageDef(
    version="1.0.0",
    label="Field Converter",
    description = "Converts the data type of a field",
    icon="converter.svg"
)
@ConfigGroups(Groups.class)
@HideConfig(onErrorRecord = true)
public class FieldTypeConverterDProcessor extends DProcessor {

  @ConfigDef(
      required = false,
      type = Type.MODEL,
      defaultValue="",
      label = "",
      description = "",
      displayPosition = 10,
      group = "TYPE_CONVERSION"
  )
  @ComplexField
  public List<FieldTypeConverterConfig> fieldTypeConverterConfigs;

  @Override
  protected Processor createProcessor() {
    return new FieldTypeConverterProcessor((fieldTypeConverterConfigs));
  }
}
