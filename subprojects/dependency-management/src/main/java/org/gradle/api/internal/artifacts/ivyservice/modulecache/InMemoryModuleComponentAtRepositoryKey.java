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

package org.gradle.api.internal.artifacts.ivyservice.modulecache;

import com.google.common.base.Objects;
import org.gradle.internal.hash.HashCode;

public class InMemoryModuleComponentAtRepositoryKey extends ModuleComponentAtRepositoryKey {
    private final ModuleComponentAtRepositoryKey delegate;

    private final HashCode rulesHash;
    private final int hashCode;

    InMemoryModuleComponentAtRepositoryKey(ModuleComponentAtRepositoryKey key, HashCode rulesHash) {
        super(key.getRepositoryId(), key.getComponentId());
        this.delegate = key;
        this.rulesHash = rulesHash;
        this.hashCode = 31 * key.hashCode() + rulesHash.hashCode();
    }

    public ModuleComponentAtRepositoryKey getKey() {
        return delegate;
    }

    @Override
    public String toString() {
        return delegate.toString() + " rules hash " + rulesHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InMemoryModuleComponentAtRepositoryKey that = (InMemoryModuleComponentAtRepositoryKey) o;
        return hashCode == that.hashCode &&
            Objects.equal(delegate, that.delegate) &&
            Objects.equal(rulesHash, that.rulesHash);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

}
