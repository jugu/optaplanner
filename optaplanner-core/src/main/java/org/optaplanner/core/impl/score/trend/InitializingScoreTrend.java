/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.core.impl.score.trend;

import java.io.Serializable;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.impl.solution.Solution;

/**
 * Bounds the possible {@link Score}s for a {@link Solution} as more and more variables are initialized
 * (while the already initialized variables don't change).
 * @see InitializingScoreTrendLevel
 */
public class InitializingScoreTrend implements Serializable {

    public static InitializingScoreTrend parseTrend(String initializingScoreTrendString, int levelsSize) {
        String[] trendTokens = initializingScoreTrendString.split("/");
        boolean tokenIsSingle = trendTokens.length == 1;
        if (!tokenIsSingle && trendTokens.length != levelsSize) {
            throw new IllegalArgumentException("The initializingScoreTrendString (" + initializingScoreTrendString
                    + ") doesn't follow the correct pattern (" + buildTrendPattern(levelsSize) + "):"
                    + " the trendTokens length (" + trendTokens.length
                    + ") differs from the levelsSize (" + levelsSize + ")." );
        }
        InitializingScoreTrendLevel[] trendLevels = new InitializingScoreTrendLevel[levelsSize];
        for (int i = 0; i < levelsSize; i++) {
            trendLevels[i] = InitializingScoreTrendLevel.valueOf(trendTokens[tokenIsSingle ? 0 : i]);
        }
        return new InitializingScoreTrend(trendLevels);
    }

    protected static String buildTrendPattern(int levelsSize) {
        StringBuilder trendPattern = new StringBuilder(levelsSize * 4);
        boolean first = true;
        for (int i = 0; i < levelsSize; i++) {
            if (first) {
                first = false;
            } else {
                trendPattern.append("/");
            }
            trendPattern.append(InitializingScoreTrendLevel.ANY.name());
        }
        return trendPattern.toString();
    }

    // ************************************************************************
    // Fields, constructions, getters and setters
    // ************************************************************************

    private final InitializingScoreTrendLevel[] trendLevels;

    public InitializingScoreTrend(InitializingScoreTrendLevel[] trendLevels) {
        this.trendLevels = trendLevels;
    }

    public InitializingScoreTrendLevel[] getTrendLevels() {
        return trendLevels;
    }

    // ************************************************************************
    // Complex methods
    // ************************************************************************

    public int getLevelCount() {
        return trendLevels.length;
    }

    public boolean isOnlyUp() {
        for (InitializingScoreTrendLevel trendLevel : trendLevels) {
            if (trendLevel != InitializingScoreTrendLevel.ONLY_UP) {
                return false;
            }
        }
        return true;
    }

    public boolean isOnlyDown() {
        for (InitializingScoreTrendLevel trendLevel : trendLevels) {
            if (trendLevel != InitializingScoreTrendLevel.ONLY_DOWN) {
                return false;
            }
        }
        return true;
    }

}