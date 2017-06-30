/**
 * Copyright 2017 Ambud Sharma
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.srotya.sidewinder.cluster.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.srotya.sidewinder.cluster.push.routing.Node;
import com.srotya.sidewinder.cluster.push.routing.RoutingEngine;
import com.srotya.sidewinder.core.ingress.http.HTTPDataPointDecoder;
import com.srotya.sidewinder.core.rpc.Point;
import com.srotya.sidewinder.core.storage.RejectException;

/**
 * @author ambud
 *
 */
@Path("/http")
public class InfluxApi {

	private static final Logger logger = Logger.getLogger(InfluxApi.class.getName());
	private Meter meter;
	private RoutingEngine router;
	private int replicationFactor;

	public InfluxApi(RoutingEngine router, MetricRegistry registry, Map<String, String> conf) {
		this.router = router;
		meter = registry.meter("writes");
		this.replicationFactor = Integer.parseInt(conf.getOrDefault("cluster.replication.factor", "3"));
	}

	@POST
	@Consumes({ MediaType.TEXT_PLAIN })
	public void insertData(@QueryParam("db") String dbName, String payload) {
		if (payload == null) {
			throw new BadRequestException("Empty request no acceptable");
		}
		List<Point> dps = HTTPDataPointDecoder.pointsFromString(dbName, payload);
		if (dps.isEmpty()) {
			throw new BadRequestException("Empty request no acceptable");
		}
		meter.mark(dps.size());
		for (Point dp : dps) {
			List<Node> nodes = router.routeData(dp, replicationFactor);
			for (int i = 0; i < nodes.size(); i++) {
				Node node = nodes.get(i);
				try {
					node.getWriter().write(dp);
				} catch (IOException e) {
					if (e instanceof RejectException) {
						break;
					} else {
						logger.log(Level.SEVERE,
								"Failed to write data point to node:" + node.getAddress() + ":" + node.getPort(), e);
						break;
					}
				}
			}
		}
	}

}
