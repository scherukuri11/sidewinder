# Basics
Before we can get started understanding how can Sidewinder benefit and solve your metrics use case, we should understand a few concepts about time series databases (TSDB). This page is intended to provide foundational understand about TSDBs in the form of FAQs.

### What's Time Series data?
A series of data points (numeric) indexed by time. Time series data can sometimes also be referred to as metrics.

### Isn't log data Time Series?
While log data is indexed by time the data is textual in nature for the most part. Any log datasets that are purely numeric in can be classified as time series datasets.

### What's a TSDB?
TSDB or a Time Series DataBase is a type of DB specifically designed to efficiently store, process and retrieve time series data.

### Why use a TSDB?
Time series data is usually produced at a phenomenally high velocity and volume, specially in the space of IoT and APM. A regular transactional database is unable to keep up with the ingress rates expected when your millions of devices produce data in real-time. Additionally, IoT data follows the principal of **"use it or Loose it"** therefore creating the need for databases that can store this data at wire speed.

There is a plethora of databases out in the wild, a fair chunk of them are NoSQL databases however, there's only a very small subset of databases that are designed around the temporal concepts. These databases can achieve a high ingestion and query rate by using specialized algorithms that are applicable to time series data.

### Can't I use indexing systems to store Time Series?
Indexing systems work great for log data, specially if you have large quantities of it. While you can store time series datasets in your indexing systems (Solr/Elasticsearch) it's not a recommended approach. Why? because indexing is a much more complicated and computationally intensive process compared to a simple database write. Also, indexing was designed to solve **needle in the haystack** problem whereas time series datasets require repeated querying of the same set of data points for the purposes of visualization and analytics.
