package com.example.premiumplus.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.premiumplus.R
import com.example.premiumplus.responseModel.DataItem

class ClientAdapter(var dataItems: ArrayList<DataItem>, val context: Context,val itemClickListner:ItemClickListner) :
    RecyclerView.Adapter<ClientAdapter.ClientViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClientAdapter.ClientViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_client,
            parent, false
        )
        return ClientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClientAdapter.ClientViewHolder, position: Int) {
         holder.fullname.text = dataItems[position].contactPersonName
         holder.name.text = dataItems[position].customerName
         holder.mail.text = dataItems[position].email
         holder.phone.text = dataItems[position].mobile
         holder.address.text = dataItems[position].location
        holder.itemView.setOnClickListener {
            itemClickListner.getItem(dataItems[position])
        }
        /* holder.retag.setOnClickListener {
             context.startActivity(Intent(context,AddClientActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
         }*/
    }

    override fun getItemCount(): Int {
        return dataItems.size
    }

    fun filterList(filteredlist: ArrayList<DataItem>) {
        dataItems = filteredlist
        notifyDataSetChanged()
    }

    class ClientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_name)
        val mail: TextView = itemView.findViewById(R.id.tv_email)
        val phone: TextView = itemView.findViewById(R.id.tv_mobile)
        val address: TextView = itemView.findViewById(R.id.tv_location)
        val fullname: TextView = itemView.findViewById(R.id.tv_fullname)
        val retag: TextView = itemView.findViewById(R.id.tv_retag)
    }
}