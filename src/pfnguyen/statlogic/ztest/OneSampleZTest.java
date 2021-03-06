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
package pfnguyen.statlogic.ztest;

import java.util.ArrayList;

import org.apache.commons.math3.distribution.NormalDistribution;

import pfnguyen.statlogic.options.CalculatorOptions.Hypothesis;

public class OneSampleZTest {
    private NormalDistribution normal = new NormalDistribution();
    private double testStatistic;
    private Double criticalRegion;
    private Hypothesis hypothesis;
    private Double lowerRegion;
    private Double upperRegion;
    private String nullHypothesis;
    private String altHypothesis;
    private double xBar;
    private int n;
    private double sigma;
    private double alpha;
    private String conclusion = "Test statistic is within the critical region \n"
            + "Reject the null hypothesis";
    private boolean rejectNull;
    private double calculatedMean;

    /**
     * Constructs OneSampleZTest with an ArrayList of values.
     * 
     * @param  hAlternative  the inequality for alternative hypothesis
     * @param  testValue     the value for the null hypothesis
     * @param  x             the data sample
     * @param  significance  the significance level
     */
    public OneSampleZTest(Hypothesis hAlternative, double testValue,
            ArrayList<Double> x, double stdDev, double significance) {
        getCriticalRegion(hAlternative, significance);
        calcTestStatistic(hAlternative, calcSampleMean(x),
                testValue, stdDev, x);
        constructStrings(this.hypothesis = hAlternative, testValue,
                calculatedMean, stdDev, x.size(), significance);
        rejectNull = testHypothesis();
    }

    /**
     * Constructs OneSampleZTest with precalculated values for
     * sample mean, sample standard deviation, and sample size.
     * 
     * @param  hAlternative  the inequality for alternative hypothesis
     * @param  testValue     the value for the null hypothesis
     * @param  xBar          the sample mean
     * @param  stdDev  the sample standard deviation
     * @param  n             the sample size
     * @param  significance  the significance level
     */
    public OneSampleZTest(Hypothesis hAlternative, double testValue,
            double xBar, double stdDev,
            int n, double significance) {
        getCriticalRegion(hAlternative, significance);
        calcTestStatistic(hAlternative, xBar, testValue, stdDev, n);
        constructStrings(this.hypothesis = hAlternative, testValue, xBar,
                stdDev, n, significance);
        rejectNull = testHypothesis();
    }

    /**
     * Constructs Strings for alternative hypothesis, sample mean, sample size,
     * sample standard deviation, and significance level.
     * 
     * @param  hAlternative  the inequality for alternative hypothesis
     * @param  testValue     the value for the null hypothesis
     * @param  xBar          the sample mean
     * @param  stdDev  the sample standard deviation
     * @param  n             the sample size
     * @param  significance  the significance level
     */
    private void constructStrings(Hypothesis hAlternative,
            double testValue, double xBar,
            double stdDev, int n, double significance) {
        String inequality = "";

        if (hAlternative == Hypothesis.LESS_THAN)
            inequality = "< ";
        else if (hAlternative == Hypothesis.GREATER_THAN)
            inequality = "> ";
        else if (hAlternative == Hypothesis.NOT_EQUAL)
            inequality = "!= ";
        nullHypothesis = "H0: mu " + "= " + testValue;
        altHypothesis = "H1: mu " + inequality + testValue;
        this.xBar = xBar;
        this.n = n;
        this.sigma = stdDev;
        this.alpha = significance;
    }

    /**
     * Calculates the critical region.
     * 
     * @param hAlternative  the inequality for alternative hypothesis
     * @param significance  the significance level
     */
    private void getCriticalRegion(Hypothesis hAlternative,
            double significance) {
        if (hAlternative == Hypothesis.NOT_EQUAL) {
            lowerRegion = normal.inverseCumulativeProbability(significance / 2);
            upperRegion = normal.inverseCumulativeProbability(1 - (significance / 2));
        }
        else if (hAlternative == Hypothesis.LESS_THAN) {
            criticalRegion = normal.inverseCumulativeProbability(significance);
        }
        else if (hAlternative == Hypothesis.GREATER_THAN) {
            criticalRegion = normal.inverseCumulativeProbability(significance);
        }
        else {
            System.out.println("No hypothesis/inequality selected");
        }
    }

    /**
     * Check if test statistic is within the critical region
     * to determine whether or not to reject the null hypothesis.
     * 
     * @return  true if null hypothesis is rejected, otherwise false
     */
    private boolean testHypothesis() {
        if (hypothesis == Hypothesis.LESS_THAN
                && testStatistic < criticalRegion) {
            return true;
        }
        else if (hypothesis == Hypothesis.GREATER_THAN
                && testStatistic > criticalRegion) {
            return true;
        }
        else if (hypothesis == Hypothesis.NOT_EQUAL
                && (testStatistic < lowerRegion ||
                        testStatistic > upperRegion)) {
            return true;
        }
        else
            conclusion = "Test statistic is NOT within the critical region \n"
                    + "Do NOT reject the null hypothesis";
        return false;
    }

    /**
     * Calculates the sample mean.
     *
     * @param  x  the data sample
     * @return    the sample mean
     */
    private double calcSampleMean(ArrayList<Double> x) {
        double sum = 0;

        for (int i = 0; i < x.size(); i++) {
            sum += x.get(i);
        }

        calculatedMean = sum / x.size();

        return calculatedMean;
    }

    /**
     * Calculates the test statistic if x is calculated from an ArrayList.
     *
     * @param  hAlternative    the inequality for alternative hypothesis
     * @param  sampleMean      the sample mean
     * @param  populationMean  the population mean
     * @param  stdDev          the standard deviation
     * @param  x               the data sample
     */
    private void calcTestStatistic(Hypothesis hAlternative,
            double sampleMean, double populationMean,
            double stdDev, ArrayList<Double> x) {

        testStatistic = (sampleMean - populationMean) /
                (stdDev / Math.sqrt(x.size()));
    }

    /**
     * Calculates the test statistic if x is provided by the user.
     * 
     * @param  hAlternative    the inequality for alternative hypothesis
     * @param  sampleMean      the sample mean
     * @param  populationMean  the population mean
     * @param  stdDev          the standard deviation
     * @param  x               the data sample
     */
    private void calcTestStatistic(Hypothesis hAlternative,
            double sampleMean, double populationMean,
            double sampleStdDev, int n) {

        testStatistic = (sampleMean - populationMean) /
                (sampleStdDev / Math.sqrt(n));
    }

    /**
     * @return  the null hypothesis
     */
    public String getNullHypothesis() {
        return nullHypothesis;
    }

    /**
     * @return  the alt hypothesis
     */
    public String getAltHypothesis() {
        return altHypothesis;
    }

    /**
     * @return  the sample mean
     */
    public double getXBar() {
        return xBar;
    }

    /**
     * @return  the sample size
     */
    public int getN() {
        return n;
    }

    /**
     * @return  the standard deviation
     */
    public double getSigma() {
        return sigma;
    }

    /**
     * @return  the significance level
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * @return  the test statistic
     */
    public double getTestStatistics() {
        return testStatistic;
    }

    /**
     * @return  the String representation of the critical region(s)
     */
    public String getCriticalRegionAsString() {
        if (hypothesis == Hypothesis.NOT_EQUAL)
            return lowerRegion.toString() + " and " + upperRegion.toString();
        else
            return criticalRegion.toString();
    }

    /**
     * @return  the String representation of the test's conclusion
     */
    public String getConclusionAsString() {
        return conclusion;
    }

    /**
     * @return  true if null hypothesis is rejected, false otherwise
     */
    public boolean getConclusion() {
        return rejectNull;
    }
}
