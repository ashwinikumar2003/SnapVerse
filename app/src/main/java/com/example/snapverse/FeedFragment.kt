package com.example.snapverse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FeedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var feedAdapter: FeedAdapter
    private val posts = mutableListOf<Post>()
    val currentUserId: String? = FirebaseAuth.getInstance().currentUser?.uid
    private val postsRef = FirebaseDatabase.getInstance().getReference("posts")
    lateinit var likeButton: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout first
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView after the view is created
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize the FeedAdapter with required click listeners
        feedAdapter = FeedAdapter(posts,
            onLikeClick = { post -> toggleLike(post) },
            onCommentClick = { post -> openComments(post) }
        )
        recyclerView.adapter = feedAdapter

        // Initialize the FloatingActionButton and set navigation action
        val fabAddPost: FloatingActionButton = view.findViewById(R.id.fab_add_post)
        fabAddPost.setOnClickListener {
            loadNewPostFragment()
        }

        // Load posts from Firebase
        loadPosts()
    }

    private fun loadNewPostFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, NewPostFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun openComments(post: Post) {
        // Placeholder for comment opening functionality
    }

    private fun loadPosts() {
        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                posts.clear()
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)
                    post?.let {
                        posts.add(it)
                    }
                }
                posts.sortByDescending { it.timestamp }
                feedAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load posts.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun toggleLike(post: Post) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId == null) {
            // Handle case where the user is not logged in (e.g., show a message or redirect to login)
            return
        }

        val postRef = postsRef.child(post.postId).child("likes")
        postRef.child(currentUserId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    postRef.child(currentUserId).removeValue()
                    post.likeCount += 1
                } else {
                    // User has not liked this post yet, so add like
                    postRef.child(currentUserId).setValue(true)
                    post.likeCount += 1
                }
                feedAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if necessary
            }
        })
    }



//    private fun openComments(post: Post) {
//        val fragment = CommentsFragment.newInstance(post.postId)
//        fragmentManager?.beginTransaction()
//            ?.replace(R.id.fragmentContainer, fragment)
//            ?.addToBackStack(null)
//            ?.commit()
//    }
}

