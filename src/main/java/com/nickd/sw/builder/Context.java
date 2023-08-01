package com.nickd.sw.builder;

import javax.annotation.Nonnull;

public interface Context {
    Context getParent();

    @Nonnull
    String getName();

    boolean isRoot();
}
