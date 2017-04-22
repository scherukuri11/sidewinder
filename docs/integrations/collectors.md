Sidewinder supports 2 varieties of plugin support that includes integration with Collectd and Telegraf

## Direct Integration
#### Collectd (https://github.com/srotya/sidewinder-collectd)
Collectd is a popular metrics collection daemon for Linux systems and there is a native Collectd output plugin to forward metrics directly to Sidewinder. This plugin leverages the Sidewinder HTTP protocol instead of the binary protocol and supports request micro-batching.

#### InfluxDB Telegraf (https://github.com/influxdata/telegraf)
Telegraf is InfluxDB's native collection agent and due to the similarity between Influx and Sidewinder HTTP protcol, you can leverage telegraf to forward metrics to Sidewinder.

## Indirect Integration
#### StatsD Collectd integration (https://collectd.org/wiki/index.php/Plugin:StatsD)
