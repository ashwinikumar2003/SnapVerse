package com.example.snapverse

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.cloudinary.Cloudinary
import com.cloudinary.android.BuildConfig
import com.cloudinary.utils.ObjectUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPostFragment : Fragment() {

    private lateinit var postContent: EditText
    private lateinit var selectImageButton: Button
    private lateinit var postButton: Button
    private lateinit var selectedImagePreview: ImageView
    private var selectedImageUri: Uri? = null
    // Enter your Cloudinary credentials here....
    private val cloudinary = Cloudinary("cloudinary://${BuildConfig.CLOUDINARY_API_KEY}:${BuildConfig.CLOUDINARY_API_SECRET}@${BuildConfig.CLOUDINARY_CLOUD_NAME}")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postContent = view.findViewById(R.id.postContent)
        selectImageButton = view.findViewById(R.id.selectImageButton)
        postButton = view.findViewById(R.id.postButton)
        selectedImagePreview = view.findViewById(R.id.selectedImagePreview)

        selectImageButton.setOnClickListener { selectImage() }
        postButton.setOnClickListener { uploadPost() }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            selectedImagePreview.setImageURI(selectedImageUri)
            selectedImagePreview.visibility = View.VISIBLE
        }
    }

    private fun uploadPost() {
        val content = postContent.text.toString()
        if (content.isBlank() && selectedImageUri == null) {
            Toast.makeText(context, "Post cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userName = FirebaseAuth.getInstance().currentUser?.displayName ?: "Anonymous"
        val databaseRef = FirebaseDatabase.getInstance().getReference("posts").push()
        val postId = databaseRef.key ?: return

        if (selectedImageUri != null) {
            uploadImageToCloudinary(selectedImageUri!!) { imageUrl ->
                savePostToDatabase(postId, content, userId, userName, imageUrl)
            }
        } else {
            savePostToDatabase(postId, content, userId, userName, null)
        }
    }

    private fun uploadImageToCloudinary(imageUri: Uri, onSuccess: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val inputStream = requireContext().contentResolver.openInputStream(imageUri)
                val result = cloudinary.uploader().upload(inputStream, ObjectUtils.emptyMap())
                val imageUrl = result["secure_url"] as String
                withContext(Dispatchers.Main) {
                    onSuccess(imageUrl)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun savePostToDatabase(postId: String, content: String, userId: String, userName: String, imageUrl: String?) {
        val post = Post(postId, userId, content, userName, null, imageUrl, System.currentTimeMillis())
        FirebaseDatabase.getInstance().getReference("posts").child(postId).setValue(post)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Post uploaded", Toast.LENGTH_SHORT).show()
                    activity?.onBackPressed()  // Go back to FeedFragment
                } else {
                    Toast.makeText(context, "Failed to upload post", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        const val IMAGE_PICK_CODE = 1000
    }
}
