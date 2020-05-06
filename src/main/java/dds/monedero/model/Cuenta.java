package dds.monedero.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  
  public void poner(double cuanto) {//LONG METHOD
	 chequeoMontoNegativo(cuanto);
    /*//logica repetida en 1 DUPLICATED CODE
     * if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }*/
	chequeoMaximaCantidadDeDepositosEnElDia(getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count());
    /*
     * if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
    */
    agregarMovimiento(cuanto);
    //new Movimiento(LocalDate.now(), cuanto, true).agregateA(this);
  }

  public void sacar(double cuanto) {//LONG METHOD
	chequeoMontoNegativo(cuanto);
    /*1) logica repetida DUPLICATED CODE
     * if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }*/
	
	chequeoSaldo(cuanto);
	/*
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }*/
    /*Temporary Field..
     * double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
     * */
    /*
     * Temporary Field...
     * double limite = 1000 - montoExtraidoHoy;
     */
    chequeoMaximaExtraccionPorDia(cuanto);
    /*
 	if (cuanto > limite(1000, getMontoExtraidoA(LocalDate.now()))) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite(1000, getMontoExtraidoA(LocalDate.now())));
    }
    */
    agregarMovimiento(cuanto);
    //new Movimiento(LocalDate.now(), cuanto, false).agregateA(this);
  }
  
  
  //Creo este metodo para solucionar el smell 3 LONG METHOD
  private void chequeoSaldo(double cuanto) {
	  if (getSaldo() - cuanto < 0) {
	      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
	    }
  }
  
  //Creo este metodo para solucionar el smell 3 LONG METHOD
  private void chequeoMaximaExtraccionPorDia(double cuanto) {
	  if (cuanto > limite(1000, getMontoExtraidoA(LocalDate.now()))) {
	      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
	          + " diarios, límite: " + limite(1000, getMontoExtraidoA(LocalDate.now())));
	    }
  }
  
  //Creo este metodo para solucionar el smell 3 LONG METHOD
  private void chequeoMaximaCantidadDeDepositosEnElDia(long depositosDelDia) {
	  if (depositosDelDia>= 3) {
	      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
	  }
  }
  
  //Creo este metodo para solucionar el smell 3 LONG METHOD
  private void  agregarMovimiento(double cuanto){
	//Aca mismo se genera otro smell, que se da en "movimiento" -> FEATURE ENVY
	  //ya que en movimiento el metodo "agregateA" pide mucha informacion de la cuenta, cuando podemos hacer lo mismo que hace ese metodo, aca.
	  //
	  new Movimiento(LocalDate.now(), cuanto, false).agregateA(this);
  }
  
  //Creo este metodo para solucionar el smell 2 DUPLICATED CODE
  private void chequeoMontoNegativo(double cuanto) {
	  if (cuanto <= 0) {
	      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
	  }
  }

  //Creo este metodo para solucionar el smell 1 TEMPORARY FIELD
  private double limite(int montoX, double montoExtraidoHoy) {
	  return montoX - montoExtraidoHoy;
  }
  

public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}
