## Deployment Tooling

While it is possible to deploy and manage Sidewinder manually using plain old SSH terminals; for today's cloud first
environments or large scale deployments one must consider deployment automation tools like Ambari, Ansible, Puppet, Chef, Docker etc.

Sidewinder comes with the following deployment automation tools:

| Tool Name | URL |
|-----------|:----|
| Ambari    |https://github.com/srotya/sidewinder-ambari-stack|
| Docker    |https://hub.docker.com/r/srotya/sidewinder/ |
| Manual    |http://sidewinder.srotya.com/docs/#/deployment/install |

More tools will be added over time.

## Performance & Capacity

Sidewinder by default uses Direct Byte Buffer for data storage and heap for metadata + tag indexing. Therefore, larger heap sizes are not relevant for performance, on the contrary will reduce the total available memory capacity for your deployment. Due to this off-heap data storage, typical limitations of max Java heap and performance degrading beyond 30GB do not apply to Sidewinder's data storage components. Providing more RAM means more storage capacity on the server for timeseries data. Depending on whether DiskStorageEngine is used or MemStorageEngine, it's crucial to understand the differences.

As mentioned earlier, heap in Sidewinder is used for metadata, tag indexing and serving requests. Serving requests the prime cause of garbage creation in Sidewinder as it creates a lot of short lived objects.

### AWS Sizing
When running Sidewinder in AWS the following matrix for instance sizing can be used:

|Workload | Instance Type | Storage | Configuration |
|---------|:---------------|:---------|:---------------|
|Small    |m4.large       | Nx80GB EBS st1 | -Xms2g -Xmx2g -XX:MaxDirectMemorySize=4g|
|Medium   |r4.2xlarge     | Nx1TB EBS st1| -Xms8g -Xmx8g -XX:MaxDirectMemorySize=48g|
|Large    |x1             | Nx10TB EBS st1 | -Xms30g -Xmx30g|

```
Note: Here 'N' is the number of drives based on storage needs
```

## JVM Settings for production
For starters ```-Xms4g -Xmx4g``` or ```-Xms8g -Xmx8g``` are very good configurations for production deployments. You can increase these if you have substantial tag count, however having more than 4-5 tags per timeseries should be seriously thought upfront since tags have "serial" performance implications for both writes and reads.

```
-Xms8g -Xmx8g -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:MaxDirectMemorySize=10G
```

```
Note: Please adjust MaxDirectMemorySize depending on the available RAM on the server.
```
