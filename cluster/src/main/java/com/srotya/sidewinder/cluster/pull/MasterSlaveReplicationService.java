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
package com.srotya.sidewinder.cluster.pull;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ListenableFuture;
import com.srotya.sidewinder.cluster.rpc.DBMetadata;
import com.srotya.sidewinder.cluster.rpc.DBMetadataRequest;
import com.srotya.sidewinder.cluster.rpc.DBMetadataResponse;
import com.srotya.sidewinder.cluster.rpc.MeasurementMetadata;
import com.srotya.sidewinder.cluster.rpc.MeasurementMetadataRequest;
import com.srotya.sidewinder.cluster.rpc.MeasurementMetadataRequest.Builder;
import com.srotya.sidewinder.cluster.rpc.MeasurementMetadataResponse;
import com.srotya.sidewinder.cluster.rpc.ReplicationServiceGrpc;
import com.srotya.sidewinder.cluster.rpc.ReplicationServiceGrpc.ReplicationServiceFutureStub;
import com.srotya.sidewinder.core.storage.StorageEngine;
import com.srotya.sidewinder.core.storage.mem.TimeSeries;

import io.dropwizard.lifecycle.Managed;
import io.grpc.CompressorRegistry;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * @author ambud
 */
public class MasterSlaveReplicationService implements Managed {

	private ReplicationServiceFutureStub stub;
	private StorageEngine engine;
	private ExecutorService executor;

	public void init(Map<String, String> conf, StorageEngine engine) {
		executor = Executors.newFixedThreadPool(2);
		this.engine = engine;
		ManagedChannel channel = ManagedChannelBuilder
				.forAddress(conf.getOrDefault("cluster.master.address", "localhost"),
						Integer.parseInt(conf.getOrDefault("cluster.grpc.port", "55021")))
				.compressorRegistry(CompressorRegistry.getDefaultInstance()).usePlaintext(true).build();
		stub = ReplicationServiceGrpc.newFutureStub(channel);
	}

	public void replicateMeasurementMetadata() throws Exception {
		Builder measurementMetadataRequest = MeasurementMetadataRequest.newBuilder()
				.setMessageId(System.currentTimeMillis());
		for (String db : engine.getDatabases()) {
			measurementMetadataRequest.addMetadata(DBMetadata.newBuilder().setDbName(db)
					.addAllMeasurement(engine.getAllMeasurementsForDb(db)).build());
		}
		ListenableFuture<MeasurementMetadataResponse> response = stub.fetchMeasurementMetadata(measurementMetadataRequest.build());
		response.addListener(()->{
			try {
				MeasurementMetadataResponse metadataResponse = response.get();
				for (MeasurementMetadata measurementMetadata : metadataResponse.getMetadataList()) {
//					engine.getOrCreateTimeSeries(dbName, measurementName, valueFieldName, tags, timeBucketSize, fp);
				}
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}, executor);
	}

	public void replicateDBMetadata() {
		ListenableFuture<DBMetadataResponse> fetchDBMetadataResponse = stub
				.fetchDBMetadata(DBMetadataRequest.newBuilder().setMessageId(System.currentTimeMillis()).build());
		fetchDBMetadataResponse.addListener(() -> {
			try {
				DBMetadataResponse metadata = fetchDBMetadataResponse.get();
				for (DBMetadata md : metadata.getMetadataList()) {
					engine.getOrCreateDatabase(md.getDbName(), md.getRetentionPolicy());
					for (String measurement : md.getMeasurementList()) {
						engine.getOrCreateMeasurement(md.getDbName(), measurement);
					}
				}
			} catch (InterruptedException | ExecutionException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}, executor);
	}

	@Override
	public void start() throws Exception {
	}

	@Override
	public void stop() throws Exception {
	}

}
