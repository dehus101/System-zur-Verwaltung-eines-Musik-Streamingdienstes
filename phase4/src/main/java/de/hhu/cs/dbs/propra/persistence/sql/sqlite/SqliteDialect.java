package de.hhu.cs.dbs.propra.persistence.sql.sqlite;

import org.springframework.data.relational.core.dialect.AbstractDialect;
import org.springframework.data.relational.core.dialect.LimitClause;
import org.springframework.data.relational.core.dialect.LockClause;
import org.springframework.data.relational.core.sql.LockOptions;

public class SqliteDialect extends AbstractDialect {

    private static final LimitClause LIMIT_CLAUSE =
            new LimitClause() {

                @Override
                public String getLimit(long limit) {
                    return "LIMIT " + limit;
                }

                @Override
                public String getOffset(long offset) {
                    throw new UnsupportedOperationException("getOffset");
                }

                @Override
                public String getLimitOffset(long limit, long offset) {
                    return String.format("LIMIT %d OFFSET %d", limit, offset);
                }

                @Override
                public Position getClausePosition() {
                    return Position.AFTER_ORDER_BY;
                }
            };

    private static final LockClause LOCK_CLAUSE =
            new LockClause() {
                @Override
                public String getLock(LockOptions lockOptions) {
                    throw new UnsupportedOperationException("getLock");
                }

                @Override
                public Position getClausePosition() {
                    return Position.AFTER_ORDER_BY;
                }
            };

    @Override
    public LimitClause limit() {
        return LIMIT_CLAUSE;
    }

    @Override
    public LockClause lock() {
        return LOCK_CLAUSE;
    }
}
