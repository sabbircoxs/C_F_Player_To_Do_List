package com.example.cfplayertodolist

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cfplayertodolist.adapter.PlayerAdapter
import com.example.cfplayertodolist.data.Player

class MainActivity : AppCompatActivity() {
    private lateinit var playerlist: MutableList<Player>
    private lateinit var recyclerview: RecyclerView
    private lateinit var playerAdapter: PlayerAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editNameEditText: EditText
    private lateinit var editPositionEditText: EditText
    private lateinit var editBirthEditText: EditText
    private lateinit var editDitalsEditText: EditText
    private lateinit var editAgeEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        recyclerview = findViewById(R.id.Recyclerview)
        editNameEditText = findViewById(R.id.EnterName)
        editPositionEditText = findViewById(R.id.EnterPosition)
        editBirthEditText = findViewById(R.id.EnterBirth)
        editDitalsEditText = findViewById(R.id.EnterDitals)
        editAgeEditText = findViewById(R.id.EnterAge)
        playerlist = retrivePlayer()

        val saveButton: Button = findViewById(R.id.SaveBtn)

        saveButton.setOnClickListener {
            val playerName = editNameEditText.text.toString()
            val playerPosition = editPositionEditText.text.toString()
            val playerBirth = editBirthEditText.text.toString()
            val playerDitals = editDitalsEditText.text.toString()
            val playerAge = editAgeEditText.text.toString()

            if (playerName.isEmpty()) {
                val player = Player(playerName, playerPosition, playerBirth, playerDitals, playerAge)
                savePlayer(playerlist)
                playerlist.add(player)
                editNameEditText.text.clear()
                editPositionEditText.text.clear()
                editBirthEditText.text.clear()
                editDitalsEditText.text.clear()
                editAgeEditText.text.clear()
            }
            else {
                Toast.makeText(this, "Please enter a player", Toast.LENGTH_SHORT).show()
            }
        }
        playerAdapter = PlayerAdapter(playerlist, object : PlayerAdapter.PlayerClickListen {
            override fun onEditClicked(position: Int) {
                editNameEditText.setText(playerlist[position].name)
                editPositionEditText.setText(playerlist[position].position)
                editBirthEditText.setText(playerlist[position].birth)
                editDitalsEditText.setText(playerlist[position].ditals)
                editAgeEditText.setText(playerlist[position].age)
                playerlist.removeAt(position)
                playerAdapter.notifyItemRemoved(position)
            }
            override fun onDeleteClicked(position: Int) {
                val alertDialog = AlertDialog.Builder(this@MainActivity)
                alertDialog.setTitle("Delete player")
                alertDialog.setMessage("Are you sure you want to delete this player?")
                alertDialog.setPositiveButton("Yes") { _, _ ->
                    deletePlayer(position)
                }
                alertDialog.setNegativeButton("No") { _, _ ->}
                alertDialog.show()
            }

        })
        recyclerview.adapter = playerAdapter
        recyclerview.layoutManager = LinearLayoutManager(this)
    }
    private fun savePlayer(playerlist: MutableList<Player>) {
        val editor = sharedPreferences.edit()
        val playerSet = HashSet<String>()
        playerlist.forEach { playerSet.add(it.name)}
        playerlist.forEach { playerSet.add(it.position)}
        playerlist.forEach { playerSet.add(it.birth)}
        playerlist.forEach { playerSet.add(it.ditals)}
        playerlist.forEach { playerSet.add(it.age)}
        editor.putStringSet("playerSet", playerSet)
        editor.apply()
    }
    private fun deletePlayer(position: Int) {
        playerlist.removeAt(position)
        playerAdapter.notifyItemRemoved(position)
        savePlayer(playerlist)
    }
    private fun retrivePlayer(): MutableList<Player> {
        val playerSet = sharedPreferences.getStringSet("playerSet", HashSet()) ?: HashSet()
        return playerSet.map { Player(it, it, it, it, it) }.toMutableList()
    }
}