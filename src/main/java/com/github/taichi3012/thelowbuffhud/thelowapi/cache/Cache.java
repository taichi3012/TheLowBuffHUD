package com.github.taichi3012.thelowbuffhud.thelowapi.cache;

public abstract class Cache {

  public final long received;

  protected Cache(long received) {
    this.received = received;
  }

}
