package co.tiagoaguiar.fitnesstracker;

import android.view.View
import co.tiagoaguiar.fitnesstracker.model.Calc

interface OnListClickListener {
	fun onClick(adapterPosition: Int, view: View, calc: Calc)
	fun onLongClick(position: Int, calc: Calc, view: View)
}