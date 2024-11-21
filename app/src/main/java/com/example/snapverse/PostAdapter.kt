package com.example.snapverse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostAdapter(
    private val posts: List<Post>,
    private val onLikeClick: (Post) -> Unit,
    private val onCommentClick: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userImage: ImageView = view.findViewById(R.id.userImage)
        val username: TextView = view.findViewById(R.id.username)
        val postText: TextView = view.findViewById(R.id.postContent)
        val postImage: ImageView = view.findViewById(R.id.postImage)
        val timestamp: TextView = view.findViewById(R.id.timestamp)
        val likeButton: ImageView = view.findViewById(R.id.likeButton)
        val likeText: TextView = view.findViewById(R.id.likeText)
        val commentButton: ImageView = view.findViewById(R.id.commentButton)
        val commentText: TextView = view.findViewById(R.id.commentText)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        // Load user image from URL if available
        post.userImageUrl?.let { url ->
            Glide.with(holder.userImage.context)
                .load(url)
                .circleCrop()
                .into(holder.userImage)
        }

        // Set post text
        holder.postText.text = post.postText

        // Load post image if available
        post.postImageUrl?.let { url ->
            Glide.with(holder.postImage.context)
                .load(url)
                .centerCrop()
                .into(holder.postImage)
            holder.postImage.visibility = View.VISIBLE
        } ?: run {
            holder.postImage.visibility = View.GONE
        }

        // Set like and comment counts
        holder.likeText.text = "${post.likeCount} Likes"
        holder.commentText.text = "${post.commentCount} Comments"
        // Set username
        holder.username.text = post.userName
        // Like button click listener
        holder.likeButton.setOnClickListener {
            onLikeClick(post)
        }

        // Comment button click listener
        holder.commentButton.setOnClickListener {
            onCommentClick(post)
        }
    }

    override fun getItemCount(): Int = posts.size
}
