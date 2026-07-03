sealed class Screen(val route: String) {

    object Login : Screen("login")
    object Registrarse : Screen("registrarse")

    object Inicio : Screen("inicio")
    object Calendario : Screen("calendario")
    object Notificaciones : Screen("notificaciones")
    object Perfil : Screen("perfil")

    object Formulario : Screen("formulario")
    object Inventario : Screen("inventario")
    object ControlEmergencia : Screen("controlEmergencia")

    object DetalleMedicamento : Screen("detalleMedicamento")
    object AgregarMedicamento : Screen("agregarMedicamento")
    object RecargarMedicamento : Screen("recargarMedicamento")

    object Tratamiento : Screen("tratamiento")
}