package com.example.snapverse


data class Post(
    val postId: String = "",
    val userId: String = "",
    val postText: String = "",
    val userName: String = "",
    val userImageUrl: String? = null,
    val postImageUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    var likeCount: Int = 0,
    val commentCount: Int = 0,
    var isLiked: Boolean = false
)