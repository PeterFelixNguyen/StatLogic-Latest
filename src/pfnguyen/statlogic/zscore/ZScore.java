/**
 * Copyright 2014 Peter "Felix" Nguyen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pfnguyen.statlogic.zscore;

import org.apache.commons.math3.distribution.NormalDistribution;

public class ZScore {
    private double xBar;
    private double populationMean;
    private double stdDev;
    private double zScore;
    private NormalDistribution normal = new NormalDistribution();

    public ZScore(double xBar, double populationMean, double stdDev) {
        this.xBar = xBar;
        this.populationMean = populationMean;
        this.stdDev = stdDev;
    }

    public double getZScore() {
        zScore = (xBar - populationMean) / stdDev;

        return zScore;
    }

    public double getProbability() {
        return normal.cumulativeProbability(zScore);
    }
}