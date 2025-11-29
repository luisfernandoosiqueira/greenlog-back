import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { RotaRequest, RotaResponse } from '../../model/Rota';
import { FormArray, FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CaminhaoResponse } from '../../model/Caminhao';
import { PontoColetaResponse } from '../../model/PontoColeta';
import { Residuos } from '../../model/Residuos';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-nova-rota',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './nova-rota.component.html',
  styleUrl: './nova-rota.component.scss'
})
export class NovaRotaComponent implements OnInit {
  @Input() rotaParaEditar: RotaResponse | null = null;

  @Output() aoSalvar = new EventEmitter<RotaRequest>();
  @Output() aoCancelar = new EventEmitter<void>();

  caminhoesDisponiveis: CaminhaoResponse[] = [];
  residuosDisponiveis: Residuos[] = [];
  pontosDisponiveis: PontoColetaResponse[] = [];

  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      nome: ['', [Validators.required]],
      caminhaoPlaca: ['', Validators.required],
      tipoResiduoId: ['', Validators.required],

      ponto1: ['', Validators.required],
      ponto2: ['', Validators.required],
      ponto3: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    if (this.rotaParaEditar) {
      this.form.patchValue({
        nome: this.rotaParaEditar.nome,
        caminhaoPlaca: this.rotaParaEditar.caminhao.placa,
        tipoResiduoId: this.rotaParaEditar.tipoResiduo.id,
      });

      // Preenche pontos se existir
      if (this.rotaParaEditar.pontosColeta?.length >= 3) {
        this.form.patchValue({
          ponto1: this.rotaParaEditar.pontosColeta[0].id,
          ponto2: this.rotaParaEditar.pontosColeta[1].id,
          ponto3: this.rotaParaEditar.pontosColeta[2].id,
        });
      }
    }
  }

  salvar() {
    if (this.form.invalid) return;

    const rota: RotaRequest = {
      nome: this.form.value.nome,
      caminhaoPlaca: this.form.value.caminhaoPlaca,
      tipoResiduoId: this.form.value.tipoResiduoId,
      pontosColetaIds: [
        this.form.value.ponto1,
        this.form.value.ponto2,
        this.form.value.ponto3
      ]
    };

    this.aoSalvar.emit(rota);
  }

  cancelar() {
    this.aoCancelar.emit();
  }
}


