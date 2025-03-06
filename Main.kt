import java.time.LocalDate //fecha sin tiempo
import java.time.temporal.ChronoUnit // días, meses, años, horas, minutos, segundos
import java.util.Scanner

fun suma(num1: Double, num2: Double,num3: Double): Double {

    return num1 + num2 + num3 //retornamos suma


}

fun nombre(nombre: String):String{

    val digitos = Regex("[0-9]+$")
    if(digitos.matches(nombre)){
        return "Ingresaste un numero, esto no es permitido"
    }
    else {
        return nombre
    }

}

fun nacimiento(dia: Int,mes: Int,ano: Int): String{
    //creamos los objetos con localDate para la fecha ingresada y otro para la actual
    val fechaNacimiento = LocalDate.of(ano, mes, dia)
    val fechaActual = LocalDate.now()

    // Calcular la diferencia en días
    val diasVividos = ChronoUnit.DAYS.between(fechaNacimiento, fechaActual)

    // Calcular la diferencia en meses
    val mesesVividos = ChronoUnit.MONTHS.between(fechaNacimiento, fechaActual)

    // Calcular la diferencia en semanas
    val semanasVividas = diasVividos / 7

    // Calcular horas, minutos y segundos
    val horasVividas = diasVividos * 24
    val minutosVividos = horasVividas * 60
    val segundosVividos = minutosVividos * 60

    // Mostrar el resultado
    return("""
        Tiempo vivido:
        - Meses: $mesesVividos meses
        - Semanas: $semanasVividas semanas
        - Días: $diasVividos días
        - Horas: $horasVividas horas
        - Minutos: $minutosVividos minutos
        - Segundos: $segundosVividos segundos
    """.trimIndent())
}

fun salir(){
    println("Saliendo del programa, espere...")
    System.exit(0)
}




fun main() {
    //intancia del scanner
    val scanner = Scanner(System.`in`)

    println("Bienvenido al menu, selecciona la accion a ejecutar:")
    println("1. Sumar 3 números")
    println("2. Nombre")
    println("3. Fecha de nacimiento")
    println("4. Salir")

    //inicio del menu
    val control= scanner.nextInt()//controlador
    scanner.nextLine()//todo:
    val accion = when(control){

        1 -> {
            //entrada de usuario
            print("Digita el primer numero:")
            val num1= scanner.nextDouble()
            print("Digita el segundo numero:")
            val num2= scanner.nextDouble()
            print("Digita el tercer numero:")
            val num3= scanner.nextDouble()
            //llamar funcion
            suma(num1,num2,num3)//retorna el resultado

        }
        2 -> {
            //entrada de usuario
            print("Ingresa tu nombre:")
            val nombre= scanner.nextLine()
            //llamar funcion
            nombre(nombre)//retorna el resultado

        }
        3 -> {
            print("Ingresa tu Dia de Nacimiento:")
            val dia= scanner.nextInt()
            print("Ingresa tu Mes de Nacimiento:")
            val mes= scanner.nextInt()
            print("Ingresa tu Año de Nacimiento:")
            val ano= scanner.nextInt()
            //llamar funcion
            nacimiento(dia,mes,ano)//retorna el resultado

        }
        4 -> {
            salir()//Salir del programa
        }


        else -> "Opcion No Valida!!!!"


    }

    println("Resultado: $accion")//mostramos el resultado de la opcion elegida

}