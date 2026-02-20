package com.tagsnap.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BlueprintScreen(navController: NavController) {
    val sections = blueprintSections()
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("System Blueprint", style = MaterialTheme.typography.displaySmall)
            Text(
                "Full project structure, schema, endpoints, and scoring logic.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        items(sections) { section ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(section.title, style = MaterialTheme.typography.titleLarge)
                section.items.forEach { item ->
                    Text("• $item", style = MaterialTheme.typography.bodyMedium)
                }
                if (section.codeBlock.isNotBlank()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = section.codeBlock,
                            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace)
                        )
                    }
                }
            }
        }
    }
}

private fun blueprintSections(): List<BlueprintSection> {
    return listOf(
        BlueprintSection(
            title = "Project Structure",
            items = listOf(
                "app/ (Compose UI, MVVM, repositories, Firebase clients)",
                "firebase/functions (ranking + moderation Cloud Functions)",
                "firebase/firestore.rules + firestore.indexes.json (permissions + indexes)",
                "docs/UpScrolledBlueprint.md (full technical blueprint)"
            )
        ),
        BlueprintSection(
            title = "Database Schema",
            items = listOf(
                "posts: content, media, poll data, engagement counters, quality/rising/controversial scores",
                "comments: threaded replies, engagement counters, score fields, moderation flags",
                "votes: weighted by reputation, track deviceHash + suspicious flag",
                "reputation_logs: deltas for upvotes, saves, reports, spam signals",
                "engagement_events: dwell time, scroll depth, completion rate, re-reads",
                "moderation_queue: reports, auto-flags, review status"
            )
        ),
        BlueprintSection(
            title = "API Endpoints",
            items = listOf(
                "POST /auth/register, POST /auth/login",
                "GET /feed?mode=top|new|rising|controversial",
                "POST /posts, GET /posts/{id}",
                "POST /posts/{id}/vote, POST /posts/{id}/save, POST /posts/{id}/share",
                "POST /posts/{id}/report, POST /comments/{id}/report",
                "GET /posts/{id}/comments?sort=top|new|rising|controversial",
                "POST /comments, POST /comments/{id}/vote"
            )
        ),
        BlueprintSection(
            title = "UI Components",
            items = listOf(
                "Card-based feed items with score pills and ranking signals",
                "Threaded comments with collapse/expand + auto-pinned top comment",
                "Composer for text, image, video, audio, poll, carousel posts",
                "Reputation dashboard with badges + trust signals"
            )
        ),
        BlueprintSection(
            title = "Scoring Formula",
            items = listOf(
                "Votes weighted by voter reputation.",
                "Engagement: dwell time, scroll depth, completion rate, re-reads.",
                "Save/share boosts, report/hide penalties.",
                "Time decay keeps feed fresh while allowing evergreen content."
            ),
            codeBlock = """
qualityScore = (voteSignal + engagementSignal + saveShareSignal + commentBoost - negativeSignal) * timeDecay
voteSignal = sum(voteValue * voterReputationWeight)
engagementSignal = ln(1 + dwellSeconds) + scrollDepth + completionRate + rereads
saveShareSignal = (saves * 2) + (shares * 3)
negativeSignal = (reports * 4) + (hides * 2)
commentBoost = sum(topCommentScores * 0.25)
timeDecay = 1 / (1 + (ageHours / 24)^1.35)
            """.trimIndent()
        ),
        BlueprintSection(
            title = "Example Calculation",
            items = listOf(
                "upvotes=120 (avg weight 1.4), downvotes=20 (avg weight 1.1)",
                "dwell=9200s, scrollDepth=0.8, completion=0.9, rereads=1.2",
                "saves=30, shares=12, reports=2, hides=1, commentBoost=18, age=6h"
            ),
            codeBlock = """
voteSignal = (120*1.4) - (20*1.1) = 146
engagementSignal = ln(9201)=9.13 + 0.8 + 0.9 + 1.2 = 12.03
saveShareSignal = (30*2) + (12*3) = 96
negativeSignal = (2*4) + (1*2) = 10
raw = 146 + 12.03 + 96 + 18 - 10 = 262.03
timeDecay = 1 / (1 + (6/24)^1.35) = 0.86
qualityScore = 262.03 * 0.86 = 225.35
            """.trimIndent()
        )
    )
}

private data class BlueprintSection(
    val title: String,
    val items: List<String>,
    val codeBlock: String = ""
)
