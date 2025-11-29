import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Residuos } from '../../model/Residuos';
import { PontoColetaRequest, PontoColetaResponse } from '../../model/PontoColeta';
import { FormArray, FormBuilder, FormControl, FormGroup, NgModel, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-novo-ponto',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './novo-ponto.component.html',
  styleUrl: './novo-ponto.component.scss'
})
export class NovoPontoComponent implements OnInit {
  @Input() pontoParaEditar: PontoColetaResponse | null = null;

  @Output() aoSalvar = new EventEmitter<PontoColetaRequest>();
  @Output() aoCancelar = new EventEmitter<void>();

  residuosDisponivel: Residuos[] = [];

  form: FormGroup;

  constructor(private fb: FormBuilder){
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      responsavel: ['', [Validators.required, Validators.minLength(3)]],
      telefone: ['', [Validators.required, Validators.maxLength(11), Validators.minLength(10)]],
      email: ['', [Validators.required, Validators.email, Validators.pattern(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)]],
      endereco: ['', [Validators.required, Validators.minLength(3)]],
      horaEntrada: ['', Validators],
      horaSaida: ['', Validators],
      tiposResiduo: this.fb.array([])
    })
  }

  ngOnInit(): void {
    const formResiduo = this.fb.array(
      this.residuosDisponivel.map(() => new FormControl(false))
    );
    this.form.setControl('tiposResiduo', formResiduo);

    if (this.pontoParaEditar) {
      this.form.patchValue({
        nome: this.pontoParaEditar.nome,
        responsavel: this.pontoParaEditar.responsavel,
        telefone: this.pontoParaEditar.telefone,
        email: this.pontoParaEditar.email,
        horaEntrada: this.pontoParaEditar.horaEntrada,
        horaSaida: this.pontoParaEditar.horaSaida,
      });

      (this.pontoParaEditar.tipoResiduo || []).forEach(residuo => {
        const index = this.residuosDisponivel.findIndex(r => r.id === residuo.id);
        if (index >= 0) {
          (this.form.get('tiposResiduo') as FormArray).at(index).setValue(true);
        }
      });
    }
  }

  salvar() {
    if (this.form.valid){
      const pontoSalvo: PontoColetaRequest = this.form.value; 
      this.aoSalvar.emit(pontoSalvo);
    }
  }

  cancelar() {
    this.aoCancelar.emit();
  }
}
