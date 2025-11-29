import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { BairroSimplesResponse } from '../../model/Bairro';
import { RuaRequest, RuaResponse } from '../../model/Rua';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-nova-rua',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './nova-rua.component.html',
  styleUrl: './nova-rua.component.scss'
})
export class NovaRuaComponent implements OnInit {
  @Input() ruaParaEditar: RuaResponse | null = null;

  @Output() aoSalvar = new EventEmitter<RuaRequest>();
  @Output() aoCancelar = new EventEmitter<void>();

  listaBairro: BairroSimplesResponse[] = [
    { id: 1, nome: 'Centro' },
    { id: 2, nome: 'Jardim Primavera' },
    { id: 3, nome: 'Vila Nova' },
    { id: 4, nome: 'São Lucas' },
    { id: 5, nome: 'Alto da Serra' },
    { id: 6, nome: 'Morada Verde' },
  ];

  form: FormGroup;

  constructor(private fb: FormBuilder){
    this.form = this.fb.group({
      origemId: [null, Validators.required],
      destinoId: [null, Validators.required],
      distanciaKm: ['', Validators.required],
    })
  }

  ngOnInit(): void {
    if (this.ruaParaEditar) {
      this.form.patchValue({
        origemId: this.ruaParaEditar.origemId,
        destinoId: this.ruaParaEditar.destinoId,
        distanciaKm: this.ruaParaEditar.distanciaKm,
      });
    }
  }

  salvar() {
    if (this.form.valid){
      const ruaSalvo: RuaRequest = this.form.value; 
      console.log(ruaSalvo.origemId + " - " + ruaSalvo.destinoId + " - " + ruaSalvo.distanciaKm);
      this.aoSalvar.emit(ruaSalvo);
    }
  }

  cancelar() {
    this.aoCancelar.emit();
  }
}
