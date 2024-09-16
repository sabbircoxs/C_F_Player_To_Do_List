package com.example.cfplayertodolist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cfplayertodolist.data.Player
import com.example.cfplayertodolist.databinding.PlayerListBinding

class PlayerAdapter(private val playerList:MutableList<Player>, private val clicklisten:PlayerClickListen):
    RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {
    interface PlayerClickListen {
        fun onEditClicked(position: Int)
        fun onDeleteClicked(position: Int)

    }

    class PlayerViewHolder(val binding: PlayerListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(player: Player) {
            binding.playerName.text = player.name
            binding.playerPosition.text = player.position
            binding.playerAge.text = player.age
            binding.playerDitals.text = player.ditals
            binding.playerBirth.text = player.birth
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = PlayerListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PlayerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = playerList[position]
        holder.bind(player)
        holder.binding.editBtn.setOnClickListener {
            clicklisten.onEditClicked(position)
        }
        holder.binding.deleteBtn.setOnClickListener {
            clicklisten.onDeleteClicked(position)
        }
    }
}