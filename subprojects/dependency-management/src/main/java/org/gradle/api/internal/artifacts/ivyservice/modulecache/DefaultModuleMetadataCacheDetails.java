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

import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.ModuleComponentRepository;
import org.gradle.internal.hash.HashCode;

public class DefaultModuleMetadataCacheDetails implements ModuleMetadataCache.ModuleMetadataDetails {
    private final ModuleComponentRepository repository;
    private final ModuleComponentIdentifier id;
    private final HashCode rulesHash;

    public DefaultModuleMetadataCacheDetails(ModuleComponentRepository repository, ModuleComponentIdentifier id, HashCode rulesHash) {
        this.repository = repository;
        this.id = id;
        this.rulesHash = rulesHash;
    }

    @Override
    public ModuleComponentRepository getRepository() {
        return repository;
    }

    @Override
    public ModuleComponentIdentifier getModuleComponentIdentifier() {
        return id;
    }

    @Override
    public HashCode getRulesHash() {
        return rulesHash;
    }
}
