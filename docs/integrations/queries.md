# Queries
This section documents how queries can be executed against Sidewinder to allows users to use APIs to pull data programmatically instead of Grafana or other visualization tools.

## APIs

## Time Series Query Language (TSQL) - [Not Released]
Sidewinder introduces a new simplified query language designed for querying time series data. The syntax is easy and to the point and uses symbols to express the concepts of query. Let's understand the detailed syntax of the language.

``
Note: TSQL is fairly basic and will add more complexity as it matures
``

TSQL follows the format of:

```
[start-timestamp]<[series filter]<[end-timestamp]=>[function definition]
```

Here's an example:
```
2000-12-10T10:10:10<cpu.value.host=2&vm=2<2020-12-10T10:10:10=>derivative,10,smean
```

In the above example one can see the two timestamps between them is the filter for selecting the metrics to be queried finally ```=>``` feeds the selected data to a function, followed by the arguments of the function.

### Series Filter
Series filter is a **.** separated expression:

```
[measurement name].[value field name].[tag filter expression]
```

The tag filter expression is an infix notation boolean expression, with tag name and boolean expression ```&``` i.e. AND, ```|``` i.e. OR. This combined together creates the tag filter that is used to

In the example below, we are selection ```measurement=cpu```, ```field=value``` where series has ```host=2 and vm=2```

```
cpu.value.host=2&vm=2
```

### Function Definition

Types of functions:
1. Single Result Function (single)
2. Windowed Function

  a. Reducing Windows Aggregator Function (redwinagg) 0 - time window, 1 - aggregator name|

**List of Supported Functions**

|Function Name|Description|Type|
|-------------|---------------------------------|-------|
|none|No function|null|
|sfirst|Get the first data point in the result|single|
|sfirst|Get the last data point in the result|single|
|smin|Get the single minimum value from the result data points|single|
|smin|Get the single maximum value from the result data points|single|
|ssum|Get the single sum of data points from the result|single|
|smean|Get the single average of data points from the result|single|
|sstddev|Get the single standard deviation from the result|single|
|derivative|Rate of change i.e. dvdt|redwinagg|
|mean|Get the mean for data points by window of time|redwinagg|
|first|Get the first data point for results by window of time|redwinagg|
|last|Get the last data point for results by window of time|redwinagg|
|min|Get the min data point for results by window of time|redwinagg|
|max|Get the max data point for results by window of time|redwinagg|
|integral|Get the integral value for results by window of time|redwinagg|



## Structured Query Language (SQL)
