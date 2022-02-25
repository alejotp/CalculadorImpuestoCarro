/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad Ean (Bogotá - Colombia)
 * Programa de Ingeniería de Sistemas
 * Licenciado bajo el esquema Academic Free License version 2.1
 * <p>
 * Bloque de Estudios: Desarrollo de Software
 * Ejercicio: Cálculo de Impuestos de Carros
 * Adaptado de: Proyecto CUPI2 - UNIANDES
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package universidadean.impuestoscarro.mundo;

import java.io.*;
import java.util.*;

import universidadean.impuestoscarro.interfaz.PanelDescuentos;

/**
 * Calculador de impuestos.
 */
public class CalculadorImpuestos {
    // -----------------------------------------------------------------
    // Constantes
    // -----------------------------------------------------------------

    /**
     * Porcentaje de descuento por pronto pago.
     */
    public static final double PORC_DESC_PRONTO_PAGO = 10.0;

    /**
     * Valor de descuento por servicio público.
     */
    public static final double VALOR_DESC_SERVICIO_PUBLICO = 50000.0;

    /**
     * Porcentaje de descuento por traslado de cuenta.
     */
    public static final double PORC_DESC_TRASLADO_CUENTA = 5.0;

    // -----------------------------------------------------------------
    // Atributos
    // -----------------------------------------------------------------

    /**
     * Vehículos que maneja el calculador.
     */
    private Vehiculo[] vehiculos;

    /**
     * Vehículo actual mostrado en la aplicación.
     */
    private int posVehiculoActual;
    
    private PanelDescuentos panelDescuentos;


    // -----------------------------------------------------------------
    // Constructores
    // -----------------------------------------------------------------

    /**
     * Crea un calculador de impuestos, cargando la información de dos archivos. <br>
     * <b>post: </b> Se inicializaron los arreglos de vehículos y rangos.<br>
     * Se cargaron los datos correctamente a partir de los archivos.
     *
     * @throws Exception Error al cargar los archivos.
     */
    public CalculadorImpuestos() throws Exception {
        cargarVehiculos("data/vehiculos.txt");
    }

    // ----------------------------------------------------------------
    // Métodos
    // ----------------------------------------------------------------

    /**
     * Carga los datos de los vehículos que maneja el calculador de impuestos. <br>
     * <b>post: </b> Se cargan todos los vehículos del archivo con sus datos.
     *
     * @param pArchivo Nombre del archivo donde se encuentran los datos de los vehículos. pArchivo != null.
     * @throws Exception Si ocurre algún error cargando los datos.
     */
    private void cargarVehiculos(String pArchivo) throws Exception {
        String texto, valores[], sMarca, sLinea, sModelo, sImagen;
        double precio;
        int cantidad = 0;
        Vehiculo vehiculo;
        try {
            File datos = new File(pArchivo);
            FileReader fr = new FileReader(datos);
            BufferedReader lector = new BufferedReader(fr);
            texto = lector.readLine();

            cantidad = Integer.parseInt(texto);
            vehiculos = new Vehiculo[cantidad];
            posVehiculoActual = 0;

            texto = lector.readLine();
            for (int i = 0; i < vehiculos.length; i++) {
                valores = texto.split(",");

                sMarca = valores[0];
                sLinea = valores[1];
                sModelo = valores[2];
                sImagen = valores[4];
                precio = Double.parseDouble(valores[3]);

                vehiculo = new Vehiculo(sMarca, sLinea, sModelo, precio, sImagen);
                vehiculos[i] = vehiculo;
                // Siguiente línea
                texto = lector.readLine();
            }
            lector.close();
        }
        catch (IOException e) {
            throw new Exception("Error al cargar los datos almacenados de vehículos.");
        }
        catch (NumberFormatException e) {
            throw new Exception("El archivo no tiene el formato esperado.");
        }
    }

    /**
     * Calcula el pago de impuesto que debe hacer el vehículo actual. <br>
     * <b>pre:</b> Las listas de rangos y vehículos están inicializadas.
     *
     * @param descProntoPago      Indica si aplica el descuento por pronto pago.
     * @param descServicioPublico Indica si aplica el descuento por servicio público.
     * @param descTrasladoCuenta  Indica si aplica el descuento por traslado de cuenta.
     * @return Valor a pagar de acuerdo con las características del vehículo y los descuentos que se pueden aplicar.
     */
    public double calcularPago(boolean descProntoPago, boolean descServicioPublico, boolean descTrasladoCuenta) {
        double pago = 0.0;
        double precio = darVehiculoActual().darPrecio();

        // Se aplica una cadena de if else partiendo del precio más bajo y según el caso se realiza el calculo sugerido
        if(precio <= 30000000)  {
        	pago=precio*1.5/100;
        }else if(precio < 70000000) {
        	pago=precio*2/100;
        }else if(precio < 200000000) {
        	pago=precio*2.5/100;
        }else {
        	pago=precio*4/100;
        }
        
        // una vez se tiene el pago según costo del carro se aplican las premisas de descuento según cada caso y en el orden estipulado 
        
        if(descProntoPago) {
        	pago=pago-(pago*10/100);
        }
        if(descServicioPublico) {
        	pago=pago-50000;
        }
        if(descTrasladoCuenta) {
        	pago=pago-(pago*5/100);
        }
       
        return pago;
    }

    /**
     * Retorna el primer vehículo. <br>
     * <b>post: </b> Se actualizó la posición del vehículo actual.
     *
     * @return El primer vehículo, que ahora es el vehículo actual.
     * @throws Exception Si ya se encuentra en el primer vehículo.
     */
    public Vehiculo darPrimero() throws Exception {
        if (posVehiculoActual == 0) {
            throw new Exception("Ya se encuentra en el primer vehículo.");
        }
        posVehiculoActual = 0;
        return darVehiculoActual();
    }

    /**
     * Retorna el vehículo anterior al actual. <br>
     * <b>post: </b> Se actualizó la posición del vehículo actual.
     *
     * @return El anterior vehículo, que ahora es el vehículo actual.
     * @throws Exception Si ya se encuentra en el primer vehículo.
     */
    public Vehiculo darAnterior() throws Exception {
        if (posVehiculoActual == 0) {
            throw new Exception("Se encuentra en el primer vehículo.");
        }
        posVehiculoActual--;
        return darVehiculoActual();
    }

    /**
     * Retorna el vehículo siguiente al actual. <br>
     * <b>post: </b> Se actualizó la posición del vehículo actual.
     *
     * @return El siguiente vehículo, que ahora es el vehículo actual.
     * @throws Exception Si ya se encuentra en el último vehículo
     */
    public Vehiculo darSiguiente() throws Exception {
        if (posVehiculoActual == vehiculos.length - 1) {
            throw new Exception("Se encuentra en el último vehículo.");
        }
        posVehiculoActual++;
        return darVehiculoActual();
    }

    /**
     * Retorna el último vehículo. <br>
     * <b>post: </b> Se actualizó la posición del vehículo actual.
     *
     * @return El último vehículo, que ahora es el vehículo actual.
     * @throws Exception Si ya se encuentra en el último vehículo
     */
    public Vehiculo darUltimo() throws Exception {
        if (posVehiculoActual == vehiculos.length - 1) {
            throw new Exception("Ya se encuentra en el último vehículo.");
        }
        posVehiculoActual = vehiculos.length - 1;
        return darVehiculoActual();
    }

    /**
     * Retorna el vehículo actual.
     *
     * @return El vehículo actual.
     */
    public Vehiculo darVehiculoActual() {
        return vehiculos[posVehiculoActual];
    }

    /**
     * Busca el vehículo más caro, lo asigna como actual y lo retorna.
     *
     * @return El vehículo más caro.
     */
    public Vehiculo buscarVehiculoMasCaro() {
        Vehiculo vehiculoMasCaro = null;
        double precioMasCaro = 0;

        // Se recorre el array de vehiculos buscando el de mayor precio. Se parte de la base de que los carros cuestan 0 y a medida que se encuentra un precio más grande se va reemplazando por el vehiculo con valor más alto
        for (Vehiculo vehiculo : vehiculos) {
			if(vehiculo.darPrecio() > precioMasCaro)
			{
				vehiculoMasCaro = vehiculo;
				precioMasCaro = vehiculo.darPrecio();
			}
		}

        return vehiculoMasCaro;

    }

    /**
     * Busca y retorna el primer vehículo que encuentra con la marca que se lee desde teclado. <br>
     *
     * @return El primer vehículo de la marca. Si no encuentra ninguno retorna null.
     */
    public Vehiculo buscarVehiculoPorMarca(String buscar) {
        Vehiculo buscado = null;

        // Se recibe como parametro la marca a buscar y se recorre el array de vehiculos comparando e ignorando mayus y minus hasta encontrar el primer carro de la marca. Se rompe el ciclo con el break ya que se pide el primer carro que se encuentre.
        for (Vehiculo vehiculo : vehiculos) {
			if(buscar.equalsIgnoreCase(vehiculo.darMarca()))
			{
				buscado = vehiculo;
				break;
			}
		}

        return buscado;
    }

    /**
     * Busca y retorna el vehículo de la línea buscada. <br>
     *
     * @return El vehículo de la línea, null si no encuentra ninguno.
     */
    public Vehiculo buscarVehiculoPorLinea(String buscar) {
        Vehiculo buscado = null;

        // Se recibe como parametro la Linea a buscar y se recorre el array de vehiculos comparando e ignorando mayus y minus hasta encontrar el primera linea. Se rompe el ciclo con el break ya que se pide el primer carro que se encuentre.
        for (Vehiculo vehiculo : vehiculos) {
            if(buscar.equalsIgnoreCase(vehiculo.darLinea()))
            {
                buscado = vehiculo;
                break;
            }
        }

        return buscado;
    }

    /**
     * Busca el vehículo más antiguo, lo asigna como actual y lo retorna.
     *
     * @return El vehículo más antiguo.
     */
    public Vehiculo buscarVehiculoMasAntiguo() {
    	Vehiculo vehiculoMasAntiguo = null;
        int masAntiguo = 2022;

        // Se recorre el array de vehiculos buscando el de año más viejo. Se parte de la base de que el más viejo es del 2022  y a medida que se encuentra un año más viejo se va reemplazando por el vehiculo de ese año
        for (Vehiculo vehiculo : vehiculos) {
			if(Integer.parseInt(vehiculo.darAnio()) < masAntiguo)
			{
				vehiculoMasAntiguo = vehiculo;
				masAntiguo = Integer.parseInt(vehiculo.darAnio());
			}
		}

        return vehiculoMasAntiguo;
    }

    /**
     * Calcula el promedio de los precios de todos los automóviles que están en el sistema
     *
     * @return Promedio de precios
     */
    public double promedioPreciosVehiculos() {
        double promedio = 0.0;

        return promedio;
    }


}
