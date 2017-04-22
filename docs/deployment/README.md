This page documents key considerations to account for before deploying Sidewinder in production.

Sidewinder by default uses Direct Byte Buffer for data storage and heap for metadata + tag indexing. Therefore, larger heap sizes are not relevant for performance, on the contrary will reduce the total available memory capacity for your deployment. Due to the off-heap data storage, typical limitations of max Java heap and performance degrading beyond 30GB do not apply to Sidewinder's data storage parts. Providing more RAM means more storage capacity on the server for timeseries data.

As mentioned earlier, heap in Sidewinder is used for metadata, tag indexing and serving requests. Serving requests the prime cause of garbage creation in Sidewinder as it creates a lot of short lived objects.

## Sizing for production
For starters ```-Xms4g -Xmx4g``` or ```-Xms8g -Xmx8g``` are very good configurations for production deployments. You can increase these if you have substantial tag count, however having more than 4-5 tags per timeseries should be seriously thought upfront since tags have "serial" performance implications for both writes and reads.

## Things to consider
