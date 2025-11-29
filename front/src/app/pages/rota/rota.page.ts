import { Component } from '@angular/core';
import { NavBar } from "../../components/nav-bar/nav-bar.component";
import { FooterComponent } from "../../components/footer/footer.component";
import { RotaRequest, RotaResponse } from '../../model/Rota';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Residuos } from '../../model/Residuos';
import { NovaRotaComponent } from "../../components/nova-rota/nova-rota.component";

@Component({
  selector: 'app-rota',
  imports: [CommonModule, FormsModule, RouterLink, NavBar, FooterComponent, NovaRotaComponent],
  templateUrl: './rota.page.html',
  styleUrl: './rota.page.scss'
})
export class RotaPage {

  listaRota: RotaResponse[] = []

  exibirModal: boolean = false;
  rotaSendoEditado: boolean = false;
  rotaParaAtualizar: RotaResponse | null = null;
  
  abrirModalNovo() {
    this.rotaParaAtualizar = null;
    this.exibirModal = true;
    this.rotaSendoEditado = false;
  }

  abrirModalEditar(rua: RotaResponse) {
    this.rotaParaAtualizar = rua;
    this.exibirModal = true;
    this.rotaSendoEditado = true;
  }

  fecharModel() {
    this.rotaParaAtualizar = null;
    this.exibirModal = false;
    this.rotaSendoEditado = false;
  }

  getTodosResiduos(tiposResiduo: Residuos[]): string {
    if (!tiposResiduo || tiposResiduo.length === 0) return "";

    return tiposResiduo.map(r => r.nome).join(", ");
  }

  salvar(ruaSalvo: RotaRequest){
  
  }
}
