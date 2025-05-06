package local.cristian.test.apptareas.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import local.cristian.test.apptareas.model.Tarea

// Repositorio que se encarga de guardar y cargar la lista de tareas usando SharedPreferences y Gson

class TareasSharedPrefRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("TareasPrefs", Context.MODE_PRIVATE)

    private val gson = Gson()
    private val key = "tareas"

    // Guarda la lista de tareas en SharedPreferences como un JSON
    fun guardarTareas(lista: List<Tarea>) {
        val json = gson.toJson(lista)
        prefs.edit().putString(key, json).apply()
    }

    // Carga la lista de tareas desde SharedPreferences. Si no hay nada guardado, devuelve una lista vacía
    fun cargarTareas(): MutableList<Tarea> {
        val json = prefs.getString(key, null)
        return if (json != null) {
            val tipo = object : TypeToken<MutableList<Tarea>>() {}.type
            gson.fromJson(json, tipo)
        } else {
            mutableListOf()
        }
    }

    // Elimina todas las tareas guardadas de SharedPreferences
    fun eliminarTodasLasTareas() {
        prefs.edit().remove(key).apply()
    }
}
