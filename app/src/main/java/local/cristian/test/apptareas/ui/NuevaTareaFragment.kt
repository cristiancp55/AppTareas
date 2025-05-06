package local.cristian.test.apptareas.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import local.cristian.test.apptareas.databinding.FragmentNuevaTareaBinding
import local.cristian.test.apptareas.model.Categoria
import local.cristian.test.apptareas.model.Tarea

// Fragmento para crear o editar una tarea
class NuevaTareaFragment : Fragment() {

    private var _binding: FragmentNuevaTareaBinding? = null
    private val binding get() = _binding!!
    private val args: NuevaTareaFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNuevaTareaBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Configura la lógica del fragmento
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewModel = ViewModelProvider(requireActivity())[TareasViewModel::class.java]

        // Configurar el spinner de categorías
        val categorias = Categoria.values().map { it.name.lowercase().replaceFirstChar { c -> c.uppercase() } }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategoria.adapter = adapter

        if (args.indice >= 0) {
            val tarea = viewModel.listaTareas.value?.get(args.indice)
            binding.etTitulo.setText(tarea?.titulo)
            binding.etDescripcion.setText(tarea?.descripcion)
            val index = Categoria.values().indexOf(tarea?.categoria ?: Categoria.PERSONAL)
            binding.spinnerCategoria.setSelection(index)
        }

        // Acción al pulsar el botón de guardar
        binding.btnGuardar.setOnClickListener {
            val titulo = binding.etTitulo.text.toString()
            val descripcion = binding.etDescripcion.text.toString()
            val categoriaSeleccionada = Categoria.values()[binding.spinnerCategoria.selectedItemPosition]

            if (titulo.isNotBlank() && descripcion.isNotBlank()) {
                val nuevaTarea = Tarea(titulo, descripcion, categoriaSeleccionada)

                if (args.indice >= 0) {
                    viewModel.editarTarea(args.indice, nuevaTarea)
                } else {
                    viewModel.agregarTarea(nuevaTarea)
                }

                findNavController().navigateUp()
            } else {
                Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
