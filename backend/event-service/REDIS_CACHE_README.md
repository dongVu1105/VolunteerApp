# Redis Caching Implementation for Event Service

## Overview
Redis caching has been implemented for the Event Service to improve performance by reducing database queries for frequently accessed event data.

## Configuration

### Redis Connection
- **Host**: localhost
- **Port**: 6379
- **TTL (Time-to-Live)**: 10 minutes (600,000 ms)

### Application Configuration (application.yaml)
```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
  cache:
    type: redis
    redis:
      time-to-live: 600000
      cache-null-values: false
```

## Dependencies Added

The following dependencies have been added to `pom.xml`:

1. **spring-boot-starter-data-redis** - Redis integration
2. **spring-boot-starter-cache** - Spring Cache abstraction

## Cached Operations

### Cache Names and Keys

| Cache Name | Operation | Key | Description |
|------------|-----------|-----|-------------|
| `events` | findById | `eventId` | Caches individual event details |
| `events` | create | `result.id` | Stores newly created events |
| `events` | update | `request.id` | Updates cached event data |
| `events` | acceptEvent | `eventId` | Updates event approval status |
| `events` | delete | `eventId` | Removes event from cache |
| `event-status` | ableToPost | `eventId` | Caches event status (approved/pending) |

### Caching Strategies

#### @Cacheable - Read Operations
- **findById(String id)**: Retrieves event from cache if available, otherwise fetches from MongoDB and caches the result
- **ableToPost(String eventId)**: Caches event approval status

#### @CachePut - Write Operations
- **create()**: Adds newly created event to cache
- **update()**: Updates existing event in cache
- **acceptEvent()**: Updates event status in cache when admin approves

#### @CacheEvict - Delete Operations
- **delete()**: Removes event from cache when deleted

## Serialization

### Event Entity
- Made `Serializable` to support Redis storage
- Supports Java time types (Instant) through Jackson's JavaTimeModule

### JSON Serialization
- Uses `GenericJackson2JsonRedisSerializer` for complex object serialization
- Properly handles:
  - Instant timestamps
  - MongoDB ObjectId
  - Nested objects

## Redis Configuration Class

`RedisConfig.java` provides:
- Custom `RedisTemplate` bean with JSON serialization
- `CacheManager` with TTL configuration
- ObjectMapper configuration for Java 8 time types

## Starting Redis

Use the provided Docker Compose file:

```bash
docker-compose -f redis-compose.yml up -d
```

## Benefits

1. **Performance**: Reduced database queries for frequently accessed events
2. **Scalability**: Offloads read operations from MongoDB
3. **Consistency**: Cache automatically updates on create/update/delete operations
4. **TTL Management**: Automatic cache expiration after 10 minutes

## Cache Flow Examples

### Read Flow (findById)
1. Check Redis cache for event by ID
2. If found (cache hit), return cached data
3. If not found (cache miss), fetch from MongoDB
4. Store result in Redis and return

### Write Flow (update)
1. Update event in MongoDB
2. Update cache with new data
3. Return updated event

### Delete Flow
1. Delete event from MongoDB
2. Evict event from Redis cache
3. Return success

## Monitoring

To verify Redis is working, connect to Redis CLI:

```bash
docker exec -it my-redis redis-cli

# Check all keys
KEYS *

# Check specific event
GET events::eventId

# Monitor cache operations
MONITOR
```

## Notes

- Cache is not shared across multiple instances (for distributed caching, consider Redis Cluster)
- Pagination results are not cached (intentional to ensure fresh data)
- Event list operations fetch from MongoDB to ensure consistency
- Null values are not cached to prevent cache pollution
