package local.cristian.test.apptareas.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import local.cristian.test.apptareas.R
import local.cristian.test.apptareas.databinding.FragmentListaTareasBinding

// Fragmento que muestra la lista de tareas con opciones para editar, eliminar y añadir
class ListaTareasFragment : Fragment() {

    private var _binding: FragmentListaTareasBinding? = null
    private val binding get() = _binding!!
    // ViewModel que cotiene la lógica y datos de las tareas
    private lateinit var viewModel: TareasViewModel
    private lateinit var adapter: TareaAdapter
    private var menuOpciones: Menu? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaTareasBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Configura el fragmento una vez que la vista ha sido creada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[TareasViewModel::class.java]

        adapter = TareaAdapter(
            emptyList(),
            onEdit = { posicion ->
                // Navega al fragmento de nueva tarea, pasando el índice para editar
                val accion = ListaTareasFragmentDirections
                    .actionListaTareasFragmentToNuevaTareaFragment(posicion)
                findNavController().navigate(accion)
            },
            onDelete = { posicion ->
                // Elimina la tarea del ViewModel y actualiza el menú
                viewModel.eliminarTarea(posicion)
                activity?.invalidateOptionsMenu()
            },
            onMarkDone = { posicion ->
                // Cambia el estado de "realizada" en la tarea
                viewModel.marcarComoRealizada(posicion)
            }
        )

        // Configura el RecyclerView con un LinearLayout
        binding.recyclerTareas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerTareas.adapter = adapter

        // Observa la lista de tareas y actualiza la interfaz cuando cambian
        viewModel.listaTareas.observe(viewLifecycleOwner) { tareas ->
            adapter.actualizarLista(tareas)
            // Muestra un mensaje si la lista está vacía
            binding.tvVacio.visibility = if (tareas.isEmpty()) View.VISIBLE else View.GONE
            // Refresca el menú de opciones para mostrar u ocultar acciones
            activity?.invalidateOptionsMenu()
        }

        // Cuando se pulsa el botón flotante, se navega para crear una nueva tarea
        binding.fab.setOnClickListener {
            findNavController().navigate(
                ListaTareasFragmentDirections.actionListaTareasFragmentToNuevaTareaFragment(-1)
            )
        }
    }

    // Infla el menú de la parte superior del fragmento
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_lista, menu) // Carga el menú XML

        // Muestra la opción de "Eliminar todas" solo si hay tareas
        val hayTareas = viewModel.listaTareas.value?.isNotEmpty() == true
        menu.findItem(R.id.action_delete_all)?.isVisible = hayTareas

        super.onCreateOptionsMenu(menu, inflater)
    }

    // Maneja lo que ocurre al pulsar una opción del menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Opción para añadir una tarea
            R.id.action_add -> {
                findNavController().navigate(
                    ListaTareasFragmentDirections.actionListaTareasFragmentToNuevaTareaFragment(-1)
                )
                true
            }
            // Opción para eliminar todas las tareas
            R.id.action_delete_all -> {
                mostrarDialogoConfirmacion()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Muestra un diálogo de confirmación antes de eliminar todas las tareas
    private fun mostrarDialogoConfirmacion() {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar todas las tareas")
            .setMessage("¿Estás seguro de que quieres eliminar todas las tareas?")
            .setPositiveButton("Sí") { _, _ ->
                // Si el usuario confirma, se eliminan todas
                viewModel.eliminarTodas()
                requireActivity().invalidateOptionsMenu()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
