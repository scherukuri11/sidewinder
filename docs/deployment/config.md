# Configure 
This page documents different configuration options for Sidewinder and also familiarize users with different configuration files Sidewinder needs to operate.

## Files
Sidewinder is configured using 2 configuration files:
1. Dropwizard YAML
2. Core Configuration File

## Dropwizard YAML
Sidewinder is a Dropwizard application, where Dropwizard provides the Jetty container where most of the read services including queries operate from. This file allows users to configure items like REST API and admin ports, service threads, logging etc. More details on Dropwizard configuration flags can be found here: http://www.dropwizard.io/1.0.5/docs/manual/configuration.html

In addition to the standard Dropwizard configuration, this file also points to the Core Configuration File using ```configPath``` property

## Core Configuration File
This file is the actual configuration file for almost everything in Sidewinder. It's a simple Java Properties file that follows ```key=value``` based configuration definition.

### Storage Engine Configuration

|Property                |Description                        |Default|Values|
|------------------------|-----------------------------------|-------|------|
|storage.engine  |Storage engine used for actual disk storage by Sidewinder|com.srotya.sidewinder.core.storage.mem.MemStorageEngine|com.srotya.sidewinder.core.storage.disk.DiskStorageEngine, com.srotya.sidewinder.core.storage.mem.MemStorageEngine|
|mem.compression.class|Compression algorithms when using Memory Storage Engine|com.srotya.sidewinder.core.storage.compression.byzantine.ByzantineWriter|com.srotya.sidewinder.core.storage.compression.dod.DodWriter, com.srotya.sidewinder.core.storage.compression.byzantine.ByzantineWriter, com.srotya.sidewinder.core.storage.compression.gorilla.GorillaWriter|
|disk.compression.class|Compression algorithms when using Disk Storage Engine|com.srotya.sidewinder.core.storage.compression.byzantine.ByzantineWriter|com.srotya.sidewinder.core.storage.compression.byzantine.ByzantineWriter|
|data.dir|Directory of series metadata and data|/tmp/sidewinder/metadata||
|index.dir|Directory of index data|/tmp/sidewinder/index||
|default.series.retention.hours |Number of hours time series are retained before collecting garbage|28||

### Server Settings
