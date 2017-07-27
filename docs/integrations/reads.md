## REST API
The REST API allows operations like metadata manipulations as well as queries to be performed on Sidewinder. Here's a
list of documented APIs:

|Path|Method|Description|
|----|:-----------|:----------|
|/databases|GET|Get a list of all databases|
|/databases|DELETE|Deletes all databases on the server|
|/databases/{dbname}|GET|List all measurements for this database|
|/databases/{dbname}|PUT|Create or update database with configuration|
|/databases/{dbname}|DELETE|Delete this database|
|/databases/{dbname}/check|GET|Checks if this database exists|
|/databases/{dbname}/measurements/{measurementName}|GET|List of all fields and tags for this measurement|
|/databases/{dbname}/measurements/{measurementName}|PUT|Create a measurement with this measurement name|
|/databases/{dbname}/measurements/{measurementName}|DELETE|Delete/drop this measurement and all timeseries in it|
|/databases/{dbname}/measurements/{measurementName}/check|GET|Checks if this measurement exists|
|/databases/{dbName}/measurements/{measurementName}/fields|GET|List all fields under this measurement|
|/databases/{dbName}/measurements/{measurementName}/fields/{value}|GET|Read all datapoints for this field for the supplied time range|
|/databases/{dbName}/measurements/{measurementName}/series|PUT|Create series with supplied series identifiers|

## Grafana (https://grafana.com/)
Grafana is an open source visualization software for time series data. Grafana supports the ability to create graphs, charts and dashboards.

As a part of the main Sidewinder project is also a Grafana Data Source for Sidewinder which can be downloaded to enable Grafana to interact with Sidewinder. This plugin is published as a zip and can be downloaded from Maven central.

Grafana plugin for Sidewinder can be found here: https://github.com/srotya/sidewinder-grafana
