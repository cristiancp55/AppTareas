package local.cristian.test.apptareas.model

// Clase de datos que representa una tarea con su título, descripción, categoría y estado (si está realizada o no)
data class Tarea(
    val titulo: String,
    val descripcion: String,
    val categoria: Categoria,
    var realizada: Boolean = false
)
