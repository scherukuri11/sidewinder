# Basics

Sidewinder is an in-memory timeseries database designed for speed and scalability. It's purpose is to provide storage, retrieval and analysis for time series data generated over the last few days.

Time series data, specially then ones used for monitoring software systems loses it's relevance over time. More importantly data for the last 24, 48 and 60 hours is deemed most import for monitoring needs since those are the usual outage resolution boundaries. Additional these windows are usually what frequently get dashboarded as well so fast access to these time windows is a critical performance requirement for a time series database used for monitoring.

### Concepts
|  Component       |                                 Notes                              |
|:-----------------|:------------------------------------------------------------------|
|   Database       |        A named group of measurements                               |
| Measurement      |        A category, logically equivalent to a table                 |
|    Series        | A combination of tags and value field that uniquely identifies a time series|
| Value Field      |        Name of quantity to be measured                             |
|     Tag          |        A classifier to help multiplex measurements                 |
|Retention Policy  |        Time in hours before data is automatically deleted          |

### Capabilities

#### Tags
Sidewinder supports use of tags to identify time series. Tags are strings, are internally indexed and are always sorted before being indexed. Their indexed versions when concatenated together with the value field name form the row key that identifies a given time series. Time series can be looked up via tags search using index and supported for complex filter conditions is provided.

#### High Dimensionality
Sidewinder supports the concept of measurements and each measurement can be further branched by the value field i.e. quantity being measured and tags. E.g. cpu is a measurement then value field is usr, sys etc. and tags scope it to a given hostname / ip address.

#### Retention Policy
Retention policies are extremely critical for Sidewinder, since it's an in-memory database therefore can't retain data for ever. Retention policy helps to automatically reclaim memory by deleting old data points. Retention policies can be applied at all levels i.e. database, measurement and series. Retention policy measured in hours and is converted to an equivalent time bucket count.

#### Time Buckets
An individual time series is broken into buckets where each bucket is a Gorilla compressed byte buffer. The default bucket size in Sidewinder is 4096 seconds i.e. 1.13 hours. This is the least unit of data that will be accessed each time data is queried. Time range based filtering is performed using predicates, which automatically narrow down the range of data the predicates need to be evaluated against.

#### Function Support
Without any analytical capabilities Sidewinder won't be of much use. Sidewinder supports a set of timeseries mathematical functions that include common operations like derivatives, sum, mean, std dev etc.

#### Archival
While being a purely in-memory system, Sidewinder does allow archival of timeseries data to persistent media for backup and long term retention needs. Archival backend is externally pluggable with current support for None, Disk, HDFS and S3. Archived data can be restored to a Sidewinder instance whose retention policies are correctly set.
