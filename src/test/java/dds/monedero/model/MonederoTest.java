package dds.monedero.model;


import org.junit.Before;
import org.junit.Test;


import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

public class MonederoTest {
  private Cuenta cuenta;

  @Before
  public void init() {
    cuenta = new Cuenta();
  }
  
  @Test
  public void Sacar(){
	  cuenta.poner(1000);
	  cuenta.sacar(999);
	  assert(cuenta.getSaldo()==1);
  }
  
  @Test(expected= SaldoMenorException.class)
  public void SacarDeMas(){
	  cuenta.poner(500);
	  cuenta.sacar(501);
  }
  
  @Test(expected= MaximoExtraccionDiarioException.class)
  public void SuperarElLimiteDeRetiro(){
	  //el limite es 1000 x dia
	  cuenta.poner(10000);
	  cuenta.sacar(500);
	  cuenta.sacar(700);
  }
  
  
  @Test
  public void Poner() {
    cuenta.poner(1500);
    assert(1500==cuenta.getSaldo());
  }

  @Test(expected = MontoNegativoException.class)
  public void PonerMontoNegativo() {
    cuenta.poner(-1500); 
  }

  @Test
  public void TresDepositos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    double suma = 1500+456+1900;
    assert(suma == cuenta.getSaldo());
    
  }



@Test(expected = MaximaCantidadDepositosException.class)
  public void MasDeTresDepositos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    cuenta.poner(245);
  }

  @Test(expected = SaldoMenorException.class)
  public void ExtraerMasQueElSaldo() {
    cuenta.setSaldo(90);
    cuenta.sacar(1001);
  }

  @Test(expected = MaximoExtraccionDiarioException.class)
  public void ExtraerMasDe1000() {
    cuenta.setSaldo(5000);
    cuenta.sacar(1001);
  }

  @Test(expected = MontoNegativoException.class)
  public void ExtraerMontoNegativo() {
    cuenta.sacar(-500);
  }

}