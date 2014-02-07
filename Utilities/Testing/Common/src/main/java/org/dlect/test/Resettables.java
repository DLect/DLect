/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;
import java.util.List;

/**
 *
 * @author lee
 */
public class Resettables {

    public static Resettable join(Resettable resettable, Resettable... resettables) {
        if (resettables == null || resettables.length == 0) {
            return resettable;
        } else {
            List<Resettable> toExpand = Lists.asList(resettable, resettables);
            return join(toExpand);
        }
    }

    public static Resettable join(List<Resettable> resettables) {
        if (resettables == null || resettables.isEmpty()) {
            return new NoOpResettable();
        } else if (resettables.size() == 1) {
            return resettables.get(0); // Save the extra calls.
        } else {
            Builder<Resettable> b = ImmutableList.builder();
            for (Resettable r : resettables) {
                if (r instanceof JoinedResettable) {
                    b.addAll(((JoinedResettable) r).copyOf);
                } else if (!(r instanceof NoOpResettable)) {
                    // Not a no-op resettable.
                    b.add(r);
                }
            }
            return new JoinedResettable(b.build());
        }
    }

    private static class JoinedResettable implements Resettable {

        private final ImmutableList<Resettable> copyOf;

        public JoinedResettable(ImmutableList<Resettable> copyOf) {
            this.copyOf = copyOf;
        }

        @Override
        @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
        public void reset() throws Exception {
            Throwable thrown = null;
            for (Resettable resettable : copyOf) {
                try {
                    resettable.reset();
                } catch (Throwable e) {
                    thrown = e;
                }
            }
            if (thrown != null) {
                if (thrown instanceof Exception) {
                    throw (Exception) thrown;
                } else if (thrown instanceof Error) {
                    throw (Error) thrown;
                } else {
                    throw new Exception(thrown);
                }
            }
        }
    }

    private Resettables() {
    }

    private static class NoOpResettable implements Resettable {

        public NoOpResettable() {
        }

        @Override
        public void reset() throws Exception {
            // No Op.
        }
    }
}
