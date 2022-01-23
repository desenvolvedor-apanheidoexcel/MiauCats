package com.ffcs.miaucats.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ffcs.miaucats.R
import com.ffcs.miaucats.model.Imagem

class MainImageAdapter(
    private var context: Context,
    private var listImagens: ArrayList<Imagem>,
    private val imagemClickListener: OnImagemClickListener
): RecyclerView.Adapter<MainImageAdapter.ViewHolder> () {

    interface OnImagemClickListener{
        fun onImagemClick(description: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_imagem, parent, false)
        return  ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrls = listImagens[position]
        val imageView = holder.imageView

        if(imageUrls.description != null && imageUrls.description != "null"){
            holder.imageDescricao.visibility = View.VISIBLE
        }else{
            holder.imageDescricao.visibility = View.GONE
        }

        imageView.setOnClickListener {
            this@MainImageAdapter.imagemClickListener.onImagemClick(imageUrls.description)
        }

        Glide.with(context)
            .load(imageUrls.link)
            .into(imageView)
    }

    fun updateAdapter(listaImagens: List<Imagem>){
        listImagens.clear()
        listImagens.addAll(listaImagens)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listImagens.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.imageView)
        var imageDescricao: ImageView = itemView.findViewById(R.id.imgPossuiDescricao)
    }
}