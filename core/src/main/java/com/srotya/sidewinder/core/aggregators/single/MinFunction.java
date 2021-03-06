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
package com.srotya.sidewinder.core.aggregators.single;

import java.util.Iterator;
import java.util.List;

import com.srotya.sidewinder.core.aggregators.SingleResultFunction;
import com.srotya.sidewinder.core.storage.DataPoint;

/**
 * @author ambud
 */
public class MinFunction extends SingleResultFunction {
	
	@Override
	protected void aggregateToSingle(List<DataPoint> dataPoints, DataPoint output) {
		if (output.isFp()) {
			double min = 0;
			for (Iterator<DataPoint> iterator = dataPoints.iterator(); iterator.hasNext();) {
				DataPoint dataPoint = iterator.next();
				if(dataPoint.getValue()<min) {
					min = dataPoint.getValue();
				}
			}
			output.setValue(min);
		} else {
			long min = 0;
			for (Iterator<DataPoint> iterator = dataPoints.iterator(); iterator.hasNext();) {
				DataPoint dataPoint = iterator.next();
				if(dataPoint.getValue()<min) {
					min = dataPoint.getLongValue();
				}
			}
			output.setLongValue(min);
		}
	}

}
