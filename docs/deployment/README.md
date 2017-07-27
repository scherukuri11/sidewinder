This page documents key considerations to account for before deploying Sidewinder in production.

Sidewinder by default uses Direct Byte Buffer for data storage and heap for metadata + tag indexing. Therefore, larger heap sizes are not relevant for performance, on the contrary will reduce the total available memory capacity for your deployment. Due to this off-heap data storage, typical limitations of max Java heap and performance degrading beyond 30GB do not apply to Sidewinder's data storage components. Providing more RAM means more storage capacity on the server for timeseries data. Depending on whether DiskStorageEngine is used or MemStorageEngine, it's crucial to understand the differences.

As mentioned earlier, heap in Sidewinder is used for metadata, tag indexing and serving requests. Serving requests the prime cause of garbage creation in Sidewinder as it creates a lot of short lived objects.

## Sizing for production

### AWS Sizing
When running Sidewinder in AWS the following matrix for instance sizing can be used:
|Workload | Instance Type | Storage | Configuration |
|---------|---------------|---------|---------------|
|Small    |m4.large       |80GB EBS st1|-Xms2g -Xmx2g -XX:MaxDirectMemorySize=4g|
|Medium   |r4.2xlarge     |1TB EBS st1|-Xms8g -Xmx8g -XX:MaxDirectMemorySize=48g|
|Large    |x1             |10TB EBS st1|-Xms30g -Xmx30g|


## JVM Settings for production
For starters ```-Xms4g -Xmx4g``` or ```-Xms8g -Xmx8g``` are very good configurations for production deployments. You can increase these if you have substantial tag count, however having more than 4-5 tags per timeseries should be seriously thought upfront since tags have "serial" performance implications for both writes and reads.

```
-Xms8g -Xmx8g -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:MaxDirectMemorySize=10G
```

```
Note: Please adjust MaxDirectMemorySize depending on the available RAM on the server.
```
