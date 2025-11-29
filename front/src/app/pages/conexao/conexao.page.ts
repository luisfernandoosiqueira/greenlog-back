import { Component } from '@angular/core';
import { NavBar } from "../../components/nav-bar/nav-bar.component";
import { FooterComponent } from "../../components/footer/footer.component";
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { RuaRequest, RuaResponse } from '../../model/Rua';
import { BairroSimplesResponse } from '../../model/Bairro';
import { NovaRuaComponent } from "../../components/nova-rua/nova-rua.component";
import { BairroService } from '../../services/bairro.service';

@Component({
  selector: 'app-conexao',
  imports: [CommonModule, RouterLink, NavBar, FooterComponent, NovaRuaComponent],
  templateUrl: './conexao.page.html',
  styleUrl: './conexao.page.scss'
})
export class ConexaoPage {

  listaRuas: RuaResponse[] = [];
  listaBairros: BairroSimplesResponse [] = [];

  exibirModal: boolean = false;
  ruaSendoEditado: boolean = false;
  ruaParaAtualizar: RuaResponse | null = null;

  constructor(private bairroService: BairroService) {
  }

  ngOnInit(): void {
    this.bairroService.findAll().subscribe({
      next: (bairros) => {
        this.listaBairros = bairros;
        console.log("Bairros carregados:", bairros);
      },
      error: (err) => {
        console.error("Erro ao carregar bairros", err);
      }
    });
  }

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
    const bairro = this.listaBairros.find(b => b.id === bairroId);
    return bairro ? bairro.nome : "Desconhecido";
  }

  salvar(ruaSalvo: RuaRequest){

  }
}
