package com.streamsets.pipeline.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.streamsets.pipeline.api.ChooserValues;
import com.streamsets.pipeline.config.CharsetChooserValues.Filter;

public class SASCharsetChooserValues implements ChooserValues {
  private static final Logger LOG = LoggerFactory.getLogger(SASCharsetChooserValues.class);

  private static final List<String> ALL_CHARSETS;

  static {
    Set<String> set = new LinkedHashSet<>();
    set.add("UTF-8");
    set.add("windows-1252");

    Iterator<String> it = set.iterator();
    while (it.hasNext()) {
      String cs = it.next();
      if (!Charset.isSupported(cs)) {
        it.remove();
        LOG.warn("Charset '{}' is not supported", cs);
      }
    }
    ALL_CHARSETS = Collections.unmodifiableList(new ArrayList<>(set));
  }

  public SASCharsetChooserValues() {
    this(null);
  }

  protected interface Filter {
    boolean accept(Charset charset);
  }

  private List<String> charsets;

  // to enable subclasses to produce subsets of charsets base on some criteria
  protected SASCharsetChooserValues(Filter filter) {
    if (filter == null) {
      charsets = ALL_CHARSETS;
    } else {
      charsets = new ArrayList<>(ALL_CHARSETS.size());
      for (String name : ALL_CHARSETS) {
        if (filter.accept(Charset.forName(name))) {
          charsets.add(name);
        }
      }
    }
  }

  @Override
  public String getResourceBundle() {
    return null;
  }

  @Override
  public List<String> getValues() {
    return charsets;
  }

  @Override
  public List<String> getLabels() {
    return charsets;
  }

}
