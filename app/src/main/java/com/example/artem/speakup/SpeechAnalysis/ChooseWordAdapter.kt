package com.example.artem.speakup.SpeechAnalysis

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artem.speakup.R
import kotlinx.android.synthetic.main.choose_word_item.view.*

class ChooseWordAdapter(var data: ArrayList<String>,
                        var callBack: ChooseWordAdapter.ChooseWordCallBack) : RecyclerView.Adapter<ChooseWordAdapter.UserViewHolder>() {

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        holder.edtxWord.setText(data[position])
        holder.edtxWord.addTextChangedListener(
                object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {
                        if (p0 != null) {
                            data[holder.adapterPosition] = p0.toString()
                        }
                    }
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                }
        )

        holder.delteBtn.setOnClickListener {
            callBack.deleteWord(position)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.choose_word_item, parent, false))

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var edtxWord = view.word_edit_txt
        var delteBtn = view.delete_btn
    }

    interface ChooseWordCallBack {
        fun deleteWord(pos: Int)
    }

}
