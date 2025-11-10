package com.example.solomillo.dialogs

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText

class TextBox {
    companion object {
        fun showNameInputDialog(teamLabel: String, context: Context, onNameEntered: (String) -> Unit) {
            val editText = EditText(context)
            editText.hint = "Enter new name"

            AlertDialog.Builder(context)
                .setTitle("Rename")
                .setView(editText)
                .setPositiveButton("OK") { dialog, _ ->
                    val newName = editText.text.toString().trim()
                    if (newName.isNotEmpty()) {
                        onNameEntered(newName)
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

}