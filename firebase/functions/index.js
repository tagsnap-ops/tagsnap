const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

const db = admin.firestore();

const SCORE_CONFIG = {
  reportWeight: 4,
  hideWeight: 2,
  saveWeight: 2,
  shareWeight: 3,
  decayHalfLifeHours: 24,
};

function voteWeight(reputationPoints) {
  const raw = 1.0 + reputationPoints / 1000.0;
  return Math.min(3.0, Math.max(1.0, raw));
}

function timeDecay(ageHours) {
  return 1.0 / (1.0 + Math.pow(ageHours / 24.0, 1.3));
}

function controversialScore(upvotes, downvotes) {
  const total = upvotes + downvotes;
  if (total === 0) return 0;
  const balance = 1.0 - Math.abs(upvotes - downvotes) / total;
  return total * balance;
}

function qualityScore({
  upvotes,
  downvotes,
  saves,
  shares,
  reports,
  hides,
  engagementSeconds,
  commentBoost,
  ageHours,
}) {
  const voteSignal = upvotes - downvotes;
  const engagementSignal = Math.log(1 + Math.max(0, engagementSeconds));
  const saveShareSignal = saves * SCORE_CONFIG.saveWeight + shares * SCORE_CONFIG.shareWeight;
  const negativeSignal = reports * SCORE_CONFIG.reportWeight + hides * SCORE_CONFIG.hideWeight;
  const decay = timeDecay(ageHours);
  return (voteSignal + engagementSignal + saveShareSignal + commentBoost - negativeSignal) * decay;
}

async function detectSuspiciousVote(targetId, deviceHash) {
  if (!deviceHash) return false;
  const tenMinutesAgo = admin.firestore.Timestamp.fromMillis(Date.now() - 10 * 60 * 1000);
  const snapshot = await db
    .collection("votes")
    .where("targetId", "==", targetId)
    .where("deviceHash", "==", deviceHash)
    .where("createdAt", ">", tenMinutesAgo)
    .get();
  return snapshot.size >= 5;
}

async function recalcTargetScores({ targetType, targetId, commentBoost = 0 }) {
  const collection = targetType === "COMMENT" ? "comments" : "posts";
  const ref = db.collection(collection).doc(targetId);
  const snapshot = await ref.get();
  if (!snapshot.exists) return;

  const data = snapshot.data();
  const now = admin.firestore.Timestamp.now();
  const lastUpdate = data.lastScoreUpdate || data.createdAt || now;
  const ageHours = Math.max(0, (now.toMillis() - data.createdAt.toMillis()) / 3600000);
  const hoursDelta = Math.max(0.1, (now.toMillis() - lastUpdate.toMillis()) / 3600000);
  const currentScore = qualityScore({
    upvotes: data.upvotesCount || 0,
    downvotes: data.downvotesCount || 0,
    saves: data.savesCount || 0,
    shares: data.sharesCount || 0,
    reports: data.reportsCount || 0,
    hides: data.hidesCount || 0,
    engagementSeconds: data.engagementSecondsTotal || 0,
    commentBoost: commentBoost || data.commentBoost || 0,
    ageHours,
  });

  const previousScore = data.qualityScore || 0;
  const velocity = (currentScore - previousScore) / hoursDelta;
  const risingScore = currentScore * 0.6 + velocity * 0.4;
  const controversy = controversialScore(data.upvotesCount || 0, data.downvotesCount || 0);

  await ref.update({
    qualityScore: currentScore,
    risingScore,
    controversialScore: controversy,
    lastScoreUpdate: now,
    commentBoost: commentBoost || data.commentBoost || 0,
  });
}

exports.onVoteWrite = functions.firestore
  .document("votes/{voteId}")
  .onWrite(async (change, context) => {
    const after = change.after.exists ? change.after.data() : null;
    const before = change.before.exists ? change.before.data() : null;

    if (!after) return;

    const userSnap = await db.collection("users").doc(after.voterId).get();
    const userData = userSnap.data() || {};
    const weight = voteWeight(userData.reputationPoints || 0);
    const suspicious = await detectSuspiciousVote(after.targetId, after.deviceHash);
    const isShadowBanned = userData.isShadowBanned === true;

    await change.after.ref.update({
      voteWeight: weight,
      isSuspicious: suspicious || isShadowBanned,
    });

    const deltaUp = (after.voteValue === 1 ? 1 : 0) - (before?.voteValue === 1 ? 1 : 0);
    const deltaDown = (after.voteValue === -1 ? 1 : 0) - (before?.voteValue === -1 ? 1 : 0);

    if (suspicious || isShadowBanned) {
      return;
    }

    const collection = after.targetType === "COMMENT" ? "comments" : "posts";
    const targetRef = db.collection(collection).doc(after.targetId);
    await targetRef.update({
      upvotesCount: admin.firestore.FieldValue.increment(deltaUp),
      downvotesCount: admin.firestore.FieldValue.increment(deltaDown),
    });

    if (after.targetType === "COMMENT") {
      const commentSnap = await targetRef.get();
      const commentData = commentSnap.data() || {};
      await recalcTargetScores({
        targetType: "COMMENT",
        targetId: after.targetId,
        commentBoost: 0,
      });
      if (commentData.postId) {
        const topCommentsSnap = await db
          .collection("comments")
          .where("postId", "==", commentData.postId)
          .orderBy("qualityScore", "desc")
          .limit(3)
          .get();
        const commentBoost = topCommentsSnap.docs.reduce((sum, doc) => sum + (doc.data().qualityScore || 0), 0);
        await recalcTargetScores({
          targetType: "POST",
          targetId: commentData.postId,
          commentBoost,
        });
        const pinned = topCommentsSnap.docs[0]?.id || null;
        await db.collection("posts").doc(commentData.postId).update({ pinnedCommentId: pinned });
      }
    } else {
      await recalcTargetScores({ targetType: "POST", targetId: after.targetId });
    }
  });

exports.onEngagementWrite = functions.firestore
  .document("{collectionId}/{docId}")
  .onWrite(async (change, context) => {
    const { collectionId } = context.params;
    const supported = ["saves", "shares", "reports", "hides"];
    if (!supported.includes(collectionId)) return;
    const after = change.after.exists ? change.after.data() : null;
    if (!after) return;
    const targetType = after.targetType || "POST";
    const targetId = after.postId || after.targetId;

    const collection = targetType === "COMMENT" ? "comments" : "posts";
    const ref = db.collection(collection).doc(targetId);
    const fieldMap = {
      saves: "savesCount",
      shares: "sharesCount",
      reports: "reportsCount",
      hides: "hidesCount",
    };
    const field = fieldMap[collectionId];
    if (!field) return;

    await ref.update({
      [field]: admin.firestore.FieldValue.increment(1),
    });

    await recalcTargetScores({ targetType: targetType.toUpperCase(), targetId });
  });

exports.applyDecay = functions.pubsub
  .schedule("every 15 minutes")
  .onRun(async () => {
    const now = admin.firestore.Timestamp.now();
    const postsSnap = await db.collection("posts").get();
    const commentsSnap = await db.collection("comments").get();

    const batch = db.batch();
    for (const doc of postsSnap.docs) {
      const data = doc.data();
      const ageHours = Math.max(0, (now.toMillis() - data.createdAt.toMillis()) / 3600000);
      const decay = timeDecay(ageHours);
      const updatedScore = (data.qualityScore || 0) * decay;
      batch.update(doc.ref, {
        qualityScore: updatedScore,
        risingScore: updatedScore * 0.6,
        lastScoreUpdate: now,
      });
    }
    for (const doc of commentsSnap.docs) {
      const data = doc.data();
      const ageHours = Math.max(0, (now.toMillis() - data.createdAt.toMillis()) / 3600000);
      const decay = timeDecay(ageHours);
      const updatedScore = (data.qualityScore || 0) * decay;
      batch.update(doc.ref, {
        qualityScore: updatedScore,
        risingScore: updatedScore * 0.6,
        lastScoreUpdate: now,
      });
    }
    await batch.commit();
  });
