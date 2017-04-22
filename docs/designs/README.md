# Designs

### Concepts
|  Component       |                                 Notes                              |
|:-----------------|:------------------------------------------------------------------|
|   Database       |        A named group of measurements                               |
| Measurement      |        A category, logically equivalent to a table                 |
|    Series        | A combination of tags and value field that uniquely identifies a time series|
| Value Field      |        Name of quantity to be measured                             |
|     Tag          |        A classifier to help multiplex measurements                 |
|Retention Policy  |        Time in hours before data is automatically deleted          |

### Internals

#### Storage
Time series data in Sidewinder is stored in buckets of time, where each bucket (explained in next section) which are unique for each series (explained above). The physical storage of data is a ```ByteBuffer``` where data is stored in a compressed format using one of the few compression algorithms implemented for time series data in Sidewinder.

#### Bucketed Storage
Time series data unlike regular data can be partitioned by time. The unit of the partition could be seconds, minutes or hours, each partition is referred in the case of Sidewinder as a bucket. All data for a given time duration is written to the same bucket. To further explain this, let's take an example of the a ```bucket size``` of 2 hours, this means data between ```12:00:00``` and ```13:59:59``` will be written in the same time series bucket. Once data older than the bounds of a bucket is received, a new bucket gets created.

Bucketed storage provides performance and efficiency while reading data by avoiding the need to fetch a read a large time segment of data even when only a small chunk is needed by the user's query. Additionally, Sidewinder uses advanced predicate pushdown hooks at the bucket level to reduce the overhead of object creation and filter data points at the lowest possible level. There are 2 types of predicates that can be pushed to the buckets while selecting data:

1. Time predicate
2. Value predicate

#### Storage Engines
Sidewinder uses the concept of Storage Engine to select what type of storage will be used for persisting data and metadata. Both storage engines use a multi-level map and tree structure to create a quick traversal path for reads and writes of data.

This path looks has the following hierarchy:
- Database (concurrenthashmap)
  - Measurement (concurrenthashmap)
    - Series (concurrentskiplistmap)
      - Buckets (concurrentskiplistmap)

There are two different storage engines available as of now:
1. MemStorageEngine (ephemeral)
2. DiskStorageEngine (persistent)

**MemStorageEngine**

This storage engine is purely ephemeral i.e. purely in-memory (RAM), as soon as the process is shutdown all data is lost. This engine should not be used for use cases and environments when data retention for prolonged period is required. The MemStorageEngine uses direct byte buffers to store the compressed time series data. It uses on-heap data structures to store the metadata about time series which includes database and measurement information, tag and value field information as well as time bucket information.

**DiskStorageEngine**

Sidewinder uses off-heap memory for storing data, even when it's persisting data to disk it uses operating system page caches to efficiently perform random writes to the different time series. Page cached persistent writes are also known as memory mapped writes. For Sidewinder both reads and writes are routed through page cache memory for persistent mode. Actual writes to disk for data points is done by the compression algorithm. Currently **only Byzantine compression supports disk based persistent** whereas others are for purely in-memory storage.

#### Indexing Tags
Tags are indexed in Sidewinder storage engines since they for the ```where``` clause for most queries. Tags can be fully or partially provided during query time to limit the amount of data that is fetched. Filters can be applied on tags which can contain boolean operators AND / OR to combine different types of filter clauses. Indexed tags are stored as maps that perform forward and reverse lookups for tag to hash and hash to tag for queries and enumeration of metadata like show all tags or series that contain a tag. Additionally, each tag also has an adjacency list that stores row keys for each tag key that assists with finding all series with partial tag information available i.e. if a time series has 5 tags that uniquely identify it however the user only inputs one and expects to get a result of all series that contain this tag then the adjacency list helps with the lookup.

Tag indices are stored as hash values while constructing a row key for a time series. Internally both of the above Storage Engines reference the Time Series by row keys each of while comprises of ```[valuefieldname]#[hash(tag1)]_[hash(tag2)]```. Tags are first sorted, then hashed and concatenated to construct a string which when combined with the value field name formulates the row key. Sorting of tags ensures consistency of the row key therefore regardless of how the tags are sent by the clients they always formulate the same row key.

When indexing is performed by the DiskStorageEngine then it just like the MemStorageEngine all indexing happens in memory however when the index changes because a new entry is detected then the deltas are written to disk.

Structure for tag storage:

- Tag Map (concurrenthashmap)
- Row Key Index (concurrenthashmap)
