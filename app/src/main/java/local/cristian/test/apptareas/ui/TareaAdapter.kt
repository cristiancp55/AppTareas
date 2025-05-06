package local.cristian.test.apptareas.ui

import android.graphics.Paint
import android.view.*
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import local.cristian.test.apptareas.R
import local.cristian.test.apptareas.databinding.ItemTareaBinding
import local.cristian.test.apptareas.model.Categoria
import local.cristian.test.apptareas.model.Tarea

// Adaptador para mostrar la lista de tareas en un RecyclerView
class TareaAdapter(
    private var lista: List<Tarea>, // Lista de tareas a mostrar
    private val onEdit: (Int) -> Unit, // Callback para editar una tarea
    private val onDelete: (Int) -> Unit, // Callback para eliminar una tarea
    private val onMarkDone: (Int) -> Unit // Callback para marcar una tarea como realizada
) : RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {

    // Clase ViewHolder que representa cada ítem (card) de tarea
    inner class TareaViewHolder(private val binding: ItemTareaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Asocia los datos de una tarea con las vistas del layout
        fun bind(tarea: Tarea) {
            binding.tvTitulo.text = tarea.titulo
            binding.tvDescripcion.text = tarea.descripcion
            binding.tvCategoria.text = tarea.categoria.name

            val contexto = binding.root.context

            // Cambia el color de fondo de la tarjeta de categoría según el tipo
            val colorFondo = when (tarea.categoria) {
                Categoria.PERSONAL -> contexto.getColor(android.R.color.holo_green_dark)
                Categoria.TRABAJO -> contexto.getColor(android.R.color.holo_blue_dark)
            }
            binding.cardCategoria.setCardBackgroundColor(colorFondo)

            // Estilos especiales si la tarea está marcada como realizada
            if (tarea.realizada) {
                binding.cardTarea.setCardBackgroundColor(
                    contexto.getColor(android.R.color.darker_gray) // Color de fondo sutil
                )
                // Tachar el texto
                binding.tvTitulo.paintFlags = binding.tvTitulo.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.tvDescripcion.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    setTextColor(contexto.getColor(R.color.black)) // Color de texto al tachar
                }
            } else {
                // Estilo por defecto si no está realizada
                binding.cardTarea.setCardBackgroundColor(
                    contexto.getColor(android.R.color.white)
                )
                // Quitar tachado
                binding.tvTitulo.paintFlags = binding.tvTitulo.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.tvDescripcion.paintFlags = binding.tvDescripcion.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            // Mostrar menú contextual al mantener pulsado
            binding.root.setOnLongClickListener {
                mostrarMenu(it, adapterPosition)
                true
            }
        }

        // Muestra el menú contextual con opciones: editar, eliminar, realizada
        private fun mostrarMenu(view: View, posicion: Int) {
            val popup = PopupMenu(view.context, view)
            popup.inflate(R.menu.menu_contextual_tarea)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_editar -> {
                        onEdit(posicion)
                        true
                    }
                    R.id.menu_eliminar -> {
                        onDelete(posicion)
                        true
                    }
                    R.id.menu_realizada -> {
                        onMarkDone(posicion)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    // Crea la vista para cada ítem
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val binding = ItemTareaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TareaViewHolder(binding)
    }

    // Asocia la vista con los datos
    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    // Devuelve el número total de tareas
    override fun getItemCount(): Int = lista.size

    // Método para actualizar la lista de tareas en el adaptador
    fun actualizarLista(nuevaLista: List<Tarea>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}
