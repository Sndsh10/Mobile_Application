package com.sandesh.notesapplication.fragments

import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sandesh.notesapplication.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DisplayNotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DisplayNotesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var notesFileDirectory: File? = null
    var directoryIsEmpty=true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_display_notes, container, false)

        notesFileDirectory = File(
            Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/SandyNotes"
        )
        traverseDirectory(notesFileDirectory!!)

        syncWithSharedPreferences(view);

        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        var fabAddNewReminder = view?.findViewById<FloatingActionButton>(R.id.fabAddReminder)
        fabAddNewReminder?.setOnClickListener {
            navController.navigate(R.id.createNotesFragment)
        }
        return view
    }

    fun readFromFiles(file: File): String {
        var messageDerived: String = ""
        var inputStream: InputStream? = null
        try {
            inputStream = FileInputStream(file)
            val byteArrayOutputStream = ByteArrayOutputStream()
            var nextByte = 0
            nextByte = inputStream.read()
            while (nextByte != -1) {
                byteArrayOutputStream.write(nextByte)
                nextByte = inputStream.read()
            }
            val plaintext = byteArrayOutputStream.toByteArray()
            val response = String(plaintext, Charset.forName("UTF-8"))
            messageDerived = response
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return messageDerived
    }

    fun traverseDirectory(dir: File) {
        if (dir.exists()) {
            val files = dir.listFiles()
            if (files != null) {

                for (i in files.indices) {
                    directoryIsEmpty=false;
                    val file = files[i]
                    if (file.isDirectory) {
                        traverseDirectory(file)
                    } else {
                        // do something here with the file
                        //Toast.makeText(context,file.name.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else{
            dir.mkdir()
        }
    }
    fun syncWithSharedPreferences(view: View) {
        val preferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        //if(File())
        if(directoryIsEmpty==false){
            val mainLayoutNotesList = view.findViewById<LinearLayout>(R.id.scrollableNotes)
            mainLayoutNotesList.removeAllViews()
            val files = notesFileDirectory!!.listFiles()
            if (files != null) {
                for (i in files.indices) {
                    val file = files[i]
                    if (file.isDirectory) {
                        traverseDirectory(file)
                    } else {
                        // do something here with the file
                        var fileValue: String =
                            readFromFiles(File(notesFileDirectory.toString() + "/" + file.name.toString()))
                        val stringFileValueArray = fileValue.split("//").toTypedArray()
                        //Log.d("tilili",stringFileValueArray[1]);

                        val individualNoteLayout =
                            LinearLayout(requireActivity().applicationContext)
                        val textViewNoteTitle = TextView(requireActivity().applicationContext)
                        val normalParam = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1.0f
                        )
                        val emptyViewParam = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            35, 1.0f
                        )

                        val emptyView = View(requireActivity().applicationContext);
                        emptyView.setLayoutParams(emptyViewParam)
                        textViewNoteTitle.setLayoutParams(normalParam)
                        textViewNoteTitle.text = stringFileValueArray[1]
                        textViewNoteTitle.setPadding(20, 40, 20, 40)
                        textViewNoteTitle.setTextSize(
                            TypedValue.COMPLEX_UNIT_PX,
                            resources.getDimension(R.dimen.middle_text_size)
                        )
                        textViewNoteTitle.setBackgroundResource(R.drawable.border)
                        textViewNoteTitle.id = View.generateViewId()

                        individualNoteLayout.addView(textViewNoteTitle.rootView)
                        individualNoteLayout.setOnClickListener {

                            val bundle = Bundle()
                            bundle.putString("Note-title", stringFileValueArray[1])
                            bundle.putString("Note-description", stringFileValueArray[2])
                            bundle.putString("Note-fileName", file.name.toString())

                            navController.navigate(R.id.editNotesFragment, bundle)

                        }
                        mainLayoutNotesList.addView(individualNoteLayout.rootView)
                        mainLayoutNotesList.addView(emptyView.rootView)
                        mainLayoutNotesList.invalidate()
                        mainLayoutNotesList.requestLayout()
//                        Toast.makeText(context,"is not null",Toast.LENGTH_LONG).show()

                    }
                }
            }
        }
        if(directoryIsEmpty==true){
            val mainLayoutNotesList = view.findViewById<LinearLayout>(R.id.scrollableNotes)
            mainLayoutNotesList.removeAllViews()
            val individualNoteLayout = LinearLayout(requireActivity().applicationContext)

            val textViewNoteTitle = TextView(requireActivity().applicationContext)
            val normalParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
            )
            val emptyViewParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                15, 1.0f
            )
            val emptyView = View(requireActivity().applicationContext);
            emptyView.setLayoutParams(emptyViewParam)
            textViewNoteTitle.setLayoutParams(normalParam)
            textViewNoteTitle.text = "no notes found"
            textViewNoteTitle.setPadding(30, 50, 30, 50)
            textViewNoteTitle.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(R.dimen.middle_text_size)
            )
            textViewNoteTitle.setBackgroundResource(R.drawable.border)
            textViewNoteTitle.id = View.generateViewId()

            individualNoteLayout.addView(textViewNoteTitle.rootView)
            individualNoteLayout.setOnClickListener {

            }
            mainLayoutNotesList.addView(individualNoteLayout.rootView)
            mainLayoutNotesList.addView(emptyView.rootView)
            mainLayoutNotesList.invalidate()
            mainLayoutNotesList.requestLayout()
//            Toast.makeText(context,"is null",Toast.LENGTH_LONG).show()
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DisplayNotesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                DisplayNotesFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}