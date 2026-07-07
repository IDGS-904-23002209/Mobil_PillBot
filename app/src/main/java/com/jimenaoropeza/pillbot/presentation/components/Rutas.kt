sealed class Screen(val route: String) {

    object Login : Screen("login")
    object Registrarse : Screen("registrarse")

    object Inicio : Screen("inicio")
    object Calendario : Screen("calendario")
    object Notificaciones : Screen("notificaciones")
    object Perfil : Screen("perfil")

    object Formulario : Screen("formulario")
    object HistorialMedicamento : Screen("historialMedicamento")
    object ControlEmergencia : Screen("controlEmergencia")

    object DetalleMedicamento : Screen("detalleMedicamento")
    object RecargarMedicamento : Screen("recargarMedicamento")

    object Tratamiento : Screen("tratamiento")

    object ProgramacionTratamiento : Screen("programacionTratamiento")}