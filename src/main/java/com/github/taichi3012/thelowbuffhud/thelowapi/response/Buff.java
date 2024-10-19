package com.github.taichi3012.thelowbuffhud.thelowapi.response;

import java.util.HashMap;

public class Buff extends HashMap<String, Buff.Value> {

  public static class Value {

    public double level;

    public long timeLimit;

  }

}
