package com.sandesh.notesapplication.fragments

import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.sandesh.notesapplication.R
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateNotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateNotesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var notesFileDirectory: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        notesFileDirectory = File(
//            Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/SandyNotes"
//        )
//        if (!notesFileDirectory!!.exists()) {
//            notesFileDirectory!!.mkdir()
//            //new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/enterminal/terminalemulatorlog.txt");
//        }
//        writeToFile("hahah");
        notesFileDirectory = File(
            Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/SandyNotes"
        )
        var view = inflater.inflate(R.layout.fragment_create_notes, container, false)
        val etTitle = view.findViewById<View>(R.id.editTextTitle) as EditText
        val etNote = view.findViewById<View>(R.id.editTextNote) as EditText
        val saveNote = view.findViewById<Button>(R.id.saveNote) as Button
        val resetNote = view.findViewById<View>(R.id.reset) as Button
        resetNote.setOnClickListener { view1: View? ->
            etNote.setText("")
            etTitle.setText("")
        }
        saveNote.setOnClickListener { view1: View? ->
            val preferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            val editor = preferences.edit()
            var fileNameTemporary="note_";
            if(!preferences.contains("noteSize")){
                editor.putInt("noteSize", 1)
                //editor.putInt("RoutineJsonCount", arrayOfIndividualRoutineKey.size)
                editor.apply()
                fileNameTemporary=fileNameTemporary+1+".txt"
            }else{
                editor.putInt("noteSize", preferences.getInt("noteSize",1)+1)
                editor.apply()
                fileNameTemporary=fileNameTemporary+(preferences.getInt("noteSize",1)+1)+".txt"
            }
            var finalValue = "//"+etTitle.text.toString()+"//"+etNote.text.toString()
            writeToFile(finalValue,fileNameTemporary)

            etNote.setText("")
            etTitle.setText("")

            Toast.makeText(context,"File written successfully", Toast.LENGTH_SHORT).show()
            val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.displayNotesFragment)

        }
        return view
    }
    fun writeToFile(stringToWrite : String,fileToWrite:String){
        //val fileToWrite = "my_sensitive_data_analysis.txt"
        val file = File(notesFileDirectory.toString() + "/" + fileToWrite)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        try {
            val fileOutputStream = FileOutputStream(file, true)
            val outputStreamWriter =
                OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8)
            outputStreamWriter.append("\n"+stringToWrite)
            outputStreamWriter.flush()
            outputStreamWriter.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateNotesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateNotesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}