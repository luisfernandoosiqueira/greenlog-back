import { StatusCaminhao } from "./enums/StatusCaminhao";
import { MotoristaResponse } from "./Motorista";
import { Residuos } from "./Residuos";

export interface CaminhaoResponse{
    placa: string,
    motorista: MotoristaResponse,
    capacidadeKg: number,
    status: StatusCaminhao
    tiposResiduoIds: Residuos[]
}

export interface CaminhaoRequest{
    placa: string,
    motoristaCpf: string,
    capacidadeKg: number,
    status: StatusCaminhao,
    tiposResiduoIds: Residuos[]
}