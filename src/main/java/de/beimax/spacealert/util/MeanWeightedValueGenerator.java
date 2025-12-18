/**
 * This file is part of the JSpaceAlertMissionGenerator software.
 * Copyright (C) 2011 Maximilian Kalus
 * See http://www.beimax.de/ and https://github.com/mkalus/JSpaceAlertMissionGenerator
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package de.beimax.spacealert.util;

import java.util.Random;

// import java.util.Random;
public class MeanWeightedValueGenerator {

    private final double gaussianWeight; // 0 = uniform, 1 = gaussian
    private final double stdDev;  // standard deviationaround the 0.5 mean beteen min (0) and max (1) values
    private final Random random;

    /**
     * @param gaussianWeight 0 = uniform, 1 = gaussian, values in between blend
     * the two.
     * @param stdDev standard deviation around the 0.5 mean beteen min (0) and
     * max (1) values
     * @param seed the seed of the internal Random generator, or null to pick
     * one at random
     */
    public MeanWeightedValueGenerator(double gaussianWeight, double stdDev, Long seed) {
        if (gaussianWeight < 0 || gaussianWeight > 1) {
            throw new IllegalArgumentException("gaussianWeight must be between 0 and 1");
        }
        if (stdDev < 0) {
            throw new IllegalArgumentException("stdDev must be positive");
        }
        this.gaussianWeight = gaussianWeight;
        this.stdDev = stdDev;
        if (seed == null) {
            this.random = new Random();
        } else {
            this.random = new Random((long) seed);
        }
    }

    /**
     * Generate a random integer between min and max (inclusive). Distribution
     * blends uniform and Gaussian based on gaussianWeight.
     *
     * @param min Minimum value (inclusive)
     * @param max Maximum value (inclusive)
     * @return Random integer
     */
    public int nextInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        if (min == max) {
            return min;
        }

        double value;
        if (gaussianWeight == 0 || random.nextDouble() >= gaussianWeight) {
            // Uniform distribution
            value = random.nextDouble();
        } else {
            // Gaussian distribution centered at 0.5
            do {
                value = nextGaussian(0.5, stdDev / 100.0);
            } while (value < 0 || value > 1);
        }

        // Scale the 0–1 value to the integer range
        // Use floor to avoid underrepresenting min/max
        return (int) Math.floor(value * (max - min + 1)) + min;
    }

    /**
     * Standard Gaussian generator (mean=0, stdDev=1) using Box-Muller
     * transform. Transformed to desired mean and standard deviation.
     */
    private double nextGaussian(double mean, double stdev) {
        double u = 1.0 - random.nextDouble(); // (0,1]
        double v = random.nextDouble();
        double z = Math.sqrt(-2.0 * Math.log(u)) * Math.cos(2.0 * Math.PI * v);
        return z * stdev + mean;
    }
}
