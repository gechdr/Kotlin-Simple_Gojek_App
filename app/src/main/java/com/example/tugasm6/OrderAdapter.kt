package com.example.tugasm6

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasm6.CurrencyUtils.toRupiah

class OrderAdapter(
    var data: MutableList<Order>,
    val onDetailClickListener: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    class ViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val ACOivType: ImageView = row.findViewById(R.id.ACOivType)
        val ACOtvType: TextView = row.findViewById(R.id.ACOtvType)
        val ACOtvDestination: TextView = row.findViewById(R.id.ACOtvDestination)
        val ACOtvSubtotal: TextView = row.findViewById(R.id.ACOtvSubtotal)
        val ACObtnDetail: Button = row.findViewById(R.id.ACObtnDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        return ViewHolder(itemView.inflate(
            R.layout.adapter_customer_order_item, parent, false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = data[position]

        holder.ACOivType.setImageResource(
            if (order.type == "Car") R.drawable.car else R.drawable.bike
        )
        holder.ACOtvType.text = order.type
        holder.ACOtvDestination.text = order.destination
        holder.ACOtvSubtotal.text = order.fare.toRupiah()

        holder.ACObtnDetail.setOnClickListener {
            onDetailClickListener.invoke(order)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
