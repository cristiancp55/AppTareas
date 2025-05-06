package local.cristian.test.apptareas.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import local.cristian.test.apptareas.data.TareasSharedPrefRepository
import local.cristian.test.apptareas.model.Tarea

// ViewModel que gestiona la lógica y los datos de la lista de tareas
class TareasViewModel(application: Application) : AndroidViewModel(application) {

    // Repositorio para acceder a SharedPreferences y guardar/cargar tareas
    private val repository = TareasSharedPrefRepository(application)

    // Lista de tareas observable, privada (para modificar internamente)
    private val _listaTareas = MutableLiveData<MutableList<Tarea>>().apply {
        value = repository.cargarTareas()  // Se inicializa cargando las tareas guardadas
    }

    // Lista de tareas pública (solo lectura) para observar desde la interfaz
    val listaTareas: LiveData<MutableList<Tarea>> get() = _listaTareas

    // Función para agregar una nueva tarea a la lista
    fun agregarTarea(tarea: Tarea) {
        val lista = _listaTareas.value ?: mutableListOf()
        lista.add(tarea)
        _listaTareas.value = lista
        repository.guardarTareas(lista)
    }

    // Función para editar una tarea existente por su índice
    fun editarTarea(indice: Int, nuevaTarea: Tarea) {
        val lista = _listaTareas.value
        if (lista != null && indice in lista.indices) {
            lista[indice] = nuevaTarea
            _listaTareas.value = lista
            repository.guardarTareas(lista)
        }
    }

    // Función para eliminar una tarea por su índice
    fun eliminarTarea(indice: Int) {
        val lista = _listaTareas.value
        if (lista != null && indice in lista.indices) {
            lista.removeAt(indice)
            _listaTareas.value = lista
            repository.guardarTareas(lista)
        }
    }

    // Función para eliminar todas las tareas
    fun eliminarTodas() {
        _listaTareas.value = mutableListOf()
        repository.eliminarTodasLasTareas()
    }

    // Función para marcar una tarea como realizada o no realizada
    fun marcarComoRealizada(indice: Int) {
        val lista = _listaTareas.value
        if (lista != null && indice in lista.indices) {
            val tarea = lista[indice]
            tarea.realizada = !tarea.realizada
            _listaTareas.value = lista
            repository.guardarTareas(lista)
        }
    }
}
