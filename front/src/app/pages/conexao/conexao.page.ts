import { Component } from '@angular/core';
import { NavBar } from "../../components/nav-bar/nav-bar.component";
import { FooterComponent } from "../../components/footer/footer.component";
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { RuaRequest, RuaResponse } from '../../model/Rua';
import { BairroSimplesResponse } from '../../model/Bairro';
import { NovaRuaComponent } from "../../components/nova-rua/nova-rua.component";

@Component({
  selector: 'app-conexao',
  imports: [CommonModule, RouterLink, NavBar, FooterComponent, NovaRuaComponent],
  templateUrl: './conexao.page.html',
  styleUrl: './conexao.page.scss'
})
export class ConexaoPage {

  //listaRuas: RuaResponse[] = [];
  //listaBairro: BairroSimplesResponse [] = [];

  listaRuas: RuaResponse[] = [
    { id: 1, origemId: 1, destinoId: 2, distanciaKm: 3.2 },
    { id: 2, origemId: 1, destinoId: 3, distanciaKm: 4.7 },
    { id: 3, origemId: 2, destinoId: 3, distanciaKm: 2.1 },
    { id: 4, origemId: 2, destinoId: 4, distanciaKm: 5.0 },
    { id: 5, origemId: 3, destinoId: 4, distanciaKm: 3.8 },
    { id: 6, origemId: 4, destinoId: 1, distanciaKm: 6.4 },
  ];


  listaBairro: BairroSimplesResponse[] = [
    { id: 1, nome: 'Centro' },
    { id: 2, nome: 'Jardim Primavera' },
    { id: 3, nome: 'Vila Nova' },
    { id: 4, nome: 'São Lucas' },
    { id: 5, nome: 'Alto da Serra' },
    { id: 6, nome: 'Morada Verde' },
  ];


  exibirModal: boolean = false;
  ruaSendoEditado: boolean = false;
  ruaParaAtualizar: RuaResponse | null = null;

  abrirModalNovo() {
    this.ruaParaAtualizar = null;
    this.exibirModal = true;
    this.ruaSendoEditado = false;
  }

  abrirModalEditar(rua: RuaResponse) {
    this.ruaParaAtualizar = rua;
    this.exibirModal = true;
    this.ruaSendoEditado = true;
  }

  fecharModel() {
    this.ruaParaAtualizar = null;
    this.exibirModal = false;
    this.ruaSendoEditado = false;
  }

  getBairroNome(bairroId: number): string {
    const bairro = this.listaBairro.find(b => b.id === bairroId);
    return bairro ? bairro.nome : "Desconhecido";
  }

  salvar(ruaSalvo: RuaRequest){

  }
}
