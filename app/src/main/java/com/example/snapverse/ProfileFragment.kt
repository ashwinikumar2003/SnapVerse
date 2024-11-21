package com.example.snapverse

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sample user data
        view.findViewById<TextView>(R.id.usernameTextView).text = "John Doe"
        view.findViewById<TextView>(R.id.bioTextView).text = "Love photography, tech, and travel."
        view.findViewById<ImageView>(R.id.profileImageView).setImageResource(R.drawable.img_1)
        // Set followers/following/posts count
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("SnapVersePrefs", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
        // Navigate to LoginActivity
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }
}
