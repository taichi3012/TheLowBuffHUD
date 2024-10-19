package com.github.taichi3012.thelowbuffhud.thelowapi.cache;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import com.github.taichi3012.thelowbuffhud.thelowapi.response.Buff;

public class BuffCache extends Cache {

  public final Map<String, Value> effects;

  public final boolean empty;

  public BuffCache(Buff buff, long received) {
    super(received);
    ImmutableMap.Builder<String, Value> builder = ImmutableMap.builder();
    buff.forEach((k, v) ->
      builder.put(k, new Value(v, this.received))
    );
    this.effects = builder.build();
    this.empty = this.effects.isEmpty();
  }

  public static class Value {

    public final double level;

    public final long end;

    public Value(Buff.Value response, long received) {
      this.level = response.level;
      this.end = received + response.timeLimit;
    }

    public long getDuration() {
      return end - System.currentTimeMillis();
    }

  }

}
