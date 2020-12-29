package br.com.caelum.leilao.teste;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;
import br.com.caelum.leilao.servico.Avaliador;

public class AvaliadorTest {
	
	private Avaliador leiloeiro;
	private Usuario joao;
	private Usuario jose;
	private Usuario maria;
	
	@Before
	public void criaAvaliador() {
		this.leiloeiro = new Avaliador();
		
		this.joao = new Usuario("Joao");
		this.jose = new Usuario("Jose");
		this.maria = new Usuario("Maria");
	}
	
	@Test/*(expected = RuntimeException.class)*/
    public void naoDeveAvaliarLeiloesSemNenhumLanceDado() {
    	
    	
    		Leilao leilao = new CriadorDeLeilao().para("PlayStation 6").constroi();
    		leiloeiro.avalia(leilao);
//    		Assert.fail(); // esse metodo fail falha o teste
    	
    	// como faremos este assert? nao existe excessoes de assert
    	// temos que colocar este trecho em um try cath
    }
	
	@Test
	public void deveEntenderLancesEmOrdemCrescente() { //o metodo de teste nao pode ser static e nem receber argumentos
		//parte 1: cenario
		
		Leilao leilao = new Leilao("Playstation 5 Novo");
		leilao.propoe(new Lance(joao, 250.0));
		leilao.propoe(new Lance(jose, 300.0));
		leilao.propoe(new Lance(maria, 400.0));
		
		//parte 2: acao
		leiloeiro.avalia(leilao);
		
		//parte 3: validacao
//		double maiorEsperado = 400;
//		double menorEsperado = 250;
		
//		System.out.println(maiorEsperado == leiloeiro.getMaiorLance()); // true, pois eh o resultado que esperamos
//		assertEquals(maiorEsperado, leiloeiro.getMaiorLance(), 0.00001); //0.00001 eh um delta pois o double tem problema de arredondamento. o jUnit vai aceitar essa pequena diferença entre os valores 
//		System.out.println(menorEsperado == leiloeiro.getMenorLance()); // false, nao eh o resultado que esperamos
//		assertEquals(menorEsperado, leiloeiro.getMenorLance(), 0.00001);
		assertThat(leiloeiro.getMaiorLance(), equalTo(400.0));
		assertThat(leiloeiro.getMenorLance(), equalTo(250.0));
	}
	
	@Test
    public void testaMediaDeZeroLance(){

        // cenario
        Usuario ewertom = new Usuario("Ewertom");

        // acao
        Leilao leilao = new Leilao("Iphone 7");

        Avaliador avaliador = new Avaliador();
        avaliador.avalia(leilao);

        //validacao
        assertEquals(0, avaliador.getMedia(), 0.0001);

    }
	
	@Test
	public void deveEntenderLeilaoComApenasUmLance() {
		Leilao leilao = new Leilao("Playstation 3 Novo");
		
		leilao.propoe(new Lance(joao, 1000.0));
		
		leiloeiro.avalia(leilao);
		
		assertEquals(1000.0, leiloeiro.getMaiorLance(), 0.00001);
		assertEquals(1000.0, leiloeiro.getMenorLance(), 0.00001);
	}
	
	@Test
	public void deveEncontrarOsTresMaioresLances() {
		
		Leilao leilao = new CriadorDeLeilao().para("Preisteixon")
				.lance(joao, 100)
				.lance(maria, 200)
				.lance(joao, 300)
				.lance(maria, 400)
				.constroi();
		
		leiloeiro.avalia(leilao);
		
		List<Lance> maiores = leiloeiro.getTresMaiores();
		assertEquals(3, maiores.size());
//		assertEquals(400.0, maiores.get(0).getValor(), 0.00001);
//		assertEquals(300.0, maiores.get(1).getValor(), 0.00001);
//		assertEquals(200.0, maiores.get(2).getValor(), 0.00001);
		
		assertThat(maiores, hasItems(
			new Lance(maria, 400),
			new Lance(joao, 300),
			new Lance(maria, 200)
		));
	}
	
    @Test
    public void deveEntenderLeilaoComLancesEmOrdemRandomica() {
        Leilao leilao = new Leilao("Playstation 3 Novo");

        leilao.propoe(new Lance(joao,200.0));
        leilao.propoe(new Lance(maria,450.0));
        leilao.propoe(new Lance(joao,120.0));
        leilao.propoe(new Lance(maria,700.0));
        leilao.propoe(new Lance(joao,630.0));
        leilao.propoe(new Lance(maria,230.0));

        leiloeiro.avalia(leilao);

        assertEquals(700.0, leiloeiro.getMaiorLance(), 0.0001);
        assertEquals(120.0, leiloeiro.getMenorLance(), 0.0001);
    }
    
    @Test
    public void deveEntenderLeilaoComLancesEmOrdemDecrescente() {
        Leilao leilao = new Leilao("Playstation 3 Novo");

        leilao.propoe(new Lance(joao,400.0));
        leilao.propoe(new Lance(maria,300.0));
        leilao.propoe(new Lance(joao,200.0));
        leilao.propoe(new Lance(maria,100.0));

        leiloeiro.avalia(leilao);

        assertEquals(400.0, leiloeiro.getMaiorLance(), 0.0001);
        assertEquals(100.0, leiloeiro.getMenorLance(), 0.0001);
    }
    
    @Test
    public void deveDevolverTodosLancesCasoNaoHajaNoMinimo3() {
        Leilao leilao = new Leilao("Playstation 3 Novo");

        leilao.propoe(new Lance(joao, 100.0));
        leilao.propoe(new Lance(maria, 200.0));

        leiloeiro.avalia(leilao);

        List<Lance> maiores = leiloeiro.getTresMaiores();

        assertEquals(2, maiores.size());
        assertEquals(200, maiores.get(0).getValor(), 0.00001);
        assertEquals(100, maiores.get(1).getValor(), 0.00001);
    }

    @Test
    public void deveDevolverListaVaziaCasoNaoHajaLances() {
        Leilao leilao = new Leilao("Playstation 3 Novo");

        leiloeiro.avalia(leilao);

        List<Lance> maiores = leiloeiro.getTresMaiores();

        assertEquals(0, maiores.size());
    }
}
