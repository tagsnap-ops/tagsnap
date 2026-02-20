# TagSnap — UpScrolled-style Quality Ranking System

This repo contains a full Android Studio project built with Kotlin + Jetpack Compose, MVVM + Repository, Coroutines/Flow, and Firebase (Auth, Firestore, Storage, Functions). It includes Cloud Functions and Firebase Security Rules.

## Scoring Model

### Formula (server-only)
```
qualityScore = (voteSignal + engagementSignal + saveShareSignal + commentBoost - negativeSignal) * timeDecay

voteSignal = upvotes - downvotes
engagementSignal = ln(1 + engagementSeconds)
saveShareSignal = (saves * 2) + (shares * 3)
negativeSignal = (reports * 4) + (hides * 2)
commentBoost = sum of top 3 comment quality scores

timeDecay = 1 / (1 + (ageHours / 24)^1.3)
```

### Example calculation
Given: upvotes=120, downvotes=18, saves=30, shares=15, reports=2, hides=1, engagementSeconds=9800, commentBoost=20, ageHours=6

```
voteSignal = 102
engagementSignal = ln(9801) = 9.19
saveShareSignal = (30*2) + (15*3) = 105
negativeSignal = (2*4) + (1*2) = 10
raw = 102 + 9.19 + 105 + 20 - 10 = 226.19

timeDecay = 1 / (1 + (6/24)^1.3) = 0.86
qualityScore = 226.19 * 0.86 = 194.52
```

### Rising velocity
```
risingScore = currentScore * 0.6 + velocity * 0.4
velocity = (currentScore - previousScore) / hoursDelta
```

### Controversial metric
```
controversialScore = totalVotes * (1 - |upvotes - downvotes| / totalVotes)
```

## Reputation System
```
voteWeight = 1.0 + (reputationPoints / 1000.0)
cap range: 1.0 to 3.0
```

## Anti-spam & Safety
- Rate limiting and brigading detection are enforced in Cloud Functions by monitoring votes by device/time window.
- Shadow banned users have votes marked suspicious and excluded from counts.
- Reports/hides are tracked and included in the scoring model.

## Firebase Collections
See `firestore.rules` for enforced permissions and index definitions in `firestore.indexes.json`.

## Project Setup

### 1) Firebase Project
1. Create a Firebase project.
2. Add Android app with package name `com.tagsnap`.
3. Download `google-services.json` and place it in `app/`.
4. Enable **Email/Password** authentication in Firebase Auth.
5. Create Firestore and Storage databases.

### 2) Deploy Functions and Rules
```
cd firebase/functions
npm install
cd ..
firebase deploy --only functions,firestore:rules,storage:rules,firestore:indexes
```

### 3) Run Android App
Open the project in Android Studio and press **Run**.

## Notes
- Firestore paging is implemented via a `PagingSource` backed by queries.
- Scores are written only by Cloud Functions (admin SDK bypasses rules).
- The Android client never computes final scores.

## Blueprint
See `docs/UpScrolledBlueprint.md` for project structure, database schema, endpoints, and UI component details.
