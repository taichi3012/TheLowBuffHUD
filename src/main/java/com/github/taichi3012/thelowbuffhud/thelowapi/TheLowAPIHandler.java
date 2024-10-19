package com.github.taichi3012.thelowbuffhud.thelowapi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.github.taichi3012.thelowbuffhud.thelowapi.cache.BuffCache;
import com.github.taichi3012.thelowbuffhud.thelowapi.response.Buff;

public class TheLowAPIHandler {

  public static void processResponse(String json) {
    Gson gson = new Gson();
    ResponseCommonFormat format = gson.fromJson(json, ResponseCommonFormat.class);

    if (format.version != 1 || !"buff".equals(format.apiType))
      return;

    CacheProvider.buffCache = new BuffCache(gson.fromJson(format.response, Buff.class), System.currentTimeMillis());
  }

  private static class ResponseCommonFormat {

    String apiType;

    int version;

    JsonObject response;

  }

}
