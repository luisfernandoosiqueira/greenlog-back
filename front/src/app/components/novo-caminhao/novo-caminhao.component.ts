import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CaminhaoRequest, CaminhaoResponse } from '../../model/Caminhao';
import { StatusCaminhao } from '../../model/enums/StatusCaminhao';
import { FormArray, FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Residuos } from '../../model/Residuos';
import { CommonModule } from '@angular/common';
import { MotoristaResponse } from '../../model/Motorista';
import { NgxMaskDirective } from 'ngx-mask';

@Component({
  selector: 'app-novo-caminhao',
  imports: [CommonModule, ReactiveFormsModule, NgxMaskDirective],
  templateUrl: './novo-caminhao.component.html',
  styleUrl: './novo-caminhao.component.scss'
})
export class NovoCaminhaoComponent implements OnInit{
  @Input() caminhaoParaEditar: CaminhaoResponse | null = null;

  @Output() aoSalvar = new EventEmitter<CaminhaoRequest>();
  @Output() aoCancelar = new EventEmitter<void>();

  statusDisponiveis = Object.values(StatusCaminhao);
  listaMotorista: MotoristaResponse[] = [];
  residuosDisponivel: Residuos[] = [];

  form: FormGroup;

  constructor(private fb: FormBuilder, private auth: AuthService){
    this.form = this.fb.group({
      placa: ['', [Validators.required, Validators.pattern(/^[A-Z]{3}[0-9][A-Z][0-9]{2}$/)]],
      motorista: ['', Validators.required],
      capacidade: ['', [Validators.required, Validators.min(1)]],
      status: [null, Validators.required],
      tiposResiduo: this.fb.array([])
    })
  }

  ngOnInit(): void {
    const formResiduo = this.fb.array(
      this.residuosDisponivel.map(() => new FormControl(false))
    );
    this.form.setControl('tiposResiduo', formResiduo);

    if (this.caminhaoParaEditar) {
      this.form.patchValue({
        placa: this.caminhaoParaEditar.placa,
        motorista: this.caminhaoParaEditar.motorista,
        capacidade: this.caminhaoParaEditar.capacidade,
        status: this.caminhaoParaEditar.status,
      });

      (this.caminhaoParaEditar.tiposResiduo || []).forEach(residuo => {
        const index = this.residuosDisponivel.findIndex(r => r.id === residuo.id);
        if (index >= 0) {
          (this.form.get('tiposResiduo') as FormArray).at(index).setValue(true);
        }
      });
    }
  }

  salvar() {
    if (this.form.valid){
      const caminhaoSalvo: CaminhaoRequest = this.form.value; 
      this.aoSalvar.emit(caminhaoSalvo);
    }
  }

  cancelar() {
    this.aoCancelar.emit();
  }
}
