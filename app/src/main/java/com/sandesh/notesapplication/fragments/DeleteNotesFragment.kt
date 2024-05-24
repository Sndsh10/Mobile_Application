package com.sandesh.notesapplication.fragments

import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.sandesh.notesapplication.R
import java.io.File
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DeleteNotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DeleteNotesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var noteTitle=""
    var noteFileName=""
    var notesFileDirectory: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            noteTitle=it.getString("Note-title")!!
            noteFileName=it.getString("Note-fileName")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notesFileDirectory = File(
            Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/SandyNotes"
        )
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_delete_notes, container, false)
        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        (activity as AppCompatActivity).supportActionBar?.title = "Delete "+noteTitle+" ??"
        val deleteNote = view.findViewById<Button>(R.id.deleteNote) as Button
        val cancelNote = view.findViewById<Button>(R.id.cancelNote) as Button
        deleteNote.setOnClickListener { view1: View? ->
            val file = File(notesFileDirectory.toString() + "/" + noteFileName)
            if (file.exists()) {
                file.delete()
            }
            navController.navigate(R.id.displayNotesFragment)
            Toast.makeText(context,"Deleted successfully", Toast.LENGTH_LONG).show()
        }
        cancelNote.setOnClickListener { view1: View? ->
            navController.navigate(R.id.displayNotesFragment)
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DeleteNotesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DeleteNotesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}