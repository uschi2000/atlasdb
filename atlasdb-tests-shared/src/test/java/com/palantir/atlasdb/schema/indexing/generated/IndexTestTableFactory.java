package com.palantir.atlasdb.schema.indexing.generated;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.palantir.atlasdb.schema.Namespace;
import com.palantir.atlasdb.table.generation.Triggers;
import com.palantir.atlasdb.transaction.api.Transaction;

public class IndexTestTableFactory {
    private final static Namespace defaultNamespace = Namespace.create("default");
    private final List<Function<? super Transaction, SharedTriggers>> sharedTriggers;
    private final Namespace namespace;

    public static IndexTestTableFactory of(List<Function<? super Transaction, SharedTriggers>> sharedTriggers, Namespace namespace) {
        return new IndexTestTableFactory(sharedTriggers, namespace);
    }

    public static IndexTestTableFactory of(List<Function<? super Transaction, SharedTriggers>> sharedTriggers) {
        return new IndexTestTableFactory(sharedTriggers, defaultNamespace);
    }

    private IndexTestTableFactory(List<Function<? super Transaction, SharedTriggers>> sharedTriggers, Namespace namespace) {
        this.sharedTriggers = sharedTriggers;
        this.namespace = namespace;
    }

    public static IndexTestTableFactory of(Namespace namespace) {
        return of(ImmutableList.<Function<? super Transaction, SharedTriggers>>of(), namespace);
    }

    public static IndexTestTableFactory of() {
        return of(ImmutableList.<Function<? super Transaction, SharedTriggers>>of(), defaultNamespace);
    }

    public DataTable getDataTable(Transaction t, DataTable.DataTrigger... triggers) {
        return DataTable.of(t, namespace, Triggers.getAllTriggers(t, sharedTriggers, triggers));
    }

    public TwoColumnsTable getTwoColumnsTable(Transaction t, TwoColumnsTable.TwoColumnsTrigger... triggers) {
        return TwoColumnsTable.of(t, namespace, Triggers.getAllTriggers(t, sharedTriggers, triggers));
    }

    public interface SharedTriggers extends
            DataTable.DataTrigger,
            TwoColumnsTable.TwoColumnsTrigger {
        /* empty */
    }

    public abstract static class NullSharedTriggers implements SharedTriggers {
        @Override
        public void putData(Multimap<DataTable.DataRow, ? extends DataTable.DataNamedColumnValue<?>> newRows) {
            // do nothing
        }

        @Override
        public void putTwoColumns(Multimap<TwoColumnsTable.TwoColumnsRow, ? extends TwoColumnsTable.TwoColumnsNamedColumnValue<?>> newRows) {
            // do nothing
        }
    }
}