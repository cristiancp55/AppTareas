# App de Tareas (To-Do List)
Autor: Cristian Castro Pena, Grupo A, DAM

## Descripción de la App

Esta aplicación permite gestionar tareas personales y laborales. El usuario puede:

- Añadir nuevas tareas con título, descripción y categoría.
- Marcar tareas como realizadas o no realizadas.
- Editar tareas existentes.
- Eliminar tareas individuales o todas ala vez.
- Las tareas se guardan de forma persistente, incluso si se cierra la app.

---

## Arquitectura

La app sigue el patrón MVVM:

- Model: Tarea, Categoria
- ViewModel: TareasViewModel
- View: ListaTareasFragment, NuevaTareaFragment

---

## Almacenamiento de datos

- Se utiliza SharedPreferences junto con la librería Gson para almacenar las tareas como JSON.
- La clase TareasSharedPrefRepository es responsable de guardar, cargar y eliminar las tareas.

---

## Componentes utilizados

- Navigation Component: para navegación entre pantallas.
- ViewBinding: para acceder a las vistas.
- Material Design: botones flotantes, CardViews, menús contextuales.
- RecyclerView: para mostrar la lista de tareas.
- ViewModel + LiveData: para gestionar los datos de forma reactiva.
- SharedPreferences + Gson: para persistencia de datos.

---

## Cómo funciona

1. La app se inicia mostrando la lista de tareas.
2. Si no hay tareas, se muestra un mensaje indicando que está vacía.
3. Puedes pulsar el botón ➕ para añadir una nueva tarea.
4. Puedes mantener pulsado sobre una tarea para editarla, eliminarla o marcarla como realizada.
5. Las tareas realizadas aparecen con fondo gris claro y texto tachado.
6. Puedes eliminar todas las tareas desde el menú superior en el icono de papelera, que solo aparece si hay tareas.

---
## Repositorio:
https://github.com/cristiancp55/AppTareas.git






