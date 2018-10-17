/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.internal.execution;

import javax.annotation.Nullable;

public enum ExecutionOutcome {
    NO_SOURCE,
    UP_TO_DATE,
    FROM_CACHE,
    EXECUTED,
    FAILED {
        @Override
        ExecutionResult asResult() {
            throw new UnsupportedOperationException();
        }
    };

    private final ExecutionResult result;

    ExecutionOutcome() {
        this.result = new ExecutionResult() {
            @Override
            public ExecutionOutcome getOutcome() {
                return ExecutionOutcome.this;
            }

            @Nullable
            @Override
            public Throwable getFailure() {
                return null;
            }
        };
    }

    ExecutionResult asResult() {
        return result;
    }
}
