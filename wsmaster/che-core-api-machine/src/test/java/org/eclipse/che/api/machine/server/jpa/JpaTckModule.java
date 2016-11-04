/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.api.machine.server.jpa;

import com.google.inject.TypeLiteral;
import com.google.inject.persist.jpa.JpaPersistModule;

import org.eclipse.che.account.spi.AccountImpl;
import org.eclipse.che.api.core.model.workspace.Workspace;
import org.eclipse.che.api.machine.server.model.impl.SnapshotImpl;
import org.eclipse.che.api.machine.server.recipe.RecipeImpl;
import org.eclipse.che.api.machine.server.spi.RecipeDao;
import org.eclipse.che.api.machine.server.spi.SnapshotDao;
import org.eclipse.che.commons.test.db.H2JpaCleaner;
import org.eclipse.che.commons.test.db.H2SchemaLoader;
import org.eclipse.che.commons.test.tck.TckModule;
import org.eclipse.che.commons.test.tck.TckResourcesCleaner;
import org.eclipse.che.commons.test.tck.repository.JpaTckRepository;
import org.eclipse.che.commons.test.tck.repository.TckRepository;
import org.eclipse.che.core.db.DBInitializer;
import org.eclipse.che.core.db.schema.SchemaInitializer;

/**
 * @author Anton Korneta
 */
public class JpaTckModule extends TckModule {

    @Override
    protected void configure() {
        install(new JpaPersistModule("main"));
        bind(DBInitializer.class).asEagerSingleton();
        bind(SchemaInitializer.class).toInstance(() -> new H2SchemaLoader().loadSilently());
        bind(TckResourcesCleaner.class).to(H2JpaCleaner.class);

        bind(new TypeLiteral<TckRepository<RecipeImpl>>() {}).toInstance(new JpaTckRepository<>(RecipeImpl.class));
        bind(new TypeLiteral<TckRepository<SnapshotImpl>>() {}).toInstance(new JpaTckRepository<>(SnapshotImpl.class));
        bind(new TypeLiteral<TckRepository<Workspace>>() {}).toInstance(new JpaTckRepository<>(TestWorkspaceEntity.class));
        bind(new TypeLiteral<TckRepository<AccountImpl>>() {}).toInstance(new JpaTckRepository<>(AccountImpl.class));

        bind(RecipeDao.class).to(JpaRecipeDao.class);
        bind(SnapshotDao.class).to(JpaSnapshotDao.class);
    }
}
