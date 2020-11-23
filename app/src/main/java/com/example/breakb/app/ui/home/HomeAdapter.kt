package com.example.breakb.app.ui.home

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.breakb.app.R
import com.example.breakb.app.databinding.ItemCharacterBinding
import com.example.breakb.app.model.Character
import com.example.breakb.app.util.inflate
import com.example.breakb.app.util.loadUrl
import kotlin.properties.Delegates

typealias CharacterClickListener = (Character) -> Unit

class HomeAdapter(private val listener: CharacterClickListener) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    var characterList: List<Character> by Delegates.observable(arrayListOf()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(parent.inflate(R.layout.item_character))
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val character = characterList[position]
        holder.bind(character)
        holder.itemView.setOnClickListener{ listener(character) }
    }

    override fun getItemCount(): Int = characterList.size

    class HomeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemCharacterBinding.bind(itemView)
        fun bind(character: Character) {
            with(binding) {
                characterName.text = character.name
                characterImage.loadUrl(character.img)
            }
        }
    }
}