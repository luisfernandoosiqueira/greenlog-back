import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CaminhaoRequest, CaminhaoResponse } from '../../model/Caminhao';
import { StatusCaminhao } from '../../model/enums/StatusCaminhao';
import { FormArray, FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Residuos } from '../../model/Residuos';
import { CommonModule } from '@angular/common';
import { MotoristaResponse } from '../../model/Motorista';
import { NgxMaskDirective } from 'ngx-mask';
import { MotoristaService } from '../../services/motorista.service';
import { ResiduoService } from '../../services/residuo.service';

@Component({
  selector: 'app-novo-caminhao',
  imports: [CommonModule, ReactiveFormsModule, NgxMaskDirective],
  templateUrl: './novo-caminhao.component.html',
  styleUrl: './novo-caminhao.component.scss'
})
export class NovoCaminhaoComponent implements OnInit {
  @Input() caminhaoParaEditar: CaminhaoResponse | null = null;

  @Output() aoSalvar = new EventEmitter<CaminhaoRequest>();
  @Output() aoCancelar = new EventEmitter<void>();

  statusDisponiveis = Object.values(StatusCaminhao);
  listaMotorista: MotoristaResponse[] = [];
  residuosDisponivel: Residuos[] = [];

  form: FormGroup;

  constructor(private fb: FormBuilder, private motoristaService: MotoristaService, private residuoService: ResiduoService) {

    this.form = this.fb.group({
      placa: ['', [Validators.required, Validators.pattern(/^[A-Z]{3}[0-9][A-Z][0-9]{2}$/)]],
      motoristaCpf: [null, Validators.required],
      capacidadeKg: ['', [Validators.required, Validators.min(1)]],
      status: [null, Validators.required],
      tiposResiduoIds: this.fb.array([])
    });

    // MOTORISTA
    motoristaService.findAll().subscribe({
      next: (motorista) => this.listaMotorista = motorista,
      error: (err) => console.error("Erro ao carregar motorista", err)
    });

    // RESÍDUOS
    residuoService.findAll().subscribe({
      next: (residuos) => {
        this.residuosDisponivel = residuos;
        console.log("Resíduos carregados:", residuos);


        // cria o formarray com base nos resíduos carregados
        const formResiduo = this.fb.array(
          residuos.map(() => new FormControl(false))
        );
        this.form.setControl('tiposResiduoIds', formResiduo);

        // se estiver editando, preenche
        if (this.caminhaoParaEditar) {
          this.form.patchValue({
            placa: this.caminhaoParaEditar.placa,
            motoristaCpf: this.caminhaoParaEditar.motorista,
            capacidadeKg: this.caminhaoParaEditar.capacidadeKg,
            status: this.caminhaoParaEditar.status
          });

          (this.caminhaoParaEditar.tiposResiduoIds || []).forEach(residuo => {
            const index = this.residuosDisponivel.findIndex(r => r.id === residuo.id);
            if (index >= 0) {
              (this.form.get('tiposResiduoIds') as FormArray).at(index).setValue(true);
            }
          });
        }

      },
      error: (err) => console.error("Erro ao carregar resíduos", err)
    });
  }

  ngOnInit(): void {
    // ❌ NÃO coloque nada relacionado aos resíduos aqui
  }

  salvar() {
    if (this.form.valid) {

      const selecionadosBoolean = this.form.value.tiposResiduoIds;

      const tiposResiduoIds = selecionadosBoolean
        .map((checked: boolean, i: number) => checked ? i : null)
        .filter((id: number | null): id is number => id !== null);

      const caminhaoSalvo: CaminhaoRequest = {
        ...this.form.value,
        tiposResiduoIds: tiposResiduoIds
      };

      this.aoSalvar.emit(caminhaoSalvo);
    }
  }

  cancelar() {
    this.aoCancelar.emit();
  }
}

