package com.github.taichi3012.thelowbuffhud.thelowapi;

import com.github.taichi3012.thelowbuffhud.thelowapi.cache.BuffCache;
import com.github.taichi3012.thelowbuffhud.thelowapi.response.Buff;

public class CacheProvider {

  static BuffCache buffCache = new BuffCache(new Buff(), System.currentTimeMillis());

  public static BuffCache getBuffCache() {
    return buffCache;
  }

}
