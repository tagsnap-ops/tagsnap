package com.tagsnap.domain

import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

object Scoring {
    fun voteWeight(reputationPoints: Int): Double {
        val raw = 1.0 + (reputationPoints / 1000.0)
        return raw.coerceIn(1.0, 3.0)
    }

    fun qualityScore(
        upvotes: Int,
        downvotes: Int,
        saves: Int,
        shares: Int,
        reports: Int,
        hides: Int,
        engagementSeconds: Long,
        commentBoost: Double,
        ageHours: Double
    ): Double {
        val voteSignal = (upvotes - downvotes).toDouble()
        val engagementSignal = ln(1 + engagementSeconds)
        val saveShareSignal = (saves * 2 + shares * 3).toDouble()
        val negativeSignal = (reports * 4 + hides * 2).toDouble()
        val decay = timeDecay(ageHours)
        return (voteSignal + engagementSignal + saveShareSignal + commentBoost - negativeSignal) * decay
    }

    fun risingScore(currentScore: Double, previousScore: Double, hoursDelta: Double): Double {
        val velocity = (currentScore - previousScore) / max(hoursDelta, 0.1)
        return currentScore * 0.6 + velocity * 0.4
    }

    fun controversialScore(upvotes: Int, downvotes: Int): Double {
        val total = upvotes + downvotes
        if (total == 0) return 0.0
        val balance = 1.0 - (abs(upvotes - downvotes).toDouble() / total)
        return total * balance
    }

    fun timeDecay(ageHours: Double): Double {
        return (1.0 / (1.0 + (ageHours / 24.0).pow(1.3)))
    }

    fun exampleCalculation(): Double {
        return qualityScore(
            upvotes = 120,
            downvotes = 18,
            saves = 30,
            shares = 15,
            reports = 2,
            hides = 1,
            engagementSeconds = 9800,
            commentBoost = 20.0,
            ageHours = 6.0
        )
    }
}
