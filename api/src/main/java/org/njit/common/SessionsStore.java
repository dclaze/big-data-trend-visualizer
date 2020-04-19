package org.njit.common;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class SessionsStore {
    private static final String SESSIONS_TABLE_NAME = System.getenv("SESSIONS_TABLE");

    private static final Logger logger = LogManager.getLogger(SessionsStore.class);

    private final DynamoDBMapper mapper;

    public SessionsStore() {
        final DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(SESSIONS_TABLE_NAME))
                .build();
        final DynamoDBAdapter dbAdapter = DynamoDBAdapter.getInstance();

        this.mapper = dbAdapter.createDbMapper(mapperConfig);

        logger.debug(String.format("Initialized session store with table %s", SESSIONS_TABLE_NAME));
    }

    public Session get(final String id) throws Exception {
        final Session session = this.mapper.load(Session.class, id);

        if (session == null) {
            throw new Exception(String.format("Session not found for session id %s", id));
        }

        return session;
    }

    public void save(final Session product) {
        this.mapper.save(product);
    }

    public Boolean delete(final String id) throws Exception {
        final Session session = get(id);
        if (session != null) {
            this.mapper.delete(session);

            return true;
        }

        return false;
    }
}
