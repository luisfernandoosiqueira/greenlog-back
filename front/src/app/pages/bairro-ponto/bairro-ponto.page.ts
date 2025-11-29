import { Component, OnInit } from '@angular/core';
import { NavBar } from "../../components/nav-bar/nav-bar.component";
import { FooterComponent } from "../../components/footer/footer.component";
import { CommonModule } from '@angular/common';
import { BairroRequest, BairroResponse } from '../../model/Bairro';
import { RouterLink } from '@angular/router';
import { BoxBairroComponent } from "../../components/box-bairro/box-bairro.component";
import { NovoBairroComponent } from "../../components/novo-bairro/novo-bairro.component";
import { NovoPontoComponent } from "../../components/novo-ponto/novo-ponto.component";

@Component({
  selector: 'app-bairro-ponto',
  imports: [CommonModule, NavBar, FooterComponent, RouterLink, BoxBairroComponent, NovoBairroComponent],
  templateUrl: './bairro-ponto.page.html',
  styleUrl: './bairro-ponto.page.scss'
})
export class BairroPontoPage implements OnInit{
  listaBairros: BairroResponse[] = []

  exibirModalBairro: boolean = false;

  ngOnInit(): void {
  }

  abrirModalNovoBairro() {
    this.exibirModalBairro = true;
  }

  abrirModalBairroEditar() {
    this.exibirModalBairro = true;
  }

  fecharModalBairro() {
    this.exibirModalBairro = false;
  }

  salvarBairro(bairroSalvo: BairroRequest){

  }

  recarregarBairros(){

  }
}
