import { Component, Input, OnInit } from '@angular/core';
import { NovoPontoComponent } from "../novo-ponto/novo-ponto.component";
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { BairroRequest, BairroResponse } from '../../model/Bairro';
import { PontoColetaRequest, PontoColetaResponse } from '../../model/PontoColeta';
import { NovoBairroComponent } from "../novo-bairro/novo-bairro.component";

@Component({
  selector: 'app-box-bairro',
  imports: [CommonModule, ReactiveFormsModule, NovoPontoComponent, NovoBairroComponent],
  templateUrl: './box-bairro.component.html',
  styleUrl: './box-bairro.component.scss'
})
export class BoxBairroComponent implements OnInit {
  @Input() bairro: BairroResponse | null = null;

  exibirModalPonto: boolean = false;
  pontoSendoEditado: boolean = false;
  pontoParaAtualizar: PontoColetaResponse | null = null;

  exibirModalBairro: boolean = false;
  bairroSendoEditado: boolean = false;
  bairroParaAtualizar: BairroResponse | null = null;

  ngOnInit(): void {
    
  }

  abrirModalNovoBairro() {
    this.bairroParaAtualizar = null;
    this.exibirModalBairro = true;
    this.bairroSendoEditado = false;
  }

  abrirModalEditarBairro(bairro: BairroResponse) {
    this.bairroParaAtualizar = bairro;
    this.exibirModalBairro = true;
    this.bairroSendoEditado = true;
  }

  fecharModalBairro() {
    this.bairroParaAtualizar = null;
    this.exibirModalBairro = false;
    this.bairroSendoEditado = false;
  }

  abrirModalNovoPonto() {
    this.pontoParaAtualizar = null;
    this.exibirModalPonto = true;
    this.pontoSendoEditado = false;
  }

  abrirModalEditarPonto(ponto: PontoColetaResponse) {
    this.pontoParaAtualizar = ponto;
    this.exibirModalPonto = true;
    this.pontoSendoEditado = true;
  }

  fecharModalPonto() {
    this.pontoParaAtualizar = null;
    this.exibirModalPonto = false;
    this.pontoSendoEditado = false;
  }

  getTiposResiduos(): string{
    return "";
  }

  salvarPonto(pontoColete: PontoColetaRequest) {
  }

  salvarBairro(bairro: BairroRequest) {
  }

}
