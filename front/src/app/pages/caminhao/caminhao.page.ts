import { Component, OnInit } from '@angular/core';
import { CaminhaoRequest, CaminhaoResponse } from '../../model/Caminhao';
import { StatusCaminhao } from '../../model/enums/StatusCaminhao';
import { StatusMotorista } from '../../model/enums/StatusMotorista';
import { NavBar } from "../../components/nav-bar/nav-bar.component";
import { FooterComponent } from "../../components/footer/footer.component";
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Residuos } from '../../model/Residuos';
import { NovoCaminhaoComponent } from "../../components/novo-caminhao/novo-caminhao.component";

@Component({
  selector: 'app-caminhao',
  imports: [NavBar, FooterComponent, RouterLink, CommonModule, NovoCaminhaoComponent],
  templateUrl: './caminhao.page.html',
  styleUrl: './caminhao.page.scss'
})
export class CaminhaoPage implements OnInit{
  listaCaminhao: CaminhaoResponse[] = [
    { placa: "ABC1A23", motorista: { cpf: "12345678900", nome: "Carlos Mendes", data: "1985-06-12", telefone: "11987654321", status: StatusMotorista.ATIVO }, capacidade: 12, status: StatusCaminhao.ATIVO, tiposResiduo: [{ id: 1, nome: "Papel" }, { id: 2, nome: "Vidro" }] },
    { placa: "XYZ9B88", motorista: { cpf: "98765432100", nome: "Roberto Silva", data: "1990-02-25", telefone: "11999998888", status: StatusMotorista.ATIVO }, capacidade: 10, status: StatusCaminhao.INATIVO, tiposResiduo: [{ id: 3, nome: "Plástico" }] },
    { placa: "BRA2C45", motorista: { cpf: "45678912300", nome: "Marcos Oliveira", data: "1988-11-03", telefone: "11988887777", status: StatusMotorista.ATIVO }, capacidade: 14, status: StatusCaminhao.ATIVO, tiposResiduo: [{ id: 4, nome: "Metal" }, { id: 1, nome: "Papel" }] },
    { placa: "QWE4D67", motorista: { cpf: "32165498700", nome: "Paulo Ferreira", data: "1982-09-18", telefone: "11977776666", status: StatusMotorista.INATIVO }, capacidade: 11, status: StatusCaminhao.ATIVO, tiposResiduo: [{ id: 5, nome: "Orgânico" }] },
    { placa: "MNO7E90", motorista: { cpf: "78912345600", nome: "João Batista", data: "1995-04-07", telefone: "11966665555", status: StatusMotorista.ATIVO }, capacidade: 9, status: StatusCaminhao.ATIVO, tiposResiduo: [{ id: 2, nome: "Vidro" }, { id: 3, nome: "Plástico" }] }
  ];


  exibirModal: boolean = false;
  caminhaoSendoEditado: boolean = false;
  caminhaoParaAtualizar: CaminhaoResponse | null = null;

  ngOnInit(): void {
  }

  abrirModalNovo() {
    this.caminhaoParaAtualizar = null;
    this.exibirModal = true;
    this.caminhaoSendoEditado = false;
  }

  abrirModalEditar(caminhao: CaminhaoResponse) {
    this.caminhaoParaAtualizar = caminhao;
    this.exibirModal = true;
    this.caminhaoSendoEditado = true;
  }

  fecharModel() {
    this.caminhaoParaAtualizar = null;
    this.exibirModal = false;
    this.caminhaoSendoEditado = false;
  }

  getStatusClass(status: StatusCaminhao): string {
    switch (status) {
      case StatusCaminhao.ATIVO:
        return 'status-ativo';
      case StatusCaminhao.INATIVO:
        return 'status-inativo';
      default:
        return '';
    }
  }

  getTiposTexto(tipos: Residuos[]): string {
    if (!tipos || tipos.length === 0) return '—';
    return tipos.map(t => t.nome).join(', ');
  }

  salvarCaminhao(caminhaoSalvo: CaminhaoRequest){
    console.log("Motorista sendo salvo: " + caminhaoSalvo);
    // 1. Lógica para Novo Cadastro
    if (!this.caminhaoSendoEditado) {
        const novoCaminhao: CaminhaoResponse = { ...caminhaoSalvo, placa: caminhaoSalvo.placa }; // Ajuste de tipo para a simulação
        this.listaCaminhao.push(novoCaminhao);
        console.log("NOVO Caminhão adicionado com sucesso:", novoCaminhao);

    // 2. Lógica para Edição
    } else {
        const index = this.listaCaminhao.findIndex(m => m.placa === caminhaoSalvo.placa); 
        
        if (index > -1) {
              this.listaCaminhao[index] = { ...this.listaCaminhao[index], ...caminhaoSalvo };
              console.log("Caminhão ATUALIZADO com sucesso:", this.listaCaminhao[index]);
        }
    }
  }


  recarregarMotorista(){

  }

  removerMotorista(){

  }
}
