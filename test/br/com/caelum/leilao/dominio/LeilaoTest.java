package br.com.caelum.leilao.dominio;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LeilaoTest {
	
	@Test
	public void deveReceberUmLance() {
		Leilao leilao = new Leilao("Macbook pro 15");
		assertEquals(0, leilao.getLances().size());
		
		leilao.propoe(new Lance(new Usuario("Steve Jobs"), 2000));
		
		assertEquals(1, leilao.getLances().size());
		assertEquals(2000.0, leilao.getLances().get(0).getValor(), 0.00001);
	}
	
	@Test
	public void deveReceberVariosLances() {
		Leilao leilao = new Leilao("Macbook pro 15");
		leilao.propoe(new Lance(new Usuario("Steve Jobs"), 2000));
		leilao.propoe(new Lance(new Usuario("Steve Wozniak"), 3000));
	
		assertEquals(2, leilao.getLances().size());
		assertEquals(2000.0, leilao.getLances().get(0).getValor(), 0.00001);
		assertEquals(3000.0, leilao.getLances().get(1).getValor(), 0.00001);
	}
	
	@Test
	public void naoDeveAceitarDoisLancesSeguidosDoMesmoUsuario() {
		Leilao leilao = new Leilao("Macbook pro 15");
		Usuario steveJobs = new Usuario("Steve Trabalhos");
		
		leilao.propoe(new Lance(steveJobs, 2000.0));
		leilao.propoe(new Lance(steveJobs, 3000.0));
		
		assertEquals(1, leilao.getLances().size());
		assertEquals(2000.0, leilao.getLances().get(0).getValor(), 0.00001);
	}
	
	@Test
	public void naoDeveAceitarMaisQueCincoLancesDoMesmoUsuario() {
		Leilao leilao = new Leilao("Dell optlex i5 pro");
		Usuario steveJobs = new Usuario("Steve Trabalho");
		Usuario billGates = new Usuario("Bill Portoes");
		
		leilao.propoe(new Lance(steveJobs, 2000));
		leilao.propoe(new Lance(billGates, 3000));

		leilao.propoe(new Lance(steveJobs, 4000));
		leilao.propoe(new Lance(billGates, 5000));
		
		leilao.propoe(new Lance(steveJobs, 6000));
		leilao.propoe(new Lance(billGates, 7000));
		
		leilao.propoe(new Lance(steveJobs, 8000));
		leilao.propoe(new Lance(billGates, 9000));
		
		leilao.propoe(new Lance(steveJobs, 10000));
		leilao.propoe(new Lance(billGates, 11000));

		// esse deve ser ignorado
		leilao.propoe(new Lance(steveJobs, 12000));
		
		assertEquals(10, leilao.getLances().size());
		assertEquals(11000, leilao.getLances().get(leilao.getLances().size()-1).getValor(), 0.00001);
	}
	
    @Test
    public void deveDobrarOUltimoLanceDado() {
        Leilao leilao = new Leilao("Macbook Pro 15");
        Usuario steveJobs = new Usuario("Steve Jobs");
        Usuario billGates = new Usuario("Bill Gates");

        leilao.propoe(new Lance(steveJobs, 2000));
        leilao.propoe(new Lance(billGates, 3000));
        leilao.dobraLance(steveJobs);

        assertEquals(4000, leilao.getLances().get(2).getValor(), 0.00001);
    }
    
    @Test
    public void naoDeveDobrarCasoNaoHajaLanceAnterior() {
        Leilao leilao = new Leilao("Macbook Pro 15");
        Usuario steveJobs = new Usuario("Steve Jobs");

        leilao.dobraLance(steveJobs);

        assertEquals(0, leilao.getLances().size());
    }
}
