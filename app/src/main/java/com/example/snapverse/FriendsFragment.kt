package com.example.snapverse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FriendsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sample data
        val friends = listOf(
            Friend("Homelander", R.drawable.img_1),
            Friend("Maeve", R.drawable.sv),
            Friend("Deep", R.drawable.img_1),
            Friend("Noir", R.drawable.sv),
            Friend("Starlight", R.drawable.img_1),
            Friend("A Train", R.drawable.sv),
            // Additional friends
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.friendsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = FriendsAdapter(friends)
    }
}
