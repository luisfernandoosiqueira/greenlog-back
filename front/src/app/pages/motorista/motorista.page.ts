import { Component, OnInit } from '@angular/core';
import { NavBar } from "../../components/nav-bar/nav-bar.component";
import { FooterComponent } from "../../components/footer/footer.component";
import { CommonModule, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MotoristaRequest, MotoristaResponse } from '../../model/Motorista';
import { StatusMotorista } from '../../model/enums/StatusMotorista';
import { NovoMotoristaComponent } from "../../components/novo-motorista/novo-motorista.component";

@Component({
  selector: 'app-motorista',
  imports: [CommonModule, RouterLink, NavBar, FooterComponent, DatePipe, NovoMotoristaComponent],
  templateUrl: './motorista.page.html',
  styleUrl: './motorista.page.scss'
})
export class MotoristaPage {
  //listaMotorista: MotoristaResponse[] = [];
  listaMotorista: MotoristaResponse[] = [
  { cpf: '11111111111', nome: 'João Silva', data: '1990-03-12', telefone: '62984571542', status: StatusMotorista.ATIVO },
  { cpf: '22222222222', nome: 'Maria Souza', data: '1992-07-25', telefone: '62984854542', status: StatusMotorista.ATIVO },
  { cpf: '33333333333', nome: 'Pedro Santos', data: '1988-01-09', telefone: '62984572542', status: StatusMotorista.INATIVO },
  { cpf: '44444444444', nome: 'Ana Oliveira', data: '1995-11-15', telefone: '62984455542', status: StatusMotorista.ATIVO },
  { cpf: '55555555555', nome: 'Carlos Lima', data: '1987-04-30', telefone: '62984524542', status: StatusMotorista.ATIVO },
  { cpf: '66666666666', nome: 'Fernanda Alves', data: '1993-09-18', telefone: '62984881552', status: StatusMotorista.INATIVO },
  { cpf: '77777777777', nome: 'Ricardo Pereira', data: '1991-12-04', telefone: '62954584542', status: StatusMotorista.ATIVO }
];

  exibirModal: boolean = false;
  motoristaSendoEditado: boolean = false;
  motoristaParaAtualizar: MotoristaResponse | null = null;
  
  ngOnInit(): void {
      
  }

  abrirModalNovo() {
    this.motoristaParaAtualizar = null;
    this.exibirModal = true;
    this.motoristaSendoEditado = false;
  }

  abrirModalEditar(motorista: MotoristaResponse) {
    this.motoristaParaAtualizar = motorista;
    this.exibirModal = true;
    this.motoristaSendoEditado = true;
  }

  fecharModel() {
    this.motoristaParaAtualizar = null;
    this.exibirModal = false;
    this.motoristaSendoEditado = false;
  }

  formatarTelefone(numero: string): string {
    const apenasNumeros = numero.replace(/\D/g, "");

    if (apenasNumeros.length !== 11) {
      return numero;
    }

    const ddd = apenasNumeros.substring(0, 2);
    const nove = apenasNumeros.substring(2, 3);
    const parte1 = apenasNumeros.substring(3, 7);
    const parte2 = apenasNumeros.substring(7);

    return `(${ddd}) ${nove} ${parte1}-${parte2}`;
  }

  formatarCPF(doc: string): string {
    const apenasNumeros = doc.replace(/\D/g, "");

    // CPF tem 11 dígitos
    if (apenasNumeros.length === 11) {
      return apenasNumeros.replace(
        /(\d{3})(\d{3})(\d{3})(\d{2})/,
        "$1.$2.$3-$4"
      );
    }
    return doc;
  }

  getStatusClass(status: StatusMotorista): string {
    switch (status) {
      case StatusMotorista.ATIVO:
        return 'status-ativo';
      case StatusMotorista.INATIVO:
        return 'status-inativo';
      default:
        return '';
    }
  }

  salvarMotorista(motoristaSalvo: MotoristaRequest){
    console.log("Motorista sendo salvo: " + motoristaSalvo);
    // 1. Lógica para Novo Cadastro
    if (!this.motoristaSendoEditado) {
        const novoMotorista: MotoristaResponse = { ...motoristaSalvo, cpf: motoristaSalvo.cpf }; // Ajuste de tipo para a simulação
        this.listaMotorista.push(novoMotorista);
        console.log("NOVO Motorista adicionado com sucesso:", novoMotorista);

    // 2. Lógica para Edição
    } else {
        const index = this.listaMotorista.findIndex(m => m.cpf === motoristaSalvo.cpf); 
        
        if (index > -1) {
             this.listaMotorista[index] = { ...this.listaMotorista[index], ...motoristaSalvo };
             console.log("Motorista ATUALIZADO com sucesso:", this.listaMotorista[index]);
        }
    }
  }

  recarregarMotorista(){

  }

  removerMotorista(){

  }
}
