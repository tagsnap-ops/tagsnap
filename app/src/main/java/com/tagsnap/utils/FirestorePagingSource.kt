package com.tagsnap.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FirestorePagingSource<T : Any>(
    private val query: Query,
    private val mapper: (Map<String, Any?>) -> T
) : PagingSource<QuerySnapshotKey, T>() {

    override suspend fun load(params: LoadParams<QuerySnapshotKey>): LoadResult<QuerySnapshotKey, T> {
        return try {
            val currentQuery = params.key?.let { key ->
                query.startAfter(key.snapshot)
            } ?: query
            val snapshot = currentQuery.limit(params.loadSize.toLong()).get().await()
            val items = snapshot.documents.mapNotNull { doc ->
                mapper(doc.data.orEmpty() + mapOf("id" to doc.id))
            }
            val nextKey = snapshot.documents.lastOrNull()?.let { QuerySnapshotKey(it) }
            LoadResult.Page(
                data = items,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshotKey, T>): QuerySnapshotKey? {
        return null
    }
}

class QuerySnapshotKey(val snapshot: com.google.firebase.firestore.DocumentSnapshot)
