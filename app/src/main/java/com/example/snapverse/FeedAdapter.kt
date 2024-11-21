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
import java.text.DateFormat
import java.text.SimpleDateFormat

class FeedAdapter(
    private val posts: List<Post>,
    private val onLikeClick: (Post) -> Unit,
    private val onCommentClick: (Post) -> Unit
) : RecyclerView.Adapter<FeedAdapter.PostViewHolder>() {

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val postContent: TextView = view.findViewById(R.id.postContent)
        val postImage: ImageView = view.findViewById(R.id.postImage)
        val timestamp: TextView = view.findViewById(R.id.timestamp)
        val username: TextView = view.findViewById(R.id.username)
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
        holder.postContent.text = post.postText

        if (!post.postImageUrl.isNullOrEmpty()) {
            Glide.with(holder.postImage.context).load(post.postImageUrl).into(holder.postImage)
            holder.postImage.visibility = View.VISIBLE
        } else {
            holder.postImage.visibility = View.GONE
        }

        holder.likeText.text = "${post.likeCount} Likes"
        holder.likeButton.setOnClickListener {
            onLikeClick(post)
        }

        holder.timestamp.text = DateFormat.getDateTimeInstance().format(post.timestamp)

        holder.commentButton.setOnClickListener {
            onCommentClick(post)
        }
    }

    override fun getItemCount(): Int = posts.size
}
