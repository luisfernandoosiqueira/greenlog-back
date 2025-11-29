import { Component } from '@angular/core';
import { NavBar } from "../../components/nav-bar/nav-bar.component";
import { FooterComponent } from "../../components/footer/footer.component";
import { RotaRequest, RotaResponse } from '../../model/Rota';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { StatusMotorista } from '../../model/enums/StatusMotorista';
import { StatusCaminhao } from '../../model/enums/StatusCaminhao';
import { Residuos } from '../../model/Residuos';
import { NovaRotaComponent } from "../../components/nova-rota/nova-rota.component";

@Component({
  selector: 'app-rota',
  imports: [CommonModule, FormsModule, RouterLink, NavBar, FooterComponent, NovaRotaComponent],
  templateUrl: './rota.page.html',
  styleUrl: './rota.page.scss'
})
export class RotaPage {

  //listaRota: RotaResponse[] = []
  listaRota: RotaResponse[] = [
    {
      id: 1,
      nome: "Rota Central 01",
      pesoTotal: 1200,
      dataCriacao: "2025-01-10",
      tipoResiduo: { id: 1, nome: "Orgânico" },
      caminhao: {
        placa: "ABC-1234",
        motorista: {
          cpf: "11111111111",
          nome: "Carlos Silva",
          data: "2024-06-10",
          telefone: "11999990001",
          status: StatusMotorista.ATIVO
        },
        capacidade: 5000,
        status: StatusCaminhao.ATIVO,
        tiposResiduo: [{ id: 1, nome: "Orgânico" }]
      },
      distanciaTotal: 18.4,
      trechos: [],
      pontosColeta: []
    },
    {
      id: 2,
      nome: "Rota Norte 02",
      pesoTotal: 870,
      dataCriacao: "2025-02-02",
      tipoResiduo: { id: 2, nome: "Reciclável" },
      caminhao: {
        placa: "XYZ-5678",
        motorista: {
          cpf: "22222222222",
          nome: "Marcos Pereira",
          data: "2024-04-01",
          telefone: "11999990002",
          status: StatusMotorista.ATIVO
        },
        capacidade: 4500,
        status: StatusCaminhao.ATIVO,
        tiposResiduo: [{ id: 2, nome: "Reciclável" }]
      },
      distanciaTotal: 25.7,
      trechos: [],
      pontosColeta: []
    },
    {
      id: 3,
      nome: "Rota Sul 03",
      pesoTotal: 1540,
      dataCriacao: "2025-01-22",
      tipoResiduo: { id: 3, nome: "Entulho" },
      caminhao: {
        placa: "JKL-9988",
        motorista: {
          cpf: "33333333333",
          nome: "João Mendes",
          data: "2024-02-20",
          telefone: "11999990003",
          status: StatusMotorista.INATIVO
        },
        capacidade: 8000,
        status: StatusCaminhao.ATIVO,
        tiposResiduo: [{ id: 3, nome: "Entulho" }]
      },
      distanciaTotal: 32.1,
      trechos: [],
      pontosColeta: []
    },
    {
      id: 4,
      nome: "Rota Leste 04",
      pesoTotal: 640,
      dataCriacao: "2025-03-01",
      tipoResiduo: { id: 4, nome: "Hospitalar" },
      caminhao: {
        placa: "DEF-5566",
        motorista: {
          cpf: "44444444444",
          nome: "Ricardo Alves",
          data: "2024-07-05",
          telefone: "11999990004",
          status: StatusMotorista.ATIVO
        },
        capacidade: 3000,
        status: StatusCaminhao.ATIVO,
        tiposResiduo: [{ id: 4, nome: "Hospitalar" }]
      },
      distanciaTotal: 12.3,
      trechos: [],
      pontosColeta: []
    },
    {
      id: 5,
      nome: "Rota Oeste 05",
      pesoTotal: 1930,
      dataCriacao: "2025-01-15",
      tipoResiduo: { id: 1, nome: "Orgânico" },
      caminhao: {
        placa: "GHI-3344",
        motorista: {
          cpf: "55555555555",
          nome: "Paulo Henrique",
          data: "2024-08-11",
          telefone: "11999990005",
          status: StatusMotorista.ATIVO
        },
        capacidade: 6000,
        status: StatusCaminhao.ATIVO,
        tiposResiduo: [{ id: 1, nome: "Orgânico" }]
      },
      distanciaTotal: 28.9,
      trechos: [],
      pontosColeta: []
    }
  ];


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
