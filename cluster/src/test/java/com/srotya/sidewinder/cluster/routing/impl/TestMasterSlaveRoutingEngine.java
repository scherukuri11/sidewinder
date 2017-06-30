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
package com.srotya.sidewinder.cluster.routing.impl;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.srotya.sidewinder.cluster.connectors.ConfigConnector;
import com.srotya.sidewinder.cluster.push.routing.Node;
import com.srotya.sidewinder.cluster.push.routing.RoutingEngine;
import com.srotya.sidewinder.cluster.push.routing.impl.MasterSlaveRoutingEngine;
import com.srotya.sidewinder.core.rpc.Point;
import com.srotya.sidewinder.core.storage.StorageEngine;
import com.srotya.sidewinder.core.storage.mem.MemStorageEngine;

/**
 * @author ambud
 */
public class TestMasterSlaveRoutingEngine {

	@Test
	public void testRouteDefault() throws Exception {
		Map<String, String> conf = new HashMap<>();
		StorageEngine engine = new MemStorageEngine();
		engine.configure(conf, null);

		ConfigConnector connector = new ConfigConnector();
		connector.init(conf, engine);

		RoutingEngine router = new MasterSlaveRoutingEngine();
		router.init(conf, connector);
		List<Node> nodes = router.routeData(Point.newBuilder().build(), 3);
		assertEquals(1, nodes.size());
	}

	@Test
	public void testRouteCustom() throws Exception {
		Map<String, String> conf = new HashMap<>();
		StorageEngine engine = new MemStorageEngine();
		engine.configure(conf, null);

		conf.put(ConfigConnector.CLUSTER_CC_SLAVES, "host1:2311, host2:3342");

		ConfigConnector connector = new ConfigConnector();
		connector.init(conf, engine);

		RoutingEngine router = new MasterSlaveRoutingEngine();
		router.init(conf, connector);
		List<Node> nodes = router.routeData(Point.newBuilder().build(), 3);
		assertEquals(3, nodes.size());
		assertEquals(3, new HashSet<>(nodes).size());
	}

}
