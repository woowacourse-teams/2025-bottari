package com.bottari.push;

import java.util.List;

public interface MultiRunnableExecutor {

    void execute(final List<Runnable> actions);

    void executeAsync(final List<Runnable> actions);
}
