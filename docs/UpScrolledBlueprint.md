# UpScrolled Blueprint

## Project Structure
- **app/**: Jetpack Compose UI, MVVM viewmodels, repositories, and Firebase clients.
- **app/src/main/java/com/tagsnap/ui**: Screens and components for feeds, profiles, messages, and creation flows.
- **app/src/main/java/com/tagsnap/data**: Models and repositories for Firestore collections.
- **firebase/functions/**: Cloud Functions for scoring, moderation, rate limiting, and reputation updates.
- **firebase/**: Security rules, storage rules, and Firestore index definitions.

## Database Schema (Firestore Collections)
### posts
| Field | Type | Notes |
| --- | --- | --- |
| postId | string | Primary document ID |
| authorId | string | UID |
| type | string | text, image, video, audio, poll, carousel |
| text | string | Body/caption |
| mediaUrls | array | Media URLs for image/video/audio/carousel |
| pollOptions | array | Poll option labels |
| pollVotesMap | map | option -> count |
| topicId | string | Optional topic/community |
| engagementSecondsTotal | number | Accumulated dwell time |
| qualityScore | number | UpScrolled quality score |
| risingScore | number | Velocity-based score |
| controversialScore | number | High up+down mix |
| reportsCount | number | Safety signal |
| hidesCount | number | Safety signal |

### comments
| Field | Type | Notes |
| --- | --- | --- |
| commentId | string | Primary document ID |
| postId | string | Parent post |
| parentCommentId | string | For nested replies |
| text | string | Body |
| qualityScore | number | UpScrolled score |

### votes
| Field | Type | Notes |
| --- | --- | --- |
| voteId | string | `${targetId}_${voterId}` |
| targetType | string | post or comment |
| voteValue | number | -1 or 1 |
| voteWeight | number | Reputation-weighted |
| deviceHash | string | Brigading detection |
| isSuspicious | boolean | Shadow-ban or flagged |

### reputation_logs
| Field | Type | Notes |
| --- | --- | --- |
| logId | string | Primary document ID |
| userId | string | Affected user |
| delta | number | Reputation change |
| reason | string | upvote, share, report, spam |

### engagement_events
| Field | Type | Notes |
| --- | --- | --- |
| eventId | string | Primary document ID |
| userId | string | Viewer |
| targetId | string | Post/comment |
| dwellSeconds | number | Dwell time |
| scrollDepth | number | 0-1 |
| completionRate | number | 0-1 |
| rereads | number | Re-read count |

### moderation_queue
| Field | Type | Notes |
| --- | --- | --- |
| queueId | string | Primary document ID |
| targetType | string | post or comment |
| reporterId | string | User ID |
| reason | string | Safety report |
| status | string | open/triaged/resolved |

## API Endpoints (Functions + REST)
- `POST /auth/register` — email + password only.
- `POST /auth/login`
- `GET /feed?mode=top|new|rising|controversial`
- `POST /posts`
- `GET /posts/{id}`
- `POST /posts/{id}/vote`
- `POST /posts/{id}/save`
- `POST /posts/{id}/share`
- `POST /posts/{id}/report`
- `GET /posts/{id}/comments?sort=top|new|rising|controversial`
- `POST /comments`
- `POST /comments/{id}/vote`
- `POST /comments/{id}/report`
- `POST /engagement` — send dwell/scroll/completion events

## UpScrolled Engine — Scoring Formula
```
qualityScore = (voteSignal + engagementSignal + saveShareSignal + commentBoost - negativeSignal) * timeDecay

voteSignal = sum(voteValue * voterReputationWeight)
engagementSignal = ln(1 + dwellSeconds) + scrollDepth + completionRate + rereads
saveShareSignal = (saves * 2) + (shares * 3)
negativeSignal = (reports * 4) + (hides * 2)
commentBoost = sum(topCommentScores * 0.25)
timeDecay = 1 / (1 + (ageHours / 24)^1.35)
```

### Example Calculation
Given:
- upvotes=120 (avg weight 1.4)
- downvotes=20 (avg weight 1.1)
- dwell=9200s, scrollDepth=0.8, completion=0.9, rereads=1.2
- saves=30, shares=12, reports=2, hides=1, commentBoost=18, age=6h

```
voteSignal = (120*1.4) - (20*1.1) = 146
engagementSignal = ln(9201)=9.13 + 0.8 + 0.9 + 1.2 = 12.03
saveShareSignal = (30*2) + (12*3) = 96
negativeSignal = (2*4) + (1*2) = 10
raw = 146 + 12.03 + 96 + 18 - 10 = 262.03
timeDecay = 1 / (1 + (6/24)^1.35) = 0.86
qualityScore = 262.03 * 0.86 = 225.35
```

## Anti-Spam & Safety
- Rate limits on posting/voting by time window.
- Brigading detection via deviceHash + burst patterns.
- Shadow bans flag suspicious votes without notifying the user.
- Moderation queue for reports and automated flags.

## UI Components
- Home feed with Top/New/Rising/Controversial tabs.
- Discover with trending topics and quality signal callouts.
- Composer supporting text, image, video, audio, poll, carousel.
- Post detail with auto-pinned top comment and threaded replies.
- Profile with reputation, badges, and trust indicators.
- Settings with privacy, safety, and blueprint reference.
