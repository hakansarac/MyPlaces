package com.hakansarac.myplaces.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hakansarac.myplaces.R
import com.hakansarac.myplaces.models.PlaceModel
import kotlinx.android.synthetic.main.item_my_place.view.*


open class MyPlacesAdapter(
    private val context: Context,
    private var list: ArrayList<PlaceModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener : OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_my_place,
                parent,
                false
            )
        )
    }

    //an adapter cannot have an on click listener
    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.imageViewPlaceImageCircular.setImageURI(Uri.parse(model.image))
            holder.itemView.textViewTitle.text = model.title
            holder.itemView.textViewDescription.text = model.description
            holder.itemView.setOnClickListener{
                if(onClickListener != null){
                    onClickListener!!.onClick(position,model)
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClickListener{
        fun onClick(position:Int,model:PlaceModel)
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}