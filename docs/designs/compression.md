# Compression

Sidewinder's performance can be attributed to the compression algorithms used by it for storing numerical timeseries data. Compression not only helps reduce the amount of stored but also helps improve performance for read, writes and complex analytics.

Timeseries data can be highly compressible as documented by the [Facebook Gorilla paper](http://www.vldb.org/pvldb/vol8/p1816-teller.pdf) ranging anywhere from 3-50x data compression making storage of large number of timeseries on a single machine practical.

Sidewinder comes with several compression schemes with varied level of complexity and performance.

### Compression Schemes
| Compression Name | Write Performance | Read Performance | Compression Ratio | Limitations |
|------------------|-------------------|------------------|-------------------|-------------|
|       None       |||| Memory |
|  Delta of Delta  |   | || Memory |
|      Gorilla     |       17.8M/s     |       28.5M/s    |   15.25GB / 450MB = 33.8x |Consistency issues|
|  Byzantine-Mem   |       39.3M/s     |       210M/s     |   15.25GB / 300MB = 50x |  None   |
|  Byzantine-Disk  |       36.7M/s     |       210M/s     |   15.25GB / 300MB = 50x |  None   |

The above tests were performed by writing a single timeseries with 100M data points (ts & value) via these compression schemes.

Notes:

* 100M data points = 100,000,000 * 16 bytes = 15.25GB

* Byzantine is now the default compression scheme in Sidewinder

* Above tests were performed on a late 2013 Macbook Pro

* These are single thread performance numbers

### Principals of Compression
The compression algorithms used by Sidewinder work primarily off the **delta of delta** compression concept as documented in the Facebook paper. Delta of Delta algorithm take the difference between 3 consecutive data points to arrive at the difference between the change in any two consecutive data points. Mathematically, this can be represented as follows:

```Let t1, t2, t3 be 3 consecutive data points then dod=(t3-t2) - (t2-t1); where dod=delta of delta```

Once delta of delta is computed, a variable length encoding scheme is used to write data point's deltas as bytes to the writers. Sidewinder compression algorithms perform these operations atomically therefore data corruption is not an issues. Another thing to note is that reads in Sidewinder do not block writes as, a variant of MVCC based mechanism is used to avoid the need to block writes while reads are happening. 
